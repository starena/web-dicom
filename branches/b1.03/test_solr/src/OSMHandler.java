/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.lucene.spatial.tier.projections.CartesianTierPlotter;
import org.apache.lucene.spatial.tier.projections.IProjector;
import org.apache.lucene.spatial.tier.projections.SinusoidalProjector;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *
 *
 **/
public class OSMHandler extends DefaultHandler {
  protected SolrServer server;
  protected OSMFactFinder finder;

  public static final int START_TIER = 4;
  public static final int END_TIER = 19;
  public static final double TO_RADS = Math.PI/180.0;

  public static final int DEFAULT_BUFFER_SIZE = 100;
  protected int bufferCount = DEFAULT_BUFFER_SIZE;//send 100 docs at a time
  protected List<SolrInputDocument> docBuffer;
  protected SolrInputDocument current;
  protected List<CartesianTierPlotter> plotters;
  protected long numIndexed;

  OSMHandler(SolrServer server, OSMFactFinder factFinder) {
    this(server, factFinder, DEFAULT_BUFFER_SIZE);
  }

  public OSMHandler(SolrServer server, OSMFactFinder finder, int bufferCount) {
    this.server = server;
    this.bufferCount = bufferCount;
    this.finder = finder;
    docBuffer = new ArrayList<SolrInputDocument>(bufferCount);
    //Let's add all tiers from 1 to 19, even though this is overkill
    plotters = new ArrayList<CartesianTierPlotter>(20);
    IProjector projector = new SinusoidalProjector();
    for (int i = START_TIER; i <= END_TIER; i++) {
      plotters.add(new CartesianTierPlotter(i, projector, ""));
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equals("node")) {
      current = new SolrInputDocument();
      long id = Long.parseLong(attributes.getValue("id"));
      current.addField("id", id);
      current.addField("type", "node");
      //NOTE: I'm doing this on the client side of Solr for demonstration purposes.  A more effective solution, coming in Solr 1.5, will handle indexing on the server side.
      double latitude = Double.parseDouble(attributes.getValue("lat"));
      double longitude = Double.parseDouble(attributes.getValue("lon"));
      current.addField("lat", latitude);
      current.addField("lon", longitude);
      //should use copy field, but do the conversion here
      current.addField("lat_rad", latitude * TO_RADS);
      current.addField("lon_rad", longitude * TO_RADS);
      
      //See http://en.wikipedia.org/wiki/Geohash
      String geoHash = GeoHashUtils.encode(latitude, longitude);
      current.addField("geohash", geoHash);
      //Cartesian Tiers
      int tier = START_TIER;
      //Create a bunch of tiers, each deeper level has more precision
      for (CartesianTierPlotter plotter : plotters) {
        current.addField("tier_" + tier , plotter.getTierBoxId(latitude, longitude));
        tier++;
      }
      int version = Integer.parseInt(attributes.getValue("version"));
      current.addField("version", version);
      String user = attributes.getValue("user");
      current.addField("user", user);
      String timestamp = attributes.getValue("timestamp");
      current.addField("timestamp", timestamp);

      addFacts(id);
      docBuffer.add(current);

    } else if (qName.equals("way")) {
      //A "way", is a bunch of nodes linked together, see the sample at the bottom
      //For ways, get the tag info just the same
      /*
       <way id="29124014" visible="true" timestamp="2008-12-21T04:44:36Z" version="2" changeset="514025" user="Shadow" uid="24160">
    <nd ref="320408502"/>
    <nd ref="320408503"/>
    <tag k="name" v="Gopher Way"/>
    <tag k="created_by" v="Potlatch 0.10f"/>
    <tag k="highway" v="footway"/>
    <tag k="source" v="knowledge; image"/>
    <tag k="layer" v="2"/>
  </way>
       */
      current = new SolrInputDocument();
      long id = Long.parseLong(attributes.getValue("id"));
      addFacts(id);
      current.addField("type", "way");
      current.addField("id", id);
      docBuffer.add(current);


    } else if (current != null && qName.equals("nd")) {
      long refId = Long.parseLong(attributes.getValue("ref"));
      current.addField("way_id", refId); 

    } else if (current != null && qName.equals("tag")) {
      String key = attributes.getValue("k");
      String value = attributes.getValue("v");
      //is it a specific one we care about?  If not, just add as a dynamic field
      //TODO: make this scalable
      //Could likely make this more pluggable, too, to give people a chance to deal with the tags in their own way
      if (key.equals("ele")) {
        key = "ele";
      } else if ((key.indexOf("County") != -1 || key.indexOf("county") != -1)
              && key.equals("gnis:County_num") == false) {
        key = "county";
      } else if (key.equals("source") || key.equals("name")
              || key.equals("population")) {
        //fall through
      } else if (key.equals("is_in")) {
        //Split on comma, reverse the array and add from large to small
        String[] splits = value.split(",");
        StringBuilder newV = new StringBuilder(value.length());
        for (int i = 0; i < splits.length; i++) {
          newV.append(splits[i]);
          if (i < splits.length - 1) {
            newV.append(' ');
          }
        }
        value = newV.toString();

      } else {
        //How should we deal with other keys? let's add them as Strings
        key += "_s";
      }
      current.addField(key, value);
    } else if (current != null && qName.equals("bounds")) {
      //Should we just ignore or index it onto every doc?
    }
  }

  protected void addFacts(long id) {
    Map<String, Fact> facts = finder.lookup(id);
    if (facts != null && facts.isEmpty() == false) {
      for (Fact entry : facts.values()) {
        current.addField(entry.name + (entry.type.equals(Fact.Type.NONE) ? "" : "_" + entry.type.dynField), entry.value);
      }
    }
  }

  public static void reverse(Object[] buffer, int start, int len) {
    if (len <= 1) return;
    int num = len >> 1;
    for (int i = start; i < (start + num); i++) {
      Object c = buffer[i];
      buffer[i] = buffer[start * 2 + len - i - 1];
      buffer[start * 2 + len - i - 1] = c;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (qName.equals("node") || qName.equals("way")) {
      //should we send?
      if (docBuffer.size() >= bufferCount) {
        try {
          server.add(docBuffer);
          numIndexed += docBuffer.size();
          docBuffer.clear();
        } catch (SolrServerException e) {
          e.printStackTrace();//improve exception handling
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      current = null;
    }
  }

  @Override
  public void endDocument() throws SAXException {
    try {
      if (docBuffer.isEmpty() == false) {
        server.add(docBuffer);//send any remaining docs
      }
      server.commit();
    } catch (SolrServerException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void characters(char[] chars, int start, int off) throws SAXException {
  }

  public long getNumIndexed() {
    return numIndexed;
  }
}

/*

Sample OSM data

 <?xml version="1.0" encoding="UTF-8"?>
 <osm version="0.6" generator="OpenStreetMap server">
 <bounds minlat="42.9986" minlon="-76.2327" maxlat="43.0977" maxlon="-76.0621"/>
 <node id="18659092" lat="43.062728" lon="-76.186141" version="2" changeset="1418180"
 user="randall375" uid="130962" visible="true" timestamp="2009-06-04T04:41:28Z">
   <tag k="source" v="PGS"/>
 </node>
 <node id="18659093" lat="43.0630298" lon="-76.18652" version="2" changeset="1418180" user="randall375" uid="130962" visible="true" timestamp="2009-06-04T04:41:28Z">
   <tag k="source" v="PGS"/>
 </node>
 <node id="18659094" lat="43.0632869" lon="-76.1872925" version="2" changeset="1418180" user="randall375" uid="130962" visible="true" timestamp="2009-06-04T04:41:28Z">
   <tag k="source" v="PGS"/>
 </node>
 </osm>
  <node id="158419502" lat="43.0306219" lon="-76.2088151" version="2" changeset="158653" user="davidearl" uid="3582" visible="true" timestamp="2008-12-06T16:31:40Z">
     <tag k="name" v="Taunton"/>
     <tag k="place" v="hamlet"/>
     <tag k="gnis:County" v="Onondaga"/>
     <tag k="import_uuid" v="bb7269ee-502a-5391-8056-e3ce0e66489c"/>
     <tag k="gnis:id" v="967073"/>
     <tag k="gnis:Class" v="Populated Place"/>
     <tag k="gnis:County_num" v="067"/>
     <tag k="is_in" v="Onondaga,New York,N.Y.,NY,USA"/>
     <tag k="ele" v="153"/>
     <tag k="gnis:ST_alpha" v="NY"/>
     <tag k="gnis:ST_num" v="36"/>
   </node>
   <node id="158460256" lat="43.0814558" lon="-76.125757" version="2" changeset="158653" user="davidearl" uid="3582" visible="true" timestamp="2008-12-06T16:32:37Z">
     <tag k="name" v="Lyncourt"/>
     <tag k="place" v="hamlet"/>
     <tag k="gnis:County" v="Onondaga"/>
     <tag k="import_uuid" v="bb7269ee-502a-5391-8056-e3ce0e66489c"/>
     <tag k="gnis:County_num" v="067"/>
     <tag k="is_in" v="Onondaga,New York,N.Y.,NY,USA"/>
     <tag k="gnis:id" v="1867409"/>
     <tag k="gnis:Class" v="Populated Place"/>
     <tag k="ele" v="136"/>
     <tag k="gnis:ST_alpha" v="NY"/>
     <tag k="gnis:ST_num" v="36"/>
   </node>
   <node id="212721138" lat="43.051604" lon="-76.139634" version="1" changeset="581165" user="DaveHansenTiger" uid="7168" visible="true" timestamp="2008-01-09T11:18:50Z">
     <tag k="tiger:county" v="Onondaga, NY"/>
     <tag k="tiger:tlid" v="46622896:46604770:46604772:46604737"/>
     <tag k="tiger:upload_uuid" v="bulk_upload.pl-9049829b-2dc7-4455-bda3-ed9049ae4dd3"/>
     <tag k="source" v="tiger_import_dch_v0.6_20070829"/>
   </node>
   <node id="212721188" lat="43.0500026" lon="-76.1432143" version="3" changeset="2970348" user="nfgusedautoparts" uid="113450" visible="true" timestamp="2009-10-28T01:06:28Z"/>
   <node id="212721184" lat="43.0501901" lon="-76.1432252" version="2" changeset="2970348" user="nfgusedautoparts" uid="113450" visible="true" timestamp="2009-10-28T01:06:28Z">
     <tag k="tiger:county" v="Onondaga, NY"/>
     <tag k="tiger:tlid" v="46614733"/>
     <tag k="tiger:upload_uuid" v="bulk_upload.pl-9049829b-2dc7-4455-bda3-ed9049ae4dd3"/>
     <tag k="source" v="tiger_import_dch_v0.6_20070829"/>
   </node>


   <way id="29124014" visible="true" timestamp="2008-12-21T04:44:36Z" version="2" changeset="514025" user="Shadow" uid="24160">
    <nd ref="320408502"/>
    <nd ref="320408503"/>
    <tag k="name" v="Gopher Way"/>
    <tag k="created_by" v="Potlatch 0.10f"/>
    <tag k="highway" v="footway"/>
    <tag k="source" v="knowledge; image"/>
    <tag k="layer" v="2"/>
  </way>

  */