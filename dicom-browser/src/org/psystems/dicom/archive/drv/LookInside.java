package org.psystems.dicom.archive.drv;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.psystems.dicom.archive.DicomDriver;
import org.psystems.dicom.archive.Study;

/**
 * 
 * Дорайвер для аппарата КРТ "Электрон"
 * 
 * @author dima_d
 * 
 */
public class LookInside extends DicomDriver {

    private static Logger logger = Logger.getLogger(LookInside.class.getName());

    @Override
    public Study getStudy(Study studySrc, DicomObject dcmObj) {
	logger.info("Driver 'LookInside'");

	SpecificCharacterSet cs = studySrc.getCs();

	//TODO Реализовать!
	
//	int tagPatientName = 0x34950022;
//	int tagPatientBirthDate = 0x349500B2;
//	int tagPatientSex = 0x34950042;
//	int tagDoctor =  0x34970062;
//	int tagOpeator = 0x34970052;
//	int tagStudyType = 0x34970002;
//	int tagStudyResult = 0x34970082;
//	int tagStudyViewprotocol = 0x349700C2;
//	
//	int tagStudyDescriptionDateDate = 0x34970012;
//	int tagStudyDescriptionDateTime = 0x34970022;
//	
//	
//	
//
//	// PatientName
//	String cusomPatientName = null;
//	DicomElement element = dcmObj.get(tagPatientName);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()).length() > 0) {
//		cusomPatientName = element.getValueAsString(cs,
//				element.length());
//		cusomPatientName = cusomPatientName.replaceAll("\\^", " ");
//		setPatientName(cusomPatientName);
//	}
//	
//	
//	
//	// PatientBirthDate
//	String cusomPatientBirthDate = null;
//	element = dcmObj.get(tagPatientBirthDate);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()).length() > 0) {
//		cusomPatientBirthDate = element.getValueAsString(cs,
//				element.length());
//		setPatientBirthDate(Date.valueOf(cusomPatientBirthDate));
//	}
//	
//	
//	
//	// PatientSex
//	String cusomPatientSex = null;
//	element = dcmObj.get(tagPatientSex);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()).length() > 0) {
//		cusomPatientSex = element.getValueAsString(cs,
//				element.length());
//		
//		if(cusomPatientSex != null) {
//			if (cusomPatientSex.length() > 1) {
//				LOG.warn(getDebugStr("PATIENT_SEX to long [" + cusomPatientSex + "]"));
//				cusomPatientSex = cusomPatientSex.substring(0, 1);
//			}
//			if (cusomPatientSex.equals("М")) {
//				LOG.warn(getDebugStr("PATIENT_SEX bad type [" + cusomPatientSex + "]"));
//				cusomPatientSex = "M";
//			}
//			if (cusomPatientSex.equals("Ж")) {
//				LOG.warn(getDebugStr("PATIENT_SEX bad type [" + cusomPatientSex + "]"));
//				cusomPatientSex = "F";
//			}
//		}
//		setPatientSex(cusomPatientSex);
//	}
//	
//	
//	
//	// PatientShortName (это КБП)
//	setPatientShortName(Extractor.makeShortName(getPatientName(),
//			getPatientBirthDate()));
//	if (getPatientShortName() == null
//			|| getPatientShortName().length() == 0) {
//		setPatientShortName("notmuch");
//	}
//	
//	
//	// Doctor
//	String customDoctor = null;
//	element = dcmObj.get(tagDoctor);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()) != null) {
//		customDoctor = element.getValueAsString(cs,
//				element.length());
//		setStudyDoctor(customDoctor);
//	}
//	
//	// Operator
//	String customOperator = null;
//	element = dcmObj.get(tagOpeator);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()) != null) {
//		customOperator = element.getValueAsString(cs,
//				element.length());
//		setStudyOperator(customOperator);
//	}
//	
//	// StudyType
//	String customStudyType = null;
//	element = dcmObj.get(tagStudyType);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()) != null) {
//		customStudyType = element.getValueAsString(cs,
//				element.length());
//		setStudyType(customStudyType);
//	}
//	
//	// StudyResult
//	String customStudyResult = null;
//	element = dcmObj.get(tagStudyResult);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()) != null) {
//		customStudyResult = element.getValueAsString(cs,
//				element.length());
//		setStudyResult(customStudyResult);
//	}
//	
//	// StudyViewProtocol
//	String customStudyViewProtocol = null;
//	element = dcmObj.get(tagStudyViewprotocol);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()) != null) {
//		customStudyViewProtocol = element.getValueAsString(cs,
//				element.length());
//		setStudyViewProtocol(customStudyViewProtocol);
//	}
//	
//	// PatientBirthDate
//	String StudyDescriptionDate = null;
//	String StudyDescriptionTime = null;
//	
//	
//	
//	element = dcmObj.get(tagStudyDescriptionDateDate);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()) != null) {
//		StudyDescriptionDate = element.getValueAsString(cs,
//				element.length());
//	}
//	
//	element = dcmObj.get(tagStudyDescriptionDateTime);
//	if (element != null
//			&& element.getValueAsString(cs, element.length()) != null) {
//		StudyDescriptionTime = element.getValueAsString(cs,
//				element.length());
//	}
//	
//	if(StudyDescriptionDate != null && StudyDescriptionTime != null) {
//		//TODO Дата описания = Дата проведеия исследования
//		setStudyViewProtocolDate(Date.valueOf(StudyDescriptionDate));
//		setStudyDate(Date.valueOf(StudyDescriptionDate));
//	}
	
	
	

	return studySrc;
    }

}
