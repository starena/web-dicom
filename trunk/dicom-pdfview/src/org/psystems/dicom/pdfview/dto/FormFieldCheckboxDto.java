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
	private boolean value;

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

}
