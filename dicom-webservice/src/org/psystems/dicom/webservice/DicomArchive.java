package org.psystems.dicom.webservice;

import java.util.ArrayList;
import java.util.Date;

public class DicomArchive {

	public org.psystems.dicom.webservice.Study getStudy(int i) {

		Study study = new Study();
		study.setId(i);
		study.setStudyDate(new Date());
		study.setStudyDescription("исследование № " + i);
		study.setStudyDevice("Аппарат №1");
		study.setStudyDoctor("Врач №1");
		study.setStudyId("studyID=" + i);
		study.setStudyPatient("Пациент №1");
		study.setStudyPatientId("patientID=ХХХ");
		study.setStudyResult("Результат 'норма'");
		study.setStudyType("флюорография");
		study.setStudyUrl("http://localhost/" + i + ".dcm");

		return study;
	}

	public Study[] findStudies(String s) {

		ArrayList<Study> data = new ArrayList<Study>();
		for (int i = 0; i < 10; i++) {
			Study study = new Study();
			study.setId(i);
			study.setStudyDate(new Date());
			study.setStudyDescription("исследование № " + i);
			study.setStudyDevice("Аппарат №1");
			study.setStudyDoctor("Врач №1");
			study.setStudyId("studyID=" + i);
			study.setStudyPatient("Пациент №1");
			study.setStudyPatientId("patientID=ХХХ");
			study.setStudyResult("Результат 'норма'");
			study.setStudyType("флюорография");
			study.setStudyUrl("http://localhost/" + i + ".dcm");
		}

		Study[] result = new Study[data.size()];
		return data.toArray(result);
	}

	/**
	 * Создание нового исследования
	 * @param PatientId
	 * @param Patient
	 * @param patientDateBirthday
	 * @param patientSex
	 * @param studyType
	 * @return
	 */
	public int newStudy(String PatientId, String Patient,
			Date patientDateBirthday, String patientSex, String studyType) {
//
//		private int id;// Идентификатор (внутренний)
//		private String studyDescription; // Описание
//		private String studyResult; // Результат исследования
//		private String studyId;// Идентификатор исследования
//		private String studyUrl; // URL для открытия в обозревателе
//		// private String studyPatient;// ФИО пациента
//		// private String studyPatientId;// Идентификатор пациента
//		private String studyDoctor; // Врач
//		private Date studyDate;// Дата исследования
//		private String studyType;// Вид исследования
//		private String studyDevice;// Аппарат, на котором проводилось
//									// исследование.
		
		return 1;
	}

}