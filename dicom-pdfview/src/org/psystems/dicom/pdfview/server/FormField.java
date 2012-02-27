package org.psystems.dicom.pdfview.server;

public class FormField implements Comparable<FormField> {

	public String fieldName;
	private String fieldNameEncoded;// перекодирован в QUERY_STRING
	private String value;
	public float upperRightY;

	/**
	 * @param name
	 * @param upperRightY
	 */
	public FormField(String name) {
		super();
		this.fieldName = name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String name) {
		this.fieldName = name;
	}

	public float getUpperRightY() {
		return upperRightY;
	}

	public void setUpperRightY(float upperRightY) {
		this.upperRightY = upperRightY;
	}

	public String getFieldNameEncoded() {
		return fieldNameEncoded;
	}

	public void setFieldNameEncoded(String fieldNameEncoded) {
		this.fieldNameEncoded = fieldNameEncoded;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(FormField o) {
		if (upperRightY > o.upperRightY)
			return 1;
		if (upperRightY < o.upperRightY)
			return -1;
		return 0;
	}

}
