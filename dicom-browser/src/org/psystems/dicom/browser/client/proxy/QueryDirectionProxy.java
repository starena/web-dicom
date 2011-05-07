package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

public class QueryDirectionProxy implements Serializable {

	private static final long serialVersionUID = 4968932590531229552L;

	private Long id; // Внутренний ID
	private String directionId; // штрих код
	private String dateDirection;// Дата направления. формат "yyyy-mm-dd"

	private String patientId; // ID пациента
	private String patientName; // ФИО пациента
	private String patientSex; // Пол пациента (M/F)
	private String patientBirthDate; // Дата рождения пациента. формат

	// "yyyy-mm-dd"

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
	 * @param patientBirthDate
	 *            Формат SQL Date - "гггг.дд.мм"
	 */
	public void setPatientBirthDate(String patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}

	@Override
	public String toString() {
		return "QueryDirectionProxy [dateDirection=" + dateDirection
				+ ", directionId=" + directionId + ", id=" + id
				+ ", patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + "]";
	}

}
