package org.psystems.dicom.pdfview.server;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class FormFieldDateTest /*extends GWTTestCase*/ {

	@Test
	public void testSetValue() throws UnsupportedEncodingException, ParseException {
		
		FormFieldDate field = new FormFieldDate("test_date");
		field.setValue("30.12.2012");
		assertEquals("30.12.2012", field.getValue());
		assertEquals("20121230", field.getValueAsDicomFmt());
		
		SimpleDateFormat dateFormatUser = new SimpleDateFormat(
		"dd.MM.yyyy");
		
		assertEquals(dateFormatUser.parse("30.12.2012"), field.getValueAsDate());
//		fail("Not yet implemented");
		
//		FormFieldDateDto fieldDto = new FormFieldDateDto();
//		fieldDto.setValue("30.12.2012");
	}

}
