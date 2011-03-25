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
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FilenameFilter;

/**
 *
 *
 **/
public class Driver {
  public static void main(String[] args) throws IOException, SAXException {
    File osmFile = new File(args[0]);
    String solrURL = args[1];
    String factFile = null;
    System.out.println("Num args: " + args.length);
    if (args.length > 2) {
      factFile = args[2];
    }
    SolrServer server = new CommonsHttpSolrServer(solrURL);
    OSM2Solr o2s = new OSM2Solr(server);

    TextFactFinder factFinder = new TextFactFinder();
    if (factFile != null) {
      System.out.println("Loading facts from: " + factFile);
      File facts = new File(factFile);
      int numFacts = 0;
      if (facts.isDirectory()) {
        File [] files = facts.listFiles(new FilenameFilter() {
          public boolean accept(File file, String s) {
            return s.endsWith(".facts");
          }
        });
        for (int i = 0; i < files.length; i++) {
          File file = files[i];
          numFacts += factFinder.loadFacts(new FileReader(file));
        }
      } else {
        numFacts += factFinder.loadFacts(new FileReader(facts));

      }
      System.out.println("Loaded " + numFacts + " facts");
    }
    System.out.println("Starting indexing");
    long start = System.currentTimeMillis();
    long numIndexed = 0;
    if (osmFile.isDirectory()){
      File [] files = osmFile.listFiles(new FilenameFilter(){
        public boolean accept(File file, String s) {
          return s.endsWith(".osm");
        }
      });

      for (int i = 0; i < files.length; i++) {
        File file = files[i];
        System.out.println("Indexing: " + file);
        numIndexed += o2s.process(new FileInputStream(file), factFinder);
      }
    } else {
      numIndexed += o2s.process(new FileInputStream(osmFile), factFinder);
    }
    long finish = System.currentTimeMillis();
    long elapsed = finish - start;
    System.out.println("Indexing took " + elapsed
            + " ms for " + numIndexed + " docs.  Rate: " + (((float) numIndexed)/elapsed) + " docs/ms");

  }
}
