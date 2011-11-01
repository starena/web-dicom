package org.psystems.dicom.archive;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;

/**
 * Драйвер обработки тэгов исслеодвания (при приеме через DICOM)
 * 
 * @author dima_d
 * 
 */
public abstract class DicomDriver {

    // public DicomDriver getInstance(DicomObject dcmObj) {
    // return this;
    //
    // }

    private static Logger logger = Logger.getLogger(DicomDriver.class.getName());

    /**
     * Получение конечного исследования
     * 
     * @param studySrc
     * @param dcmObj TODO
     * @return
     */
    public abstract Study getStudy(Study studySrc, DicomObject dcmObj); 

    /**
     * @param dcmObj
     * @return
     */
    public Study getStudy(DicomObject dcmObj) {

	Study study = getDefaultStudy(dcmObj);
	//TODO сделать
	
	// Создаем экземпляр с дефолтовым разбором тегов

	// выбираем нужный драйвер

	return null;

    }

    /**
     * Получение дефолтового экземпляра исследования
     * 
     * @param dcmObj
     * @return
     */
    protected Study getDefaultStudy(DicomObject dcmObj) {

	SpecificCharacterSet cs;

	Study study = new Study();

	// SpecificCharacterSet
	if (dcmObj.get(Tag.SpecificCharacterSet) != null && dcmObj.get(Tag.SpecificCharacterSet).length() > 0) {
	    cs = SpecificCharacterSet.valueOf(dcmObj.get(Tag.SpecificCharacterSet).getStrings(null, false));
	} else {
	    cs = new CharacterSetCp1251();
	    logger.warn(getDebugStr("Character Ser (tag: SpecificCharacterSet) is empty!"));
	}
	study.setCs(cs);

	// StudyInstanceUID
	DicomElement element = dcmObj.get(Tag.StudyInstanceUID);
	// setStudyInstanceUID("empty");
	if (element == null) {
	    logger.warn(getDebugStr("Study ID (tag: StudyUID) is empty!"));

	} else {
	    study.setStudyInstanceUID(element.getValueAsString(cs, element.length()));
	}

	// Manufacturer
	element = dcmObj.get(Tag.Manufacturer);
	// setManufacturer("empty");
	if (element == null) {
	    logger.warn(getDebugStr("Study ID (tag: Manufacturer) is empty!"));

	} else {
	    study.setManufacturer(element.getValueAsString(cs, element.length()));
	}

	// ManufacturerModelName
	// ManufacturerModelName = "empty";
	element = dcmObj.get(Tag.ManufacturerModelName);
	if (element != null && element.getValueAsString(cs, element.length()).length() > 0) {
	    study.ManufacturerModelName = element.getValueAsString(cs, element.length());
	} else {
	    logger.warn(getDebugStr("Study ID (tag: ManufacturerModelName) is empty!"));
	}

	// Modality
	// setModality("empty");
	element = dcmObj.get(Tag.Modality);
	if (element == null) {
	    logger.warn(getDebugStr("Study ID (tag: Modality) is empty!"));
	} else {
	    study.setModality(element.getValueAsString(cs, element.length()));
	}

	// StudyID
	element = dcmObj.get(Tag.StudyID);
	// setStudyID("empty");
	if (element == null) {
	    logger.warn(getDebugStr("Study ID (tag: StudyID) is empty!"));
	} else {
	    study.setStudyID(element.getValueAsString(cs, element.length()));
	}

	// PatientBirthDate
	if (dcmObj.get(Tag.PatientBirthDate) != null) {
	    study.setPatientBirthDate(new java.sql.Date(dcmObj.get(Tag.PatientBirthDate).getDate(false).getTime()));
	} else {
	    study.setPatientBirthDate(new java.sql.Date(0));
	    logger.warn(getDebugStr("Patient Birth Date (tag: PatientBirthDate) is empty!"));
	}

	// PatientName
	element = dcmObj.get(Tag.PatientName);
	// setPatientName("empty");
	if (element == null) {
	    logger.warn(getDebugStr("Patien Name (tag: PatientName) is empty!"));
	} else {
	    String pname = element.getValueAsString(cs, element.length());
	    study.setPatientName(pname);
	}

	// PatientID
	element = dcmObj.get(Tag.PatientID);
	// setPatientID("empty");
	if (element == null) {
	    logger.warn(getDebugStr("Patien ID (tag: PatientID) is empty!"));
	} else {
	    study.setPatientID(element.getValueAsString(cs, element.length()));
	}

	// PatientSex
	element = dcmObj.get(Tag.PatientSex);
	// setPatientSex("");
	if (element == null) {
	    logger.warn(getDebugStr("Patient sex (tag: PatientSex) is empty!"));
	} else {
	    study.setPatientSex(element.getValueAsString(cs, element.length()));
	    // TODO Убрать проверку на длинну поля "ПОЛ" в конкретный драйвер!
	    if (study.getPatientSex().length() > 1) {
		logger.warn(getDebugStr("PATIENT_SEX to long [" + study.getPatientSex() + "]"));
		study.setPatientSex(study.getPatientSex().substring(0, 1));
	    }
	}

	// StudyDate
	if (dcmObj.get(Tag.StudyDate) != null) {
	    study.setStudyDate(new java.sql.Date(dcmObj.get(Tag.StudyDate).getDate(false).getTime()));
	    // TODO Убрать или передалать!
	    logger.info("tag: StudyDate: " + study.getStudyDate().toString() + " " + study.getStudyDate().getTime());
	} else {
	    study.setStudyDate(null);
	    logger.warn(getDebugStr("Study Date (tag: StudyDate) is empty!"));
	}

	// StudyDoctor (Tag.ReferringPhysicianName)
	// setStudyDoctor("empty");
	element = dcmObj.get(Tag.ReferringPhysicianName);
	if (element != null) {
	    study.setStudyDoctor(element.getValueAsString(cs, element.length()));
	    if (study.getStudyDoctor() == null || study.getStudyDoctor().length() == 0) {
		study.setStudyDoctor("");
	    }
	}

	// OperatorsName
	// setStudyOperator("empty");
	element = dcmObj.get(Tag.OperatorsName);
	if (element != null) {
	    study.setStudyOperator(element.getValueAsString(cs, element.length()));
	    if (study.getStudyOperator() == null || study.getStudyOperator().length() == 0) {
		study.setStudyOperator("");
	    }
	}

	// StudyResult
	element = dcmObj.get(Tag.MedicalAlerts);
	if (element != null) {
	    study.setStudyResult(element.getValueAsString(cs, element.length()));
	}

	// StudyDescription
	element = dcmObj.get(Tag.StudyDescription);
	if (element != null) {
	    study.setStudyDescription(element.getValueAsString(cs, element.length()));
	}

	// StudyViewProtocol
	element = dcmObj.get(Tag.StudyComments);
	if (element != null) {
	    study.setStudyViewProtocol(element.getValueAsString(cs, element.length()));
	}

	// Mime Type document
	element = dcmObj.get(Tag.MIMETypeOfEncapsulatedDocument);
	if (element != null) {
	    study.setMimeType(element.getValueAsString(cs, element.length()));
	}

	// EncapsulatedDocument Size
	element = dcmObj.get(Tag.EncapsulatedDocument);
	if (element != null) {
	    study.setEncapsulatedDocSize(element.getBytes().length);
	}

	// StudyCompletionDate
	if (dcmObj.get(Tag.StudyCompletionDate) != null) {
	    study.setStudyViewProtocolDate(new java.sql.Date(dcmObj.get(Tag.StudyCompletionDate).getDate(false)
		    .getTime()));
	} else {
	    study.setStudyViewProtocolDate(null);
	    logger.warn(getDebugStr("Study Completion Date (tag: StudyCompletionDate) is empty!"));
	}

	// Tag.StudyVerifiedDate;
	// Tag.VerificationDateTime;
	// Tag.StudyCompletionDate;

	// PatientShortName (это КБП)
	study.setPatientShortName(Extractor.makeShortName(study.getPatientName(), study.getPatientBirthDate()));
	if (study.getPatientShortName() == null || study.getPatientShortName().length() == 0) {
	    study.setPatientShortName("notmuch");
	    logger.warn(getDebugStr("Patient Short Name not muched! [" + study.getPatientName() + "]["
		    + study.getPatientBirthDate() + "]"));
	}

	return study;

    }

    /**
     * Получение строки для лога
     * 
     * @param string
     * @return
     */
    protected String getDebugStr(String string) {

	// TODO Сделать префикс
	// DicomElement element = dcmObj.get(Tag.StudyInstanceUID);
	// if (element != null) {
	// debugId = element.getValueAsString(new CharacterSetCp1251(),
	// element.length());
	// }
	return string;
    }
}
