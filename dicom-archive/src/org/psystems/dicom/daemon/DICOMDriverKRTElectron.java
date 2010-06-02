package org.psystems.dicom.daemon;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Драйвер для КРТ "Электрон"
 * 
 * @author dima_d
 * 
 */
public class DICOMDriverKRTElectron extends DICOMDriver {

	private static Logger LOG = LoggerFactory.getLogger(DICOMDriverKRTElectron.class);
	
	public Study getStudyImpl(DicomObject dcmObj) {

		LOG.info("DICOM Driver 'KRT Electron'");
		
		
		Study study = super.getStudyImpl(dcmObj);
		SpecificCharacterSet cs = study.getCs();
		
		int tagStudyDescriptionDate = 0x00211110;
		int tagStudyType1 = 0x00291106;
		int tagStudyType2 = 0x00291107;
		int tagStudyResult = 0x00211103;
		int tagStudyViewprotocol = 0x00211118;

		// ManufacturerModelName
		study.ManufacturerModelName = "empty";
		DicomElement element1 = dcmObj.get(Tag.ManufacturerModelName);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			study.ManufacturerModelName = element1.getValueAsString(cs,
					element1.length());
		}

		// StudyType
		study.StudyType = "empty";
		element1 = dcmObj.get(tagStudyType1);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			study.StudyType = element1.getValueAsString(cs, element1.length());
		}
		element1 = dcmObj.get(tagStudyType2);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			study.StudyType += ", "
					+ element1.getValueAsString(cs, element1.length());
		}

		// StudyResult
		study.StudyResult = "empty";
		element1 = dcmObj.get(tagStudyResult);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			study.StudyResult = element1
					.getValueAsString(cs, element1.length());
		}

		// StudyViewProtocol
		study.StudyViewProtocol = "empty";
		element1 = dcmObj.get(tagStudyViewprotocol);
		if (element1 != null
				&& element1.getValueAsString(cs, element1.length()).length() > 0) {
			study.StudyViewProtocol = element1.getValueAsString(cs, element1
					.length());
		}
		
		return study;
	}
}
