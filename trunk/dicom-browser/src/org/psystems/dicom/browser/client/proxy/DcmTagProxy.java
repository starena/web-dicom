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
	private String majorStr = null;
	private String minorStr = null;
	private short major;
	private short minor;
	private String tagType; // Тип
	private String tagName; // Имя
	private String tagValue; // Значение

	/**
	 * Инициализация класса
	 * 
	 * @param idDcm
	 * @param idTag
	 * @param major
	 * @param majorStr
	 * @param minor
	 * @param minorStr
	 * @param tagType
	 * @param tagName
	 * @param tagValue
	 */
	public void init(Integer idDcm, Integer idTag, short major,
			String majorStr, short minor, String minorStr, String tagType,
			String tagName, String tagValue) {
		this.idDcm = idDcm;
		this.major = major;
		this.minor = minor;
		this.majorStr = majorStr;
		this.minorStr = minorStr;
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
	
	

	public short getMajor() {
		return major;
	}

	public short getMinor() {
		return minor;
	}

	public String getMajorStr() {
		return majorStr;
	}

	public String getMinorStr() {
		return minorStr;
	}

	@Override
	public String toString() {
		return "DcmTagProxy id: " + idDcm + " (" + getMajorStr() + ","
				+ getMinorStr() + ") " + tagType + ";" + tagName + ";"
				+ tagValue;
	}

}
