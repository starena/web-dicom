package org.psystems.dicom.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.psystems.dicom.solr.entity.Diagnosis;
import org.psystems.dicom.solr.entity.Employee;
import org.psystems.dicom.solr.entity.Patient;
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

		syncDicPatients(server);
		server.optimize();
		syncDicDiagnosis(server);
		server.optimize();
		syncDicServices(server);
		server.optimize();
		syncDicEmployes(server);
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
		logger.info("Sync Diagnosis...");
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Diagnosis dia = new Diagnosis();
			dia.setId(dia.getDicName() + i);
			dia.setDiagnosisCode("CODE" + i);
			dia.setDiagnosisDescription("DESCR" + i);

			solr.addBean(dia);
			solr.commit();
		}
		logger.info("Sync Diagnosis [OK]");
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
		logger.info("Sync Service...");
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
		logger.info("Sync Service [OK]");
	}
	
	/**
	 * Синхронизация словаря сотрудников
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public void syncDicEmployes(SolrServer solr) throws IOException,
			SolrServerException {
		// TODO Взять реальные данные
		logger.info("Sync Employee...");
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Employee emp = new Employee();
			emp.setId(emp.getDicName() + i);
			if(i%2==0) {
				emp.setEmployeeType(Employee.TYPE_DOCTOR);
			} else {
				emp.setEmployeeType(Employee.TYPE_OPERATOR);
			}
			emp.setEmployeeCode("CODE"+i);
			emp.setEmployeeName("NAME"+i);
			
			solr.addBean(emp);
			solr.commit();
		}
		logger.info("Sync Employee [OK]");
	}
	
	/**
	 * Синхронизация словаря пациентов
	 * 
	 * @param solr
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public void syncDicPatients(SolrServer solr) throws IOException,
			SolrServerException {
		// TODO Взять реальные данные
		logger.info("Sync Patient...");
		int maxDocs = 30;
		for (int i = 0; i < maxDocs; i++) {
			Patient patient = new Patient();
			patient.setId(patient.getDicName() + i);
			patient.setPatientId("ID"+i);
			patient.setPatientSex("M");
			patient.setPatientBirthDate(Date.valueOf("1974-03-01"));
			patient.setPatientName("PATIENT PATI PAT"+i);
			patient.setPatientShortName("PATPP74");
			
			solr.addBean(patient);
			solr.commit();
		}
		logger.info("Sync Patient [OK]");
	}

}
