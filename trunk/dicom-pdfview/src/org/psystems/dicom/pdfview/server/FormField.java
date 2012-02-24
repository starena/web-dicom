package org.psystems.dicom.pdfview.server;

public class FormField implements Comparable<FormField> {

	public String fieldName;
	public float upperRightY;

	/**
	 * @param name
	 * @param upperRightY
	 */
	public FormField(String name, float upperRightY) {
		super();
		this.fieldName = name;
		this.upperRightY = upperRightY;
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

	@Override
	public int compareTo(FormField o) {
		if (upperRightY > o.upperRightY)
			return 1;
		if (upperRightY < o.upperRightY)
			return -1;
		return 0;
	}

}
