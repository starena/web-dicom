package org.psystems.dicom.pdfview.server;

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
	 */
	public FormField(String name) {
		super();
		this.fieldName = name;
		init();
	}

	/**
	 * Разбор имени поля (Поле форматированного ввода дата
	 * DD.MM.YYYY|tag=00030004|format=DD.MM.YYYY)
	 */
	public void init() {
		fieldTitle = fieldName;
		String[] vals = fieldName.split("\\|");
		if(vals.length>0) fieldTitle = vals[0];
		for (String token : vals) {
			if(token.startsWith("tag=")) {
				setTag(token.replaceAll("tag\\=", ""));
			}
			if(token.startsWith("format=")) {
				setFormat(token.replaceAll("format\\=", ""));
			}
		}
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

	public void setFieldNameEncoded(String fieldNameEncoded) {
		this.fieldNameEncoded = fieldNameEncoded;
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

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
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
