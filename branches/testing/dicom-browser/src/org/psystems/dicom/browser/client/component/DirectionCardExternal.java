package org.psystems.dicom.browser.client.component;

import java.util.Date;
import java.util.Iterator;

import org.psystems.dicom.browser.client.Browser;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Направление из других ЛПУ
 * 
 * @author dima_d
 * 
 */
public class DirectionCardExternal extends Composite {

    private FlexTable formTable;
    private TextBox tbLpu;
    private TextBox tbDrnNum;
    private DateBox drnDateBox;
    private Button btnSaveAll;
    private ListBox lbModality;
    private PatientProxy patientProxy;

    public DirectionCardExternal(PatientProxy patientProxy) {
	
	this.patientProxy = patientProxy;
	DecoratorPanel mainPanel = new DecoratorPanel();

	formTable = new FlexTable();
	mainPanel.setWidget(formTable);

	int rowCounter = 0;

	//

	tbLpu = new TextBox();
	tbLpu.setWidth("40em");
	addFormRow(rowCounter++, "ЛПУ", tbLpu);

	//

	tbDrnNum = new TextBox();
	tbDrnNum.setWidth("40em");
	addFormRow(rowCounter++, "№", tbDrnNum);

	//

	drnDateBox = new DateBox();
	drnDateBox.setWidth("10em");
	drnDateBox.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	Date d = new Date();
	drnDateBox.setValue(d);

	addFormRow(rowCounter++, "Дата", drnDateBox);

	// Модальность

	lbModality = new ListBox();
	for (Iterator<String> iter = StudyManagePanel.dicModalityKeys.iterator(); iter.hasNext();) {
	    String key = iter.next();
	    lbModality.addItem(StudyManagePanel.dicModality.get(key) + " (" + key + ")", key);
	}

	addFormRow(rowCounter++, "Модальность", lbModality);

	//
	btnSaveAll = new Button("Сохранить направление");
	addFormRow(rowCounter++, btnSaveAll);

	btnSaveAll.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {

		saveDrn();

	    }
	});

	initWidget(mainPanel);
    }

    /**
     * 
     */
    protected void saveDrn() {

	DirectionProxy drnProxy = new DirectionProxy();
	drnProxy.setPatient(patientProxy);
	
	String date = Utils.dateFormatSql.format(drnDateBox.getValue());
	drnProxy.setDateDirection(date);
	drnProxy.setDateTimePlanned(date + " 00:00:00");
	drnProxy.setModality(lbModality.getValue(lbModality.getSelectedIndex()));
	drnProxy.setSenderLpu(tbLpu.getValue());
	drnProxy.setDirectionId(tbDrnNum.getValue());

	Browser.browserService.saveDirection(drnProxy, new AsyncCallback<Void>() {

	    @Override
	    public void onFailure(Throwable caught) {
		btnSaveAll.setEnabled(true);
		Browser.showErrorDlg(caught);
	    }

	    @Override
	    public void onSuccess(Void result) {
		btnSaveAll.setEnabled(false);
		DirectionCardExternal.this.removeFromParent();
	    }
	});

    }

    /**
     * Добавление строчки на форму
     * 
     * @param row
     * @param title
     * @param input
     */
    private void addFormRow(int row, String title, Widget input) {

	formTable.setWidget(row, 0, makeItemLabel(title));
	formTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	formTable.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
	formTable.setWidget(row, 1, input);

    }

    /**
     * Добавление вмджетов на форму
     * 
     * @param row
     * @param title
     * @param input
     */
    private void addFormRow(int row, Widget title, Widget input) {

	formTable.setWidget(row, 0, title);
	formTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	formTable.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
	formTable.setWidget(row, 1, input);

    }

    /**
     * Добавление виджета на форму
     * 
     * @param row
     * @param input
     */
    private void addFormRow(int row, Widget input) {

	formTable.setWidget(row, 0, input);
	formTable.getFlexCellFormatter().setColSpan(row, 0, 2);
	formTable.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);

    }

    /**
     * @param title
     * @return
     */
    Widget makeItemLabel(String title) {
	Label label = new Label(title);
	// TODO Убрать в css
	DOM.setStyleAttribute(label.getElement(), "font", "1.5em/ 150% normal Verdana, Tahoma, sans-serif");
	return label;
    }
}
