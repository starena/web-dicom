package org.psystems.dicom.commons.orm.entity;

import org.psystems.dicom.commons.orm.ORMUtil;


/**
 * Запрос направлений
 * 
 * @author dima_d
 * 
 */
public class QueryDirection {

	private static final long serialVersionUID = -2840335603832244555L;
	private Long id; // Внутренний ID
	private String directionId; // штрих код
	private String dateDirection;// Дата направления. формат "yyyy-mm-dd"

	private String patientId; // ID пациента
	private String patientName; // ФИО пациента
	private String patientSex; // Пол пациента (M/F)
	private String patientBirthDate; // Дата рождения пациента. формат "yyyy-mm-dd"

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDirectionId() {
		return directionId;
	}

	public void setDirectionId(String directionId) {
		this.directionId = directionId;
	}

	public String getDateDirection() {
		return dateDirection;
	}

	
	/**
	 * @param dateDirection Формат SQL Date - "гггг.дд.мм"
	 */
	public void setDateDirection(String dateDirection) {
		this.dateDirection = dateDirection;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	
	public String getPatientBirthDate() {
		return patientBirthDate;
	}

	
	/**
	 * @param patientBirthDate Формат SQL Date - "гггг.дд.мм"
	 */
	public void setPatientBirthDate(String patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}
	
	/**
	 * Проверка всех полей.
	 */
	public void chechEntity() {
		String field = null;
		try {
			if (dateDirection != null) {
				field = "dateDirection";
				ORMUtil.dateSQLToUtilDate(dateDirection);
			}
			if (patientBirthDate != null) {
				field = "patientBirthDate";
				ORMUtil.dateSQLToUtilDate(patientBirthDate);
			}
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("field " + field + " ", ex);
		}
	}

	@Override
	public String toString() {
		return "QueryDirection [dateDirection=" + dateDirection
				+ ", directionId=" + directionId + ", id=" + id
				+ ", patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + "]";
	}
	
	

}
