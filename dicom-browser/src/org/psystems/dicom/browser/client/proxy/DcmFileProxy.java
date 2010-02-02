package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * @author dima_d
 * 
 */
public class DcmFileProxy implements Serializable {

	private static final long serialVersionUID = -7977302129675187420L;

	private Integer id; // ID
	private String dcmFileName; // Имя DCM файла
	private String patientName; // ФИО пациента
	private Date patientBirthDate; // Дата рождения пациента
	private Date studyDate; // Дата исследования

	/**
	 * Инициализация класса
	 * 
	 * @param id
	 * @param dcmFileName
	 * @param patientName
	 * @param patientBirthDate
	 * @param studyDate
	 */
	public void init(Integer id, String dcmFileName, String patientName,
			Date patientBirthDate, Date studyDate) {
		this.id = id;
		this.dcmFileName = dcmFileName;
		this.patientName = patientName;
		this.patientBirthDate = patientBirthDate;
		this.studyDate = studyDate;
	}

	public Integer getId() {
		return id;
	}

	public String getDcmFileName() {
		return dcmFileName;
	}

	public String getPatientName() {
		return patientName;
	}

	public Date getPatientBirthDate() {
		return patientBirthDate;
	}

	@Override
	public String toString() {
		return "DcmFileProxy " + id + ";" + dcmFileName + ";" + patientName
				+ ";" + patientBirthDate + ";" + studyDate;
	}

}
