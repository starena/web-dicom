package org.psystems.dicom.webservice.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.psystems.dicom.webservice.clientimg.UserManager;
import org.psystems.dicom.webservice.clientimg.UserManagerService;

public class TestImages {

	private static UserManagerService service;
	private static UserManager port;

//	private static final String WS_URL = "http://localhost:8080/dicom-webservice3/UserManager?wsdl";
//	private static final String WS_URL = "https://proxy.gp1.psystems.org:38081/dicom-webservice3/UserManager?wsdl";
	private static final String WS_URL = "http://localhost:38081/dicom-webservice3/UserManager?wsdl";
	
	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {

		URL url = new URL(WS_URL);
		QName qname = new QName("http://webservice.dicom.psystems.org/", "UserManagerService");

		System.setProperty("javax.net.ssl.keyStore", "client.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "derenok");
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStore", "client.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "derenok");
		
		service = new UserManagerService(url,qname);
		port = service.getUserManagerPort();

		getImage();
	}

	private static void getImage() {
		byte[] img = port.downloadImage("/tmp/test.gif");
		try {
			FileOutputStream fos = new FileOutputStream("c:\\temp\\2.gif");
			fos.write(img);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
