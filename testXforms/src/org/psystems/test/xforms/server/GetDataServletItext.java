package org.psystems.test.xforms.server;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adobe.fdf.FDFDoc;
import com.adobe.fdf.exceptions.FDFException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.TextField;

public class GetDataServletItext extends HttpServlet {
	
	private HashMap<String, String> formValues = new HashMap<String, String>();
	private PdfStamper stamper;

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

			formValues.put("Name", getFieldValue(FdfInput,"Name"));
			formValues.put("tf1", getFieldValue(FdfInput,"tf1"));
			
//			res.setContentType("application/vnd.fdf");
//			OutputStream out = res.getOutputStream();
//			FdfInput.Save(out);
//			out.close();
			
//			FileOutputStream fos = new FileOutputStream("12345.pdf");
//			FdfInput.Save(fos);
//			fos.close();
			
			PdfReader reader;
	        stamper = null;

	        FileInputStream fis = new FileInputStream("C:\\WORK\\workspace\\testXforms\\war\\PDFs\\ooform.pdf");
	        reader = new PdfReader(fis);
	        
//	        FileOutputStream fos = new FileOutputStream("C:\\WORK\\workspace\\testXforms\\war\\PDFs\\ooform-out.pdf");
			res.setContentType("application/pdf");
			OutputStream out = res.getOutputStream();
	        
	        try {
//				stamper = new PdfStamper(reader,fos);
				stamper = new PdfStamper(reader,out);
				fill(stamper.getAcroFields());
				
//				System.out.println("!!! F:"+stamper.getAcroFields().getField("Name"));
				
				stamper.close();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        
	        fis.close();
			
		} catch (FDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	}


	private void fill(AcroFields form) throws IOException, DocumentException {
		form.setField("Name", formValues.get("Name"));
		form.removeField("tf1");
		form.setFieldProperty("Name", "setfflags", TextField.READ_ONLY, null);
		form.removeField("Name");
		
		
		String data = "ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"------------------------------ \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n" +
				"ффвфы фывфы фыв фыв фывфыв фы фы \n";
		
		 PdfContentByte canvas = stamper.getOverContent(1);
	     ColumnText.showTextAligned(canvas,
	                Element.ALIGN_LEFT, new Phrase("ффвфы фывфы фыв фыв фывфыв фы фы"), 36, 540, 0);


//		form.setFieldProperty("text_2", "bordercolor", BaseColor.RED, null);

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