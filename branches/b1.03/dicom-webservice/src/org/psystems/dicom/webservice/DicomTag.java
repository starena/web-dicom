package org.psystems.dicom.webservice;

/**
 * Тег DICOM-файла
 * 
 * @author dima_d
 * 
 */
public class DicomTag {

	private String tagName;
	private String tagValue;


	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	public String getTagName() {
		return tagName;
	}

	public String getTagValue() {
		return tagValue;
	}

	@Override
	public String toString() {
		return "DicomTag [tagName=" + tagName + ", tagValue=" + tagValue + "]";
	}

}