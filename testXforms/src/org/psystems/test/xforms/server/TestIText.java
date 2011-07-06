package org.psystems.test.xforms.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class TestIText {

	private String file = "C:\\WORK\\workspace\\testXforms\\war\\PDFs\\ooform.pdf";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestIText();
	}

	public TestIText() {
		super();
		
		try {
			FileInputStream fis;
			fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream("C:\\WORK\\workspace\\testXforms\\war\\PDFs\\ooform-out.pdf");
			PdfReader reader = new PdfReader(fis);
			PdfStamper stamper = new PdfStamper(reader, fos);

			AcroFields fields = stamper.getAcroFields();
			Set<String> parameters = fields.getFields().keySet();
            for (String parameter : parameters) {
            	
           
            	//пропускаем радиобаттоны
            	if(fields.getFieldType(parameter)==AcroFields.FIELD_TYPE_RADIOBUTTON ) continue;
            
                fields.setField(parameter, "тест");
            }
		

//		stamper.setFormFlattening(true);
		stamper.close();
		fis.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	

}
