package org.psystems.dicom.commons.orm;

import junit.framework.TestCase;

public class EmployeeTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testToPersistentString() {
		Employee emp = new Employee();
		emp.setEmployeeCode("EMP1");
		emp.setEmployeeType(emp.TYPE_DOCTOR);
		emp.setEmployeeName("Doctor Pupkin");
		assertEquals(emp.toPersistentString(), "EMP1^DOCTOR^Doctor Pupkin");

		try {
			emp.setEmployeeType("TTT");
			assertEquals(emp.toPersistentString(), "EMP1^TTT^Doctor Pupkin");
			fail();
		} catch (IllegalArgumentException ex) {
		}

		emp.setEmployeeCode("EMP1 ^ |");
		emp.setEmployeeType(emp.TYPE_DOCTOR);
		emp.setEmployeeName("Doctor ^ | Pupkin");
		assertEquals(emp.toPersistentString(),
				"EMP1 ##### @@@@@^DOCTOR^Doctor ##### @@@@@ Pupkin");

	}

	public void testGetFromPersistentString() {
		Employee emp = Employee
				.getFromPersistentString("EMP1^DOCTOR^Doctor Pupkin");
		assertEquals(emp.getEmployeeCode(), "EMP1");
		assertEquals(emp.getEmployeeType(), "DOCTOR");
		assertEquals(emp.getEmployeeName(), "Doctor Pupkin");

		try {
			emp = Employee.getFromPersistentString("EMP1^DOCTOR^");
			assertEquals(emp.toPersistentString(), "EMP1^TTT^Doctor Pupkin");
			fail();
		} catch (IllegalArgumentException ex) {
		}

		emp = Employee.getFromPersistentString("EMP ##### @@@@@ 1^DOCTOR^Doctor ########## @@@@@ @@@@@ Pupkin");
		assertEquals(emp.getEmployeeCode(), "EMP ^ | 1");
		assertEquals(emp.getEmployeeType(), "DOCTOR");
		assertEquals(emp.getEmployeeName(), "Doctor ^^ | | Pupkin");
	}

}
