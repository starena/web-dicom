package org.psystems.dicom.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.xml.sax.SAXException;

/**
 *
 *
 **/
public class Driver {
	public static void main(String[] args) throws IOException, SAXException {

		int maxDocs = 20;
		SolrServer server = new CommonsHttpSolrServer(
				"http://localhost:8983/solr");
		
		List<SolrInputDocument> docBuffer = new ArrayList<SolrInputDocument>(
				maxDocs);

		try {
			for (int i = 0; i < maxDocs; i++) {
				
				/*
				SolrInputDocument currentDicomStudy = new SolrInputDocument();
				currentDicomStudy.addField("id", "key" + i);
				currentDicomStudy.addField("studyId", "study st" + i);
				currentDicomStudy.addField("patientName", " VASIA FIO" + i);
				currentDicomStudy.addField("studyDescription",
						"Description of study FIO" + i);
//				docBuffer.add(currentDicomStudy);
				server.add(currentDicomStudy);
				
				*/
				
				Study study = new Study();
				study.id = "key" + i;
				study.patientName = "FIO" + i;
				study.study_Id = "STUDYID" + i;
				study.studyDescription = "Description of study FIO" + i;
				study.diagnozis =  new String[] { "aaa", "bbb", "ccc" };
				ArrayList<String> services = new ArrayList<String>();
				services.add("service1");
				services.add("service2");
				services.add("service3");
				study.services = services;
				server.addBean(study);
				server.commit();	
				

			}

//			server.add(docBuffer);
//			int numIndexed = docBuffer.size();
//			docBuffer.clear();
			
			
			server.optimize();

		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
