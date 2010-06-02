package org.psystems.dicom.daemon;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.StringUtils;

import junit.framework.TestCase;

public class StudyTest extends TestCase {

	public StudyTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetInstance() {
		
		DicomObject dcmObj;
		DicomInputStream din = null;
		String fileName = "test/data/elektron/1_1.2.826.0.1.3680043.2.634.0.19902.201041.165915.1.dcm";
		try {

			File f = new File(fileName);
			din = new DicomInputStream(f);
			dcmObj = din.readDicomObject();
			Study study = Study.getInstance(dcmObj);
			assertEquals(study.getClass().getName(), "org.psystems.dicom.daemon.StudyImplElektron");
			assertEquals(study.getStudyInstanceUID(), "1.2.826.0.1.3680043.2.634.30.1.94286.20100401164900");

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}
	}
	
}
