package org.psystems.dicom.pdfview.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.psystems.dicom.commons.Config;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.PdfContentReaderTool;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * @author dima_d
 * 
 *         Сервлет управления формирования PDF шаблонов
 * 
 */
public class Pdf2HTMLServlet extends HttpServlet {

	private static final long serialVersionUID = 8911247236211732365L;
	private static Logger logger = Logger.getLogger(Pdf2HTMLServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		prontForm(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("cp1251");

//		changePDFContent(req, resp, true);
	}

	/**
	 * Преообразование PDF-ки
	 * 
	 * @param req
	 * @param resp
	 * @param finalPhase
	 *            - финальная стадия
	 * @throws IOException
	 */
	private void prontForm(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
//		resp.setContentType("text/html; charset=UTF-8");
		
		String tmplDir = Config.getTemplateFolder();
		String file = tmplDir + req.getPathInfo() + ".pdf";
		String tmplName = req.getPathInfo().replaceFirst("/", "");

		try {

			FileInputStream fis = new FileInputStream(file);
			PdfReader reader = new PdfReader(fis);
			// OutputStream out = resp.getOutputStream();
			// PdfStamper stamper = new PdfStamper(reader, out);

			// Ищем шрифты
			PdfDictionary resource = reader.getPageN(1).getAsDict(
					PdfName.RESOURCES);
			PdfDictionary fonts = resource.getAsDict(PdfName.FONT);
			if (fonts != null) {
				PdfDictionary font;
				for (PdfName key : fonts.getKeys()) {
					font = fonts.getAsDict(key);
					String name = font.getAsName(PdfName.BASEFONT).toString();
					if (name.length() > 8 && name.charAt(7) == '+') {
						name = String.format("%s subset (%s)",
								name.substring(8), name.substring(1, 7));
					} else {
						name = name.substring(1);
						PdfDictionary desc = font
								.getAsDict(PdfName.FONTDESCRIPTOR);
						if (desc == null)
							name += " nofontdescriptor";
						else if (desc.get(PdfName.FONTFILE) != null)
							name += " (Type 1) embedded";
						else if (desc.get(PdfName.FONTFILE2) != null)
							name += " (TrueType) embedded";
						else if (desc.get(PdfName.FONTFILE3) != null)
							name += " ("
									+ font.getAsName(PdfName.SUBTYPE)
											.toString().substring(1)
									+ ") embedded";
					}
//					System.out.println("!!!! font: = " + name);
				}

			}

			PRAcroForm fff = reader.getAcroForm();
			for (PdfName pdfName : fff.getKeys()) {
				if (fff.get(pdfName) instanceof PdfArray) {
					PdfArray array = (PdfArray) fff.get(pdfName);
					System.out.println(">> form=" + pdfName + " val=" + array);

				}
			}

			// Пробегаем по полям формы.
			// Если поле READ_ONLY - заменяем его на текст
			AcroFields form = reader.getAcroFields();

			String fName;

			
			// ФИО Пациента
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

			// ===================================================================
			// TODO !!!======== дополнить остальные поля ==================!!!!!
			// ===================================================================

			for (String fieldName : form.getFields().keySet()) {

				// пропускаем "радиобаттоны"
				if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_RADIOBUTTON)
					continue;

				// Установка значений полей из БД

				// Установка значений полей из QUERY_STRING
				if (req.getParameter(fieldName) != null) {
					form.setField(fieldName, req.getParameter(fieldName));
				}

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

			
			printFields(resp, reader, tmplName);

			
//			String sss = PdfTextExtractor.getTextFromPage(reader, 1);
//			System.out.println("========== " + sss);
			

			PdfContentReaderTool.listContentStreamForPage(reader, 1, new PrintWriter(System.out));
			
			reader.close();
			
			resp.getWriter().print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		

		} catch (DocumentException e) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setContentType("text/html; charset=UTF-8");
			String msg = loggingException("<b>Bad PDF !</b> " + file, e);
			resp.getWriter().print(msg);
			logger.warn(msg);
		} catch (FileNotFoundException e) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setContentType("text/html; charset=UTF-8");
			String msg = loggingException("<b>PDF template not found!</b> "
					+ file + " Не найден!", e);
			resp.getWriter().print(msg);
			logger.warn(msg);
			return;
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setContentType("text/html; charset=UTF-8");
			String msg = loggingException(e.getMessage(), e);
			resp.getWriter().print(msg);
			logger.warn(msg);
		} finally {
			
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

	/**
	 * Замена полей на текст
	 * 
	 * @param reader
	 * @param stamper
	 * @param onlyROfields
	 *            удалять только поля которые READ_ONLY
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void printFields( HttpServletResponse resp, PdfReader reader, 
			String tmplName) throws IOException,
			DocumentException {
		Set<String> parameters = reader.getAcroFields().getFields().keySet();
		AcroFields form = reader.getAcroFields();

		String[] fields = parameters.toArray(new String[parameters.size()]);

		for (String fieldName : fields) {

			// Замещаем только READ_ONLY поля
//			if (fieldIsREADONLY(form, fieldName)) continue;

			Item field = form.getFieldItem(fieldName);

			PdfDictionary widgetDict = field.getWidget(0);

			// System.out.println("********************** = " + form.get);

			// for (PdfName pdfName : field.getWidget(0).getKeys()) {
			// System.out.println("!!! pdfName="+pdfName +" = "+
			// field.getWidget(0).get(pdfName));
			// }
			
			
			PdfString title = widgetDict.getAsString(PdfName.TITLE);
			
			
			

			// pdf rectangles are stored as [llx, lly, urx, ury]
			PdfArray rectArr = widgetDict.getAsArray(PdfName.RECT); // should
			float llX = rectArr.getAsNumber(0).floatValue();
			float llY = rectArr.getAsNumber(1).floatValue();
			float urX = rectArr.getAsNumber(2).floatValue();
			float urY = rectArr.getAsNumber(3).floatValue();

			String value = form.getField(fieldName);

			System.out.println("!!! fieldName="+fieldName + " " + llX+";"+llY+";"+urX+";"+urY);
			
			for (PdfName pdfName :  widgetDict.getKeys()) {
				
				
				
				
				
				
				if (widgetDict.get(pdfName) instanceof PdfDictionary) {

					System.out.println("      > pdfName="+
							PdfContentReaderTool.getDictionaryDetail((PdfDictionary)widgetDict.get(pdfName)));
				}
			}
			
			String fieldNameDecoded = fieldName.replaceAll("#", "%");
			fieldNameDecoded = URLDecoder.decode(fieldNameDecoded,"UTF-8");
			
			resp.getWriter().println("("+title + ")" +fieldNameDecoded + ": ");
			if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_COMBO || form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_LIST) {
				
			
				
				resp.getWriter().println("<select name='"+fieldName+"'>");
					resp.getWriter().println("<option value=''>");
				for (String fitem : form.getAppearanceStates(fieldName)) {
					resp.getWriter().println("<option value='"+fitem+"'>" + fitem);
					resp.getWriter().println("</option>");
				}
				resp.getWriter().println("</select>");
				
			} else if(form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_CHECKBOX) {
				resp.getWriter().println("<input type='checkbox' name='" +fieldName +">");
			} 
			
			else {
				resp.getWriter().println("<input type='text' name='" +fieldName +
						"' value='" + value +"'>");

			}
			
			resp.getWriter().println("<br><br>");
				
			
			

		}

	}

	/**
	 * Проверка у поля аттрибута READ_ONLY
	 * 
	 * @param form
	 * @param fieldName
	 */
	private boolean fieldIsREADONLY(AcroFields form, String fieldName) {

		// ArrayList<BaseFont> fonts = form.getSubstitutionFonts();

		Map<String, AcroFields.Item> fieldsMap = form.getFields();
		AcroFields.Item item;
		PdfDictionary dict;
		int flags = 0;
		for (Map.Entry<String, AcroFields.Item> entry : fieldsMap.entrySet()) {
			String key = entry.getKey();

			if (fieldName.equals(key)) {

				item = entry.getValue();
				dict = item.getMerged(0);

				System.out
						.println("\n\n!!! ************ FIELD ***************!!!!!!!!!!!!!!");

				PdfDictionary di = (PdfDictionary) dict.getAsDict(PdfName.DR);

				for (PdfName pdfName : di.getKeys()) {

					PdfDictionary di2 = (PdfDictionary) di.get(pdfName);

					for (PdfName pdfName2 : di2.getKeys()) {

						PRIndirectReference ref = (PRIndirectReference) di2
								.get(pdfName2);
						System.out.println(" FONT:" + ref.getBytes());
					}
				}

				for (PdfName pdfName : item.getWidget(0).getKeys()) {
					System.out.println("!!! item key=" + pdfName + " val="
							+ item.getWidget(0).get(pdfName) + " class:"
							+ item.getWidget(0).get(pdfName).getClass());

					if (item.getWidget(0).get(pdfName) instanceof PdfDictionary) {
						PdfDictionary dic = ((PdfDictionary) item.getWidget(0)
								.get(pdfName));

						for (PdfName d : dic.getKeys()) {
							System.out.println("  !!! > [" + d + "]"
									+ dic.get(d));

							if (dic.get(d) instanceof PdfDictionary) {
								PdfDictionary ddd = (PdfDictionary) dic.get(d);
								for (PdfName kkk : ddd.getKeys()) {
									System.out.println("      !!! > [" + kkk
											+ "]" + ddd.get(kkk));
									PRIndirectReference ref = (PRIndirectReference) ddd
											.get(kkk);
								}
							}

						}
					}

				}

				if (dict.getAsNumber(PdfName.FF) != null)
					flags = dict.getAsNumber(PdfName.FF).intValue();

				if ((flags & BaseField.READ_ONLY) > 0)
					return true;
				else
					return false;
			}
		}
		return false;
	}

}