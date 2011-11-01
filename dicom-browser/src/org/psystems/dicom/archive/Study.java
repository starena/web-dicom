package org.psystems.dicom.archive;

import org.dcm4che2.data.SpecificCharacterSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Кастомный класс описания исследования
 * 
 * @author dima_d
 * 
 */
public class Study {

    private SpecificCharacterSet cs;
    private String StudyInstanceUID = "";
    private String Modality = "";
    private String StudyID = "";
    private java.sql.Date PatientBirthDate = null;
    private String PatientName = "";
    private String PatientID = "";
    private String PatientSex = "";
    private java.sql.Date StudyDate = null;
    private String StudyDoctor = "";// Tag.ReferringPhysicianName
    private String StudyOperator = "";
    protected String StudyDescription = "";// Tag.StudyDestination
    private String PatientShortName = "";
    protected String ManufacturerModelName = "";
    // Tag.Manufacturer
    // (0008,0070) LO #18 [JV HELPIC (MOSCOW)] Manufacturer
    protected String Manufacturer = "";
    protected String StudyType = "";
    protected String StudyResult = "";
    protected String StudyViewProtocol = "";
    private String MimeType = "";
    private long encapsulatedDocSize = 0;

    // TODO Manufacturer в файлах не фигурирует...
    protected String ManufacturerUID = "not implemented";

    // TODO в файлах не фигурирует
    private java.sql.Date StudyViewProtocolDate = null;

    // TODO Тип файла (снимок, описание). пока не сделано.
    protected String DcmType = "not implemented";

   
    

   
    
    



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

    public void setStudyOperator(String operatorsName) {
	StudyOperator = operatorsName;
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

    public String getStudyOperator() {
	return StudyOperator;
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

    public String getManufacturer() {
	return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
	Manufacturer = manufacturer;
    }

    public String getMimeType() {
	return MimeType;
    }

    public void setMimeType(String mimeType) {
	MimeType = mimeType;
    }

    public long getEncapsulatedDocSize() {
	return encapsulatedDocSize;
    }

    public void setEncapsulatedDocSize(long encapsulatedDocSize) {
	this.encapsulatedDocSize = encapsulatedDocSize;
    }

}
