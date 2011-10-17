/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.psystems.dicom.browser.client.Browser;
import org.psystems.dicom.browser.client.TransactionTimer;
import org.psystems.dicom.browser.client.proxy.RPCDcmProxyEvent;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Панель со списком текущих исследований
 * 
 * @author dima_d
 * 
 */
public class WorkListPanel extends Composite {

    private DateBox studyDateBoxBegin;
    protected static String dateBegin;
    protected static String dateEnd;
    private DateBox studyDateBoxEnd;
    protected static String manufacturerModelName = "";
    protected static String studyResult = "all";
    protected static ArrayList<StudyProxy> studies;

    protected String sortOrder = null;
    public final static int maxResultCount = 300;
    SearchPanel spanel;

    /**
     * @param application
     *            TODO Убрать и вызывать через static методы???
     */
    public WorkListPanel(final SearchPanel spanel) {

	// this.Application = application;
	this.spanel = spanel;

	dateBegin = Utils.dateFormatSql.format(new Date());
	dateEnd = Utils.dateFormatSql.format(new Date());

	VerticalPanel mainPanel = new VerticalPanel();

	HorizontalPanel toolPanel = new HorizontalPanel();
	toolPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	mainPanel.add(toolPanel);

	Label label = new Label("Рабочий лист: с ");
	label.addStyleName("DicomItemValue");
	toolPanel.add(label);

	studyDateBoxBegin = new DateBox();
	studyDateBoxBegin.setWidth("80px");
	studyDateBoxBegin.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	studyDateBoxBegin.setValue(new Date());

	studyDateBoxBegin.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		dateBegin = Utils.dateFormatSql.format(Utils.dateFormatUser.parse(event.getValue()));
		spanel.searchWorklist();
		// searchStudyes(false);
	    }
	});

	studyDateBoxBegin.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Date> event) {
		dateBegin = Utils.dateFormatSql.format(event.getValue());
		spanel.searchWorklist();
		// searchStudyes(false);
	    }
	});

	toolPanel.add(studyDateBoxBegin);

	label = new Label(" по ");
	label.addStyleName("DicomItemValue");
	toolPanel.add(label);

	studyDateBoxEnd = new DateBox();
	studyDateBoxEnd.setWidth("80px");
	studyDateBoxEnd.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	studyDateBoxEnd.setValue(new Date());

	studyDateBoxEnd.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		dateEnd = Utils.dateFormatSql.format(Utils.dateFormatUser.parse(event.getValue()));
		spanel.searchWorklist();
		// searchStudyes(false);
	    }
	});

	studyDateBoxEnd.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Date> event) {
		dateEnd = Utils.dateFormatSql.format(event.getValue());
		spanel.searchWorklist();
		// searchStudyes(false);
	    }
	});

	toolPanel.add(studyDateBoxEnd);

	label = new Label("аппарат:");
	label.addStyleName("DicomItemValue");
	toolPanel.add(label);

	//
	final ListBox lbManufacturerModelName = new ListBox();
	// lbCommentsTemplates.setName("00100040");
	lbManufacturerModelName.addItem("ВСЕ", "");
	lbManufacturerModelName.addItem("RENEXFLUORO3", "RENEXFLUORO3");
	lbManufacturerModelName.addItem("КРТ-Электрон", "КРТ-Электрон");
	lbManufacturerModelName.addItem("Маммограф (LORAD AFFINITY)", "LORAD AFFINITY");
	lbManufacturerModelName.addItem("Эндоскоп (FUJINON EG WR5)", "FUJINON EG WR5");
	lbManufacturerModelName.addItem("Рентген (CLINOMAT)", "CLINOMAT");
	lbManufacturerModelName.addItem("Рентген (РДК 50/6)", "РДК 50/6");
	lbManufacturerModelName.addItem("Рентген (DUODiagnost)", "DUODiagnost");

	lbManufacturerModelName.addItem("УЗИ-1 (SonoScape) ДП 523 каб.", "SonoScape-1");
	lbManufacturerModelName.addItem("УЗИ-2 (SonoScape)", "SonoScape-2");
	lbManufacturerModelName.addItem("УЗИ-3 (SonoScape)", "SonoScape-3");
	lbManufacturerModelName.addItem("УЗИ (VIVID) ДП 527 каб.", "VIVID 3");
	lbManufacturerModelName.addItem("VOLUSON 730 BT04 EXPERT", "УЗИ (VOLUSON)");
	lbManufacturerModelName.addItem("УЗИ (Acuson Sequoia) каб.408", "Acuson Sequoia");

	// TODO Уточнить имена и кабинеты
	lbManufacturerModelName.addItem("SSI-1000");
	lbManufacturerModelName.addItem("Эхоэнцефалоскоп-ЭЭС-25-ЭМА");
	lbManufacturerModelName.addItem("Электроэнцефалограф-Alliance");
	lbManufacturerModelName.addItem("Companion III");
	lbManufacturerModelName.addItem("Microvit");
	lbManufacturerModelName.addItem("СМАД, Schiller AG");
	lbManufacturerModelName.addItem("ЭКГ-Schiller Medical S.A.");
	lbManufacturerModelName.addItem("ЭКГ-Cardiovit AT-2 plus C");
	lbManufacturerModelName.addItem("Велоэргометрия-АТ- 104 Schiller");
	lbManufacturerModelName.addItem("Voluson 730 Expert");
	lbManufacturerModelName.addItem("SSD-3500");
	lbManufacturerModelName.addItem("Спирометр-Spirovit SP-1");
	lbManufacturerModelName.addItem("Спиро-Спектр 2");

	lbManufacturerModelName.addItem("Aloka alfa");
	lbManufacturerModelName.addItem("Aloka 3500 ВП 303 каб.", "Aloka 3500");

	// TODO Убрал из списка
	// lbManufacturerModelName.addItem("Мамография","MAMMOGRAF");

	lbManufacturerModelName.addChangeHandler(new ChangeHandler() {

	    @Override
	    public void onChange(ChangeEvent event) {
		int i = lbManufacturerModelName.getSelectedIndex();
		manufacturerModelName = lbManufacturerModelName.getValue(i);
		spanel.searchWorklist();
		// searchStudyes(false);
	    }
	});

	toolPanel.add(lbManufacturerModelName);

	//
	final ListBox lbStudyResult = new ListBox();
	lbStudyResult.addItem("Все", "all");
	lbStudyResult.addItem("Неописанные", "new");
	lbStudyResult.addItem("Описанные", "old");

	lbStudyResult.addChangeHandler(new ChangeHandler() {

	    @Override
	    public void onChange(ChangeEvent event) {
		// TODO Auto-generated method stub
		// System.out.println("!!! "+event)!!!;
		int i = lbStudyResult.getSelectedIndex();
		studyResult = lbStudyResult.getValue(i);
		spanel.searchWorklist();
		// searchStudyes(false);
	    }
	});

	toolPanel.add(lbStudyResult);

	//
	final ListBox lbSortOrder = new ListBox();
	lbSortOrder.addItem("ФИО", "PATIENT_NAME, STUDY_DATE");
	lbSortOrder.addItem("ДАТА", "STUDY_DATE, PATIENT_NAME");
	lbSortOrder.addItem("МОДИФИКАЦИЯ", "DATE_MODIFIED, PATIENT_NAME");

	lbSortOrder.addChangeHandler(new ChangeHandler() {

	    @Override
	    public void onChange(ChangeEvent event) {
		// TODO Auto-generated method stub
		int i = lbSortOrder.getSelectedIndex();
		sortOrder = lbSortOrder.getValue(i);
		spanel.searchWorklist();
		// searchStudyes(false);
	    }
	});

	toolPanel.add(lbSortOrder);

	// label = new Label("максимум - " + maxResultCount);
	// label.addStyleName("DicomItemValue");
	// toolPanel.add(label);

	Button printBtn = new Button("Печать");
	toolPanel.add(printBtn);
	printBtn.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if (studies != null) {
		    spanel.resultPanel.clear();

		    Label l = new Label("Количество Исследований: " + studies.size());
		    l.addStyleName("DicomItem");
		    spanel.resultPanel.add(l);

		    Grid resultTable = new Grid(studies.size() + 1, 5);
		    resultTable.addStyleName("WorkListPanel");
		    // DOM.setStyleAttribute(resultTable.getElement(),
		    // "background", "white");

		    resultTable.setWidget(0, 0, makeItem("№"));
		    resultTable.setWidget(0, 1, makeItem("Врач"));
		    resultTable.setWidget(0, 2, makeItem("ФИО"));
		    resultTable.setWidget(0, 3, makeItem("ГОД"));
		    resultTable.setWidget(0, 4, makeItem("ОПИСАНИЕ"));

		    resultTable.setBorderWidth(1);
		    int row = 1;
		    for (Iterator<StudyProxy> iter = studies.iterator(); iter.hasNext();) {
			StudyProxy proxy = iter.next();
			resultTable.setWidget(row, 0, makeItem("" + row));
			resultTable.setWidget(row, 1, makeItem(proxy.getStudyDoctor()));
			resultTable.setWidget(row, 2, makeItem(proxy.getPatientName()));

			resultTable.setWidget(row, 3, makeItem(Utils.dateFormatYEARONLY.format(Utils.dateFormatSql
				.parse(proxy.getPatientBirthDate()))));
			resultTable.setWidget(row, 4, makeItem(proxy.getStudyViewprotocol()));
			row++;
		    }
		    spanel.resultPanel.add(resultTable);
		    Window.print();
		}
	    }

	    private HTML makeItem(String s) {
		// TODO Auto-generated method stub
		HTML html = new HTML(s);
		html.addStyleName("WorkListPanelItem");
		return html;

	    }
	});

	initWidget(mainPanel);

    }

}
