package org.psystems.webdicom2.ws;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.MTOMFeature;

import org.psystems.webdicom2.ws.client.stub.Dcm;
import org.psystems.webdicom2.ws.client.stub.Direction;
import org.psystems.webdicom2.ws.client.stub.Gate;
import org.psystems.webdicom2.ws.client.stub.GateService;
import org.psystems.webdicom2.ws.client.stub.StudyResult;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("test!");
		new Test();
	}

	
	private GateService service;
	private static Gate port;
	private static final String WS_URL = "http://localhost:8080/dicom2-ws/ws?wsdl";

	public Test() throws IOException {

		
		
		URL url = new URL(WS_URL);
		QName qname = new QName("http://ws.webdicom2.psystems.org/", "GateService");
		
		service = new GateService(url,qname);
		port = service.getGatePort(new MTOMFeature());

		AuthBASIC();
		
		testDirrection();
	}

	private void testDirrection() throws IOException {

		
		String barCode = "123456675";
		
		Direction drn = new Direction();
		drn.setBarCode(barCode);
		drn.setDateBirsday("1974/03/01");
		drn.setDateStudy("2013/08/10");
		drn.setModality("MG");
		drn.setPatientId("123123");
		drn.setPatientName("Derenok D.V. ДЕренок Д.В.");
		drn.setServiceName("mammografia");
		drn.setSex("M");
		Direction resultDrn = port.sendDirection(drn);
		
		System.out.println("Direction:"+resultDrn);
		
		port.sendFinalResult(barCode, "Финальный результат");
		
		port.sendPhysician(barCode, "Врач Иванов");
		
//		FileInputStream fidPdf = new FileInputStream("/tmp/usi_pochek.pdf");
		
		RandomAccessFile f = new RandomAccessFile("/tmp/usi_pochek.pdf", "r");
		byte[] b = new byte[(int)f.length()];
		f.read(b);
		
		String studyUID = "study456";
		port.sendPdf(barCode , b);
		
		StudyResult complResult = port.getCompliteStudyResult(barCode);
		System.out.println("Compl Result: " + complResult );
		
		for (String url : complResult.getImageUrls()) {
			System.out.println("  img: " + url);
		}
		
		for (String url : complResult.getPdfUrls()) {
			System.out.println("  pdf: " + url);
		}
		
		
		
		List<Dcm> dcm = port.getDCM(barCode);
		for (Dcm dcmDto : dcm) {
			System.out.println(" !!! dcm id "+dcmDto.getId() + " barCode " + dcmDto.getBarCode() + " img:" + dcmDto.getImageId()+" pdf:"+dcmDto.getPdfId());
			
			if(dcmDto.getImageId()!=null) {
				byte[] content = port.getDCMContent(barCode, dcmDto.getImageId());
				System.out.println("!!! image content "+content.length);
			}
			
			if(dcmDto.getPdfId()!=null) {
				byte[] content = port.getDCMContent(barCode, dcmDto.getPdfId());
				System.out.println("!!! pdf content "+content.length);
			}
		}
		
//		String id = "1380553624798";
//		String id = "1380557182924";
		
//		byte[] content = port.getDCMContent(barCode, id);
		
		
		
//		System.out.println("!!! content "+content);
//		System.out.println("!!! content "+content.length);
		
//		List<RisCode> codes = port.getRISCodes();
//		for (RisCode risCode : codes) {
//			System.out.println("RisCode: " + risCode);
//		}

	}
	
	private static void AuthBASIC() {
		// BASIC авторизация
		// http://www.mkyong.com/webservices/jax-ws/application-authentication-with-jax-ws/
		// http://stackoverflow.com/questions/1613212/jax-ws-and-basic-authentication-when-user-names-and-passwords-are-in-a-database
		/******************* UserName & Password ******************************/
		Map<String, Object> req_ctx = ((BindingProvider) port)
				.getRequestContext();
		req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("Username", Collections.singletonList("admin"));
		headers.put("Password", Collections.singletonList("admin"));
		req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		/**********************************************************************/
	}

}
