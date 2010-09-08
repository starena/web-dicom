/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.psystems.dicom.browser.client.Dicom_browser;
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

	VerticalPanel resultPanel;
	private DateBox studyDateBoxBegin;
	private long searchTransactionID;
	private Dicom_browser Application;
	protected String dateBegin;
	protected String dateEnd;
	private DateBox studyDateBoxEnd;
	protected String manufacturerModelName = "RENEXFLUORO3";
	protected String studyResult = "new";
	protected ArrayList<StudyProxy> studies;
	
//	private String datePattern = "dd.MM.yyyy";
	private String datePatternYEAR = "yyyy";
	protected String sortOrder = null;
	public final static int maxResultCount = 300;

	/**
	 * @param application TODO Убрать и вызывать через static методы???
	 */
	public WorkListPanel(Dicom_browser application) {

		this.Application = application;
		
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
		studyDateBoxBegin.setFormat(new DateBox.DefaultFormat(
				Utils.dateFormatUser));
		studyDateBoxBegin.setValue(new Date());
		studyDateBoxBegin.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				dateBegin = Utils.dateFormatSql.format(event
						.getValue());
//				System.out.println("!!!!val="+dateBegin);
				searchStudyes(false);
			}
		});

		toolPanel.add(studyDateBoxBegin);
		
		label = new Label(" по ");
		label.addStyleName("DicomItemValue");
		toolPanel.add(label);

		studyDateBoxEnd = new DateBox();
		studyDateBoxEnd.setWidth("80px");
		studyDateBoxEnd.setFormat(new DateBox.DefaultFormat(
				Utils.dateFormatUser));
		studyDateBoxEnd.setValue(new Date());
		studyDateBoxEnd.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				dateEnd = Utils.dateFormatSql.format(event
						.getValue());
//				System.out.println("!!!!val="+dateBegin);
				searchStudyes(false);
			}
		});

		toolPanel.add(studyDateBoxEnd);

		label = new Label("аппарат:");
		label.addStyleName("DicomItemValue");
		toolPanel.add(label);
		
		//
		final ListBox lbManufacturerModelName = new ListBox();
//		lbCommentsTemplates.setName("00100040");
		lbManufacturerModelName.addItem("RENEXFLUORO3", "RENEXFLUORO3");
		lbManufacturerModelName.addItem("КРТ-Электрон","КРТ-Электрон");
		lbManufacturerModelName.addItem("Мамография","MAMOGRAF");
		
		lbManufacturerModelName.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				int i = lbManufacturerModelName.getSelectedIndex();
				manufacturerModelName = lbManufacturerModelName.getValue(i);
				searchStudyes(false);
			}
		});
		
		toolPanel.add(lbManufacturerModelName);
		
		
		//
		final ListBox lbStudyResult = new ListBox();
		lbStudyResult.addItem("Неописанные", "new");
		lbStudyResult.addItem("Все","all");
		lbStudyResult.addItem("Описанные","old");
		
		lbStudyResult.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
//				System.out.println("!!! "+event)!!!;
				int i = lbStudyResult.getSelectedIndex();
				studyResult = lbStudyResult.getValue(i);
				searchStudyes(false);
			}
		});
		
		
		toolPanel.add(lbStudyResult);
		
		//
		final ListBox lbSortOrder = new ListBox();
		lbSortOrder.addItem("ФИО", "PATIENT_NAME, STUDY_DATE");
		lbSortOrder.addItem("ДАТА","STUDY_DATE, PATIENT_NAME");
		
		lbSortOrder.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				int i = lbSortOrder.getSelectedIndex();
				sortOrder = lbSortOrder.getValue(i);
				searchStudyes(false);
			}
		});
		
		
		toolPanel.add(lbSortOrder);
		label = new Label("максимум - "+maxResultCount);
		label.addStyleName("DicomItemValue");
		toolPanel.add(label);
		
		Button printBtn = new Button("Печать");
		toolPanel.add(printBtn);
		printBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(studies!=null) {
					resultPanel.clear();
					
					
					Label l = new Label("Количество Исследований: "+studies.size());
					l.addStyleName("DicomItem");
					resultPanel.add(l);
					
					Grid resultTable = new Grid(studies.size()+1, 5);
					resultTable.addStyleName("WorkListPanel");
//					DOM.setStyleAttribute(resultTable.getElement(), "background", "white");
					
					
					resultTable.setWidget(0, 0, makeItem("№"));
					resultTable.setWidget(0, 1, makeItem("Врач"));
					resultTable.setWidget(0, 2, makeItem("ФИО"));
					resultTable.setWidget(0, 3, makeItem("ГОД"));
					resultTable.setWidget(0, 4, makeItem("ОПИСАНИЕ"));
					
					resultTable.setBorderWidth(1);
					int row = 1;
					for(  Iterator<StudyProxy> iter = studies.iterator(); iter.hasNext(); ) {
						StudyProxy proxy = iter.next();
						resultTable.setWidget(row, 0, makeItem(""+row));
						resultTable.setWidget(row, 1, makeItem(proxy.getStudyDoctor()));
						resultTable.setWidget(row, 2, makeItem(proxy.getPatientName()));
						resultTable.setWidget(row, 3, makeItem(proxy.getPatientBirthDateAsString(datePatternYEAR)));
						resultTable.setWidget(row, 4,makeItem(proxy.getStudyViewprotocol()));
						row++;
					}
					resultPanel.add(resultTable);
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
		
		resultPanel = new VerticalPanel();
		mainPanel.add(resultPanel);
		//TODO Убрать в css
		resultPanel.setSpacing(10);
		DOM.setStyleAttribute(resultPanel.getElement(), "background", "#E9EDF5");
		resultPanel.setWidth("100%");
		
		initWidget(mainPanel);
		//TODO Убрать в css
		setWidth("100%");
		searchStudyes(false);

	}

	public void clear() {
		resultPanel.clear();
	}

	public void add(Widget w) {
		resultPanel.add(w);
	}
	
	
	
	/**
	 * Поиск исследований
	 */
	private void searchStudyes(boolean forPrint) {

		Date d = new Date();
		searchTransactionID = d.getTime();

		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat("dd.MM.yyyy. G 'at' HH:mm:ss vvvv");
		// showWorkStatusMsg("Послан <b> запрос данных </b> по пациенту ... "
		// + dateFormat.format(d));
		Application.showWorkStatusMsg("");

		TransactionTimer t = new TransactionTimer() {

			private int counter = 0;

			public void run() {

				// System.out.println("!!!!!!!!!!!! " + getTransactionId() + "="
				// + searchTransactionID);
				if (getTransactionId() != searchTransactionID) {
					cancel();
					return;
				}

				if (counter == 0) {

					Button b = new Button("Остановить поиск");
					b.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							transactionInterrupt();
						}
					});

					// TODO Вынести логику в Application
					Application.addToWorkStatusWidget(b);
					// TODO Вынести логику в Application
					Application
							.addToWorkStatusMsg(" Возможно имеется <i>проблема</i> со связью. Вы <b>всегда</b> можете остановить поиск...");
				}
				counter++;

				Application.addToWorkStatusMsg(" Поиск продолжается " + counter
						* 2 + " сек.");
				// HTML l = new HTML("<a href=''>[Остановить]</a>");
				// DOM.setStyleAttribute(l.getElement(), "cursor",
				// "pointer");

			}
		};
		t.setTransactionId(searchTransactionID);
		// t.schedule(2000);
		t.scheduleRepeating(3000);

//		String querystr = nameField.getText();
		transactionStarted();

//		PatientsRPCRequest req = new PatientsRPCRequest();
//		req.setTransactionId(searchTransactionID);
//		req.setQueryStr(querystr);
//		req.setLimit(20);
//		
		
		HashMap<String, String> attrs = new HashMap<String, String>();
		attrs.put("beginStudyDate", dateBegin);
		attrs.put("endStudyDate", dateEnd);
		attrs.put("manufacturerModelName", manufacturerModelName);
		attrs.put("studyResult", studyResult);
		attrs.put("sortOrder", sortOrder);
		
		Application.browserService.findStudy(searchTransactionID,
				Dicom_browser.version, "%", attrs,
				new AsyncCallback<RPCDcmProxyEvent>() {

					public void onFailure(Throwable caught) {

						transactionFinished();
						Application
								.showErrorDlg(caught);

					}

					public void onSuccess(RPCDcmProxyEvent result) {

						// TODO попробовать сделать нормлаьный interrupt (дабы
						// не качать все данные)
						// Если сменился идентификатор транзакции, то ничего не
						// принимаем
						if (searchTransactionID != result.getTransactionId()) {
							return;
						}

						Application.hideWorkStatusMsg();

						studies = result.getData();
						
						Label l = new Label("Количество Исследований: "+studies.size());
						l.addStyleName("DicomItem");
						resultPanel.add(l);
						
						for (Iterator<StudyProxy> it = studies.iterator(); it
								.hasNext();) {

							StudyProxy studyProxy = it.next();

							// if(cortege.getDcmProxies().size()>1) {
							// DecoratorPanel item = new DecoratorPanel();
							// DOM.setStyleAttribute(item.getElement(),
							// "margin",
							// "5px");
							// RootPanel.get("resultContainer").add(item);
							// item.setWidget(table);
							// } else {
							// RootPanel.get("resultContainer").add(table);
							// }


							StudyCard s = new StudyCard(false);
							s.setProxy(studyProxy);
							resultPanel.add(s);

							// for (Iterator<DcmFileProxy> iter = studyProxy
							// .getFiles().iterator(); iter.hasNext();) {
							// DcmFileProxy dcmfileProxy = iter.next();
							// SearchedItem s = new SearchedItem(
							// browserService, dcmfileProxy);
							// table.add(s);
							// }
							
							
							
						}
						
						if (studies.size() >= maxResultCount) {
							HTML emptyStr = new HTML();
							emptyStr.setWidth("900px");
							emptyStr.setStyleName("DicomItemValue");
							emptyStr
									.setHTML("Показаны только первые "
											+ maxResultCount
											+ " строк! Чтобы посмотреть все - сужайте критерий поиска.");

							resultPanel.add(emptyStr);
						}

						if (studies.size() == 0) {
							showNotFound();
						}

						transactionFinished();

					}

				});
	}

	/**
	 * старт транзакции
	 */
	private void transactionStarted() {
		resultPanel.clear();
//		sendButton.setEnabled(false);
//		clearButton.setEnabled(false);
	}

	/**
	 * Завершение транзакции
	 */
	private void transactionFinished() {

		Application.hideWorkStatusMsg();
//		sendButton.setEnabled(true);
//		clearButton.setEnabled(true);
//		nameField.setFocus(true);
		searchTransactionID = new Date().getTime();
	}

	/**
	 * Прерывание транзакции
	 */
	private void transactionInterrupt() {
		transactionFinished();
	}
	
	protected void showNotFound() {
		HTML emptyStr = new HTML();
		emptyStr.setWidth("400px");
//		emptyStr.setStyleName("DicomItemValue");
		emptyStr.setHTML("Ничего не найдено... Попробуйте выбрать другие даты...");

		resultPanel.add(emptyStr);

//		emptyStr = new HTML();
//		emptyStr.setWidth("800px");
//		emptyStr
//				.setHTML(
//
//				" <p> Попробуйте выбрать другую дату... </p>");
//
//		resultPanel.add(emptyStr);
	}
}