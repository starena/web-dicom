/**
 * 
 */
package org.psystems.webdicom2.ws.dto;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.psystems.webdicom2.ws.WsException;

/**
 * @author ddv
 *
 */
public class DirectionTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setBarCode(java.lang.String)}.
	 */
	@Test
	public void testSetBarCode() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setMisId(java.lang.String)}.
	 */
	@Test
	public void testSetMisId() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setPatientId(java.lang.String)}.
	 */
	@Test
	public void testSetPatientId() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setPatientName(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test
	public void testSetPatientName() throws WsException {
		Direction drn = new Direction();
		drn.setPatientName("Derenok^D^V");
		assertEquals("Derenok^D^V",drn.getPatientName());
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setPatientName(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test(expected=WsException.class)
	public void testSetPatientNameFaultType() throws WsException {
		Direction drn = new Direction();
		drn.setPatientName("Derenok");
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setPatientName(java.lang.String)}.
	 * @throws WsException 
	 */
	public void testSetPatientTranslit() throws WsException {
		Direction drn = new Direction();
		drn.setPatientName("Деренок^Д^В");
		assertEquals("Derenok^D^V",drn.getPatientName());
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setPatientNameTranslit(java.lang.String)}.
	 */
	@Test
	public void testSetPatientNameTranslit() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setSex(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test
	public void testSetSex() throws WsException {
		Direction drn = new Direction();
		drn.setSex("M");
		assertEquals(drn.getSex(), "M");
		drn.setSex("F");
		assertEquals(drn.getSex(), "F");
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setSex(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test(expected=WsException.class)
	public void testSetSexFail() throws WsException {
		Direction drn = new Direction();
		drn.setSex("Z");
		drn.setSex("");
		drn.setSex(null);
	}


	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setDateBirsday(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test
	public void testSetDateBirsday() throws WsException {
		Direction drn = new Direction();
		drn.setDateBirsday("1974/03/01");
		assertEquals("1974/03/01", drn.getDateBirsday());
	}
	

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setDateBirsday(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test(expected=WsException.class)
	public void testSetDateBirsdayFail() throws WsException {
		Direction drn = new Direction();
		drn.setDateBirsday("1974/03/1");
		drn.setDateBirsday("1974/3/01");
		drn.setDateBirsday("19740301");
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setDateBirsdayDicom(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test
	public void testSetDateBirsdayDicom() throws WsException {
		Direction drn = new Direction();
		drn.setDateBirsday("1974/03/01");
		assertEquals("19740301", drn.getDateBirsdayDicom());
		drn.setDateBirsdayDicom("19740303");
		assertEquals("19740303", drn.getDateBirsdayDicom());
		assertEquals("1974/03/03", drn.getDateBirsday());
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setDateBirsdayDicom(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test(expected=WsException.class)
	public void testSetDateBirsdayDicomFail() throws WsException {
		Direction drn = new Direction();
//		drn.setDateBirsday("1974031");
		drn.setDateBirsdayDicom("1974033");
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setModality(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test
	public void testSetModality() throws WsException {
		Direction drn = new Direction();
		drn.setModality("MG");
		assertEquals("MG",drn.getModality());
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setModality(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test(expected=WsException.class)
	public void testSetModalityFault() throws WsException {
		Direction drn = new Direction();
		drn.setModality("M");
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setDateStudy(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test
	public void testSetDateStudy() throws WsException {
		Direction drn = new Direction();
		drn.setDateStudy("2013/11/25");
		assertEquals("2013/11/25", drn.getDateStudy());
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setDateStudy(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test(expected=WsException.class)
	public void testSetDateStudyFault() throws WsException {
		Direction drn = new Direction();
		drn.setDateStudy("2013/11/5");
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setDateStudyDicom(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test
	public void testSetDateStudyDicom() throws WsException {
		Direction drn = new Direction();
		drn.setDateStudyDicom("20131125");
		assertEquals("2013/11/25", drn.getDateStudy());
	}
	
	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setDateStudyDicom(java.lang.String)}.
	 * @throws WsException 
	 */
	@Test(expected=WsException.class)
	public void testSetDateStudyDicomFault() throws WsException {
		Direction drn = new Direction();
		drn.setDateStudyDicom("2013115");
		assertEquals("2013/11/25", drn.getDateStudy());
	}

	/**
	 * Test method for {@link org.psystems.webdicom2.ws.dto.Direction#setServiceName(java.lang.String)}.
	 */
	@Test
	public void testSetServiceName() {
		fail("Not yet implemented");
	}

}
