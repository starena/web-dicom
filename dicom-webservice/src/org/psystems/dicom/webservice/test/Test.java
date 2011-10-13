package org.psystems.dicom.webservice.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.psystems.dicom.webservice.client.ByteArray;
import org.psystems.dicom.webservice.client.Diagnosis;
import org.psystems.dicom.webservice.client.DicomService;
import org.psystems.dicom.webservice.client.DicomServiceService;
import org.psystems.dicom.webservice.client.Direction;
import org.psystems.dicom.webservice.client.Employee;
import org.psystems.dicom.webservice.client.ManufacturerDevice;
import org.psystems.dicom.webservice.client.Patient;
import org.psystems.dicom.webservice.client.QueryDirection;
import org.psystems.dicom.webservice.client.QueryDirectionReq;
import org.psystems.dicom.webservice.client.QueryStudy;
import org.psystems.dicom.webservice.client.QueryStudyReq;
import org.psystems.dicom.webservice.client.Service;
import org.psystems.dicom.webservice.client.Study;

public class Test {

	private static DicomServiceService service;
	private static DicomService port;

	private static final String WS_URL = "http://localhost:8080/dicom-webservice/DicomService?wsdl";

	// private static final String WS_URL =
	// "https://proxy.gp1.psystems.org:38081/dicom-webservice3/DicomService?wsdl";
	// private static final String WS_URL =
	// "https://proxy.gp1.psystems.org:38081/dicom-webservice/DicomService?wsdl";
	// private static final String WS_URL =
	// "https://proxy.gp1.psystems.org:38081/dicom-webservice.test/DicomService?wsdl";
	/**
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException {

		URL url = new URL(WS_URL);
		QName qname = new QName("http://webservice.dicom.psystems.org/",
				"DicomServiceService");

		System.setProperty("javax.net.ssl.keyStore", "client.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "derenok");
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStore", "client.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "derenok");

		service = new DicomServiceService(url, qname);
		port = service.getDicomServicePort();

		// авторизация
		// http://www.mkyong.com/webservices/jax-ws/application-authentication-with-jax-ws/
		// http://stackoverflow.com/questions/1613212/jax-ws-and-basic-authentication-when-user-names-and-passwords-are-in-a-database

		/******************* UserName & Password ******************************/
		Map<String, Object> req_ctx = ((BindingProvider) port)
				.getRequestContext();
		req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("Username", Collections.singletonList("dicomuser"));
		headers.put("Password", Collections.singletonList("dicomtestpwd"));
		req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		/**********************************************************************/

		// queryStudy();
		// qetDirrection();
		// queryDirrection();
//		 makeDirection();
		getStudyImages();
//		getStudyProtocols();
	}

	private static void queryStudy() {

		System.out.println("queryStudy...");

		QueryStudy query = new QueryStudy();
		// query.set
		// query.setId(18l);
		query.setStudyComplite(true);
		QueryStudyReq request = new QueryStudyReq();
		request.setQuery(query);

		List<Study> studies = port.queryStudies(request).getReturn();
		for (Study study : studies) {
			System.out.println("study: " + study.getDcmFilesId());
		}
	}

	private static void qetDirrection() {

		System.out.println("qetDirrection...");

		Direction drn = port.getDirectionById(130l);
		System.out.println("Direction: " + drn);

	}

	private static void queryDirrection() {
		System.out.println("queryDirrection...");

		QueryDirectionReq request = new QueryDirectionReq();
		QueryDirection query = new QueryDirection();
		query.setId(167l);
		query.setSenderLPU("");
		// query.setPatientShortName("ДЕРДВ74");
		request.setQuery(query);

		List<Direction> drns = port.queryDirections(request).getReturn();
		for (Direction direction : drns) {
			System.out.println("Direction: " + direction);
		}

	}

	private static void getStudyImages() {
		System.out.println("getImage...");

		List<byte[]> images = port.getStudyImages(2);
		System.out.println(" image count:" +images.size());
		int index = 1;
		for (byte[] image : images) {
			
			try {
				String fileName = "c:\\temp\\wstest_"+ index + ".jpg";
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(image);
				fos.flush();
				fos.close();
				
				System.out.println("!!!! fileName=" + fileName);
				
				index++;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	

	private static void getStudyProtocols() {
		System.out.println("get Protocols...");

		List<byte[]> pdfs = port.getStudyProtocols(1);
		System.out.println(" protocol count:" +pdfs.size());
		int index = 1;
		
		for (byte[] pdfData : pdfs) {
			try {
				String fileName = "c:\\temp\\wstest_"+ index + ".pdf";
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(pdfData);
				fos.flush();
				fos.close();
				
				System.out.println("!!!! fileName=" + fileName);
				
				index++;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		

	}

	private static void makeDirection() {

		System.out.println("makeDirrection...");

		Employee doctor = new Employee();
		doctor.setEmployeeName("Врач doctor1");
		doctor.setEmployeeType("DOCTOR");
		doctor.setEmployeeCode("123");

		//
		ArrayList<Diagnosis> diagnosis = new ArrayList<Diagnosis>();

		Diagnosis dia1 = new Diagnosis();
		dia1.setDiagnosisCode("Z01");
		dia1.setDiagnosisType("ОСНОВНОЙ");
		dia1.setDiagnosisDescription("Диагноз1");

		diagnosis.add(dia1);

		Diagnosis dia2 = new Diagnosis();
		dia2.setDiagnosisCode("Z02");
		dia2.setDiagnosisDescription("Диагноз2");

		diagnosis.add(dia2);

		//

		//

		ArrayList<Service> services = new ArrayList<Service>();

		Service service1 = new Service();
		service1.setServiceCode("code1");
		service1.setServiceDescription("Описание1");
		service1.setServiceCount(10);
		services.add(service1);

		Service service2 = new Service();
		service2.setServiceCode("code2");
		service2.setServiceDescription("Описание2");
		services.add(service2);

		//
		ManufacturerDevice device = new ManufacturerDevice();
		device.setModality("DS");
		device.setManufacturerModelName("УЗИаппарат");

		//
		Patient patient = new Patient();
		patient.setPatientName("Деренок Дмитрий Владимирович");
		patient.setPatientId("PATID1234");
		patient.setPatientSex("M");
		patient.setPatientBirthDate("1974-03-01");
		patient.setPatientShortName("ДЕРДВ74");

		long id = port.makeDirection(new Date().getTime() + "", "DS", doctor,
				diagnosis, services, "2011-09-21", device,
				"2011-09-22 12:00:00", "CODE123", "GP1-ROOM515", patient,
				"LPUTEST");

		System.out.println("!!!! makeDirection id:" + id);

	}

}
