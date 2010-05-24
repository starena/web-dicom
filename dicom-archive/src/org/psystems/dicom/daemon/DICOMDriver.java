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
		study.cs = cs;

		// StudyInstanceUID
		DicomElement element1 = dcmObj.get(Tag.StudyInstanceUID);
		study.StudyInstanceUID = "empty";
		if (element1 == null) {
			LOG.warn("Study ID (tag: StudyID) is empty!");
		} else {
			study.StudyInstanceUID = element1.getValueAsString(cs, element1
					.length());
		}

		// Modality
		study.Modality = "empty";
		element1 = dcmObj.get(Tag.Modality);
		if (element1 == null) {
			LOG.warn("Study ID (tag: Modality) is empty!");
		} else {
			study.Modality = element1.getValueAsString(cs, element1.length());
		}

		// StudyID
		element1 = dcmObj.get(Tag.StudyID);
		study.StudyID = "empty";
		if (element1 == null) {
			LOG.warn("Study ID (tag: StudyID) is empty!");
		} else {
			study.StudyID = element1.getValueAsString(cs, element1.length());
		}

		// PatientBirthDate
		if (dcmObj.get(Tag.PatientBirthDate) != null) {
			study.PatientBirthDate = new java.sql.Date(dcmObj.get(
					Tag.PatientBirthDate).getDate(false).getTime());
		} else {
			study.PatientBirthDate = new java.sql.Date(0);
			LOG.warn("Patient Birth Date (tag: PatientBirthDate) is empty!");
		}

		// PatientName
		element1 = dcmObj.get(Tag.PatientName);
		study.PatientName = "empty";
		if (element1 == null) {
			LOG.warn("Patien Name (tag: PatientName) is empty!");
		} else {
			study.PatientName = element1
					.getValueAsString(cs, element1.length());
		}

		// PatientID
		element1 = dcmObj.get(Tag.PatientID);
		study.PatientID = "empty";
		if (element1 == null) {
			LOG.warn("Patien ID (tag: PatientID) is empty!");
		} else {
			study.PatientID = element1.getValueAsString(cs, element1.length());
		}

		// PatientSex
		element1 = dcmObj.get(Tag.PatientSex);
		study.PatientSex = "";
		if (element1 == null) {
			LOG.warn("Patient sex (tag: PatientSex) is empty!");
		} else {
			study.PatientSex = element1.getValueAsString(cs, element1.length());
			if (study.PatientSex.length() > 1) {
				LOG.warn("PATIENT_SEX to long [" + study.PatientSex + "]");
				study.PatientSex = study.PatientSex.substring(0, 1);
			}
		}

		// StudyDate
		if (dcmObj.get(Tag.StudyDate) != null) {
			study.StudyDate = new java.sql.Date(dcmObj.get(Tag.StudyDate)
					.getDate(false).getTime());
		} else {
			study.StudyDate = new java.sql.Date(0);
			LOG.warn("Patient Birth Date (tag: StudyDate) is empty!");
		}

		// StudyDoctor (Tag.ReferringPhysicianName)
		study.StudyDoctor = "empty";
		element1 = dcmObj.get(Tag.ReferringPhysicianName);
		if (element1 != null) {
			study.StudyDoctor = element1
					.getValueAsString(cs, element1.length());
			if (study.StudyDoctor == null || study.StudyDoctor.length() == 0) {
				study.StudyDoctor = "not defined";
			}
		}

		// OperatorsName
		study.OperatorsName = "empty";
		element1 = dcmObj.get(Tag.OperatorsName);
		if (element1 != null) {
			study.OperatorsName = element1.getValueAsString(cs, element1
					.length());
			if (study.OperatorsName == null
					|| study.OperatorsName.length() == 0) {
				study.OperatorsName = "not defined";
			}
		}

		// StudyDescription (Tag.MedicalAlerts)
		study.StudyDescription = "empty";
		element1 = dcmObj.get(Tag.MedicalAlerts);
		if (element1 != null) {
			study.StudyDescription = element1.getValueAsString(cs, element1
					.length());
			if (study.StudyDescription == null
					|| study.StudyDescription.length() == 0) {
				study.StudyDescription = "not defined";
			}
		}

		// PatientShortName (это КБП)
		study.PatientShortName = Extractor.makeShortName(study.PatientName,
				study.PatientBirthDate);
		if (study.PatientShortName == null
				|| study.PatientShortName.length() == 0) {
			study.PatientShortName = "notmuch";
		}

		// Date STUDY_VIEW_PROTOCOL_DATE = null;// TODO Проверить Дата ли
		// // возвращается или строка
		// String STUDY_MANUFACTURER_UID = "empty";// TODO Реализовать!!!
		// String DCM_TYPE = "empty";// Тип файла (снимок,
		// // исследование) TODO
		// // Реализовать!!!

		return study;

	}

	/**
	 * Кастомный класс описанием исследования
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

}
