package org.psystems.dicom.daemon;

import org.dcm4che2.data.SpecificCharacterSet;

/**
 * Кастомный класс описания исследования
 * 
 * @author dima_d
 * 
 */
public class CustomStudy {

	private SpecificCharacterSet cs;
	private String StudyInstanceUID;
	private String Modality;
	private String StudyID;
	private java.sql.Date PatientBirthDate;
	private String PatientName;
	private String PatientID;
	private String PatientSex;
	private java.sql.Date StudyDate;
	private String StudyDoctor;// Tag.ReferringPhysicianName
	private String OperatorsName;
	private String StudyDescription;// Tag.MedicalAlerts
	private String PatientShortName;
	protected String ManufacturerModelName;
	protected String StudyType;
	protected String StudyResult;
	protected String StudyViewProtocol;
	protected String ManufacturerUID = "empty"; // TODO Manufacturer в
	// файлах не фигурирует...
	private java.sql.Date StudyViewProtocolDate = null;// TODO в файлах не
	// фигурирует

	protected String DcmType = "empty"; // TODO Тип файла (снимок,

	// описание). пока не сделано.

	public void setCs(SpecificCharacterSet cs) {
		this.cs = cs;
	}

	public void setStudyInstanceUID(String studyInstanceUID) {
		StudyInstanceUID = studyInstanceUID;
	}

	public void setModality(String modality) {
		Modality = modality;
	}

	public void setStudyID(String studyID) {
		StudyID = studyID;
	}

	public void setPatientBirthDate(java.sql.Date patientBirthDate) {
		PatientBirthDate = patientBirthDate;
	}

	public void setPatientName(String patientName) {
		PatientName = patientName;
	}

	public void setPatientID(String patientID) {
		PatientID = patientID;
	}

	public void setPatientSex(String patientSex) {
		PatientSex = patientSex;
	}

	public void setStudyDate(java.sql.Date studyDate) {
		StudyDate = studyDate;
	}

	public void setStudyDoctor(String studyDoctor) {
		StudyDoctor = studyDoctor;
	}

	public void setOperatorsName(String operatorsName) {
		OperatorsName = operatorsName;
	}

	public void setStudyDescription(String studyDescription) {
		StudyDescription = studyDescription;
	}

	public void setPatientShortName(String patientShortName) {
		PatientShortName = patientShortName;
	}

	public void setManufacturerModelName(String manufacturerModelName) {
		ManufacturerModelName = manufacturerModelName;
	}

	public void setStudyType(String studyType) {
		StudyType = studyType;
	}

	public void setStudyResult(String studyResult) {
		StudyResult = studyResult;
	}

	public void setStudyViewProtocol(String studyViewProtocol) {
		StudyViewProtocol = studyViewProtocol;
	}

	public void setManufacturerUID(String manufacturerUID) {
		ManufacturerUID = manufacturerUID;
	}

	public void setStudyViewProtocolDate(java.sql.Date studyViewProtocolDate) {
		StudyViewProtocolDate = studyViewProtocolDate;
	}

	public void setDcmType(String dcmType) {
		DcmType = dcmType;
	}

	public SpecificCharacterSet getCs() {
		return cs;
	}

	public String getStudyInstanceUID() {
		return StudyInstanceUID;
	}

	public String getModality() {
		return Modality;
	}

	public String getStudyID() {
		return StudyID;
	}

	public java.sql.Date getPatientBirthDate() {
		return PatientBirthDate;
	}

	public String getPatientName() {
		return PatientName;
	}

	public String getPatientID() {
		return PatientID;
	}

	public String getPatientSex() {
		return PatientSex;
	}

	public java.sql.Date getStudyDate() {
		return StudyDate;
	}

	public String getStudyDoctor() {
		return StudyDoctor;
	}

	public String getOperatorsName() {
		return OperatorsName;
	}

	public String getStudyDescription() {
		return StudyDescription;
	}

	public String getPatientShortName() {
		return PatientShortName;
	}

	public String getManufacturerModelName() {
		return ManufacturerModelName;
	}

	public String getStudyType() {
		return StudyType;
	}

	public String getStudyResult() {
		return StudyResult;
	}

	public String getStudyViewProtocol() {
		return StudyViewProtocol;
	}

	public String getManufacturerUID() {
		return ManufacturerUID;
	}

	public java.sql.Date getStudyViewProtocolDate() {
		return StudyViewProtocolDate;
	}

	public String getDcmType() {
		return DcmType;
	}

}
