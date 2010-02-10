package org.psystems.dicom.browser.client;

import java.util.Date;

import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	private SuggestBox nameField;

	long lastRequestTime; // Время последнего запроса

	private String searchTitle = "...введите фамилию (% - любой символ)...";
	private static PopupPanel workStatusPanel;// панель состояния работы
	// запросов
	private static HTML workMsg;

	private boolean showPageIntro = true;// Показ страницы с приглашением

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		_workStatusDlg();

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

		Button b = new Button("Сброс");
		hp.add(b);

		b.addClickHandler(new ClickHandler() {

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
				sendNameToServer();
			}
		});

		sendButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}
		});

		HTML intro = new HTML();
		intro.setWidth("800px");
		intro.setStyleName("DicomItemValue");
		intro
				.setHTML(" <br><p> Добро пожаловать в экспериментальную версию проекта"
						+ " по работе с исследованиями полученных с аппаратов поддерживающих стандарт DICOM </p>" +
								" <p> Начните свою работу с .... в поисковой строке .... </p>"
						+ " Всю информацию по данному проекту вы можете получить"
						+ " <a href='http://code.google.com/p/web-dicom/'> [здесь] (необходимо подключение к глобальной сети internet) </a>"
						+ "<br><br>");

		RootPanel.get("resultContainer").add(intro);

		Image image = new Image("/chart/usagestorage");
		image.setTitle("Диаграмма");
		RootPanel.get("resultContainer").add(image);
		
		HTML introFooter = new HTML();
		introFooter.setWidth("800px");
		introFooter.setStyleName("DicomItemValue");
		introFooter
				.setHTML(" <br><br> psystems.org");

		RootPanel.get("resultContainer").add(introFooter);

	}

	/**
	 * Send the name from the nameField to the server and wait for a response.
	 */
	private void sendNameToServer() {

		Date d = new Date();
		lastRequestTime = d.getTime();

		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat("dd.MM.YYY H:m:s S");
		showWorkStatusMsg("идет <b> получение данных </b> ... "
				+ dateFormat.format(d));

		sendButton.setEnabled(false);
		String textToServer = nameField.getText();
		RootPanel.get("resultContainer").clear();

		browserService.findStudy(version, textToServer,
				new AsyncCallback<DcmFileProxy[]>() {

					public void onFailure(Throwable caught) {
						hideWorkStatusMsg();
						showErrorDlg((DefaultGWTRPCException) caught);
						sendButton.setEnabled(true);
						nameField.setFocus(true);
					}

					public void onSuccess(DcmFileProxy[] result) {

						hideWorkStatusMsg();

						for (int i = 0; i < result.length; i++) {
							DcmFileProxy proxy = result[i];
							SearchedItem s = new SearchedItem(proxy);
							RootPanel.get("resultContainer").add(s);
						}
						sendButton.setEnabled(true);
						nameField.setFocus(true);
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
				ItemSuggestService.Util.getInstance().getSuggestions(version,
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
				callback.onSuggestionsReady(req,
						(SuggestOracle.Response) retValue);
			}
		}

	}

	/**
	 * создание диалога состояния поцесса работы
	 */
	private void _workStatusDlg() {

		workStatusPanel = new PopupPanel();
		workStatusPanel.hide();
		workStatusPanel.setStyleName("msgPopupPanel");
		// workStatusPanel.setAnimationEnabled(false);
		workMsg = new HTML("");
		workMsg.addStyleName("msgPopupPanelItem");

		workStatusPanel.add(workMsg);
	}

	/**
	 * Показ панели состояния процесса
	 * 
	 * @param msg
	 */
	public static void showWorkStatusMsg(String msg) {

		workMsg.setHTML(msg);
		workStatusPanel.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {

				workStatusPanel.setPopupPosition(offsetWidth, offsetHeight);
				int left = (Window.getClientWidth() - offsetWidth) >> 1;
				int top = 0;

				workStatusPanel.setPopupPosition(Window.getScrollLeft() + left,
						Window.getScrollTop() + top);
			}

		});
	}

	/**
	 * Скрытие панели состояния процесса
	 */
	public static void hideWorkStatusMsg() {
		workStatusPanel.hide();
	}
}