package org.psystems.dicom.webservice;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;

/**
 * @author dima_d
 * 
 *         http://jax-ws.java.net/
 * 
 */
@WebService()
@MTOM
public class UserManager {

	/**
	 * Web service operation
	 */
	@WebMethod(operationName = "addUser")
	public boolean addUser(@WebParam(name = "userName") String userName) {
		System.out.println(userName);
		return true;
	}

	public Image downloadImage(String name) {

		try {

			File image = new File(name);
			return ImageIO.read(image);

		} catch (IOException e) {

			e.printStackTrace();
			return null;

		}
	}

	public String uploadImage(Image data) throws DicomWebServiceException {

		if (data != null) {
			// store somewhere
			return "Upload Successful";
		}

		throw new DicomWebServiceException("Upload Failed!");

	}

}
