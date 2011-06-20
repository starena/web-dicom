package org.psystems.dicom.solr;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.psystems.dicom.solr.entity.Diagnosis;
import org.psystems.dicom.solr.entity.Service;
import org.xml.sax.SAXException;

/**
 *
 *
 **/
public class Loader {

	private static Logger logger = Logger.getLogger(Loader.class.getName());

	public static void main(String[] args) throws IOException, SAXException {

		try {
			new Loader();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Loader() throws IOException, SolrServerException {

		SolrServer server = new CommonsHttpSolrServer(
				"http://localhost:8983/solr");

		// передача будет в бинарном формате
		((CommonsHttpSolrServer) server)
				.setRequestWriter(new BinaryRequestWriter());

		syncDicDiagnosis(server);
		syncDicServices(server);
		server.optimize();

	}

	/**
	 * Синхронизация словаря диагнозов
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public void syncDicDiagnosis(SolrServer solr) throws IOException,
			SolrServerException {
		// TODO Взять реальные данные
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Diagnosis dia = new Diagnosis();
			dia.setId(dia.getDicName() + i);
			dia.setDiagnosisCode("CODE" + i);
			dia.setDiagnosisDescription("DESCR" + i);

			solr.addBean(dia);
			solr.commit();
		}
	}

	/**
	 * Синхронизация словаря услуг
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public void syncDicServices(SolrServer solr) throws IOException,
			SolrServerException {
		// TODO Взять реальные данные
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Service srv = new Service();
			srv.setId(srv.getDicName() + i);
			srv.setServiceAlias("ALIAS" + i);
			srv.setServiceCode("CODE" + i);
			srv.setServiceDescription("DESCR" + i);

			solr.addBean(srv);
			solr.commit();
		}
	}

}
