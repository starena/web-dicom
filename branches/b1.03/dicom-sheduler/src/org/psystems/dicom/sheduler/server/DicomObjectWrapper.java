package org.psystems.dicom.sheduler.server;

import java.sql.Date;



public class DicomObjectWrapper {

	private Integer ID;
	private String DCM_FILE_NAME;
	private Date PATIENT_BIRTH_DATE;
	private String PATIENT_NAME;
	private Date STUDY_DATE;
	public Integer getID() {
		return ID;
	}
	public void setID(Integer id) {
		ID = id;
	}
	public String getDCM_FILE_NAME() {
		return DCM_FILE_NAME;
	}
	public void setDCM_FILE_NAME(String dcm_file_name) {
		DCM_FILE_NAME = dcm_file_name;
	}
	public Date getPATIENT_BIRTH_DATE() {
		return PATIENT_BIRTH_DATE;
	}
	public void setPATIENT_BIRTH_DATE(Date patient_birth_date) {
		PATIENT_BIRTH_DATE = patient_birth_date;
	}
	public String getPATIENT_NAME() {
		return PATIENT_NAME;
	}
	public void setPATIENT_NAME(String patient_name) {
		PATIENT_NAME = patient_name;
	}
	public Date getSTUDY_DATE() {
		return STUDY_DATE;
	}
	public void setSTUDY_DATE(Date study_date) {
		STUDY_DATE = study_date;
	}
	
	
	
}
