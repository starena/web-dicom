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
public class Renex extends DicomDriver {

    private static Logger logger = Logger.getLogger(Renex.class.getName());

    @Override
    public Study getStudy(Study studySrc, DicomObject dcmObj) {
	logger.info("Driver 'Renex'");

	SpecificCharacterSet cs = studySrc.getCs();

	// int tagStudyDescriptionDate = 0x00211110;
	// int tagStudyType = 0x00291106;
	// int tagStudyTypeDescription = 0x00291107;
	int tagStudyResult = 0x00211103;
	int tagStudyViewprotocol = 0x00211118;

	DicomElement element = dcmObj.get(Tag.PatientName); //
	studySrc.setPatientName("empty");
	if (element == null) {
	    logger.warn(getDebugStr("Patien Name (tag: PatientName) is empty!"));
	} else {
	    String pname = element.getValueAsString(cs, element.length());
	    String newname = pname.replaceAll("\\^", " ");
	    studySrc.setPatientName(newname);
	    logger.info("Fix PatientName ["+pname+"] -> [" + newname+"]");
	}

	// StudyType
	studySrc.setStudyType(new String("Флюорография"));

	// StudyResult
	// StudyResult = "empty";
	element = dcmObj.get(tagStudyResult);
	if (element != null && element.getValueAsString(cs, element.length()).length() > 0) {
	    studySrc.setStudyResult(element.getValueAsString(cs, element.length()));
	}

	// StudyViewProtocol
	// StudyViewProtocol = "empty";
	element = dcmObj.get(tagStudyViewprotocol);
	if (element != null && element.getValueAsString(cs, element.length()).length() > 0) {
	    studySrc.setStudyViewProtocol(element.getValueAsString(cs, element.length()));
	}

	return studySrc;
    }

}
