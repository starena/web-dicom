package org.psystems.dicom.daemon;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudyImplRenex extends Study {

	private static Logger LOG = LoggerFactory.getLogger(StudyImplRenex.class);
	
	StudyImplRenex(DicomObject dcmObj ) {

		LOG.info(getDebugStr("DICOM Driver 'Renex'"));
		
		//инициализируем общие теги
		implCommon(dcmObj);
		
		SpecificCharacterSet cs = getCs();
		
		int tagStudyDescriptionDate = 0x00211110;
		int tagStudyType = 0x00291106;
		int tagStudyTypeDescription = 0x00291107;
		int tagStudyResult = 0x00211103;
		int tagStudyViewprotocol = 0x00211118;

		

		// StudyType
		StudyType = new String("Флюорография");
		

		// StudyResult
//		StudyResult = "empty";
		DicomElement element = dcmObj.get(tagStudyResult);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			StudyResult = element
					.getValueAsString(cs, element.length());
		}

		// StudyViewProtocol
//		StudyViewProtocol = "empty";
		element = dcmObj.get(tagStudyViewprotocol);
		if (element != null
				&& element.getValueAsString(cs, element.length()).length() > 0) {
			StudyViewProtocol = element.getValueAsString(cs, element
					.length());
		}
		
	}

}
