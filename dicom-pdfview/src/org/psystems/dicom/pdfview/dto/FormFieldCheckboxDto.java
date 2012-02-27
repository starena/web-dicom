package org.psystems.dicom.pdfview.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Поле PDF-формы (Список выбора)
 * 
 * @author dima_d
 * 
 */
public class FormFieldCheckboxDto extends FormFieldDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<String> values = new ArrayList<String>();

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

}
