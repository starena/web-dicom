package org.psystems.dicom.pdfview.dto;

import java.io.Serializable;

/**
 * Поле PDF-формы
 * 
 * @author dima_d
 * 
 */
public class FormFieldDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
