package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.Date;

/**
 * Пациент
 * 
 * @author dima_d
 * 
 */
public class Patient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1336950569742992093L;
	private String patientId; // ID пациента
	private String patientName; // ФИО пациента
	private String patientShortName; // КБП пациента
	private String patientSex; // Пол пациента (M/F)
	private Date patientBirthDate; // Дата рождения пациента

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

	public String getPatientShortName() {
		return patientShortName;
	}

	public void setPatientShortName(String patientShortName) {
		this.patientShortName = patientShortName;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public Date getPatientBirthDate() {
		return patientBirthDate;
	}

	public void setPatientBirthDate(Date patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}

	@Override
	public String toString() {
		return "Patient [patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + ", patientShortName=" + patientShortName + "]";
	}

}
