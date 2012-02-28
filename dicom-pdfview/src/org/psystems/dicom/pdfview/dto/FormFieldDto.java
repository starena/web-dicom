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
	private String fieldName;
	private String fieldNameEncoded;
	private String value;
	public float upperRightY;
	public float upperRightX;
	public float lowerLeftY;
	public float lowerLeftX;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFieldNameEncoded() {
		return fieldNameEncoded;
	}

	public void setFieldNameEncoded(String fieldNameEncoded) {
		this.fieldNameEncoded = fieldNameEncoded;
	}

	public float getUpperRightY() {
		return upperRightY;
	}

	public void setUpperRightY(float upperRightY) {
		this.upperRightY = upperRightY;
	}

	public float getUpperRightX() {
		return upperRightX;
	}

	public void setUpperRightX(float upperRightX) {
		this.upperRightX = upperRightX;
	}

	public float getLowerLeftY() {
		return lowerLeftY;
	}

	public void setLowerLeftY(float lowerLeftY) {
		this.lowerLeftY = lowerLeftY;
	}

	public float getLowerLeftX() {
		return lowerLeftX;
	}

	public void setLowerLeftX(float lowerLeftX) {
		this.lowerLeftX = lowerLeftX;
	}

}
