package org.psystems.dicom.commons.orm;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Запрос на исследование
 * 
 * @author dima_d
 * 
 */
public class QueryDirection {

	private static final long serialVersionUID = -2840335603832244555L;
	private Long id; // Внутренний ID
	private String directionId; // штрих код
	private Date dateDirection;// Дата направления

	private String patientId; // ID пациента
	private String patientName; // ФИО пациента
	private String patientSex; // Пол пациента (M/F)
	private Date patientBirthDate; // Дата рождения пациента (0016,0048) DA

	public static SimpleDateFormat formatSQL = new SimpleDateFormat(
			"yyyy-MM-dd");

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

	/**
	 * @return Формат SQL Date - "гггг.дд.мм"
	 */
	public String getDateDirectionAsString() {
		return formatSQL.format(dateDirection);
	}

	/**
	 * Формат SQL Date - "гггг.дд.мм"
	 * 
	 * @param date
	 */
	public void setDateDirectionAsString(String date) {
		this.dateDirection = java.sql.Date.valueOf(formatSQL.format(date));
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

	/**
	 * Формат SQL Date - "гггг.дд.мм"
	 * 
	 * @return
	 */
	public String getPatientBirthDate() {
		return formatSQL.format(patientBirthDate);
	}

	/**
	 * Формат SQL Date - "гггг.дд.мм"
	 * 
	 * @param date
	 */
	public void setPatientBirthDate(Date date) {
		this.patientBirthDate = java.sql.Date.valueOf(formatSQL.format(date));
	}

}
