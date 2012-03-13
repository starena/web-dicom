package org.psystems.dicom.pdfview.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Properties;

import com.google.gwt.dev.util.collect.HashMap;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.AcroFields.Item;

/**
 * Фабрика PDF-полей
 * 
 * @author dima_d
 * 
 */
public class FormFieldFactory {

	/**
	 * @param form
	 * @param fieldName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static FormField getFormFieldInstance(AcroFields form,
			String fieldName) throws UnsupportedEncodingException {

		HashMap<String, String> atts = getFieldAtts(fieldName);

		Item field = form.getFieldItem(fieldName);
		PdfDictionary widgetDict = field.getWidget(0);

		// pdf rectangles are stored as [llx, lly, urx, ury]
		PdfArray rectArr = widgetDict.getAsArray(PdfName.RECT); // should
		float llX = rectArr.getAsNumber(0).floatValue();
		float llY = rectArr.getAsNumber(1).floatValue();
		float urX = rectArr.getAsNumber(2).floatValue();
		float urY = rectArr.getAsNumber(3).floatValue();

		String value = form.getField(fieldName);
		// перекодировка в QUERY_STRING
		// String fieldNameDecoded = fieldName.replaceAll("#", "%");
		// fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");

		FormField ff = null;

		// Если комбо или лист
		if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_COMBO
				|| form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_LIST) {

			ff = new FormFieldList(fieldName);
			ArrayList<String> opts = new ArrayList<String>();
			for (String opt : form.getAppearanceStates(fieldName)) {
				opts.add(opt);
			}
			((FormFieldList) ff).setValues(opts);
		}
		// Если чекбокс
		else if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_CHECKBOX) {

			ff = new FormFieldCheckbox(fieldName);
		}
		// Если радиокнопка
		else if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_RADIOBUTTON) {

			ff = new FormFieldRadioBtn(fieldName);
			ArrayList<String> opts = new ArrayList<String>();
			for (String opt : form.getAppearanceStates(fieldName)) {
				opts.add(opt);
			}
			((FormFieldRadioBtn) ff).setValues(opts);
		}
		// Если текстовое поле
		else {
			// Если календарик
			if (atts.get("format") != null
					&& (atts.get("format").equalsIgnoreCase("dd.mm.yyyy") || 
							atts.get("format").equalsIgnoreCase("dd_mm_yyyy"))) {

				ff = new FormFieldDate(fieldName);
				
			} else {
				ff = new FormField(fieldName);
			}
			
		}

		ff.setUpperRightY(urY);
		ff.setUpperRightX(urX);
		ff.setLowerLeftY(llY);
		ff.setLowerLeftX(llX);
		ff.setValue(value);

		return ff;

	}

	/**
	 * Получение аттрибутов поля (|tag=00030004|format=DD_MM_YYYY)
	 * 
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HashMap<String, String> getFieldAtts(String name)
			throws UnsupportedEncodingException {

		String fieldNameEnc = name.replaceAll("#", "%");
		fieldNameEnc = URLDecoder.decode(fieldNameEnc, "UTF-8");
		String fieldNameEncoded = fieldNameEnc;

		HashMap<String, String> props = new HashMap<String, String>();

		props.put("encname", fieldNameEncoded);

		String fieldTitle = fieldNameEncoded;
		String[] vals = fieldNameEncoded.split("\\|");
		if (vals.length > 0)
			fieldTitle = vals[0];
		props.put("title", fieldTitle);

		for (String token : vals) {
			if (token.startsWith("tag=")) {
				String tag = token.replaceAll("tag\\=", "");
				props.put("tag", tag);
			}
			if (token.startsWith("format=")) {
				String format = token.replaceAll("format\\=", "");
				props.put("format", format);
			}
		}
		return props;
	}

}
