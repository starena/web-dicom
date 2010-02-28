package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author dima_d
 * 
 */
public class DcmFileProxy implements Serializable {

	private static final long serialVersionUID = -7977302129675187420L;

	private Integer id; // ID
	private String dcmFileName; // Имя DCM файла
	private String patientName; // ФИО пациента
	private String patientSex; // Пол пациента
	private String patientId; // ID пациента
	private Date patientBirthDate; // Дата рождения пациента
	private String studyId; // ID исследования
	private Date studyDate; // Дата исследования
	private String studyDoctor; // Врач исследования
	private String studyOperator; // Оператор исследования
	private String studyDescription;// Описание исследования.
	// TODO нужны еще поля

	private ArrayList<DcmImageProxy> images = new ArrayList<DcmImageProxy>();

	/**
	 * Инициализация класса
	 * 
	 * @param id
	 * @param dcmFileName
	 * @param patientName
	 * @param patientSex
	 * @param patientId
	 * @param patientBirthDate
	 * @param studyId
	 * @param studyDate
	 * @param studyDoctor
	 * @param studyOperator
	 * @param studyDescription
	 */
	public void init(Integer id, String dcmFileName, String patientName, String patientSex, String patientId,
			Date patientBirthDate, String studyId, Date studyDate, String studyDoctor, String studyOperator,
			String studyDescription) {

		this.id = id;
		this.dcmFileName = dcmFileName;
		this.patientName = patientName;
		this.patientSex = patientSex;
		this.patientId = patientId;
		this.patientBirthDate = patientBirthDate;
		this.studyId = studyId;
		this.studyDate = studyDate;
		this.studyDoctor = studyDoctor;
		this.studyOperator = studyOperator;
		this.studyDescription = studyDescription;

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

	public String getPatientBirthDateAsString(String pattern) {
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(pattern);
		return dateFormat.format(patientBirthDate);
	}

	public String getPatientSex() {
		return patientSex;
	}

	public String getPatientId() {
		return patientId;
	}

	public String getStudyId() {
		return studyId;
	}

	public Date getStudyDate() {
		return studyDate;
	}

	public String getStudyDateAsString(String pattern) {
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(pattern);
		return dateFormat.format(studyDate);
	}

	public String getStudyDoctor() {
		return studyDoctor;
	}

	public String getStudyOperator() {
		return studyOperator;
	}

	public String getStudyDescription() {
		return studyDescription;
	}

	public ArrayList<DcmImageProxy> getImages() {
		return images;
	}

	public ArrayList<DcmImageProxy> getImagesIds() {
		return images;
	}

	public void setImagesIds(ArrayList<DcmImageProxy> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "DcmFileProxy " + id + ";" + dcmFileName + ";" + patientName + ";" + patientBirthDate + ";"
				+ studyDate + ";images count:" + images.size();
	}

}
