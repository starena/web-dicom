package org.psystems.dicom.browser.client;

import java.util.Iterator;

import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.DcmImageProxy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dicom_browser implements EntryPoint {

	private DialogBox errorDialogBox;
	private HTML errorResponseLabel;

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final BrowserServiceAsync browserService = GWT
			.create(BrowserService.class);
	private Button sendButton;
	private SuggestBox nameField;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		HorizontalPanel hp = new HorizontalPanel();
		RootPanel.get("searchContainer").add(hp);

		sendButton = new Button("Поиск");
		ItemSuggestOracle oracle = new ItemSuggestOracle();
		nameField = new SuggestBox(oracle);
		nameField.setLimit(10);
		nameField.setWidth("600px");

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
				nameField.setText("");
				RootPanel.get("resultContainer").clear();
			}

		});

		// Focus the cursor on the name field when the app loads
		// nameField.setFocus(true);
		// nameField.selectAll();

		// nameField.addKeyUpHandler(new KeyUpHandler() {
		//
		// @Override
		// public void onKeyUp(KeyUpEvent event) {
		// if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		//					
		// if(nameField.isSuggestionListShowing()) {
		// //TODO Странно, но при нажатии [ENTER] генерируется два эвента !!!
		// System.out.println("!!!! onKeyUp="+event.getSource());
		// sendNameToServer();
		// }
		// }
		// }
		//			
		// });

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

	}

	/**
	 * Send the name from the nameField to the server and wait for a response.
	 */
	private void sendNameToServer() {
		sendButton.setEnabled(false);
		String textToServer = nameField.getText();
		RootPanel.get("resultContainer").clear();

		browserService.findStudy(textToServer,
				new AsyncCallback<DcmFileProxy[]>() {

					public void onFailure(Throwable caught) {
						showErrorDlg((DefaultGWTRPCException) caught);
						sendButton.setEnabled(true);
						nameField.setFocus(true);
						// nameField.selectAll();
					}

					public void onSuccess(DcmFileProxy[] result) {

						// System.out.println("!!! result="+result.length);
						for (int i = 0; i < result.length; i++) {
							DcmFileProxy proxy = result[i];

							HorizontalPanel dcmImage = new HorizontalPanel();
							HorizontalPanel dcmItem = new HorizontalPanel();
							RootPanel.get("resultContainer").add(dcmItem);

							FlexTable t = new FlexTable();
							t.setStyleName("SearchItem");
//							t.setBorderWidth(2);

							Label l = new Label(proxy.getPatientName()
									+ " ?? (М) 01.03.1974");
							l.setStyleName("DicomItem");

							t.setWidget(0, 0, l);
							t.getFlexCellFormatter().setColSpan(0, 0, 6);
							t.getFlexCellFormatter().setAlignment(0, 0,
									HorizontalPanel.ALIGN_CENTER,
									HorizontalPanel.ALIGN_MIDDLE);

							t.setWidget(0, 1, dcmImage);
							t.getFlexCellFormatter().setAlignment(0, 0,
									HorizontalPanel.ALIGN_CENTER,
									HorizontalPanel.ALIGN_MIDDLE);
							t.getFlexCellFormatter().setRowSpan(0, 1, 5);

							l = new Label("Дата исследования:");
							l.setStyleName("DicomItemName");
							t.setWidget(1, 0, l);
							l = new Label("01.01.2001");
							l.setStyleName("DicomItemValue");
							t.setWidget(1, 1, l);

							l = new Label("ID исследования:");
							l.setStyleName("DicomItemName");
							t.setWidget(1, 2, l);
							l = new Label("LAB_XXXX");
							l.setStyleName("DicomItemValue");
							t.setWidget(1, 3, l);

							l = new Label("ID пациента:");
							l.setStyleName("DicomItemName");
							t.setWidget(1, 4, l);
							l = new Label("ID_PATI");
							l.setStyleName("DicomItemValue");
							t.setWidget(1, 5, l);

							l = new Label("Результаты:");
							l.setStyleName("DicomItemName");
							t.setWidget(2, 0, l);
							l = new Label("Не выявлено отклонений");
							l.setStyleName("DicomItemValue");
							t.setWidget(2, 1, l);
							t.getFlexCellFormatter().setColSpan(2, 1, 5);

							l = new Label("Врач:");
							l.setStyleName("DicomItemName");
							t.setWidget(3, 0, l);
							l = new Label("врач иванов");
							l.setStyleName("DicomItemValue");
							t.setWidget(3, 1, l);

							l = new Label("Оператор:");
							l.setStyleName("DicomItemName");
							t.setWidget(3, 2, l);
							l = new Label("Оператор сидоров");
							l.setStyleName("DicomItemValue");
							t.setWidget(3, 3, l);

							l = new Label("Аппарат:");
							l.setStyleName("DicomItemName");
							t.setWidget(3, 4, l);
							l = new Label("Рентген ХХХ");
							l.setStyleName("DicomItemValue");
							t.setWidget(3, 5, l);

							HTML linkDcm = new HTML();
							linkDcm
									.setHTML("<a href='"
											+ "dcm/"
											+ proxy.getId()
											+ "' target='new'> получить оригнальный DCM-файл </a>"
											+ "<a href='help.html'> Инструкция (не реализовано) </a>");
							linkDcm.setStyleName("DicomItemName");

							t.setWidget(4, 0, linkDcm);
							t.getFlexCellFormatter().setColSpan(4, 0, 6);

							// t.setText(2, 2, "bottom-right corner");
							// t.setWidget(1, 0, new Button("Wide Button"));
							// t.getFlexCellFormatter().setColSpan(1, 0, 3);
							dcmItem.add(t);

							for (Iterator<DcmImageProxy> it = proxy.getImagesIds()
									.iterator(); it.hasNext();) {
								DcmImageProxy imageProxy = it.next();

								Image image = new Image("images/" + imageProxy.getId());
								image.addStyleName("Image");
								image
										.setTitle("Щелкните здесь чтобы увеличить изображение");
								image.setWidth("150px");
								// image.setSize("150px", "150px");
								// System.out.println("!!! image SIZE: "
								// + image.getWidth() + ";"
								// + image.getHeight());

								VerticalPanel vp = new VerticalPanel();
								dcmImage.add(vp);

								vp.add(image);

								final Image imageFull = new Image("images/"
										+ imageProxy.getId());
								imageFull.addStyleName("Image");
								imageFull
										.setTitle("Щелкните здесь чтобы закрыть изображение");
								imageFull.setWidth("600px");

								HorizontalPanel hp = new HorizontalPanel();
								vp.add(hp);

								HTML link = new HTML();
								link
										.setHTML("<a href='"
												+ "images/"
												+ imageProxy.getId()
												+ "' target='new'> Открыть в новом окне </a>");
								link.setStyleName("DicomItemName");
								hp.add(link);
								//

								Button b = new Button("Увеличить...");
								hp.add(b);

								ClickHandler clickOpenHandler = new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {

										final PopupPanel pGlass = new PopupPanel();
										pGlass.setStyleName("GlassPanel");
										pGlass.setModal(true);
										pGlass.show();

										final DialogBox db = new DialogBox();
										db.setModal(true);
										db.setTitle("Увеличенное изображение");

										VerticalPanel vp = new VerticalPanel();
										db.add(vp);
										vp.add(imageFull);
										Button b = new Button("Закрыть");
										vp.add(b);

										ClickHandler clickCloseHandler = new ClickHandler() {

											@Override
											public void onClick(ClickEvent event) {
												pGlass.hide();
												db.hide();
											}

										};
										b.addClickHandler(clickCloseHandler);
										imageFull
												.addClickHandler(clickCloseHandler);

										db.show();
										db.center();
									}

								};

								b.addClickHandler(clickOpenHandler);
								image.addClickHandler(clickOpenHandler);

								//
							}

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
			ItemSuggestService.Util.getInstance().getSuggestions(req,
					new ItemSuggestCallback(req, callback));
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
				callback.onSuggestionsReady(req, new SuggestOracle.Response());
			}

			public void onSuccess(Object retValue) {
				callback.onSuggestionsReady(req,
						(SuggestOracle.Response) retValue);
			}
		}

	}
}