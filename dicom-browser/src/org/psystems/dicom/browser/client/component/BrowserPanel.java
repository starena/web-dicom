/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.psystems.dicom.browser.client.Dicom_browser;
import org.psystems.dicom.browser.client.SearchedItem;
import org.psystems.dicom.browser.client.TransactionTimer;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.RPCDcmProxyEvent;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;
import org.psystems.dicom.browser.client.service.ItemSuggestService;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * Панель обозревателя
 * 
 * @author dima_d
 * 
 */
public class BrowserPanel extends Composite implements
		ValueChangeHandler<String> {

	private Dicom_browser Application;

	// TODO Вынести панель с приглашением в отдельный класс
	private boolean showPageIntro = true;// Показ страницы с приглашением

	private String searchTitle = "...введите фамилию (% - любой символ)...";
	private VerticalPanel resultPanel;
	private Button sendButton;
	private Button clearButton;
	private SuggestBox nameField;

	// Идентификатор транзакции (Время последнего запроса)
	private long searchTransactionID;

	/**
	 * @param application
	 */
	public BrowserPanel(Dicom_browser application) {

		this.Application = application;
		History.addValueChangeHandler(this);

		// DecoratorPanel mainPanel = new DecoratorPanel();
		VerticalPanel mainPanel = new VerticalPanel();
		_construct(mainPanel);
		initWidget(mainPanel);

	}

	/**
	 * Размещение компонентов TODO Перенести в суперкласс и сделать абстрактным?
	 * 
	 * @param mainPanel
	 */
	private void _construct(Panel mainPanel) {

		HorizontalPanel searchPanel = new HorizontalPanel();
		mainPanel.add(searchPanel);
		searchPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

		resultPanel = new VerticalPanel();
		mainPanel.add(resultPanel);

		sendButton = new Button("Поиск");
		ItemSuggestOracle oracle = new ItemSuggestOracle();
		nameField = new SuggestBox(oracle);
		nameField.addStyleName("DicomSuggestionEmpty");

		nameField.setLimit(10);
		nameField.setWidth("600px");
		nameField.setTitle(searchTitle);
		nameField.setText(searchTitle);

		nameField.getTextBox().addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {

				if (showPageIntro) {
					showPageIntro = false;
					resultPanel.clear();
				}

				nameField.removeStyleName("DicomSuggestionEmpty");
				nameField.addStyleName("DicomSuggestion");

				if (nameField.getText().equals(nameField.getTitle())) {
					nameField.setValue("");
				} else {
					nameField.setValue(nameField.getValue());
				}
			}

		});

		nameField.getTextBox().addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {

				if (nameField.getText().equals("")) {
					nameField.setValue(nameField.getTitle());
					nameField.removeStyleName("DicomSuggestion");
					nameField.addStyleName("DicomSuggestionEmpty");
				} else {
					nameField.setValue(nameField.getValue());
				}

			}

		});

		// onblur="this.value=(this.value=='')?this.title:this.value;"
		// onfocus="this.value=(this.value==this.title)?'':this.value;"

		sendButton.addStyleName("sendButton");

		searchPanel.add(nameField);

		searchPanel.add(sendButton);

		clearButton = new Button("Сброс");
		searchPanel.add(clearButton);

		clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				nameField.setText(searchTitle);
				nameField.removeStyleName("DicomSuggestion");
				nameField.addStyleName("DicomSuggestionEmpty");
				resultPanel.clear();
				transactionFinished();
			}

		});

		nameField.addSelectionHandler(new SelectionHandler<Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				// System.out.println("addSelectionHandler "+event);
				searchItems();
			}
		});

		sendButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchItems();
			}
		});

		HTML intro = new HTML();
		intro.setWidth("800px");
		// intro.setStyleName("DicomItemValue");
		intro
				.setHTML(" <br><p> Добро пожаловать в экспериментальную версию проекта"
						+ " по работе с исследованиями полученных с аппаратов поддерживающих стандарт DICOM </p>"
						+ " <p> Начните свою работу с поиска необходимого вам исследования."
						+ " Просто начните в поисковой строке набирать фамилию пациента и нажмите [enter]."
						+ " В качестве дополнения можно использовать маску 'групповой символ' процент (%) или подчеркивание (_)"
						+ " Для начала поиска можно также нажать кнопу 'Поиск'  </p>"
						+ " <p> Дополнительная информация по проекту"
						+ " <a href='http://code.google.com/p/web-dicom/' target='new'>"
						+ " http://code.google.com/p/web-dicom/</a>"
						+ " (откроется в новом окне) </p>" + "<br><br>");

		resultPanel.add(intro);

		FlexTable statanel = new FlexTable();
		resultPanel.add(statanel);

		Image image = new Image("stat/chart/clientreqs/");
		image.setTitle("Поисковые запросы");
		statanel.setWidget(1, 0, image);
		statanel.getFlexCellFormatter().setColSpan(0, 0, 2);

		image = new Image("stat/chart/dailyload/");
		image.setTitle("Загрузка данных");
		statanel.setWidget(2, 0, image);
		statanel.getFlexCellFormatter().setColSpan(1, 0, 2);

		// image = new Image("stat/chart/usagestore/");
		// image.setTitle("Использование дискового пространства");
		// statanel.setWidget(3,0,image);
		// statanel.getFlexCellFormatter().setColSpan(2, 0, 2);

		Hyperlink tools = new Hyperlink("Создать исследование", "newstudy");
		searchPanel.add(tools);

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// TODO Auto-generated method stub

	}

	/**
	 * старт транзакции
	 */
	private void transactionStarted() {
		resultPanel.clear();
		sendButton.setEnabled(false);
		clearButton.setEnabled(false);
	}

	/**
	 * Завершение транзакции
	 */
	private void transactionFinished() {

		Application.hideWorkStatusMsg();
		sendButton.setEnabled(true);
		clearButton.setEnabled(true);
		nameField.setFocus(true);
		searchTransactionID = new Date().getTime();
	}

	/**
	 * Прерывание транзакции
	 */
	private void transactionInterrupt() {
		transactionFinished();
	}

	/**
	 * Send the name from the nameField to the server and wait for a response.
	 */
	private void searchItems() {

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

		String textToServer = nameField.getText();
		transactionStarted();

		Application.browserService.findStudy(searchTransactionID,
				Dicom_browser.version, textToServer,
				new AsyncCallback<RPCDcmProxyEvent>() {

					public void onFailure(Throwable caught) {

						transactionFinished();
						Application
								.showErrorDlg((DefaultGWTRPCException) caught);

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

							SearchedItem s = new SearchedItem(
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

						if (cortegeList.size() == 0) {
							showNotFound();
						}

						transactionFinished();

					}

				});
	}

	protected void showNotFound() {
		HTML emptyStr = new HTML();
		emptyStr.setWidth("400px");
		emptyStr.setStyleName("DicomItemValue");
		emptyStr.setHTML("Ничего не найдено...");

		resultPanel.add(emptyStr);

		emptyStr = new HTML();
		emptyStr.setWidth("800px");
		emptyStr
				.setHTML(

				" <p> Попробуйте сузить поиск используя 'групповой символ' (англ. wildcard). "
						+ " <br><br>Групповой символ (заменяющий один или несколько символов:"
						+ " знак 'подчеркивание' (_) может представлять любой одиночный символ; "
						+ " процент (%) используется для представления любого символа или группы символов) "
						+ " Например набрав: пе%в получите результаты пациентов с фамилиями"
						+ " Петров, Переладов. А набрав  пе___в - получите результат: Петров"
						+ " </p>");

		resultPanel.add(emptyStr);
	}

	public class ItemSuggestOracle extends SuggestOracle {
		public boolean isDisplayStringHTML() {
			return true;
		}

		public void requestSuggestions(SuggestOracle.Request req,
				SuggestOracle.Callback callback) {
			try {
				ItemSuggestService.Util.getInstance().getSuggestions(
						searchTransactionID, Dicom_browser.version, req,
						new ItemSuggestCallback(req, callback));
			} catch (DefaultGWTRPCException e) {
				Application.showErrorDlg(e);
				e.printStackTrace();
			}
		}

		class ItemSuggestCallback implements AsyncCallback {
			private SuggestOracle.Request req;
			private SuggestOracle.Callback callback;

			public ItemSuggestCallback(SuggestOracle.Request _req,
					SuggestOracle.Callback _callback) {
				req = _req;
				callback = _callback;
			}

			public void onFailure(Throwable error) {
				Application.showErrorDlg((DefaultGWTRPCException) error);
				callback.onSuggestionsReady(req, new SuggestOracle.Response());
				Application.showErrorDlg((DefaultGWTRPCException) error);
			}

			public void onSuccess(Object retValue) {

				// TODO попробовать сделать нормлаьный interrupt (дабы
				// не качать все данные)
				// Если сменился идентификатор транзакции, то ничего не
				// принимаем
				if (searchTransactionID != ((SuggestTransactedResponse) retValue)
						.getTransactionId()) {
					return;
				}

				callback.onSuggestionsReady(req,
						(SuggestOracle.Response) retValue);
			}
		}

	}

}
