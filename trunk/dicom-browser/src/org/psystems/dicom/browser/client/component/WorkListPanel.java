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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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
	
	public final static int maxResultCount = 100;

	/**
	 * @param application TODO Убрать и вызывать через static методы???
	 */
	public WorkListPanel(Dicom_browser application) {

		this.Application = application;
		
		dateBegin = Dicom_browser.dateFormatSql.format(new Date());
		dateEnd = Dicom_browser.dateFormatSql.format(new Date());
		
		VerticalPanel mainPanel = new VerticalPanel();

		HorizontalPanel toolPanel = new HorizontalPanel();
		mainPanel.add(toolPanel);

		Label label = new Label("Список исследований (максимум - "+maxResultCount+"). выберите диапазон дат: ");
		label.addStyleName("DicomItemValue");
		toolPanel.add(label);
		
		studyDateBoxBegin = new DateBox();
		studyDateBoxBegin.setFormat(new DateBox.DefaultFormat(
				Dicom_browser.dateFormatUser));
		studyDateBoxBegin.setValue(new Date());
		studyDateBoxBegin.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				dateBegin = Dicom_browser.dateFormatSql.format(event
						.getValue());
//				System.out.println("!!!!val="+dateBegin);
				searchStudyes();
			}
		});

		toolPanel.add(studyDateBoxBegin);
		
		label = new Label(" -  ");
		label.addStyleName("DicomItemValue");
		toolPanel.add(label);

		studyDateBoxEnd = new DateBox();
		studyDateBoxEnd.setFormat(new DateBox.DefaultFormat(
				Dicom_browser.dateFormatUser));
		studyDateBoxEnd.setValue(new Date());
		studyDateBoxEnd.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				dateEnd = Dicom_browser.dateFormatSql.format(event
						.getValue());
//				System.out.println("!!!!val="+dateBegin);
				searchStudyes();
			}
		});

		toolPanel.add(studyDateBoxEnd);

		
		resultPanel = new VerticalPanel();
		mainPanel.add(resultPanel);
		resultPanel.setSpacing(10);

		initWidget(mainPanel);
		
		searchStudyes();

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
	private void searchStudyes() {

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

						ArrayList<StudyProxy> cortegeList = result.getData();
						for (Iterator<StudyProxy> it = cortegeList.iterator(); it
								.hasNext();) {

							StudyProxy studyProxy = it.next();
							VerticalPanel table = new VerticalPanel();

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

							resultPanel.add(table);

							StudyCardPanel s = new StudyCardPanel(
									Application.browserService, studyProxy);
							table.add(s);

							// for (Iterator<DcmFileProxy> iter = studyProxy
							// .getFiles().iterator(); iter.hasNext();) {
							// DcmFileProxy dcmfileProxy = iter.next();
							// SearchedItem s = new SearchedItem(
							// browserService, dcmfileProxy);
							// table.add(s);
							// }
							
							
							
						}
						
						if (cortegeList.size() >= maxResultCount) {
							HTML emptyStr = new HTML();
							emptyStr.setWidth("900px");
							emptyStr.setStyleName("DicomItemValue");
							emptyStr
									.setHTML("Показаны только первые "
											+ maxResultCount
											+ " строк! Чтобы посмотреть все - сужайте критерий поиска.");

							resultPanel.add(emptyStr);
						}

						if (cortegeList.size() == 0) {
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
