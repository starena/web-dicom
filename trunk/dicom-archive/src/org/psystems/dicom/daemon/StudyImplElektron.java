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

		LOG.info("DICOM Driver 'KRT Electron'");
		
		//инициализируем общие теги
		implCommon(dcmObj);
		
		SpecificCharacterSet cs = getCs();
		
		int tagStudyDescriptionDate = 0x00211110;
		int tagStudyType1 = 0x00291106;
		int tagStudyType2 = 0x00291107;
		int tagStudyResult = 0x00211103;
		int tagStudyViewprotocol = 0x00211118;

		// ManufacturerModelName
		ManufacturerModelName = "empty";
		DicomElement element1 = dcmObj.get(Tag.ManufacturerModelName);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			ManufacturerModelName = element1.getValueAsString(cs,
					element1.length());
		}

		// StudyType
		StudyType = "empty";
		element1 = dcmObj.get(tagStudyType1);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			StudyType = element1.getValueAsString(cs, element1.length());
		}
		element1 = dcmObj.get(tagStudyType2);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			StudyType += ", "
					+ element1.getValueAsString(cs, element1.length());
		}

		// StudyResult
		StudyResult = "empty";
		element1 = dcmObj.get(tagStudyResult);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			StudyResult = element1
					.getValueAsString(cs, element1.length());
		}

		// StudyViewProtocol
		StudyViewProtocol = "empty";
		element1 = dcmObj.get(tagStudyViewprotocol);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			StudyViewProtocol = element1.getValueAsString(cs, element1
					.length());
		}
		
	}

}
