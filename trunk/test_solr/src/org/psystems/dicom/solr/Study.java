package org.psystems.dicom.solr;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class Study {
	@Field
	String id;

	@Field("studyId")
	String study_Id;

	String patientName;

	String studyDescription;

	@Field("dia")
	String[] diagnozis;

	@Field
	List<String> services;

	public String getId() {
		return id;
	}

	@Field
	public void setId(String id) {
		this.id = id;
	}

	public String getStudy_Id() {
		return study_Id;
	}

	@Field("studyId")
	public void setStudy_Id(String study_Id) {
		this.study_Id = study_Id;
	}

	public String getPatientName() {
		return patientName;
	}

	@Field
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getStudyDescription() {
		return studyDescription;
	}

	@Field
	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String[] getDiagnozis() {
		return diagnozis;
	}

	@Field("dia")
	public void setDiagnozis(String[] diagnozis) {
		this.diagnozis = diagnozis;
	}

	public List<String> getServices() {
		return services;
	}

	@Field
	public void setServices(List<String> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return "Study [id=" + id + ", study_Id=" + study_Id + ", patientName="
				+ patientName + ", studyDescription=" + studyDescription
				+ ", diagnozis=" + Arrays.toString(diagnozis) + ", services="
				+ services + ", getDiagnozis()="
				+ Arrays.toString(getDiagnozis()) + ", getServices()="
				+ getServices() + "]";
	}
	
	

}
