package org.psystems.dicom.webservice.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.psystems.dicom.webservice.clientimg.UserManager;
import org.psystems.dicom.webservice.clientimg.UserManagerService;

public class TestImages {

	private static UserManagerService service;
	private static UserManager port;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		service = new UserManagerService();
		port = service.getUserManagerPort();

		getImage();
	}

	private static void getImage() {
		byte[] img = port.downloadImage("c:\\temp\\1.gif");
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
