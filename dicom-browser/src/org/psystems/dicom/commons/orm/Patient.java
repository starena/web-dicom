package org.psystems.dicom.commons.orm;

import java.io.Serializable;

/**
 * Пациент
 * 
 * @author dima_d
 * 
 */
public class Patient implements Serializable {

	private static final long serialVersionUID = 1336950569742992093L;
	private String patientId; // ID пациента
	private String patientName; // ФИО пациента
	private String patientShortName; // КБП пациента
	private String patientSex; // Пол пациента (M/F)
	private String patientBirthDate; // Дата рождения пациента
	static String persistentDelimeter = "|";// разделитель структур

	/**
	 * формат строки пациента: ID^ФИО^МКБ^ПОЛ^ДР;
	 * 
	 * @return
	 */
	public String toPersistentString() {
		return patientId + "^" + patientName + "^" + patientSex + "^"
				+ getPatientBirthDate();
	}

	/**
	 * Создание экземпляра из строки
	 * 
	 * @param data
	 * @return
	 */
	public static Patient getFromPersistentString(String data) {
		String[] d = data.split("\\^");
		Patient patient = new Patient();
		patient.setPatientId(d[0]);
		patient.setPatientName(d[1]);
		patient.setPatientSex(d[3]);
		ORMUtil.dateSQLToUtilDate(d[4]);
		patient.setPatientBirthDate(d[4]);
		ORMUtil.makeShortName(patient.getPatientName(), patient
				.getPatientBirthDate());
		return patient;
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

	public void setPatientBirthDate(String patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}

	public String getPatientBirthDate() {
		return patientBirthDate;
	}
	
	/**
	 * Проверка всех полей.
	 */
	public void chechEntity() {
		try {
			if (patientBirthDate != null) {
				ORMUtil.dateSQLToUtilDate(patientBirthDate);
			}
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Patient field Birth Date wrong format: "+ex.getMessage(), ex);
		}

	}

	@Override
	public String toString() {
		return "Patient [patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + ", patientShortName=" + patientShortName + "]";
	}

}
