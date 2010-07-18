/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.dicom.browser.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.RPCDcmProxyEvent;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;
import org.psystems.dicom.browser.client.service.BrowserService;
import org.psystems.dicom.browser.client.service.BrowserServiceAsync;
import org.psystems.dicom.browser.client.service.ItemSuggestService;
import org.psystems.dicom.browser.client.service.ManageStydyService;
import org.psystems.dicom.browser.client.service.ManageStydyServiceAsync;

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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
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

	
	final ManageStydyServiceAsync manageStudyService = GWT
	.create(ManageStydyService.class);
	
	private DialogBox errorDialogBox;
	private HTML errorResponseLabel;
	private Button sendButton;
	private Button clearButton;
	private SuggestBox nameField;

	// Идентификатор транзакции (Время последнего запроса)
	long searchTransactionID;

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

		RootPanel.get("resultContainer").add(intro);

		FlexTable statanel = new FlexTable();
		RootPanel.get("resultContainer").add(statanel);

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
		hp.add(tools);
		
		
		
		
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				// TODO Auto-generated method stub
				System.out.println("!!! "+event.getValue());
				if(event.getValue().equals("newstudy")) {
					
					RootPanel.get("searchContainer").clear();
					RootPanel.get("resultContainer").clear();
					
					NewStudyPanel panel = new NewStudyPanel(manageStudyService);
					RootPanel.get("searchContainer").add(panel);
					
				}
			}
		});
		

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
		showWorkStatusMsg("");

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

					addToWorkStatusWidget(b);
					addToWorkStatusMsg(" Возможно имеется <i>проблема</i> со связью. Вы <b>всегда</b> можете остановить поиск...");
				}
				counter++;

				addToWorkStatusMsg(" Поиск продолжается " + counter * 2
						+ " сек.");
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

		browserService.findStudy(searchTransactionID, version, textToServer,
				new AsyncCallback<RPCDcmProxyEvent>() {

					public void onFailure(Throwable caught) {

						transactionFinished();
						showErrorDlg((DefaultGWTRPCException) caught);

					}

					public void onSuccess(RPCDcmProxyEvent result) {

						// TODO попробовать сделать нормлаьный interrupt (дабы
						// не качать все данные)
						// Если сменился идентификатор транзакции, то ничего не
						// принимаем
						if (searchTransactionID != result.getTransactionId()) {
							return;
						}

						hideWorkStatusMsg();

						ArrayList<StudyProxy> cortegeList = result
								.getData();
						for (Iterator<StudyProxy> it = cortegeList
								.iterator(); it.hasNext();) {

							StudyProxy studyProxy = it.next();
							VerticalPanel table = new VerticalPanel();
							
//							if(cortege.getDcmProxies().size()>1) {
//								DecoratorPanel item = new DecoratorPanel();
//								DOM.setStyleAttribute(item.getElement(), "margin",
//										"5px");
//								RootPanel.get("resultContainer").add(item);
//								item.setWidget(table);
//							} else {
//								RootPanel.get("resultContainer").add(table);
//							}

							RootPanel.get("resultContainer").add(table);
							
							SearchedItem s = new SearchedItem(browserService, studyProxy);
							table.add(s);

//							for (Iterator<DcmFileProxy> iter = studyProxy
//									.getFiles().iterator(); iter.hasNext();) {
//								DcmFileProxy dcmfileProxy = iter.next();
//								SearchedItem s = new SearchedItem(
//										browserService, dcmfileProxy);
//								table.add(s);
//							}
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

		RootPanel.get("resultContainer").add(emptyStr);

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

		RootPanel.get("resultContainer").add(emptyStr);
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
				ItemSuggestService.Util.getInstance().getSuggestions(
						searchTransactionID, version, req,
						new ItemSuggestCallback(req, callback));
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
				if (searchTransactionID != ((SuggestTransactedResponse) retValue)
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
		workStatusPopuppopupCentering();
	}

	/**
	 * Центровка сообщения
	 */
	private void workStatusPopuppopupCentering() {
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
		workStatusPopuppopupCentering();
	}

	/**
	 * Добавление в сообщение виджета
	 * 
	 * @param html
	 */
	private void addToWorkStatusWidget(Widget widget) {
		workStatusPanel.add(widget);
		workStatusPopuppopupCentering();
	}

	/**
	 * Скрытие панели состояния процесса
	 */
	public void hideWorkStatusMsg() {
		workStatusPanel.clear();
		workStatusPopup.hide();
	}

	/**
	 * старт транзакции
	 */
	private void transactionStarted() {
		RootPanel.get("resultContainer").clear();
		sendButton.setEnabled(false);
		clearButton.setEnabled(false);
	}

	/**
	 * Завершение транзакции
	 */
	private void transactionFinished() {

		hideWorkStatusMsg();
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
}
