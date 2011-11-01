package org.psystems.dicom.archive.drv;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.psystems.dicom.archive.DicomDriver;
import org.psystems.dicom.archive.Study;

/**
 * 
 * Дорайвер для аппарата КРТ "Электрон"
 * 
 * @author dima_d
 * 
 */
public class KRTElectron extends DicomDriver {

    private static Logger logger = Logger.getLogger(KRTElectron.class.getName());

    @Override
    public Study getStudy(Study studySrc, DicomObject dcmObj) {

	logger.info("Driver 'KRT Electron'");
	SpecificCharacterSet cs = studySrc.getCs();

//	int tagStudyDescriptionDate = 0x00211110;
	int tagStudyType = 0x00291106;
	int tagStudyTypeDescription = 0x00291107;
	int tagStudyResult = 0x00211103;
	int tagStudyViewprotocol = 0x00211118;

	// StudyType
	// StudyType = "empty";
	DicomElement element = dcmObj.get(tagStudyType);
	if (element != null && element.getValueAsString(cs, element.length()).length() > 0) {
	    studySrc.setStudyType(element.getValueAsString(cs, element.length()));
	}
	element = dcmObj.get(tagStudyTypeDescription);
	if (element != null && element.getValueAsString(cs, element.length()).length() > 0) {

	    studySrc.setStudyDescription(studySrc.getStudyType() + ", "
		    + element.getValueAsString(cs, element.length()));
	}

	// StudyResult
	// StudyResult = "empty";
	element = dcmObj.get(tagStudyResult);
	if (element != null && element.getValueAsString(cs, element.length()).length() > 0) {
	    studySrc.setStudyResult(element.getValueAsString(cs, element.length()));
	    if (studySrc.getStudyResult().equalsIgnoreCase("норма")) {
		studySrc.setStudyResult("");
	    }
	}

	// StudyViewProtocol
	// StudyViewProtocol = "empty";
	element = dcmObj.get(tagStudyViewprotocol);
	if (element != null && element.getValueAsString(cs, element.length()).length() > 0) {
	    studySrc.setStudyViewProtocol(element.getValueAsString(cs, element.length()));
	}

	// StudyResult
	element = dcmObj.get(Tag.MedicalAlerts);
	if (element != null) {
	    studySrc.setStudyResult(element.getValueAsString(cs, element.length()));
	    // Костыль. в это теге из Электрона приходит сточка "Без диагноза"
	    if (studySrc.getStudyResult() != null && studySrc.getStudyResult().equals("Без диагноза")) {
		studySrc.setStudyResult(null);
	    }
	}

	// Если заполнен протокол осмотра - то и дату осмотра ставим
	if (studySrc.getStudyViewProtocol() != null && studySrc.getStudyViewProtocol().length() > 0) {
	    studySrc.setStudyViewProtocolDate(studySrc.getStudyDate());
	}

	return studySrc;
    }

}
