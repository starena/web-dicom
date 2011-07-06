package org.psystems.test.xforms.server;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adobe.fdf.FDFDoc;
import com.adobe.fdf.exceptions.FDFException;

public class GetDataServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		
		FDFDoc FdfInput = null;

		// get the length of incoming data
		int howMany = req.getContentLength();

		byte data[] = new byte[howMany];

		// read data into byte array
		req.getInputStream().read(data);
		String s = new String(data,"Cp1251");
		

		// Get the value of the /V key (the value of the specified field
		try {
			// create FDFDoc from data
			FdfInput = new FDFDoc(s.getBytes("Cp1251"));
			
			
			System.out.println("Name="+getFieldValue(FdfInput,"Name"));
			System.out.println("tf1="+getFieldValue(FdfInput,"tf1"));

			
//			res.setContentType("application/vnd.fdf");
//			OutputStream out = res.getOutputStream();
//			FdfInput.Save(out);
//			out.close();
			
			FileOutputStream fos = new FileOutputStream("12345.pdf");
			FdfInput.Save(fos);
			fos.close();
			
			
		} catch (FDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	}


	/**
	 * Получене значения поля с маленьким хак-ом...
	 * 
	 * @param doc
	 * @param fname
	 * @return
	 * @throws FDFException
	 * @throws UnsupportedEncodingException
	 */
	public static String getFieldValue(FDFDoc doc, String fname) throws FDFException, UnsupportedEncodingException {
		return new String((doc.GetValue(fname)).getBytes("ISO-8859-1"),"Cp1251");
	}
	

}