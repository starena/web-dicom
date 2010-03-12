package org.psystems.dicom.webservice;

import java.util.Date;

public class Study {

	private int id;// Идентификатор (внутренний)
	private String studyDescription; // Описание
	private String studyResult; // Результат исследования
	private String studyId;// Идентификатор исследования
	private String studyUrl; // URL для открытия в обозревателе
	private String studyPatient;// ФИО пациента
	private String studyPatientId;// Идентификатор пациента
	private String studyDoctor; // Врач
	private Date studyDate;// Дата исследования
	private String studyType;// Вид исследования
	private String studyDevice;// Аппарат, на котором проводилось исследование.

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStudyDescription() {
		return studyDescription;
	}

	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String getStudyResult() {
		return studyResult;
	}

	public void setStudyResult(String studyResult) {
		this.studyResult = studyResult;
	}

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}

	public String getStudyUrl() {
		return studyUrl;
	}

	public void setStudyUrl(String studyUrl) {
		this.studyUrl = studyUrl;
	}

	public String getStudyDoctor() {
		return studyDoctor;
	}

	public void setStudyDoctor(String studyDoctor) {
		this.studyDoctor = studyDoctor;
	}

	public Date getStudyDate() {
		return studyDate;
	}

	public void setStudyDate(Date studyDate) {
		this.studyDate = studyDate;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public String getStudyDevice() {
		return studyDevice;
	}

	public void setStudyDevice(String studyDevice) {
		this.studyDevice = studyDevice;
	}

	public String getStudyPatient() {
		return studyPatient;
	}

	public void setStudyPatient(String studyPatient) {
		this.studyPatient = studyPatient;
	}

	public String getStudyPatientId() {
		return studyPatientId;
	}

	public void setStudyPatientId(String studyPatientId) {
		this.studyPatientId = studyPatientId;
	}

}