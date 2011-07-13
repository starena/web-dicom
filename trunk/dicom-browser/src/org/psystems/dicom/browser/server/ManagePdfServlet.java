package org.psystems.dicom.browser.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.AcroFields.Item;

/**
 * @author dima_d
 * 
 *         Сервлет управления формирования PDF шаблонов
 * 
 */
public class ManagePdfServlet extends HttpServlet {

    private static final long serialVersionUID = 8911247236211732365L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	// req.setCharacterEncoding("cp1251");
	// TODO Поменять имя переменной webdicom.dir.ootmpl на webdicom.dir.pdf
	String tmplDir = getServletContext().getInitParameter("webdicom.dir.ootmpl");
	String file = tmplDir + req.getPathInfo();

	try {

	    FileInputStream fis = new FileInputStream(file);
	    PdfReader reader = new PdfReader(fis);
	    OutputStream out = resp.getOutputStream();
	    PdfStamper stamper = new PdfStamper(reader, out);

	   

	    // Пробегаем по полям формы.
	    // Если поле READ_ONLY - заменяем его на текст
	    AcroFields fields = stamper.getAcroFields();
	    for (String fieldName : fields.getFields().keySet()) {

		// пропускаем "радиобаттоны"
		if (fields.getFieldType(fieldName) == AcroFields.FIELD_TYPE_RADIOBUTTON)
		    continue;

		// Установка значений полей из БД

		// Установка значений полей из QUERY_STRING
		if (req.getParameter(fieldName) != null) {
		    fields.setField(fieldName, req.getParameter(fieldName));
		}

		// Свойства поля

		boolean readOnly = fieldIsREADONLY(fields, fieldName);
		System.out.println("!!! field " + fieldName + " RO is " + readOnly);

		// ------------

		// //Задаем доп. флаги для поля
		// fields.setField(fieldName, "Тестимся");
		// fields.setFieldProperty(fieldName, "setfflags",
		// TextField.READ_ONLY, null);
	    }
	    
	    replaceROFields(reader, stamper);

	    // Добавляем кнопку Submit
	    int btnWidth = 100;
	    int btnHeight = 30;
	    PushbuttonField button = new PushbuttonField(stamper.getWriter(), new Rectangle(90, 60, 90+btnWidth, 60+btnHeight), "submit");
	    button.setText("Передать в архив...");
	    button.setBackgroundColor(new GrayColor(0.7f));

	    // button.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);

	    PdfFormField submit = button.getField();

	    submit.setAction(PdfAction.createSubmitForm(req.getServletPath(), null, PdfAction.SUBMIT_HTML_FORMAT));

	    // submit.setAction(PdfAction.createSubmitForm("http://localhost:8080/template",
	    // null, PdfAction.SUBMIT_HTML_FORMAT));

	    stamper.addAnnotation(submit, 1);

	    resp.setContentType("application/pdf; charset=UTF-8");
	    // resp.setCharacterEncoding("UTF-8");

	    stamper.close();
	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (FileNotFoundException ex) {
	    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    resp.setContentType("text/html; charset=UTF-8");

	    resp.getWriter().print("<b>PDF template not found!</b> " + file + " Не найден!");
	    ex.printStackTrace();
	    return;
	}
    }

    /**
     * Замена полей на текст
     * 
     * @param reader
     * @param stamper
     * @throws IOException
     * @throws DocumentException
     */
    private void replaceROFields(PdfReader reader, PdfStamper stamper) throws IOException, DocumentException {
	Set<String> parameters = stamper.getAcroFields().getFields().keySet();
	AcroFields form = stamper.getAcroFields();

	String[] fields = parameters.toArray(new String[parameters.size()]);

	for (String fieldName : fields) {

	    // Замещаем только READ_ONLY поля
	    if (!fieldIsREADONLY(form, fieldName))
		continue;

	    Item field = form.getFieldItem(fieldName);
	    System.out.println("!!! replaceFields [" + form.getField(fieldName) + "]");
	    PdfDictionary widgetDict = field.getWidget(0);
	    // pdf rectangles are stored as [llx, lly, urx, ury]
	    PdfArray rectArr = widgetDict.getAsArray(PdfName.RECT); // should
	    float llX = rectArr.getAsNumber(0).floatValue();
	    float llY = rectArr.getAsNumber(1).floatValue();
	    float urX = rectArr.getAsNumber(2).floatValue();
	    float urY = rectArr.getAsNumber(3).floatValue();

	    System.out.println("!!! replaceFields fieldName=" + fieldName + " " + rectArr);

	    String value = form.getField(fieldName);

	    form.removeField(fieldName);

	    PdfContentByte canvas = stamper.getOverContent(1);

	    // TODO Вынести в конфиг! или положить в приложение
	    String fontPath = "C:\\WINDOWS\\Fonts\\arial.ttf";

	    Font font = new Font(BaseFont.createFont(fontPath, "Cp1251", BaseFont.NOT_EMBEDDED), 14);

	    Phrase phrase = new Phrase("Значение="+value, font);

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

	Map<String, AcroFields.Item> fieldsMap = form.getFields();
	AcroFields.Item item;
	PdfDictionary dict;
	int flags = 0;
	for (Map.Entry<String, AcroFields.Item> entry : fieldsMap.entrySet()) {
	    String key = entry.getKey();

	    if (fieldName.equals(key)) {

		item = entry.getValue();
		dict = item.getMerged(0);

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	try {

	    req.setCharacterEncoding("cp1251");

	    String tmplDir = getServletContext().getInitParameter("webdicom.dir.ootmpl");
	    String file = tmplDir + req.getPathInfo();
	    System.out.println("! CS = " + req.getCharacterEncoding() + "; " + req.getParameter("charset"));

	    for (Enumeration iter = req.getParameterNames(); iter.hasMoreElements();) {
		String name = (String) iter.nextElement();
		System.out.println("!!! name=" + name + "=" + req.getParameter(name));
	    }

	    FileInputStream fis = new FileInputStream(file);
	    PdfReader reader = new PdfReader(fis);
	    OutputStream out = resp.getOutputStream();

	    PdfStamper stamper = new PdfStamper(reader, out);

	    AcroFields fields = stamper.getAcroFields();
	    Set<String> parameters = fields.getFields().keySet();
	    for (String parameter : parameters) {

		System.out.println("!!!   parameter=" + parameter + " TYPE=" + fields.getFieldType(parameter) + "["
			+ req.getParameter(parameter) + "]");

		// пропускаем радиобаттоны
		if (fields.getFieldType(parameter) == AcroFields.FIELD_TYPE_RADIOBUTTON)
		    continue;

		if (req.getParameter(parameter) != null) {
		    fields.setField(parameter, req.getParameter(parameter));
		}
		fields.setFieldProperty(parameter, "setfflags", TextField.READ_ONLY, null);
	    }

	    // stamper.setFormFlattening(true);

	    resp.setContentType("application/pdf");
	    stamper.close();

	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}