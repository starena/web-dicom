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
import org.psystems.dicom.index.entity.Study;
import org.xml.sax.SAXException;

/**
 * Все примеры здесь: http://wiki.apache.org/solr/Solrj
 * 
 **/
public class Searcher {

	public static void main(String[] args) throws IOException, SAXException {

		SolrServer server = new CommonsHttpSolrServer(
				"http://localhost:8983/solr");
		// передача будет в бинарном формате
		((CommonsHttpSolrServer) server)
				.setRequestWriter(new BinaryRequestWriter());

		Searcher searcher = new Searcher();

		try {
			searcher.testQuery(server);
			searcher.testQueryFacets(server);
			searcher.testQueryHighLight(server);
			searcher.testQuerySpelling(server);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}

	public Searcher() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Простой запрос
	 * 
	 * @param server
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 */
	public void testQuery(SolrServer server) throws MalformedURLException,
			SolrServerException {

		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		// query.setQuery("ник*");
//		 query.setFields("patientName,tags");
		query.addSortField("studyId", SolrQuery.ORDER.asc);
		QueryResponse rsp;

		rsp = server.query(query);
//		Iterator<SolrDocument> docs = rsp.getResults().iterator();
		for (SolrDocument doc : rsp.getResults()) {
			System.out.println("testQuery: doc: " + doc.getFieldValue("tags"));
		}

		List<Study> beans = rsp.getBeans(Study.class);
		for (Study study : beans) {
//			System.out.println("testQuery: " + study);
		}

	}

	/**
	 * Получение фасетов
	 * 
	 * @param server
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 */
	public void testQueryFacets(SolrServer server)
			throws MalformedURLException, SolrServerException {

		SolrQuery query = new SolrQuery();
		query.setQuery("ник*");
		// query.setFields("patientName");
		query.setFacet(true);
		query.addFacetField("patientName");
		query.addSortField("studyId", SolrQuery.ORDER.asc);
		QueryResponse rsp;

		rsp = server.query(query);

		List<FacetField> facets = rsp.getFacetFields();
		if (facets != null)
			for (FacetField facet : facets) {
				System.out.println("testQueryFacets: " + facet);
			}

	}

	/**
	 * Подсветка строк
	 * 
	 * @param server
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 */
	public void testQueryHighLight(SolrServer server)
			throws MalformedURLException, SolrServerException {

		SolrQuery query = new SolrQuery();
		query.setQuery("ник*");
		query.setHighlight(true).setHighlightSnippets(1);
		query.setParam("hl.fl", "patientName");

		query.addSortField("studyId", SolrQuery.ORDER.asc);
		QueryResponse rsp;

		rsp = server.query(query);
		Iterator<SolrDocument> docs = rsp.getResults().iterator();

		while (docs.hasNext()) {
			SolrDocument resultDoc = docs.next();

			// String content = (String) resultDoc.getFieldValue("patientName");
			String id = (String) resultDoc.getFieldValue("id"); // id is the

			if (rsp.getHighlighting().get(id) != null) {
				List<String> highlightSnippets = rsp.getHighlighting().get(id)
						.get("patientName");
				for (String string : highlightSnippets) {
					System.out.println("testQueryHighLight: " + string);
				}
			}
		}

		SpellCheckResponse spell = rsp.getSpellCheckResponse();
		if (spell != null) {
			System.out.println("!! getCollatedResult="
					+ spell.getCollatedResult());
		}

	}

	/**
	 * Поиск по словарю
	 * 
	 * @param server
	 * @throws SolrServerException
	 * @throws IOException 
	 */
	public void testQuerySpelling(SolrServer server)
			throws SolrServerException, IOException {

//		SolrInputDocument doc = new SolrInputDocument();
//		doc.setField("id", "111");
//		doc.setField("name", "Samsung");
//		server.add(doc);
//		server.commit(true, true);
		    
		SolrQuery query = new SolrQuery("*:*");
		query.set(CommonParams.QT, "/spell");
		query.set("spellcheck", true);
		query.set(SpellingParams.SPELLCHECK_Q, "николуева");
		query.set(SpellingParams.SPELLCHECK_BUILD, true);
		query.set(SpellingParams.SPELLCHECK_EXTENDED_RESULTS, true);
		QueryRequest request = new QueryRequest(query);
		SpellCheckResponse response = request.process(server).getSpellCheckResponse();
		
		if(response!=null) { 
			System.out.println("testQuerySpelling: " + response.getFirstSuggestion("николуева"));
		}
		

	}

}
