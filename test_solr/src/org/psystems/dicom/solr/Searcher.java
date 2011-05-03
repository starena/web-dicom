package org.psystems.dicom.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.xml.sax.SAXException;

/**
 *
 *
 **/
public class Searcher {
	public static void main(String[] args) throws IOException, SAXException {

		//Все примеры здесь:
		//http://wiki.apache.org/solr/Solrj
		
		
		SolrServer server = new CommonsHttpSolrServer(
				"http://localhost:8983/solr");
		//передача будет в бинарном формате
		 ((CommonsHttpSolrServer) server).setRequestWriter(new BinaryRequestWriter());
		SolrQuery query = new SolrQuery();
//		query.setQuery( "*:*" );
	    query.setQuery( "ник*" );
//	    query.setFields("patientName");
//	    query.setFacet(true);
//	    query.addFacetField("patientName");
	    
	    query.addSortField( "studyId", SolrQuery.ORDER.asc );
	    QueryResponse rsp;
		try {
			rsp = server.query( query );
			SolrDocumentList docs = rsp.getResults();
			
			List<Study> beans = rsp.getBeans(Study.class);
			for (Study study : beans) {
				System.out.println("!!!" + study);
				
				System.out.println("!!! image ="+new String(study.getImagefull()).length());
			}
			
//			for (SolrDocument solrDocument : docs) {
//				System.out.println("patientName = " + solrDocument.getFieldValue("patientName"));
//			}
			
			List<FacetField> facets = rsp.getFacetFields();
			if(facets!=null)
			for (FacetField facet : facets) {
			  System.out.println("Facet:" + facet);
			}

			
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
}
