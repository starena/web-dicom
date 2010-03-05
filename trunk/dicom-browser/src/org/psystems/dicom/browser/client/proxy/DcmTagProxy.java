package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

/**
 * @author dima_d
 * 
 */
public class DcmTagProxy implements Serializable {

	private static final long serialVersionUID = -7977302129675187320L;

	private Integer idDcm; // ID файла в БД
	private Integer idTag; // ID
	private String major = null;
	private String minor = null;
	private String tagType; // Тип
	private String tagName; // Имя
	private String tagValue; // Значение

	/**
	 * Инициализация класса
	 * 
	 * @param idDcm
	 * @param idTag
	 * @param major
	 * @param minor
	 * @param tagType
	 * @param tagName
	 * @param tagValue
	 */
	public void init(Integer idDcm, Integer idTag, String major, String minor,
			String tagType, String tagName, String tagValue) {
		this.idDcm = idDcm;
		this.major = major;
		this.idTag = idTag;
		this.tagType = tagType;
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	public Integer getIdDcm() {
		return idDcm;
	}

	public Integer getIdTag() {
		return idTag;
	}

	public String getTagType() {
		return tagType;
	}

	public String getTagValue() {
		return tagValue;
	}

	public String getMajor() {
		return major;
	}

	public String getMinor() {
		return minor;
	}

	@Override
	public String toString() {
		return "DcmTagProxy " + idDcm + ";" + idTag + ";" + tagType + ";"
				+ tagName + ";" + tagValue;
	}

}
