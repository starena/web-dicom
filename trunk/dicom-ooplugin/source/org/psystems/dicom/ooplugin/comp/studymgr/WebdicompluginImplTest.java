package org.psystems.dicom.ooplugin.comp.studymgr;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import junit.framework.TestCase;

public class WebdicompluginImplTest extends TestCase {
	
	private String data ="\n\r\n\n\n" +

"###ID###\n"+
"1\n"+
"###STUDY_ID###\n"+
"94286\n"+
"###STUDY_UID###\n"+
"1.2.826.0.1.3680043.2.634.30.1.94286.20100401164900\n"+
"###STUDY_VIEW_PROTOCOL###\n"+
"Органы грудной полости без видимых патологических изменений (придуманная)\n"+
"придуманное - не согласие!!!!!!!!!!!\n"+
"###STUDY_VIEW_PROTOCOL_DATE###\n"+
"2010-04-01\n"+
"###STUDY_RESULT###\n"+
"\n"+
"###PATIENT_BIRTH_DATE###\n"+
"1961-04-09";	




	public WebdicompluginImplTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

//	public void testUpdateDocument() {
////		fail("Not yet implemented");
//
//		
//		HttpClient httpclient = new DefaultHttpClient();
//		String host = "http://127.0.0.1:8888/oostudy/1"; 
//		HttpGet httpget = new HttpGet(host); 
//
//        System.out.println("executing request " + httpget.getURI());
//
//        // Create a response handler
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//        String responseBody = null;
//		try {
//			responseBody = httpclient.execute(httpget, responseHandler);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//		}
//		String result = responseBody;
//		System.out.println("executing result " + result);
//		
//		httpclient.getConnectionManager().shutdown(); 
//		
//		HashMap<String, String> d = WebdicompluginImpl.parse(result);
////		assertEquals(6, d.size());
////		assertEquals(d.get("ID"),"1");
////		assertEquals(d.get("STUDY_ID"),"94286");
////		assertEquals(d.get("STUDY_UID"),"1.2.826.0.1.3680043.2.634.30.1.94286.20100401164900");
////		assertEquals(d.get("STUDY_VIEW_PROTOCOL"),"Органы грудной полости без видимых патологических изменений (придуманная)придуманное - не согласие!!!!!!!!!!!");
////		assertEquals(d.get("STUDY_VIEW_PROTOCOL_DATE"),"2010-04-01");
////		assertEquals(d.get("STUDY_RESULT"),"");
//	}

	public void testSendDocument() {
//		fail("Not yet implemented");
		assertEquals("123", WebdicompluginImpl.getIdFormURL("file:///C:/Documents%20and%20Settings/dima_d/Local%20Settings/Temp/123.odt"));
		assertEquals("123", WebdicompluginImpl.getIdFormURL("file:///C:/Documents%20and%20Settings/dima_d/Local%20Settings/Temp/123-18.odt"));
	}
	
	public void testgetConfiguration() {
		String cfg = "dicomuser:dicom@https://proxy.gp1.psystems.org:38081/browser.01";
		HashMap<String, String> result = WebdicompluginImpl.getConfiguration(cfg);
		assertEquals(result.get("login"), "dicomuser");
		assertEquals(result.get("password"), "dicom");
		assertEquals(result.get("protocol"), "https");
		assertEquals(result.get("host"), "proxy.gp1.psystems.org");
		assertEquals(result.get("port"), "38081");
		assertEquals(result.get("url"), "browser.01");
		
		assertEquals("https://proxy.gp1.psystems.org:38081/browser.01",
				WebdicompluginImpl.getConfigURL(result));
	}
	
	public void testgetConfiguration_local () {
		
		String cfg = "dicomuser:dicom@http://localhost:8888/";
		HashMap<String, String> result = WebdicompluginImpl.getConfiguration(cfg);
		assertEquals(result.get("login"), "dicomuser");
		assertEquals(result.get("password"), "dicom");
		assertEquals(result.get("protocol"), "http");
		assertEquals(result.get("host"), "localhost");
		assertEquals(result.get("port"), "8888");
		assertEquals(result.get("url"), "");
		
		assertEquals("http://localhost:8888",
				WebdicompluginImpl.getConfigURL(result));
	}
	
	public void testFormatDate() {
//		fail("Not yet implemented");
		try {
			assertEquals("25.12.2000", WebdicompluginImpl.Dicomdate2Localdate("2000-12-25"));
			assertEquals("2000-12-25", WebdicompluginImpl.Localdate2Dicomdate("25.12.2000"));
			assertEquals("М", WebdicompluginImpl.DicomSex2LocalSex("M"));
			assertEquals("Ж", WebdicompluginImpl.DicomSex2LocalSex("F"));
			assertEquals("F", WebdicompluginImpl.LocalSex2DicomSex("Ж"));
			assertEquals("M", WebdicompluginImpl.LocalSex2DicomSex("М"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

		

	

}
