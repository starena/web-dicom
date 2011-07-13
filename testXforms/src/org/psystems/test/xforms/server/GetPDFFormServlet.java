package org.psystems.test.xforms.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
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
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.AcroFields.Item;

public class GetPDFFormServlet extends HttpServlet {

    private String file = "C:\\WORK\\workspace\\testXforms\\war\\PDFs\\ooform.pdf";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	// req.setCharacterEncoding("cp1251");

	try {

	    FileInputStream fis = new FileInputStream(file);
	    PdfReader reader = new PdfReader(fis);
	    OutputStream out = resp.getOutputStream();
	    PdfStamper stamper = new PdfStamper(reader, out);

	    // Задание значения полей из QUERY_STRING
	    replaceFields(reader, stamper);
	    // changeFieldProps(stamper);
	    // changeFieldWidth(stamper);

	    AcroFields fields = stamper.getAcroFields();
	    Set<String> parameters = fields.getFields().keySet();
	    for (String fieldName : parameters) {

		System.out.println("!!!   FIELD=" + fieldName + " TYPE=" + fields.getFieldType(fieldName) + "[" + "]");

		Map<String, AcroFields.Item> fieldsMap = fields.getFields();
		AcroFields.Item item;
		PdfDictionary dict;
		int flags;
		for (Map.Entry<String, AcroFields.Item> entry : fieldsMap.entrySet()) {
		    String key = entry.getKey();
		    item = entry.getValue();
		    dict = item.getMerged(0);
		    flags = dict.getAsNumber(PdfName.FF).intValue();
		    System.out.println("!!!   FIELD PROP " + key + "=" + item + " -> " + dict); 
			    
		    if ((flags & BaseField.READ_ONLY) > 0) {
			System.out.println("!!!! READ_ONLY !!!");
		    }
		    // if ((flags & BaseField.PASSWORD) > 0)
		    // if ((flags & BaseField.MULTILINE) > 0)

		}

		// пропускаем радиобаттоны
		if (fields.getFieldType(fieldName) == AcroFields.FIELD_TYPE_RADIOBUTTON)
		    continue;

		if (req.getParameter(fieldName) != null) {
		    fields.setField(fieldName, req.getParameter(fieldName));
		}
		fields.setFieldProperty(fieldName, "setfflags", TextField.READ_ONLY, null);
	    }

	    PushbuttonField button = new PushbuttonField(stamper.getWriter(), new Rectangle(90, 60, 140, 190), "submit");
	    button.setText("POST2");
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
	}
    }

    /**
     * Изменение размера полей
     * http://stackoverflow.com/questions/2072468/fit-text-in-textfield-itext
     * 
     * @param stamper
     */
    private void changeFieldWidth(PdfStamper stamper) {
	Set<String> parameters = stamper.getAcroFields().getFields().keySet();
	for (String fieldName : parameters) {

	    AcroFields.Item fldItem = stamper.getAcroFields().getFieldItem(fieldName);

	    for (int i = 0; i < fldItem.size(); ++i) {
		// "widget" is the visible portion of the field
		PdfDictionary widgetDict = fldItem.getWidget(0);
		System.out.println("!!! fieldName=" + fieldName + " " + widgetDict);

		// pdf rectangles are stored as [llx, lly, urx, ury]
		PdfArray rectArr = widgetDict.getAsArray(PdfName.RECT); // should
		// never
		// be
		// null
		float origX = rectArr.getAsNumber(0).floatValue();
		float FUDGE_FACTOR = 5;
		float newWidth = 20;
		// overwrite the old value.
		rectArr.set(2, new PdfNumber(origX + newWidth + FUDGE_FACTOR));
	    }
	}
    }

    /**
     * Изменение сойств полей http://itextpdf.com/examples/iia.php?id=157
     * 
     * @param stamper
     * @throws DocumentException
     * @throws IOException
     */
    private void changeFieldProps(PdfStamper stamper) throws IOException, DocumentException {
	Set<String> parameters = stamper.getAcroFields().getFields().keySet();
	AcroFields form = stamper.getAcroFields();
	for (String fieldName : parameters) {

	    form.setFieldProperty(fieldName, "textsize", new Float(12), null);
	    form.regenerateField(fieldName);

	}
    }

    /**
     * http://itextpdf.com/examples/iia.php?id=117
     * 
     * @param reader
     * @param stamper
     * @throws IOException
     * @throws DocumentException
     */
    private void replaceFields(PdfReader reader, PdfStamper stamper) throws IOException, DocumentException {
	Set<String> parameters = stamper.getAcroFields().getFields().keySet();
	AcroFields form = stamper.getAcroFields();

	String[] fields = parameters.toArray(new String[parameters.size()]);

	for (String fieldName : fields) {
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

	    String fontPath = "C:\\WINDOWS\\Fonts\\arial.ttf";
	    Font font = new Font(BaseFont.createFont(fontPath, "Cp1251", BaseFont.NOT_EMBEDDED), 14);
	    // Font font = new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLDITALIC,
	    // new BaseColor(0, 0, 255))

	    System.out.println("[2] WidthPoint="
		    + font.getCalculatedBaseFont(false).getWidthPoint("Hello people!! " + value, 14));
	    System.out.println("[3] AscentPoint="
		    + font.getCalculatedBaseFont(false).getAscentPoint("Hello people!! " + value, 14));
	    System.out.println("[4] DescentPoint="
		    + font.getCalculatedBaseFont(false).getDescentPoint("Hello people!! " + value, 14));

	    // " Привет привет привет Привет привет привет Привет привет привет"

	    Phrase phrase = new Phrase("Hello people!! " + value, font);

	    Chunk ck = new Chunk("Red ", font);

	    ck.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 1f, BaseColor.RED);

	    phrase.add(ck);

	    phrase.add(" means дима stop. ");

	    ck = new Chunk("Green");
	    ck.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 1, BaseColor.GREEN);
	    phrase.add(ck);

	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");
	    phrase.add(" means proceed with caution.");

	    // ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase,
	    // llX, urY, 0);
	    ColumnText columnText = new ColumnText(canvas);
	    columnText.setSimpleColumn(llX, llY, urX, urY, columnText.getLeading(), Element.ALIGN_LEFT);
	    columnText.setText(phrase);

	    columnText.go();
	}

    }

    // public static String getFieldValue(String value) throws
    // UnsupportedEncodingException {
    // // return value;
    // // return new String((value).getBytes("ISO-8859-1"),"Cp1251");
    // // return new String((value).getBytes("Cp1251"), "ISO-8859-1");
    // return new String((value).getBytes("Cp1251"), "ISO-8859-5");
    // // return new String((value).getBytes("UTF-8"),"ISO-8859-1");
    // // return new String((value).getBytes("UTF-8"),"Cp1251");
    // // return new String((value).getBytes("ISO-8859-1"),"UTF-8");
    // }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	try {

	    req.setCharacterEncoding("cp1251");

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
