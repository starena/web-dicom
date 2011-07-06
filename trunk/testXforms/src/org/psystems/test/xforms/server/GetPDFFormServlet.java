package org.psystems.test.xforms.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adobe.fdf.exceptions.FDFException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;

public class GetPDFFormServlet extends HttpServlet {

	private String file = "C:\\WORK\\workspace\\testXforms\\war\\PDFs\\ooform.pdf";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

//		req.setCharacterEncoding("cp1251");
		
		try {

			FileInputStream fis = new FileInputStream(file);
			PdfReader reader = new PdfReader(fis);
			OutputStream out = resp.getOutputStream();
			PdfStamper stamper = new PdfStamper(reader, out);

			PushbuttonField button = new PushbuttonField(stamper.getWriter(),
					new Rectangle(90, 60, 140, 190), "submit");
			button.setText("POST2");
			button.setBackgroundColor(new GrayColor(0.7f));

			// button.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);

			PdfFormField submit = button.getField();
			
			submit.setAction(PdfAction.createSubmitForm(req.getServletPath(),
					null, PdfAction.SUBMIT_HTML_FORMAT));
			
//			submit.setAction(PdfAction.createSubmitForm("http://localhost:8080/template",
//					null, PdfAction.SUBMIT_HTML_FORMAT));
		
			stamper.addAnnotation(submit, 1);

			resp.setContentType("application/pdf; charset=UTF-8");
//			resp.setCharacterEncoding("UTF-8");

			stamper.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			
			req.setCharacterEncoding("cp1251");
		
		System.out.println("! CS = " + req.getCharacterEncoding() + "; " + req.getParameter("charset"));
			
			for( Enumeration iter = req.getParameterNames(); iter.hasMoreElements();) {
				String name = (String) iter.nextElement();
				System.out.println("!!! name="+name + "=" + req.getParameter(name));
			}
			

			FileInputStream fis = new FileInputStream(file);
			PdfReader reader = new PdfReader(fis);
			OutputStream out = resp.getOutputStream();

			PdfStamper stamper = new PdfStamper(reader, out);

			AcroFields fields = stamper.getAcroFields();
			 Set<String> parameters = fields.getFields().keySet();
	            for (String parameter : parameters) {
	            	
	            	
	            	System.out.println("!!!   parameter="+parameter + " TYPE="+fields.getFieldType(parameter) +
	            			"["  +req.getParameter(parameter)+"]");
	            
	            	//пропускаем радиобаттоны
	            	if(fields.getFieldType(parameter)==AcroFields.FIELD_TYPE_RADIOBUTTON ) continue;
	            	
	            	if(req.getParameter(parameter)!=null) {
	                fields.setField(parameter, req.getParameter(parameter));
	            	}
	            	fields.setFieldProperty(parameter, "setfflags", TextField.READ_ONLY, null);
	            }

			
			 

			

//			stamper.setFormFlattening(true);

			resp.setContentType("application/pdf");
			stamper.close();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}