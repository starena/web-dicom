package org.psystems.dicom.index.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SpellingParams;
import org.psystems.dicom.index.entity.Diagnosis;
import org.psystems.dicom.index.entity.Employee;
import org.psystems.dicom.index.entity.Service;
import org.psystems.dicom.index.entity.Study;
import org.xml.sax.SAXException;

/**
 * Все примеры здесь: http://wiki.apache.org/solr/Solrj
 * 
 **/
public class Tester {

	public static void main(String[] args) throws IOException, SAXException {

		SolrServer server = new CommonsHttpSolrServer(
				"http://localhost:8983/solr");
		// передача будет в бинарном формате
		((CommonsHttpSolrServer) server)
				.setRequestWriter(new BinaryRequestWriter());

		Tester searcher = new Tester();

		try {
			searcher.testDicDiagnosis(server);
			searcher.testDicServices(server);
			searcher.testDicEmployes(server);
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}

	public Tester() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Диагнозы
	 * 
	 * @param server
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 */
	public void testDicDiagnosis(SolrServer server) throws MalformedURLException,
			SolrServerException {

		SolrQuery query = new SolrQuery();
		query.setQuery("dicName:diagnosis");
		query.setFilterQueries("diagnosisCode:CO*1");
		query.setRows(20);
		query.setFields("diagnosisCode,diagnosisDescription");
		query.addSortField("diagnosisCode", SolrQuery.ORDER.asc);
		QueryResponse rsp;

		rsp = server.query(query);
		
//		for (SolrDocument doc : rsp.getResults()) {
//			System.out.println("Diagnosis: " + doc);
//		}

		List<Diagnosis> beans = rsp.getBeans(Diagnosis.class);
		for (Diagnosis study : beans) {
			System.out.println("dia: " + study);
		}

	}
	
	/**
	 * Услуги
	 * 
	 * @param server
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 */
	public void testDicServices(SolrServer server) throws MalformedURLException,
			SolrServerException {

		SolrQuery query = new SolrQuery();
		query.setQuery("dicName:service");
//		query.setFilterQueries("serviceAlias:ЭЭ*");
		query.setRows(20);
//		query.setFields("diagnosisCode,diagnosisDescription");
//		query.addSortField("diagnosisCode", SolrQuery.ORDER.asc);
		QueryResponse rsp;

		rsp = server.query(query);
		
//		for (SolrDocument doc : rsp.getResults()) {
//			System.out.println("Diagnosis: " + doc);
//		}

		List<Service> beans = rsp.getBeans(Service.class);
		for (Service study : beans) {
			System.out.println("srv: " + study);
		}

	}
	
	/**
	 * Сотрудники
	 * 
	 * @param server
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 */
	public void testDicEmployes(SolrServer server) throws MalformedURLException,
			SolrServerException {

		SolrQuery query = new SolrQuery();
		query.setQuery("dicName:employee");
//		query.setFilterQueries("diagnosisCode:CO*");
		query.setRows(20);
//		query.setFields("diagnosisCode,diagnosisDescription");
//		query.addSortField("diagnosisCode", SolrQuery.ORDER.asc);
		QueryResponse rsp;

		rsp = server.query(query);
		
//		for (SolrDocument doc : rsp.getResults()) {
//			System.out.println("Diagnosis: " + doc);
//		}

		List<Employee> beans = rsp.getBeans(Employee.class);
		for (Employee study : beans) {
			System.out.println("emp: " + study);
		}

	}

	
}
