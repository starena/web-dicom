package org.psystems.dicom.solr;

import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * @author EDaniel
 */
public class SolrExampleTests {

  public static void main(String[] args) {
    try {
      //Solr cell can also index MS file (2003 version and 2007 version) types.
      String fileName = "c:/Sample.pdf"; 
      //this will be unique Id used by Solr to index the file contents.
      String solrId = "Sample.pdf"; 
      
      indexFilesSolrCell(fileName, solrId);
      
    } catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }
  
  /**
   * Method to index all types of files into Solr. 
   * @param fileName
   * @param solrId
   * @throws IOException
   * @throws SolrServerException
   */
  public static void indexFilesSolrCell(String fileName, String solrId) 
    throws IOException, SolrServerException {
    
    String urlString = "http://localhost:8983/solr"; 
    SolrServer solr = new CommonsHttpSolrServer(urlString);
    
    ContentStreamUpdateRequest up 
      = new ContentStreamUpdateRequest("/update/extract");
    
    up.addFile(new File(fileName));
    
    up.setParam("literal.id", solrId);
    up.setParam("uprefix", "attr_");
    up.setParam("fmap.content", "attr_content");
    
    up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
    
    solr.request(up);
    
    QueryResponse rsp = solr.query(new SolrQuery("*:*"));
    
    System.out.println(rsp);
  }
}
