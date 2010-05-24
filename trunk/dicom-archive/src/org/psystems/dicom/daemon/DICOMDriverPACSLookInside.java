package org.psystems.dicom.daemon;

import org.dcm4che2.data.DicomObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Драйвер для КРТ "Электрон"
 * 
 * @author dima_d
 * 
 */
public class DICOMDriverPACSLookInside extends DICOMDriver {

	private static Logger LOG = LoggerFactory.getLogger(DICOMDriverPACSLookInside.class);
	
	public CustomStudy getStudyImpl(DicomObject dcmObj) {

		LOG.info("DICOM Driver 'PACS LookInside'");
		
		CustomStudy study = super.getStudyImpl(dcmObj);
		
		
		return study;
	}
}
