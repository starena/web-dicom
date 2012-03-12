package org.psystems.dicom.pdfview.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FormFieldList extends FormField {

	public FormFieldList(String name) throws UnsupportedEncodingException {
		super(name);
	}

	private ArrayList<String> values = new ArrayList<String>();

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

}
