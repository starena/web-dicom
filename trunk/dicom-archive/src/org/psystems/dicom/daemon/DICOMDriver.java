package org.psystems.dicom.daemon;

import java.sql.Date;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DICOMDriver {

	private static Logger LOG = LoggerFactory.getLogger(DICOMDriver.class);

	static DICOMDriver getInstance(DicomObject dcmObj) {

		// LookInside
		// (0002,0002) UI #26 [1.2.826.0.1.3680043.2.706.5476834] Media Storage
		// SOP Class UID

		// KRT Electron
		// (0002,0002) UI #26 [1.2.840.10008.5.1.4.1.1.1] Media Storage SOP
		// Class UID
		// (0009,0010) LO #20 [KRT_ELECTRON_PRIVATE] Private Creator Data
		// Element

		SpecificCharacterSet cs;

		// SpecificCharacterSet
		if (dcmObj.get(Tag.SpecificCharacterSet) != null
				&& dcmObj.get(Tag.SpecificCharacterSet).length() > 0) {
			cs = SpecificCharacterSet.valueOf(dcmObj.get(
					Tag.SpecificCharacterSet).getStrings(null, false));
		} else {
			cs = new Win1251CharacterSet();
			LOG.warn("Character Ser (tag: SpecificCharacterSet) is empty!");
		}


		DicomElement element = dcmObj.get(Tag.MediaStorageSOPClassUID);
		String MediaStorageSOPClassUID = element.getValueAsString(cs, element
				.length());

		if (MediaStorageSOPClassUID.equals("1.2.840.10008.5.1.4.1.1.1")) {
			int tag = 0x00090010;
			element = dcmObj.get(tag);
			if (element != null
					&& element.getValueAsString(cs, element.length()).equals(
							"KRT_ELECTRON_PRIVATE")) {
				return new DICOMDriverKRTElectron();
			}
		}
		if (MediaStorageSOPClassUID.equals("1.2.826.0.1.3680043.2.706.5476834")) {
			return new DICOMDriverPACSLookInside();
		}

		// deafault driver
		return new DICOMDriver();

	}

	/**
	 * Получение исследвания
	 * 
	 * @param dcmObj
	 * @return
	 */
	public static CustomStudy getStudy(DicomObject dcmObj) {

		DICOMDriver driver = getInstance(dcmObj);
		CustomStudy study = driver.getStudyImpl(dcmObj);
		return study;

	}

	/**
	 * Получение общей части исследования
	 * 
	 * @param dcmObj
	 * @return
	 */
	public CustomStudy getStudyImpl(DicomObject dcmObj) {
		return getStudyCommon(dcmObj);
	}

	/**
	 * Получение общей части исследования
	 * 
	 * @param dcmObj
	 * @return
	 */
	public CustomStudy getStudyCommon(DicomObject dcmObj) {

		CustomStudy study = new CustomStudy();
		SpecificCharacterSet cs;

		// SpecificCharacterSet
		if (dcmObj.get(Tag.SpecificCharacterSet) != null
				&& dcmObj.get(Tag.SpecificCharacterSet).length() > 0) {
			cs = SpecificCharacterSet.valueOf(dcmObj.get(
					Tag.SpecificCharacterSet).getStrings(null, false));
		} else {
			cs = new Win1251CharacterSet();
			LOG.warn("Character Ser (tag: SpecificCharacterSet) is empty!");
		}
		study.setCs(cs);

		// StudyInstanceUID
		DicomElement element1 = dcmObj.get(Tag.StudyInstanceUID);
		study.setStudyInstanceUID("empty");
		if (element1 == null) {
			LOG.warn("Study ID (tag: StudyID) is empty!");
		} else {
			study.setStudyInstanceUID(element1.getValueAsString(cs, element1
					.length()));
		}

		// Modality
		study.setModality("empty");
		element1 = dcmObj.get(Tag.Modality);
		if (element1 == null) {
			LOG.warn("Study ID (tag: Modality) is empty!");
		} else {
			study.setModality(element1.getValueAsString(cs, element1.length()));
		}

		// StudyID
		element1 = dcmObj.get(Tag.StudyID);
		study.setStudyID("empty");
		if (element1 == null) {
			LOG.warn("Study ID (tag: StudyID) is empty!");
		} else {
			study.setStudyID(element1.getValueAsString(cs, element1.length()));
		}

		// PatientBirthDate
		if (dcmObj.get(Tag.PatientBirthDate) != null) {
			study.setPatientBirthDate(new java.sql.Date(dcmObj.get(
					Tag.PatientBirthDate).getDate(false).getTime()));
		} else {
			study.setPatientBirthDate( new java.sql.Date(0));
			LOG.warn("Patient Birth Date (tag: PatientBirthDate) is empty!");
		}

		// PatientName
		element1 = dcmObj.get(Tag.PatientName);
		study.setPatientName("empty");
		if (element1 == null) {
			LOG.warn("Patien Name (tag: PatientName) is empty!");
		} else {
			study.setPatientName(element1
					.getValueAsString(cs, element1.length()));
		}

		// PatientID
		element1 = dcmObj.get(Tag.PatientID);
		study.setPatientID("empty");
		if (element1 == null) {
			LOG.warn("Patien ID (tag: PatientID) is empty!");
		} else {
			study.setPatientID(element1.getValueAsString(cs, element1.length()));
		}

		// PatientSex
		element1 = dcmObj.get(Tag.PatientSex);
		study.setPatientSex("");
		if (element1 == null) {
			LOG.warn("Patient sex (tag: PatientSex) is empty!");
		} else {
			study.setPatientSex(element1.getValueAsString(cs, element1.length()));
			if (study.getPatientSex().length() > 1) {
				LOG.warn("PATIENT_SEX to long [" + study.getPatientSex() + "]");
				study.setPatientSex(study.getPatientSex().substring(0, 1));
			}
		}

		// StudyDate
		if (dcmObj.get(Tag.StudyDate) != null) {
			study.setStudyDate( new java.sql.Date(dcmObj.get(Tag.StudyDate)
					.getDate(false).getTime()));
		} else {
			study.setStudyDate (new java.sql.Date(0));
			LOG.warn("Patient Birth Date (tag: StudyDate) is empty!");
		}

		// StudyDoctor (Tag.ReferringPhysicianName)
		study.setStudyDoctor("empty");
		element1 = dcmObj.get(Tag.ReferringPhysicianName);
		if (element1 != null) {
			study.setStudyDoctor(element1
					.getValueAsString(cs, element1.length()));
			if (study.getStudyDoctor() == null || study.getStudyDoctor().length() == 0) {
				study.setStudyDoctor("not defined");
			}
		}

		// OperatorsName
		study.setOperatorsName("empty");
		element1 = dcmObj.get(Tag.OperatorsName);
		if (element1 != null) {
			study.setOperatorsName(element1.getValueAsString(cs, element1
					.length()));
			if (study.getOperatorsName() == null
					|| study.getOperatorsName().length() == 0) {
				study.setOperatorsName ("not defined");
			}
		}

		// StudyDescription (Tag.MedicalAlerts)
		study.setStudyDescription("empty");
		element1 = dcmObj.get(Tag.MedicalAlerts);
		if (element1 != null) {
			study.setStudyDescription(element1.getValueAsString(cs, element1
					.length()));
			if (study.getStudyDescription() == null
					|| study.getStudyDescription().length() == 0) {
				study.setStudyDescription( "not defined");
			}
		}

		// PatientShortName (это КБП)
		study.setPatientShortName(Extractor.makeShortName(study.getPatientName(),
				study.getPatientBirthDate()));
		if (study.getPatientShortName() == null
				|| study.getPatientShortName().length() == 0) {
			study.setPatientShortName("notmuch");
		}

		// Date STUDY_VIEW_PROTOCOL_DATE = null;// TODO Проверить Дата ли
		// // возвращается или строка
		// String STUDY_MANUFACTURER_UID = "empty";// TODO Реализовать!!!
		// String DCM_TYPE = "empty";// Тип файла (снимок,
		// // исследование) TODO
		// // Реализовать!!!

		return study;

	}

	
}
