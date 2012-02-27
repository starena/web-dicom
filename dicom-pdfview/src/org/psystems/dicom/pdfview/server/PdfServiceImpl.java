package org.psystems.dicom.pdfview.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.psystems.dicom.commons.Config;
import org.psystems.dicom.commons.ConfigTemplate;
import org.psystems.dicom.pdfview.client.PdfService;
import org.psystems.dicom.pdfview.dto.ConfigTemplateDto;
import org.psystems.dicom.pdfview.dto.FormFieldCheckboxDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;
import org.psystems.dicom.pdfview.dto.FormFieldListDto;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.sun.xml.internal.txw2.IllegalAnnotationException;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PdfServiceImpl extends RemoteServiceServlet implements PdfService {

	@Override
	public ArrayList<ConfigTemplateDto> getTemplates()
			throws IllegalArgumentException {

		ArrayList<ConfigTemplateDto> result = new ArrayList<ConfigTemplateDto>();
		for (ConfigTemplate tmpl : Config.getTemplates()) {
			ConfigTemplateDto tmplDto = new ConfigTemplateDto();
			tmplDto.setDescription(tmpl.getDescription());
			tmplDto.setFontsize(tmpl.getFontsize());
			tmplDto.setModality(tmpl.getModality());
			tmplDto.setName(tmpl.getName());
			result.add(tmplDto);
		}
		return result;
	}

	@Override
	public ArrayList<FormFieldDto> getFormFields(String tmplName)
			throws IllegalArgumentException {

		ArrayList<FormFieldDto> result = new ArrayList<FormFieldDto>();
		String tmplDir = Config.getTemplateFolder();
		String file = tmplDir + File.separator + tmplName + ".pdf";

		FileInputStream fis;
		try {

			fis = new FileInputStream(file);
			PdfReader reader = new PdfReader(fis);
			for (FormField ffield : getAllFields(reader)) {

				FormFieldDto dto = null;

				// Если комбо или лист
				if (ffield instanceof FormFieldList) {
					dto = new FormFieldListDto();
					((FormFieldListDto) dto).setValues(((FormFieldList) ffield)
							.getValues());
				}
				// Если чекбокс
				else if (ffield instanceof FormFieldCheckbox) {
					dto = new FormFieldCheckboxDto();
				}
				// Если текстовое поле
				else {
					dto = new FormFieldDto();
				}

				dto.setFieldName(ffield.getFieldName());
				dto.setFieldNameEncoded(ffield.getFieldNameEncoded());
				dto.setValue(ffield.getValue());
				dto.setUpperRightY(ffield.getUpperRightY());
				result.add(dto);
			}

			reader.close();

		} catch (Exception e) {
			throw new IllegalAnnotationException(e);
		}

		return result;

	}

	/**
	 * 
	 * Получение всех полей формы
	 * 
	 * @param resp
	 * @param reader
	 * @param tmplName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private ArrayList<FormField> getAllFields(PdfReader reader)
			throws UnsupportedEncodingException {

		ArrayList<FormField> fieldsList = new ArrayList<FormField>();
		Set<String> parameters = reader.getAcroFields().getFields().keySet();
		AcroFields form = reader.getAcroFields();
		String[] fields = parameters.toArray(new String[parameters.size()]);

		for (String fieldName : fields) {

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
			String fieldNameDecoded = fieldName.replaceAll("#", "%");
			fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");

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
			// Если текстовое поле
			else {

				ff = new FormField(fieldName);
			}

			ff.setFieldNameEncoded(fieldNameDecoded);
			ff.setUpperRightY(urY);
			ff.setValue(value);
			fieldsList.add(ff);

		}

		Collections.sort(fieldsList, Collections.reverseOrder());

		return fieldsList;

	}

	/**
	 * @param reader
	 * @param ffield
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	// private FormFieldDto getFormEltDto(PdfReader reader, FormField ffield)
	// throws UnsupportedEncodingException, IOException {
	//
	// Set<String> formParams = reader.getAcroFields().getFields().keySet();
	// AcroFields form = reader.getAcroFields();
	//
	// for (String fieldName : formParams
	// .toArray(new String[formParams.size()])) {
	//
	// if (!fieldName.equals(ffield.getFieldName()))
	// continue;
	//
	// Item field = form.getFieldItem(fieldName);
	// PdfDictionary widgetDict = field.getWidget(0);
	//
	// // pdf rectangles are stored as [llx, lly, urx, ury]
	// PdfArray rectArr = widgetDict.getAsArray(PdfName.RECT); // should
	// float llX = rectArr.getAsNumber(0).floatValue();
	// float llY = rectArr.getAsNumber(1).floatValue();
	// float urX = rectArr.getAsNumber(2).floatValue();
	// float urY = rectArr.getAsNumber(3).floatValue();
	//
	// // Значение поля
	// String value = form.getField(fieldName);
	//
	// // переколировка в QUERY_STRING
	// String fieldNameDecoded = fieldName.replaceAll("#", "%");
	// fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");
	//
	// resp.getWriter().println(
	// "[" + urX + ";" + urY + "]" + fieldNameDecoded + ": ");
	//
	// // Если комбо или лист
	// if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_COMBO
	// || form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_LIST) {
	//
	// resp.getWriter().println("<select name='" + fieldName + "'>");
	// resp.getWriter().println("<option value=''>");
	// for (String fitem : form.getAppearanceStates(fieldName)) {
	// resp.getWriter().println(
	// "<option value='" + fitem + "'>" + fitem);
	// resp.getWriter().println("</option>");
	// }
	// resp.getWriter().println("</select>");
	//
	// }
	// // Если чекбокс
	// else if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_CHECKBOX)
	// {
	// resp.getWriter().println(
	// "<input type='checkbox' name='" + fieldName + "'>");
	// }
	// // Если текстовое поле
	// else {
	// resp.getWriter().println(
	// "<input type='text' name='" + fieldName + "' value='"
	// + value + "'>");
	//
	// }
	//
	// resp.getWriter().println("<br><br>");
	//
	// }
	//
	// }

}
