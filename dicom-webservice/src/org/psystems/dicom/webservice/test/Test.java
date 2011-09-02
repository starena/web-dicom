package org.psystems.dicom.webservice.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.psystems.dicom.webservice.client.DicomService;
import org.psystems.dicom.webservice.client.DicomServiceService;
import org.psystems.dicom.webservice.client.Direction;
import org.psystems.dicom.webservice.client.QueryStudy;
import org.psystems.dicom.webservice.client.QueryStudyReq;
import org.psystems.dicom.webservice.client.Study;

public class Test {

	private static DicomServiceService service;
	private static DicomService port;

	private static final String WS_URL = "http://localhost:8080/dicom-webservice/DicomService?wsdl";
//	private static final String WS_URL = "https://proxy.gp1.psystems.org:38081/dicom-webservice3/DicomService?wsdl";
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

		queryStudy();
		qetDirrection();
		// makeDirection();
	}

	private static void queryStudy() {

		System.out.println("queryStudy...");

		
		QueryStudy query = new QueryStudy();
		query.setId(18l);
		QueryStudyReq request = new QueryStudyReq();
		request.setQuery(query);
		
		List<Study> studies = port.queryStudies(request).getReturn();
		for (Study study : studies) {
			System.out.println("study: " + study.getPatientShortName());
		}
	}

	private static void qetDirrection() {

		System.out.println("qetDirrection...");

		Direction drn = port.getDirectionById(130l);
		System.out.println("Direction: " + drn);

	}

	private static void makeDirection() {
		// port.ma
	}

}
