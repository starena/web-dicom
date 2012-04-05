package org.psystems.dicom.webservice;

import java.text.ParseException;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {

	public UtilsTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConvertDate() {
		try {
			assertEquals(Utils.SqlDate2DicomDate("2010-12-30"), "20101230");
			
		} catch (DicomWebServiceException e) {
			e.printStackTrace();
			fail("ParseException "+e);
		}
	}
	
	

}
