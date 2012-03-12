package org.psystems.dicom.pdfview.server;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Test;

public class FormFieldTest {

	@Test
	public void testInit() throws UnsupportedEncodingException {

		String fieldName;
		FormField field;
		String fieldNameDecoded;

		fieldName = "Поле форматированного ввода дата DD.MM.YYYY|tag=00030004|format=DD.MM.YYYY";
		field = new FormField(fieldName);

		// перекодировка в QUERY_STRING
		fieldNameDecoded = fieldName.replaceAll("#", "%");
		fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");
		field.setFieldNameEncoded(fieldNameDecoded);

		assertEquals(field.getFieldName(), fieldName);
		assertEquals(field.getFieldNameEncoded(), fieldName);
		assertEquals(field.getFieldTitle(),
				"Поле форматированного ввода дата DD.MM.YYYY");
		assertEquals(field.getTag(), "00030004");
		assertEquals(field.getFormat(), "DD.MM.YYYY");

		// ------------------------------

		fieldName = "Поле форматированного ввода дата DD.MM.YYYY|tag=00030004";
		field = new FormField(fieldName);

		// перекодировка в QUERY_STRING
		fieldNameDecoded = fieldName.replaceAll("#", "%");
		fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");
		field.setFieldNameEncoded(fieldNameDecoded);

		assertEquals(field.getFieldName(), fieldName);
		assertEquals(field.getFieldNameEncoded(), fieldName);
		assertEquals(field.getFieldTitle(),
				"Поле форматированного ввода дата DD.MM.YYYY");
		assertEquals(field.getTag(), "00030004");
		assertEquals(field.getFormat(), null);

		// ------------------------------

		fieldName = "Поле форматированного ввода дата DD.MM.YYYY";
		field = new FormField(fieldName);

		// перекодировка в QUERY_STRING
		fieldNameDecoded = fieldName.replaceAll("#", "%");
		fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");
		field.setFieldNameEncoded(fieldNameDecoded);

		assertEquals(field.getFieldName(), fieldName);
		assertEquals(field.getFieldNameEncoded(), fieldName);
		assertEquals(field.getFieldTitle(),
				"Поле форматированного ввода дата DD.MM.YYYY");
		assertEquals(field.getTag(), null);
		assertEquals(field.getFormat(), null);

		// ------------------------------

		fieldName = "Поле форматированного ввода дата DD.MM.YYYY|format=DD.MM.YYYY";
		field = new FormField(fieldName);

		// перекодировка в QUERY_STRING
		fieldNameDecoded = fieldName.replaceAll("#", "%");
		fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");
		field.setFieldNameEncoded(fieldNameDecoded);

		assertEquals(field.getFieldName(), fieldName);
		assertEquals(field.getFieldNameEncoded(), fieldName);
		assertEquals(field.getFieldTitle(),
				"Поле форматированного ввода дата DD.MM.YYYY");
		assertEquals(field.getTag(), null);
		assertEquals(field.getFormat(), "DD.MM.YYYY");

	}

}
