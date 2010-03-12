package org.psystems.dicom.webservice;

import java.util.ArrayList;

public class DicomDB {

	// private org.psystems.dicom.webservice.Study[] study;
	public org.psystems.dicom.webservice.Study getStudy(int i) {
		Study study = new Study();
		study.setId(i);
		study.setDescription("this is [" + i + "] исследование.");
		return study;

	}

	public ArrayList<org.psystems.dicom.webservice.Study> findStudy(String s) {

		ArrayList<Study> data = new ArrayList<Study>();
		for (int i = 0; i < 10; i++) {
			Study study = new Study();
			study.setId(i);
			study.setDescription("this is [" + i + "] исследование по поиску: "
					+ s);
			data.add(study);
		}
		return data;

	}

	public Study[] findStudies(String s) {

		ArrayList<Study> data = new ArrayList<Study>();
		for (int i = 0; i < 10; i++) {
			Study study = new Study();
			study.setId(i);
			study.setDescription("this is [" + i + "] исследование по поиску: "
					+ s);
			data.add(study);
		}

		Study[] result = new Study[data.size()];
		return data.toArray(result);

	}

	public int startTransaction(Study study) {
		return study.getId();
	}
}