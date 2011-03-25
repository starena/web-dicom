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

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Convert an Open Street Map OSM file to Solr docs and send them to Solr for indexing
 */
public class OSM2Solr {
  private SolrServer server;

  public OSM2Solr(SolrServer server) {
    this.server = server;
  }

  public long process(InputStream is, OSMFactFinder finder) throws IOException, SAXException {
    long result = 0;
    SAXParserFactory spf = SAXParserFactory.newInstance();
    try {
      SAXParser sp = spf.newSAXParser();
      OSMHandler handler = new OSMHandler(server, finder);
      sp.parse(is, handler);
      server.optimize();
      result = handler.getNumIndexed();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SolrServerException e) {
      e.printStackTrace();
    }
    return result;
  }
}
