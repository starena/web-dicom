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
import org.psystems.webdicom2.ws.client.stub.DcmTag;
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
		QName qname = new QName("http://ws.webdicom2.psystems.org/",
				"GateService");

		service = new GateService(url, qname);
		port = service.getGatePort(new MTOMFeature());

		AuthBASIC();

		testDirrection("127", "11111");
	}

	private void testDirrection(String misId, String barCode)
			throws IOException {

		Direction drn = new Direction();
		drn.setBarCode(barCode);
		drn.setMisId(misId);
		drn.setDateBirsday("1974/03/01");
		drn.setDateStudy("2013/08/10");
		drn.setModality("MG");
		drn.setPatientId("123123");
		drn.setPatientName("Derenok D.V. ДЕренок Д.В.");
		drn.setServiceName("mammografia");
		drn.setSex("M");
		Direction resultDrn = port.sendDirection(drn);

		System.out.println("Direction:" + resultDrn);

		port.sendFinalResult(misId, "Финальный результат");

		port.sendPhysician(misId, "Врач Иванов");

		// FileInputStream fidPdf = new FileInputStream("/tmp/usi_pochek.pdf");

		RandomAccessFile f = new RandomAccessFile("/tmp/data.pdf", "r");
		byte[] b = new byte[(int) f.length()];
		f.read(b);

		port.sendPdf(misId, b);

		f = new RandomAccessFile("/tmp/data.jpg", "r");
		b = new byte[(int) f.length()];
		f.read(b);

		port.sendImage(misId, b);

		StudyResult complResult = port.getCompliteStudyResult(misId);
		System.out.println("Compl Result: " + complResult);

		for (String url : complResult.getImageUrls()) {
			System.out.println("  img: " + url);
		}

		for (String url : complResult.getPdfUrls()) {
			System.out.println("  pdf: " + url);
		}

		System.out
				.println("Compl Result getResult: " + complResult.getResult());

		List<Dcm> dcm = port.getDCM(misId);
		for (Dcm dcmDto : dcm) {
			System.out.println(" !!! dcm id " + dcmDto.getDcmId() + " misId "
					+ dcmDto.getMisId() + " img:" + dcmDto.getImageId()
					+ " pdf:" + dcmDto.getPdfId());

			if (dcmDto.getImageId() != null) {
				byte[] content = port.getDCMContent(misId, dcmDto.getImageId());
				System.out.println("   !!! image content " + content.length);
			}

			if (dcmDto.getPdfId() != null) {
				byte[] content = port.getDCMContent(misId, dcmDto.getPdfId());
				System.out.println("   !!! pdf content " + content.length);
			}
			
			System.out.println("content URL:"+dcmDto.getContentUrl());

			List<DcmTag> tags = port.getDCMTags(dcmDto.getDcmId());
			for (DcmTag dcmTag : tags) {
				System.out.println("    tag: " + dcmTag.getName()+" = " + dcmTag.getValue());
			}
			

		}
		

		

		dcm = port.getDCMbyDate("20131001");
		
		System.out.println("FIND !!!!! " + dcm.size());
		
		for (Dcm dcmDto : dcm) {
			System.out.println(" !!! dcm id " + dcmDto.getDcmId() + " misId "
					+ dcmDto.getMisId() + " img:" + dcmDto.getImageId()
					+ " pdf:" + dcmDto.getPdfId());

			if (dcmDto.getImageId() != null) {
				byte[] content = port.getDCMContent(dcmDto.getMisId(), dcmDto.getImageId());
				System.out.println("   !!! image content " + content.length);
			}

			if (dcmDto.getPdfId() != null) {
				byte[] content = port.getDCMContent(dcmDto.getMisId(), dcmDto.getPdfId());
				System.out.println("   !!! pdf content " + content.length);
			}

			List<DcmTag> tags = port.getDCMTags(dcmDto.getDcmId());
			for (DcmTag dcmTag : tags) {
				System.out.println("    tag: " + dcmTag.getName()+" = " + dcmTag.getValue());
			}
			

		}

		// port.sendPdf("rrrr" , b);

		// port.removeDirection(barCode);

		// List<RisCode> codes = port.getRISCodes();
		// for (RisCode risCode : codes) {
		// System.out.println("RisCode: " + risCode);
		// }

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
