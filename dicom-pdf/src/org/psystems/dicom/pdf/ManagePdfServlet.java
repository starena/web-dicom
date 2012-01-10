package org.psystems.dicom.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.psystems.dicom.commons.Config;
import org.psystems.dicom.commons.ConfigTemplate;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PushbuttonField;

/**
 * @author dima_d
 * 
 *         Сервлет управления формирования PDF шаблонов
 * 
 */
public class ManagePdfServlet extends HttpServlet {

    private static final long serialVersionUID = 8911247236211732365L;
    private static Logger logger = Logger.getLogger(ManagePdfServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	changePDFContent(req, resp, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	req.setCharacterEncoding("cp1251");

	changePDFContent(req, resp, true);
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
    private void changePDFContent(HttpServletRequest req, HttpServletResponse resp, boolean finalPhase)
	    throws IOException {

	String dcmTmpDir = "./tmp";
	String pdfTmpFilename = dcmTmpDir + "/" + new Date().getTime() + "_" + (int) (Math.random() * 10000000l)
		+ ".pdf";
	File pdfTmpFile = null;
	String tmplDir = "./pdfs";
	String file = tmplDir + req.getPathInfo() + ".pdf";
	String tmplName = req.getPathInfo().replaceFirst("/", "");
	
	
	
	try {

	    FileInputStream fis = new FileInputStream(file);
	    PdfReader reader = new PdfReader(fis);
	    pdfTmpFile = new File(pdfTmpFilename);
	    // OutputStream out = resp.getOutputStream();
	    // PdfStamper stamper = new PdfStamper(reader, out);
	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfTmpFile));

	    // Ищем шрифты
	    PdfDictionary resource = reader.getPageN(1).getAsDict(PdfName.RESOURCES);
	    PdfDictionary fonts = resource.getAsDict(PdfName.FONT);
	    if (fonts != null) {
		PdfDictionary font;
		for (PdfName key : fonts.getKeys()) {
		    font = fonts.getAsDict(key);
		    String name = font.getAsName(PdfName.BASEFONT).toString();
		    if (name.length() > 8 && name.charAt(7) == '+') {
			name = String.format("%s subset (%s)", name.substring(8), name.substring(1, 7));
		    } else {
			name = name.substring(1);
			PdfDictionary desc = font.getAsDict(PdfName.FONTDESCRIPTOR);
			if (desc == null)
			    name += " nofontdescriptor";
			else if (desc.get(PdfName.FONTFILE) != null)
			    name += " (Type 1) embedded";
			else if (desc.get(PdfName.FONTFILE2) != null)
			    name += " (TrueType) embedded";
			else if (desc.get(PdfName.FONTFILE3) != null)
			    name += " (" + font.getAsName(PdfName.SUBTYPE).toString().substring(1) + ") embedded";
		    }
		    System.out.println("!!!! font: = " + name);
		}

	    }
	    
	    PRAcroForm fff = reader.getAcroForm();
	    for (PdfName pdfName : fff.getKeys()) {
		if(fff.get(pdfName) instanceof PdfArray) {
		    PdfArray array = (PdfArray)fff.get(pdfName);
		System.out.println(">> form="+pdfName+" val=" + array);
		    
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

	    // Удаляем поля
	    replaceFields(reader, stamper, tmplName, !finalPhase);

	    if (!finalPhase) {
		// Добавляем кнопку Submit
		int btnWidth = 100;
		int btnHeight = 30;
		PushbuttonField button = new PushbuttonField(stamper.getWriter(), new Rectangle(10, 10, 90 + btnWidth,
			10 + btnHeight), "submit");
		button.setText("Передать в архив...");
		button.setBackgroundColor(new GrayColor(0.7f));

		// button.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
		PdfFormField submit = button.getField();
		submit.setAction(PdfAction.createSubmitForm(req.getServletPath(), null, PdfAction.SUBMIT_HTML_FORMAT));

		// System.out.println("!!!!!!!!!!! getRequestURI "+req.getRequestURI());
		String url = req.getRequestURI() + "?final=" + finalPhase + "&" + req.getQueryString();
		submit.setAction(PdfAction.createSubmitForm(url, null, PdfAction.SUBMIT_HTML_FORMAT));

		// Количество страниц
		int npages = reader.getNumberOfPages();
		stamper.addAnnotation(submit, npages);

	    } else {
		// Добавляем кнопку "Закрыть"
		// int btnWidth = 300;
		// int btnHeight = 30;
		// PushbuttonField button = new
		// PushbuttonField(stamper.getWriter(), new Rectangle(90, 60, 90
		// + btnWidth,
		// 60 + btnHeight), "submit");
		// button.setText("Данные сохранены. [закрыть]");
		//
		// button.setBackgroundColor(BaseColor.YELLOW);
		//
		// button.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
		// PdfFormField submit = button.getField();
		// submit.setAction(PdfAction.createSubmitForm(req.getServletPath(),
		// null, PdfAction.SUBMIT_HTML_FORMAT));
		//
		//
		// //
		// System.out.println("!!!!!!!!!!! getRequestURI "+req.getRequestURI());
		// String url = req.getRequestURI()+ "?final=" + finalPhase +
		// "&" + req.getQueryString();
		//
		// PdfAction action = PdfAction.javaScript(
		// "app.alert('This day is reserved for people with an accreditation "
		// + "or an invitation.');", stamper.getWriter());
		//
		// submit.setAction(action);
		//
		//
		// stamper.addAnnotation(submit, 1);
	    }

	    stamper.close();
	    resp.setContentType("application/pdf; charset=UTF-8");

	    // stamper.close();
	    // Передача результата в броузер
	    FileInputStream in = new FileInputStream(pdfTmpFilename);
	    BufferedOutputStream out = new BufferedOutputStream(resp.getOutputStream());

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
	    // sendToArchive(study, pdfTmpFile);

	} catch (DocumentException e) {
	    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    resp.setContentType("text/html; charset=UTF-8");
	    String msg = loggingException("<b>Bad PDF !</b> " + file, e);
	    resp.getWriter().print(msg);
	    logger.warn(msg);
	} catch (FileNotFoundException e) {
	    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    resp.setContentType("text/html; charset=UTF-8");
	    String msg = loggingException("<b>PDF template not found!</b> " + file + " Не найден!", e);
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
	    if (pdfTmpFile != null)
		pdfTmpFile.delete();
	}
    }

    /**
     * Журналирование Эксепшина в логе файле Получением стека
     * 
     * @param e
     * @return
     */
    public static String loggingException(String msg, Throwable e) {

	String marker = Thread.currentThread().getId() + "_" + new Date().getTime();
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	String stack = sw.toString();
	// TODO Сделать через Log4j
	System.err.println("Portal Error [" + marker + "] " + e.getMessage() + " stack:\n" + stack);
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
    private void replaceFields(PdfReader reader, PdfStamper stamper, String tmplName, boolean onlyROfields) throws IOException,
	    DocumentException {
	Set<String> parameters = stamper.getAcroFields().getFields().keySet();
	AcroFields form = stamper.getAcroFields();

	String[] fields = parameters.toArray(new String[parameters.size()]);

	for (String fieldName : fields) {

	    // Замещаем только READ_ONLY поля
	    if (onlyROfields && !fieldIsREADONLY(form, fieldName))
		continue;

	    Item field = form.getFieldItem(fieldName);

	    PdfDictionary widgetDict = field.getWidget(0);

	    // System.out.println("********************** = " + form.get);

	    // for (PdfName pdfName : field.getWidget(0).getKeys()) {
	    // System.out.println("!!! pdfName="+pdfName +" = "+
	    // field.getWidget(0).get(pdfName));
	    // }

	    // pdf rectangles are stored as [llx, lly, urx, ury]
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
	    String fontPath = "fonts/arial.ttf";
	    ConfigTemplate tmpl = Config.getTemplateByName(tmplName);
	    int fontSize = 10;//по умолчанию
	    if(tmpl!=null) {
		fontSize = tmpl.getFontsize();
	    }
	    
	    Font font = new Font(BaseFont.createFont(fontPath, "Cp1251", BaseFont.NOT_EMBEDDED), fontSize);

	    Phrase phrase = new Phrase(value, font);

	    ColumnText columnText = new ColumnText(canvas);
	    columnText.setSimpleColumn(llX, llY, urX, urY, columnText.getLeading(), Element.ALIGN_LEFT);
	    columnText.setText(phrase);

	    columnText.go();
	}

    }

    /**
     * Проверка у поля аттрибута READ_ONLY
     * 
     * @param form
     * @param fieldName
     */
    private boolean fieldIsREADONLY(AcroFields form, String fieldName) {

//	ArrayList<BaseFont> fonts = form.getSubstitutionFonts();

	Map<String, AcroFields.Item> fieldsMap = form.getFields();
	AcroFields.Item item;
	PdfDictionary dict;
	int flags = 0;
	for (Map.Entry<String, AcroFields.Item> entry : fieldsMap.entrySet()) {
	    String key = entry.getKey();

	    if (fieldName.equals(key)) {

		item = entry.getValue();
		dict = item.getMerged(0);

		System.out.println("\n\n!!! ************ FIELD ***************!!!!!!!!!!!!!!");
		
		PdfDictionary di = (PdfDictionary)dict.getAsDict(PdfName.DR);
		
		for (PdfName pdfName : di.getKeys()) {
		     
		    PdfDictionary di2 = (PdfDictionary) di.get(pdfName);
		    
		    for (PdfName pdfName2 : di2.getKeys()) {
			
			PRIndirectReference ref = (PRIndirectReference) di2.get(pdfName2);
			System.out.println(" FONT:" + ref.getBytes());
		    }
		}
		
		
		
		
		for (PdfName pdfName : item.getWidget(0).getKeys()) {
		    System.out.println("!!! item key=" + pdfName + " val=" + item.getWidget(0).get(pdfName) + " class:"
			    + item.getWidget(0).get(pdfName).getClass());
		    
		    
		    
		    if (item.getWidget(0).get(pdfName) instanceof PdfDictionary) {
			PdfDictionary dic = ((PdfDictionary) item.getWidget(0).get(pdfName));

			for (PdfName d : dic.getKeys()) {
			    System.out.println("  !!! > [" + d + "]" + dic.get(d));

			    if (dic.get(d) instanceof PdfDictionary) {
				PdfDictionary ddd = (PdfDictionary) dic.get(d);
				for (PdfName kkk : ddd.getKeys()) {
				    System.out.println("      !!! > [" + kkk + "]" + ddd.get(kkk));
				    PRIndirectReference ref = (PRIndirectReference)ddd.get(kkk);
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