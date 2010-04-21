/**
 * 
 */
package org.psystems.dicom.daemon;

import java.sql.Date;

import junit.framework.TestCase;

/**
 * @author dima_d
 * 
 */
public class ExtractorTest extends TestCase {

	/**
	 * @param name
	 */
	public ExtractorTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link org.psystems.dicom.daemon.Extractor#makeShortName(java.lang.String, java.sql.Date)}
	 * .
	 */
	public void testMakeShortName() {
		// fail("Not yet implemented");

		String name = null;
		Date date = null;
		String result = null;

		name = "Деренок Дмирий Владимирович".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);

		name = "Деренок Д В".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);

		name = "Деренок^Д^В".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);

		name = "Деренок^Дмирий^Владимирович".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);
		
		name = "Деренок  Дмирий  Владимирович".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);
		
		name = " Деренок  Дмирий  Владимирович".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);
		
		name = " Деренок  Дмирий  Владимирович ".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);
		
		name = " Деренок Д   В ".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);
		
		name = " Деренок Д   В ".toUpperCase();
		date = Date.valueOf("1974-03-01");
		result = Extractor.makeShortName(name, date);
		assertEquals("ДЕРДВ74", result);

	}

}
