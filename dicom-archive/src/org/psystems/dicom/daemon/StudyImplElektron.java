package org.psystems.dicom.daemon;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudyImplElektron extends Study {

	private static Logger LOG = LoggerFactory.getLogger(StudyImplElektron.class);
	
	StudyImplElektron(DicomObject dcmObj ) {

		LOG.info(getDebugStr("DICOM Driver 'KRT Electron'"));
		
		//инициализируем общие теги
		implCommon(dcmObj);
		
		SpecificCharacterSet cs = getCs();
		
		int tagStudyDescriptionDate = 0x00211110;
		int tagStudyType = 0x00291106;
		int tagStudyTypeDescription = 0x00291107;
		int tagStudyResult = 0x00211103;
		int tagStudyViewprotocol = 0x00211118;

		

		// StudyType
//		StudyType = "empty";
		DicomElement element = dcmObj.get(tagStudyType);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			StudyType = element.getValueAsString(cs, element.length());
		}
		element = dcmObj.get(tagStudyTypeDescription);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			
			StudyDescription = StudyType + ", "
					+ element.getValueAsString(cs, element.length());
		}

		// StudyResult
//		StudyResult = "empty";
		element = dcmObj.get(tagStudyResult);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			StudyResult = element
					.getValueAsString(cs, element.length());
			if (StudyResult.equalsIgnoreCase("норма")) {
				StudyResult="";
			}
		}

		// StudyViewProtocol
//		StudyViewProtocol = "empty";
		element = dcmObj.get(tagStudyViewprotocol);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			StudyViewProtocol = element.getValueAsString(cs, element
					.length());
		}
		
		// StudyResult
		element = dcmObj.get(Tag.MedicalAlerts);
		if (element != null) {
			setStudyResult(element.getValueAsString(cs, element
					.length()));
			//Костыль. в это теге из Электрона приходит сточка "Без диагноза"
			if (getStudyResult() != null && getStudyResult().equals("Без диагноза")) {
				setStudyResult(null);
			}
		}
		
		//Если заполнен протокол осмотра - то и дату осмотра ставим
		if(StudyViewProtocol !=null && StudyViewProtocol.length()>0) {
			setStudyViewProtocolDate(getStudyDate());
		}
		
	}

}
