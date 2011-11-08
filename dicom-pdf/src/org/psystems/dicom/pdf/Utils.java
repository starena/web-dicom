package org.psystems.dicom.pdf;

import java.io.File;
import java.util.ArrayList;

public class Utils {

    public ArrayList<String> getPDFTemplates() {
	ArrayList<String> result = new ArrayList<String>();
	File[] files = new File("./pdfs").listFiles();
	for (File file : files) {
	   if(file.getName().endsWith(".pdf")) {
	       result.add(file.getName());
	   }
	}
	return result;
    }
}
