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

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.function.ConstValueSource;
import org.apache.solr.search.function.FunctionQuery;
import org.apache.solr.search.function.LiteralValueSource;
import org.apache.solr.search.function.ReciprocalFloatFunction;
import org.apache.solr.search.function.ValueSource;
import org.apache.solr.search.function.distance.Constants;
import org.apache.solr.search.function.distance.GeohashHaversineFunction;
import org.apache.solr.search.function.distance.HaversineFunction;
import org.apache.solr.search.function.distance.VectorDistanceFunction;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import java.util.ArrayList;
import java.util.List;

/**
 * Use the Geonames Webservices API to get geocoding information to create a query.  Each Toponym match is added
 * as a function
 * <p/>
 * Local Params:
 * <ul>
 * <li>topo - Short for toponym. The location to search for in Geonames.  Required.</li>
 * <li>rows - The number of rows to get back from GeoNames. Optional. Default 1.</li>
 * <li>start - The result to start on.  Optional. Default: 0</li>
 * <li>lat - The latitude field name to use as a ValueSource for the FunctionQuery.  If specified, lon must also be set.</li>
 * <li>lon - The longitude field name to use as a ValueSource for the FunctionQuery. If specified, lat must also be set.</li>
 * <li>gh - The geohash field name to use as a ValueSource for the FunctionQuery. If specified, lat/lon must NOT be set.</li>
 * <li>dist - The Distance function to use. String. One of [hsin, 0-Integer.MAX_VALUE, ghhsin].  If a geohash field is specified, then this field is disregarded and ghhsin is automatic.  Default is 2 for the 2-norm (Euclidean)</li>
 * <li>unit - KM|M - The units to use, KM for metric, M for English.  Default is M</li>
 * <li>boost - float.  The amount to boost the function query by.  Default is 1.</li>
 * </ul>
 * <p/>
 * NOTE: This is not intended for production use.
 */
public class GeonamesQParserPlugin extends QParserPlugin {
  public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    return new QParser(qstr, localParams, params, req) {
      @Override
      public Query parse() throws ParseException {
        final int rows = localParams.getInt("rows", 1);
        final int start = localParams.getInt("start", 0);
        String topo = localParams.get("topo");
        String distFunc = localParams.get("dist");
        String latFieldName = localParams.get("lat");
        String lonFieldName = localParams.get("lon");
        String ghFieldName = localParams.get("gh");
        String units = localParams.get("unit", "M");
        float boost = localParams.getFloat("boost", 1.0f);
        double radius = Constants.EARTH_RADIUS_MI;
        if (units.equalsIgnoreCase("KM")) {
          radius = Constants.EARTH_RADIUS_KM;
        }
        ValueSource vs = null, latVS = null, lonVS = null, ghVS = null;
        IndexSchema schema = req.getSchema();
        DistType distType = DistType.NORM;
        float power = 2;
        List<ValueSource> latLon = new ArrayList<ValueSource>(2);
        if (ghFieldName != null && ghFieldName.equals("") == false) {
          FieldType ft = schema.getFieldType(ghFieldName);
          SchemaField sf = schema.getField(ghFieldName);
          ghVS = ft.getValueSource(sf, this);//Should we pass this here?
          distType = DistType.GHHSIN;
        } else if (latFieldName != null && latFieldName.equals("") == false
                && lonFieldName != null && lonFieldName.equals("") == false) {
          FieldType ftLat = schema.getFieldType(latFieldName);
          FieldType ftLon = schema.getFieldType(lonFieldName);
          SchemaField sfLat = schema.getField(latFieldName);
          SchemaField sfLon = schema.getField(lonFieldName);
          latVS = ftLat.getValueSource(sfLat, this);
          lonVS = ftLon.getValueSource(sfLon, this);
          latLon.add(latVS);
          latLon.add(lonVS);
          try {
            power = Float.parseFloat(distFunc);
            distType = DistType.NORM;
          } catch (NumberFormatException e) {
            if (distFunc.equals("hsin")) {
              distType = DistType.HSIN;
            } else if (distFunc.equals("ghsin")) {
              distType = DistType.GHHSIN;
            }
          }
        } else {
          throw new ParseException("Either gh or both lat and lon must be specified.");
        }


        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
        searchCriteria.setQ(topo);
        searchCriteria.setMaxRows(rows);
        searchCriteria.setStartRow(start);
        Query query = null;
        try {
          ToponymSearchResult searchResult = WebService.search(searchCriteria);

          List<Toponym> topos = searchResult.getToponyms();
          if (topos.size() > 1) {
            BooleanQuery tmp = new BooleanQuery();
            for (Toponym toponym : topos) {
              double lat = toponym.getLatitude();
              double lon = toponym.getLongitude();
              FunctionQuery fq = getFunction(distType, ghVS, power, latLon, lat, lon, radius);
              tmp.add(fq, BooleanClause.Occur.SHOULD);
            }
            query = tmp;
          } else if (topos.size() == 1){
            Toponym curr = topos.get(0);
            query = getFunction(distType, ghVS, power, latLon, curr.getLatitude(), curr.getLongitude(), radius);
          }
        } catch (Exception e) {
          //TODO: deal with errors
        }
        query.setBoost(boost);
        return query;
      }


    };
  }

  private FunctionQuery getFunction(DistType distType, ValueSource ghVS, float power, List<ValueSource> latLon, double currLat, double currLon, double radius) {
    ValueSource vs = null;
    switch (distType) {
      case GHHSIN: {
        String ghStr = GeoHashUtils.encode(currLat, currLon);
        vs = new GeohashHaversineFunction(new LiteralValueSource(ghStr), ghVS, radius);
        break;
      }
      case HSIN: {
        vs = new HaversineFunction(new ConstValueSource((float) currLat),
                new ConstValueSource((float) currLon), latLon.get(0), latLon.get(1), radius);
        break;
      }
      case NORM: {
        List<ValueSource> current = new ArrayList<ValueSource>(2);
        current.add(new ConstValueSource((float) currLat));
        current.add(new ConstValueSource((float) currLon));
        vs = new VectorDistanceFunction(power, latLon, current);
        break;
      }
      default: {//assume 2-norm
        List<ValueSource> current = new ArrayList<ValueSource>(2);
        current.add(new ConstValueSource((float) currLat));
        current.add(new ConstValueSource((float) currLon));
        vs = new VectorDistanceFunction(2, latLon, current);
        break;
      }
    }
    return new FunctionQuery(new ReciprocalFloatFunction(vs, 1, 1, 0)); // 1/distance
  }

  public void init(NamedList namedList) {

  }

  private enum DistType {
    NORM, HSIN, GHHSIN;
  }
}
