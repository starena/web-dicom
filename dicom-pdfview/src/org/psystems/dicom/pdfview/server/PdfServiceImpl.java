package org.psystems.dicom.pdfview.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.psystems.dicom.commons.Config;
import org.psystems.dicom.commons.ConfigTemplate;
import org.psystems.dicom.pdfview.client.PdfService;
import org.psystems.dicom.pdfview.dto.ConfigTemplateDto;
import org.psystems.dicom.pdfview.dto.FormFieldCheckboxDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;
import org.psystems.dicom.pdfview.dto.FormFieldListDto;
import org.psystems.dicom.pdfview.dto.FormFieldRadioBtnDto;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
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
			for (FormField ffield : getPDFFields(reader)) {

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
				// Если радиокнопка
				else if (ffield instanceof FormFieldRadioBtn) {

					dto = new FormFieldRadioBtnDto();
					((FormFieldRadioBtnDto) dto)
							.setValues(((FormFieldRadioBtn) ffield).getValues());

				}
				// Если текстовое поле
				else {
					dto = new FormFieldDto();
				}

				dto.setFieldName(ffield.getFieldName());
				dto.setFieldNameEncoded(ffield.getFieldNameEncoded());
				dto.setFieldTitle(ffield.getFieldTitle());
				dto.setTag(ffield.getTag());
				dto.setFormat(ffield.getFormat());
				dto.setValue(ffield.getValue());
				dto.setUpperRightX(ffield.getUpperRightX());
				dto.setUpperRightY(ffield.getUpperRightY());
				dto.setLowerLeftX(ffield.getLowerLeftX());
				dto.setLowerLeftY(ffield.getLowerLeftY());
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
	 * Получение всех полей PDf-формы
	 * 
	 * @param resp
	 * @param reader
	 * @param tmplName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private ArrayList<FormField> getPDFFields(PdfReader reader)
			throws UnsupportedEncodingException {

		ArrayList<FormField> fieldsList = new ArrayList<FormField>();
		// Set<String> parameters = reader.getAcroFields().getFields().keySet();
		AcroFields form = reader.getAcroFields();
		// String[] fields = parameters.toArray(new String[parameters.size()]);
		Set<String> fields = form.getFields().keySet();

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

				ff = new FormField(fieldName);
			}

			
			ff.setUpperRightY(urY);
			ff.setUpperRightX(urX);
			ff.setLowerLeftY(llY);
			ff.setLowerLeftX(llX);
			ff.setValue(value);
			fieldsList.add(ff);

			// System.out.println(" !!!! ff="+ff);

		}

		Collections.sort(fieldsList, Collections.reverseOrder());

		return fieldsList;

	}

	@Override
	public void makePdf(String tmplName, ArrayList<FormFieldDto> fields)
			throws IllegalArgumentException {
		String dcmTmpDir = Config.getTmpFolder();
		String pdfTmpFilename = dcmTmpDir + "/" + new Date().getTime() + "_"
				+ (int) (Math.random() * 10000000l) + ".pdf";
		File pdfTmpFile = null;
		String tmplDir =  Config.getTemplateFolder();
		String file = tmplDir + File.separator + tmplName + ".pdf";
//		String tmplName = req.getPathInfo().replaceFirst("/", "");

		try {

			FileInputStream fis = new FileInputStream(file);
			PdfReader reader = new PdfReader(fis);
			pdfTmpFile = new File(pdfTmpFilename);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					pdfTmpFile));
			
			System.err.println("!!! pdfTmpFilename="+pdfTmpFilename);


			PRAcroForm fff = reader.getAcroForm();
			for (PdfName pdfName : fff.getKeys()) {
				if (fff.get(pdfName) instanceof PdfArray) {
					PdfArray array = (PdfArray) fff.get(pdfName);
					System.out.println(">> form=" + pdfName + " val=" + array);

				}
			}

			// Пробегаем по полям формы.
			// Если поле READ_ONLY - заменяем его на текст
			AcroFields form = stamper.getAcroFields();

			String fName;

			// ФИО Пациента
			fName = "PatientName";
			if (form.getField(fName) != null)
				form.setField(fName, "Иванов Иван Иванович");

			// ДР Пациента
			fName = "PatientBirthDate";
			if (form.getField(fName) != null)
				form.setField(fName, "20.03.1974");

			// Аппарат
			fName = "ManufacturerModelName";
			if (form.getField(fName) != null)
				form.setField(fName, "Аппарат XXX");

			// Протокол осмотра
			fName = "StudyViewprotocol";
			if (form.getField(fName) != null)
				form.setField(fName, "Протокол осмотра");

			// Дата протокола осмотра
			fName = "StudyViewprotocolDate";
			if (form.getField(fName) != null)
				form.setField(fName, "20.03.2011");

			// ===================================================================
			// TODO !!!======== дополнить остальные поля ==================!!!!!
			// ===================================================================

			for (String fieldName : form.getFields().keySet()) {

				// пропускаем "радиобаттоны"
				if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_RADIOBUTTON)
					continue;

				// Установка значений полей из БД

				// Установка значений полей из QUERY_STRING
				for (FormFieldDto formFieldDto : fields) {
					if(formFieldDto.getFieldName().equals(fieldName))
					form.setField(fieldName, formFieldDto.getValue());
				}
				
//				if (req.getParameter(fieldName) != null) {
//					form.setField(fieldName, req.getParameter(fieldName));
//				}

				// Свойства поля

				// boolean readOnly = fieldIsREADONLY(form, fieldName);
				// System.out.println("!!! field " + fieldName + " RO is " +
				// readOnly);

				// ------------

				// //Задаем доп. флаги для поля
				// fields.setField(fieldName, "Тестимся");
				// fields.setFieldProperty(fieldName, "setfflags",
				// TextField.READ_ONLY, null);
			}

			// Удаляем поля
			replaceFields(reader, stamper, tmplName);

			stamper.close();
//			resp.setContentType("application/pdf; charset=UTF-8");
//
//			// Передача результата в броузер
			FileInputStream in = new FileInputStream(pdfTmpFilename);
			FileOutputStream out = new FileOutputStream("../tmp/ttt.pdf");
//			BufferedOutputStream out = new BufferedOutputStream(
//					resp.getOutputStream());
//
			byte b[] = new byte[8];
			int count;
			while ((count = in.read(b)) != -1) {
				out.write(b, 0, count);

			}
			out.flush();
			out.close();
			in.close();

			// Отправка данных в архив
			// if (finalPhase)
//			 sendToArchive(study, pdfTmpFile);

		} catch (DocumentException e) {
//			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			resp.setContentType("text/html; charset=UTF-8");
//			String msg = loggingException("<b>Bad PDF !</b> " + file, e);
//			resp.getWriter().print(msg);
			throw new IllegalArgumentException(e);
//			logger.warn(msg);
		} catch (FileNotFoundException e) {
//			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			resp.setContentType("text/html; charset=UTF-8");
//			String msg = loggingException("<b>PDF template not found!</b> "
//					+ file + " Не найден!", e);
//			resp.getWriter().print(msg);
////			logger.warn(msg);
//			return;
			throw new IllegalArgumentException(e);
		} catch (Exception e) {
//			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			resp.setContentType("text/html; charset=UTF-8");
//			String msg = loggingException(e.getMessage(), e);
//			resp.getWriter().print(msg);
////			logger.warn(msg);
			throw new IllegalArgumentException(e);
		} finally {
			if (pdfTmpFile != null)
				pdfTmpFile.delete();
		}


	}

	/**
	 * Преообразование PDF-ки
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
//	private void changePDFContent(HttpServletRequest req,
//			HttpServletResponse resp) throws IOException {
//
//		String dcmTmpDir = "./tmp";
//		String pdfTmpFilename = dcmTmpDir + "/" + new Date().getTime() + "_"
//				+ (int) (Math.random() * 10000000l) + ".pdf";
//		File pdfTmpFile = null;
//		String tmplDir = "./pdfs";
//		String file = tmplDir + req.getPathInfo() + ".pdf";
//		String tmplName = req.getPathInfo().replaceFirst("/", "");
//
//		try {
//
//			FileInputStream fis = new FileInputStream(file);
//			PdfReader reader = new PdfReader(fis);
//			pdfTmpFile = new File(pdfTmpFilename);
//			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
//					pdfTmpFile));
//
//
//			PRAcroForm fff = reader.getAcroForm();
//			for (PdfName pdfName : fff.getKeys()) {
//				if (fff.get(pdfName) instanceof PdfArray) {
//					PdfArray array = (PdfArray) fff.get(pdfName);
//					System.out.println(">> form=" + pdfName + " val=" + array);
//
//				}
//			}
//
//			// Пробегаем по полям формы.
//			// Если поле READ_ONLY - заменяем его на текст
//			AcroFields form = stamper.getAcroFields();
//
//			String fName;
//
//			// ФИО Пациента
//			fName = "PatientName";
//			if (form.getField(fName) != null)
//				form.setField(fName, "Иванов Иван Иванович");
//
//			// ДР Пациента
//			fName = "PatientBirthDate";
//			if (form.getField(fName) != null)
//				form.setField(fName, "20.03.1974");
//
//			// Аппарат
//			fName = "ManufacturerModelName";
//			if (form.getField(fName) != null)
//				form.setField(fName, "Аппарат XXX");
//
//			// Протокол осмотра
//			fName = "StudyViewprotocol";
//			if (form.getField(fName) != null)
//				form.setField(fName, "Протокол осмотра");
//
//			// Дата протокола осмотра
//			fName = "StudyViewprotocolDate";
//			if (form.getField(fName) != null)
//				form.setField(fName, "20.03.2011");
//
//			// ===================================================================
//			// TODO !!!======== дополнить остальные поля ==================!!!!!
//			// ===================================================================
//
//			for (String fieldName : form.getFields().keySet()) {
//
//				// пропускаем "радиобаттоны"
//				if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_RADIOBUTTON)
//					continue;
//
//				// Установка значений полей из БД
//
//				// Установка значений полей из QUERY_STRING
//				if (req.getParameter(fieldName) != null) {
//					form.setField(fieldName, req.getParameter(fieldName));
//				}
//
//				// Свойства поля
//
//				// boolean readOnly = fieldIsREADONLY(form, fieldName);
//				// System.out.println("!!! field " + fieldName + " RO is " +
//				// readOnly);
//
//				// ------------
//
//				// //Задаем доп. флаги для поля
//				// fields.setField(fieldName, "Тестимся");
//				// fields.setFieldProperty(fieldName, "setfflags",
//				// TextField.READ_ONLY, null);
//			}
//
//			// Удаляем поля
//			replaceFields(reader, stamper, tmplName);
//
//			stamper.close();
//			resp.setContentType("application/pdf; charset=UTF-8");
//
//			// Передача результата в броузер
//			FileInputStream in = new FileInputStream(pdfTmpFilename);
//			BufferedOutputStream out = new BufferedOutputStream(
//					resp.getOutputStream());
//
//			byte b[] = new byte[8];
//			int count;
//			while ((count = in.read(b)) != -1) {
//				out.write(b, 0, count);
//
//			}
//			out.flush();
//			out.close();
//			in.close();
//
//			// Отправка данных в архив
//			// if (finalPhase)
//			// sendToArchive(study, pdfTmpFile);
//
//		} catch (DocumentException e) {
//			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			resp.setContentType("text/html; charset=UTF-8");
//			String msg = loggingException("<b>Bad PDF !</b> " + file, e);
//			resp.getWriter().print(msg);
////			logger.warn(msg);
//		} catch (FileNotFoundException e) {
//			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			resp.setContentType("text/html; charset=UTF-8");
//			String msg = loggingException("<b>PDF template not found!</b> "
//					+ file + " Не найден!", e);
//			resp.getWriter().print(msg);
////			logger.warn(msg);
//			return;
//		} catch (Exception e) {
//			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			resp.setContentType("text/html; charset=UTF-8");
//			String msg = loggingException(e.getMessage(), e);
//			resp.getWriter().print(msg);
////			logger.warn(msg);
//		} finally {
//			if (pdfTmpFile != null)
//				pdfTmpFile.delete();
//		}
//	}

	/**
	 * Замена полей на текст
	 * 
	 * @param reader
	 * @param stamper
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void replaceFields(PdfReader reader, PdfStamper stamper,
			String tmplName) throws IOException, DocumentException {
		Set<String> parameters = stamper.getAcroFields().getFields().keySet();
		AcroFields form = stamper.getAcroFields();

		String[] fields = parameters.toArray(new String[parameters.size()]);
		for (String fieldName : fields) {

			Item field = form.getFieldItem(fieldName);
			PdfDictionary widgetDict = field.getWidget(0);
			PdfArray rectArr = widgetDict.getAsArray(PdfName.RECT); // should
			float llX = rectArr.getAsNumber(0).floatValue();
			float llY = rectArr.getAsNumber(1).floatValue();
			float urX = rectArr.getAsNumber(2).floatValue();
			float urY = rectArr.getAsNumber(3).floatValue();

			String value = form.getField(fieldName);
			// Удаляем поле
			form.removeField(fieldName);

			PdfContentByte canvas = stamper.getOverContent(1);

			// FIXME Сделать конфигуриремым или засунуть шоифт в CLASSPATH
			// Сейчас просто закинул на продуктиве в папку tomcat/lib
			String fontPath = "../fonts/arial.ttf";
			ConfigTemplate tmpl = Config.getTemplateByName(tmplName);
			int fontSize = 10;// по умолчанию
			if (tmpl != null) {
				fontSize = tmpl.getFontsize();
			}

			Font font = new Font(BaseFont.createFont(fontPath, "Cp1251",
					BaseFont.NOT_EMBEDDED), fontSize);

			Phrase phrase = new Phrase(value, font);

			ColumnText columnText = new ColumnText(canvas);
			columnText.setSimpleColumn(llX, llY, urX, urY,
					columnText.getLeading(), Element.ALIGN_LEFT);
			columnText.setText(phrase);

			columnText.go();
		}

	}

	/**
	 * Журналирование Эксепшина в логе файле Получением стека
	 * 
	 * @param e
	 * @return
	 */
	public static String loggingException(String msg, Throwable e) {

		String marker = Thread.currentThread().getId() + "_"
				+ new Date().getTime();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stack = sw.toString();
		// TODO Сделать через Log4j
		System.err.println("Portal Error [" + marker + "] " + e.getMessage()
				+ " stack:\n" + stack);
		return e.getClass() + "[" + marker + "] " + msg;

	}

}
