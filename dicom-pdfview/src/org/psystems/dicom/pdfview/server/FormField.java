package org.psystems.dicom.pdfview.server;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class FormField implements Comparable<FormField> {

	private String fieldName;
	private String fieldTitle;
	private String fieldNameEncoded;// перекодирован в QUERY_STRING
	private String value;

	private float upperRightY;
	private float upperRightX;
	private float lowerLeftY;
	private float lowerLeftX;

	/**
	 * Перечень "автораспознаваемых" тегов: (Operator #00081070)(Doctor
	 * #00080090)(StudyDate #00080020) (StudyViewprotocolDate #00321050)
	 * (StudyDescription #00081030) (StudyResult #00102000) (StudyComments
	 * #00324000)
	 */
	private String tag;// DICOM tag

	private String format;// Формат поля (например dd.mm.yyyy для ввода даты)

	/**
	 * @param name
	 * @param upperRightY
	 * @throws UnsupportedEncodingException
	 */
	public FormField(String name) throws UnsupportedEncodingException {
		super();
		this.fieldName = name;

		HashMap<String, String> atts = FormFieldFactory.getFieldAtts(fieldName);
		if (atts.get("encname") != null)
			fieldNameEncoded = atts.get("encname");
		if (atts.get("title") != null)
			fieldTitle = atts.get("title");
		if (atts.get("tag") != null)
			tag = atts.get("tag");
		if (atts.get("format") != null)
			format = atts.get("format");
	}

	public String getFieldName() {
		return fieldName;
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

	public String getFieldNameEncoded() {
		return fieldNameEncoded;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public String getTag() {
		return tag;
	}

	public String getFormat() {
		return format;
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
