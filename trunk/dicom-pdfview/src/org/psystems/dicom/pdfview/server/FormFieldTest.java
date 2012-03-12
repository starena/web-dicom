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

		// ------------------------------

		fieldName = "#D0#9F#D0#BE#D0#BB#D0#B5#20#D1#84#D0#BE#D1#80#D0#BC#D0#B0#D1#82#D0#B8#D1#80#D0#BE#D0#B2#D0#B0#D0#BD#D0#BD#D0#BE#D0#B3#D0#BE#20#D0#B2#D0#B2#D0#BE#D0#B4#D0#B0#20#D0#B4#D0#B0#D1#82#D0#B0#20DD_MM_YYYY|tag=00030004|format=DD_MM_YYYY";
		fieldNameDecoded = fieldName.replaceAll("#", "%");
		fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");

		field = new FormField(fieldName);

		assertEquals(field.getFieldName(), fieldName);
		assertEquals(field.getFieldNameEncoded(), fieldNameDecoded);
		assertEquals(field.getFieldTitle(),
				"Поле форматированного ввода дата DD_MM_YYYY");
		assertEquals(field.getTag(), "00030004");
		assertEquals(field.getFormat(), "DD_MM_YYYY");

		// ------------------------------

		fieldName = "#D0#9F#D0#BE#D0#BB#D0#B5#20#D1#84#D0#BE#D1#80#D0#BC#D0#B0#D1#82#D0#B8#D1#80#D0#BE#D0#B2#D0#B0#D0#BD#D0#BD#D0#BE#D0#B3#D0#BE#20#D0#B2#D0#B2#D0#BE#D0#B4#D0#B0#20#D0#B4#D0#B0#D1#82#D0#B0#20DD_MM_YYYY|format=DD_MM_YYYY";
		fieldNameDecoded = fieldName.replaceAll("#", "%");
		fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");

		field = new FormField(fieldName);

		assertEquals(field.getFieldName(), fieldName);
		assertEquals(field.getFieldNameEncoded(), fieldNameDecoded);
		assertEquals(field.getFieldTitle(),
				"Поле форматированного ввода дата DD_MM_YYYY");
		assertEquals(field.getTag(), null);
		assertEquals(field.getFormat(), "DD_MM_YYYY");

		// ------------------------------

		fieldName = "#D0#9F#D0#BE#D0#BB#D0#B5#20#D1#84#D0#BE#D1#80#D0#BC#D0#B0#D1#82#D0#B8#D1#80#D0#BE#D0#B2#D0#B0#D0#BD#D0#BD#D0#BE#D0#B3#D0#BE#20#D0#B2#D0#B2#D0#BE#D0#B4#D0#B0#20#D0#B4#D0#B0#D1#82#D0#B0#20DD_MM_YYYY|tag=00030004";
		fieldNameDecoded = fieldName.replaceAll("#", "%");
		fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");

		field = new FormField(fieldName);

		assertEquals(field.getFieldName(), fieldName);
		assertEquals(field.getFieldNameEncoded(), fieldNameDecoded);
		assertEquals(field.getFieldTitle(),
				"Поле форматированного ввода дата DD_MM_YYYY");
		assertEquals(field.getTag(), "00030004");
		assertEquals(field.getFormat(), null);

	}

}
