package org.psystems.dicom.pdfview.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.psystems.dicom.commons.Config;
import org.psystems.dicom.commons.ConfigTemplate;
import org.psystems.dicom.pdfview.client.PdfService;
import org.psystems.dicom.pdfview.dto.ConfigTemplateDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.parser.PdfContentReaderTool;
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
		String file = tmplDir + tmplName + ".pdf";

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			PdfReader reader = new PdfReader(fis);

		} catch (Exception e) {
			throw new IllegalAnnotationException(e);
		}

		return result;

	}

	/**
	 * @param resp
	 * @param reader
	 * @param tmplName
	 * @return
	 */
	private ArrayList<FormField> getAllFields(HttpServletResponse resp,
			PdfReader reader, String tmplName) {

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
			FormField ff = new FormField(fieldName, urY);
			fieldsList.add(ff);

		}

		Collections.sort(fieldsList, Collections.reverseOrder());

		return fieldsList;

	}
	
	
	/**
	 * @param resp
	 * @param reader
	 * @param ffield
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private void printFormElt(PdfReader reader,
			FormField ffield) throws UnsupportedEncodingException, IOException {

		Set<String> formParams = reader.getAcroFields().getFields().keySet();
		AcroFields form = reader.getAcroFields();

		for (String fieldName : formParams
				.toArray(new String[formParams.size()])) {

			if (!fieldName.equals(ffield.getFieldName()))
				continue;

			Item field = form.getFieldItem(fieldName);
			PdfDictionary widgetDict = field.getWidget(0);

			// pdf rectangles are stored as [llx, lly, urx, ury]
			PdfArray rectArr = widgetDict.getAsArray(PdfName.RECT); // should
			float llX = rectArr.getAsNumber(0).floatValue();
			float llY = rectArr.getAsNumber(1).floatValue();
			float urX = rectArr.getAsNumber(2).floatValue();
			float urY = rectArr.getAsNumber(3).floatValue();

			// Значение поля
			String value = form.getField(fieldName);

			// переколировка в QUERY_STRING
			String fieldNameDecoded = fieldName.replaceAll("#", "%");
			fieldNameDecoded = URLDecoder.decode(fieldNameDecoded, "UTF-8");

			resp.getWriter().println(
					"[" + urX + ";" + urY + "]" + fieldNameDecoded + ": ");

			// Если комбо или лист
			if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_COMBO
					|| form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_LIST) {

				resp.getWriter().println("<select name='" + fieldName + "'>");
				resp.getWriter().println("<option value=''>");
				for (String fitem : form.getAppearanceStates(fieldName)) {
					resp.getWriter().println(
							"<option value='" + fitem + "'>" + fitem);
					resp.getWriter().println("</option>");
				}
				resp.getWriter().println("</select>");

			}
			// Если чекбокс
			else if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_CHECKBOX) {
				resp.getWriter().println(
						"<input type='checkbox' name='" + fieldName + "'>");
			}
			// Если текстовое поле
			else {
				resp.getWriter().println(
						"<input type='text' name='" + fieldName + "' value='"
								+ value + "'>");

			}

			resp.getWriter().println("<br><br>");

		}

		

	}

}
