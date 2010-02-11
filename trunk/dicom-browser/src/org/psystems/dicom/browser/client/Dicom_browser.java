package org.psystems.dicom.browser.client;

import java.util.Date;

import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.RPCDcmFileProxyEvent;
import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;
import org.psystems.dicom.browser.client.service.BrowserService;
import org.psystems.dicom.browser.client.service.BrowserServiceAsync;
import org.psystems.dicom.browser.client.service.ItemSuggestService;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dicom_browser implements EntryPoint {

	// Версия ПО (используется для проверки на стороне сервере при обновлении
	// клиента)
	public static String version = "0.1a";

	// Create a remote service proxy
	private final BrowserServiceAsync browserService = GWT
			.create(BrowserService.class);

	private DialogBox errorDialogBox;
	private HTML errorResponseLabel;
	private Button sendButton;
	private Button clearButton;
	private SuggestBox nameField;

	// Идентификатор транзакции (Время последнего запроса)
	long searchItemsStransactionID;

	private String searchTitle = "...введите фамилию (% - любой символ)...";
	// панель состояния работы запросов
	private PopupPanel workStatusPopup;
	private FlowPanel workStatusPanel;

	private boolean showPageIntro = true;// Показ страницы с приглашением

	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		_workStatusPopup();

		HorizontalPanel hp = new HorizontalPanel();
		RootPanel.get("searchContainer").add(hp);
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

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
					RootPanel.get("resultContainer").clear();
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

		createErorrDlg();

		sendButton.addStyleName("sendButton");

		hp.add(nameField);

		hp.add(sendButton);

		clearButton = new Button("Сброс");
		hp.add(clearButton);

		clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				nameField.setText(searchTitle);
				nameField.removeStyleName("DicomSuggestion");
				nameField.addStyleName("DicomSuggestionEmpty");
				RootPanel.get("resultContainer").clear();
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
		intro.setStyleName("DicomItemValue");
		intro
				.setHTML(" <br><p> Добро пожаловать в экспериментальную версию проекта"
						+ " по работе с исследованиями полученных с аппаратов поддерживающих стандарт DICOM </p>"
						+ " <p> Начните свою работу с .... в поисковой строке .... </p>"
						+ " Всю информацию по данному проекту вы можете получить"
						+ " <a href='http://code.google.com/p/web-dicom/'> [здесь] (необходимо подключение к глобальной сети internet) </a>"
						+ "<br><br>");

		RootPanel.get("resultContainer").add(intro);

		Image image = new Image("chart/usagestorage");
		image.setTitle("Диаграмма");
		RootPanel.get("resultContainer").add(image);

		HTML introFooter = new HTML();
		introFooter.setWidth("800px");
		introFooter.setStyleName("DicomItemValue");
		introFooter.setHTML(" <br><br> psystems.org");

		RootPanel.get("resultContainer").add(introFooter);

	}

	/**
	 * Send the name from the nameField to the server and wait for a response.
	 */
	private void searchItems() {

		Date d = new Date();
		searchItemsStransactionID = d.getTime();

		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat("dd.MM.yyyy. G 'at' HH:mm:ss vvvv");
		showWorkStatusMsg("идет <b> получение данных </b> ... "
				+ dateFormat.format(d));

		Timer t = new Timer() {
			public void run() {
				addToWorkStatusMsg("Еще работаем...");
				// HTML l = new HTML("<a href=''>[Остановить]</a>");
				// DOM.setStyleAttribute(l.getElement(), "cursor", "pointer");

				Button b = new Button("Остановить");
				b.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						transactionInterrupt();
						
					}

				});

				addToWorkStatusWidget(b);
			}
		};
		t.schedule(2000);

		
		String textToServer = nameField.getText();
		transactionStarted();

		browserService.findStudy(searchItemsStransactionID, version,
				textToServer, new AsyncCallback<RPCDcmFileProxyEvent>() {

					public void onFailure(Throwable caught) {

						transactionFinished();
						showErrorDlg((DefaultGWTRPCException) caught);
						
					}

					public void onSuccess(RPCDcmFileProxyEvent result) {

						// TODO попробовать сделать нормлаьный interrupt (дабы
						// не качать все данные)
						// Если сменился идентификатор транзакции, то ничего не
						// принимаем
						if (searchItemsStransactionID != result
								.getTransactionId()) {
							return;
						}

						DcmFileProxy[] data = result.getData();
						hideWorkStatusMsg();

						for (int i = 0; i < data.length; i++) {
							DcmFileProxy proxy = data[i];
							SearchedItem s = new SearchedItem(proxy);
							RootPanel.get("resultContainer").add(s);
						}
						
						transactionFinished();
						
					}

				});
	}

	private void showErrorDlg(DefaultGWTRPCException e) {
		errorResponseLabel.setHTML("Ошибка: " + e.getText());
		errorDialogBox.show();
		errorDialogBox.center();

	}

	/**
	 * 
	 */
	private void createErorrDlg() {
		errorDialogBox = new DialogBox();
		errorDialogBox.setText("Ошибка!");
		errorDialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		errorResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");

		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(errorResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		errorDialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				errorDialogBox.hide();
			}
		});

	}

	public class ItemSuggestOracle extends SuggestOracle {
		public boolean isDisplayStringHTML() {
			return true;
		}

		public void requestSuggestions(SuggestOracle.Request req,
				SuggestOracle.Callback callback) {
			try {
				ItemSuggestService.Util.getInstance().getSuggestions(searchItemsStransactionID,version,
						req, new ItemSuggestCallback(req, callback));
			} catch (DefaultGWTRPCException e) {
				showErrorDlg(e);
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
				showErrorDlg((DefaultGWTRPCException) error);
				callback.onSuggestionsReady(req, new SuggestOracle.Response());
				showErrorDlg((DefaultGWTRPCException) error);
			}

			public void onSuccess(Object retValue) {
				
				// TODO попробовать сделать нормлаьный interrupt (дабы
				// не качать все данные)
				// Если сменился идентификатор транзакции, то ничего не
				// принимаем
				
				if (searchItemsStransactionID != ((SuggestTransactedResponse)retValue)
						.getTransactionId()) {
					return;
				}
				
				callback.onSuggestionsReady(req,
						(SuggestOracle.Response) retValue);
			}
		}

	}

	/**
	 * создание диалога состояния поцесса работы
	 */
	private void _workStatusPopup() {

		workStatusPopup = new PopupPanel();
		workStatusPopup.hide();
		workStatusPopup.setStyleName("msgPopupPanel");
		// workStatusPanel.setAnimationEnabled(false);
		workStatusPanel = new FlowPanel();
		// workMsg = new HTML("");
		workStatusPanel.addStyleName("msgPopupPanelItem");
		workStatusPopup.add(workStatusPanel);
	}

	/**
	 * Показ панели состояния процесса
	 * 
	 * @param html
	 *            HTML сообщение
	 */
	private void showWorkStatusMsg(String html) {

		workStatusPanel.add(new HTML(html));
		workStatusPopup.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {

				workStatusPopup.setPopupPosition(offsetWidth, offsetHeight);
				int left = (Window.getClientWidth() - offsetWidth) >> 1;
				int top = 0;

				workStatusPopup.setPopupPosition(Window.getScrollLeft() + left,
						Window.getScrollTop() + top);
			}

		});
	}

	/**
	 * Добавление в сообщение строки
	 * 
	 * @param html
	 */
	private void addToWorkStatusMsg(String html) {
		workStatusPanel.add(new HTML(html));
	}

	/**
	 * Добавление в сообщение виджета
	 * 
	 * @param html
	 */
	private void addToWorkStatusWidget(Widget widget) {
		workStatusPanel.add(widget);
	}

	/**
	 * Скрытие панели состояния процесса
	 */
	public void hideWorkStatusMsg() {
		workStatusPanel.clear();
		workStatusPopup.hide();
	}

	/**
	 * Завершение транзакции
	 */
	private void transactionFinished() {
		hideWorkStatusMsg();
		sendButton.setEnabled(true);
		clearButton.setEnabled(true);
		nameField.setFocus(true);
	}
	
	/**
	 * Завершение транзакции
	 */
	private void transactionStarted() {
		RootPanel.get("resultContainer").clear();
		sendButton.setEnabled(false);
		clearButton.setEnabled(false);
	}
	
	
	private void transactionInterrupt() {
		searchItemsStransactionID = new Date().getTime();
		transactionFinished() ;
	}
}
