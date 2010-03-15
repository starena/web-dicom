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
	private String studyDescriptionDate;// Дата описания исследования.
	private String studyType;// Вид исследования.
	private String studyDescription;// Описание исследования.
	private String studyViewprotocol;// Протокол осмотра
	private String studyResult;// Результат исследования.
	private String ManufacturerModelName; // Аппарат
	// TODO нужны еще поля

	private ArrayList<DcmImageProxy> images = new ArrayList<DcmImageProxy>();

	/**
	 * Инициализация класса
	 * 
	 * @param id
	 * @param dcmFileName
	 * @param ManufacturerModelName
	 * @param patientName
	 * @param patientSex
	 * @param patientId
	 * @param patientBirthDate
	 * @param studyId
	 * @param studyType
	 * @param studyDate
	 * @param studyDescriptionDate
	 * @param studyDoctor
	 * @param studyOperator
	 * @param studyDescription
	 * @param studyViewprotocol
	 * @param studyResult
	 */
	public void init(Integer id, String dcmFileName,
			String ManufacturerModelName, String patientName,
			String patientSex, String patientId, Date patientBirthDate,
			String studyId, String studyType, Date studyDate,
			String studyDescriptionDate, String studyDoctor,
			String studyOperator, String studyDescription,
			String studyViewprotocol, String studyResult) {

		this.id = id;
		this.dcmFileName = dcmFileName;
		this.ManufacturerModelName = ManufacturerModelName;
		this.patientName = patientName;
		this.patientSex = patientSex;
		this.patientId = patientId;
		this.patientBirthDate = patientBirthDate;
		this.studyId = studyId;
		this.studyType = studyType;
		this.studyDate = studyDate;
		this.studyDescriptionDate = studyDescriptionDate;
		this.studyDoctor = studyDoctor;
		this.studyOperator = studyOperator;
		this.studyDescription = studyDescription;
		this.studyViewprotocol = studyViewprotocol;
		this.studyResult = studyResult;

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

	public String getStudyResult() {
		return studyResult;
	}

	public String getStudyDescriptionDate() {
		return studyDescriptionDate;
	}

	public String getStudyType() {
		return studyType;
	}

	public String getManufacturerModelName() {
		return ManufacturerModelName;
	}

	public String getStudyViewprotocol() {
		return studyViewprotocol;
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
		return "DcmFileProxy " + id + ";" + dcmFileName + ";" + patientName
				+ ";" + patientBirthDate + ";" + studyDate + " ["
				+ getStudyResult() + "] images count:" + images.size();
	}

}
