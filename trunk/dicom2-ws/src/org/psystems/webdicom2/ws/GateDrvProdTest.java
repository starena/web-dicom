/**
 * 
 */
package org.psystems.webdicom2.ws;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.MTOMFeature;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.psystems.webdicom2.ws.client.stub.Direction;
import org.psystems.webdicom2.ws.client.stub.Gate;
import org.psystems.webdicom2.ws.client.stub.GateService;

/**
 * @author ddv
 * 
 */
public class GateDrvProdTest {

	private GateService service;
	private String barCode;
	private String misId;
	private static Gate port;
	private static final String WS_URL = "http://localhost:8081/dicom2-ws/ws?wsdl";

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

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		URL url = new URL(WS_URL);
		QName qname = new QName("http://ws.webdicom2.psystems.org/",
				"GateService");

		service = new GateService(url, qname);
		port = service.getGatePort(new MTOMFeature());
		
		long time = new Date().getTime();
		
		barCode="bar"+time;
		misId="misid"+time;

		AuthBASIC();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.psystems.webdicom2.ws.GateDrvProd#GateDrvProd(org.psystems.webdicom2.ws.Gate)}
	 * .
	 */
	@Test
	public void testGateDrvProd() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.psystems.webdicom2.ws.GateDrvProd#sendDirection(org.psystems.webdicom2.ws.dto.Direction)}
	 * .
	 */
	@Test
	public void testSendDirection() {
		
		Direction drn = new Direction();
		drn.setBarCode(barCode);
		drn.setMisId(misId);
		//TODO проверку на формат даты
		drn.setDateBirsday("1974/03/01");
		//TODO проверку на формат даты
		drn.setDateStudy("2013/08/10");
		drn.setModality("MG");
		drn.setPatientId("123123");
		drn.setPatientName("Tester^Test^Testovich");
		drn.setServiceName("mammografia");
		drn.setSex("M");
		Direction resultDrn = port.sendDirection(drn);

	}

//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#removeDirection(java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testRemoveDirection() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#getDCM(java.lang.String)}.
//	 */
//	@Test
//	public void testGetDCM() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#getDCMbyDate(java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testGetDCMbyDate() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#getDCMTags(java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testGetDCMTags() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#getDCMContent(java.lang.String, java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testGetDCMContent() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#getCompliteStudyResult(java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testGetCompliteStudyResult() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#sendPdf(java.lang.String, byte[])}
//	 * .
//	 */
//	@Test
//	public void testSendPdf() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#sendImage(java.lang.String, byte[])}
//	 * .
//	 */
//	@Test
//	public void testSendImage() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#sendFinalResult(java.lang.String, java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testSendFinalResult() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#sendPhysician(java.lang.String, java.lang.String)}
//	 * .
//	 */
//	@Test
//	public void testSendPhysician() {
//		fail("Not yet implemented");
//	}
//
//	/**
//	 * Test method for
//	 * {@link org.psystems.webdicom2.ws.GateDrvProd#getRISCodes()}.
//	 */
//	@Test
//	public void testGetRISCodes() {
//		fail("Not yet implemented");
//	}

}
