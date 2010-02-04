package org.psystems.dicom.browser.client;

import java.util.Iterator;

import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;

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

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
//		final TextBox nameField = new TextBox();
		
		  ItemSuggestOracle oracle = new ItemSuggestOracle();
		  
//	        
//		  final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();  
//		   oracle.add("Cat");
//		   oracle.add("Dog");
//		   oracle.add("Horse");
//		   oracle.add("Canary");
		   
// http://groups.google.com/group/google-web-toolkit/browse_frm/thread/56942afe0c404d86/e2860187517c4a6c?lnk=gst&q=SuggestBox+oracle+event#e2860187517c4a6c

		final SuggestBox nameField = new SuggestBox(oracle);
		nameField.setLimit(10);
		
	

		
		createErorrDlg();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
//		nameField.selectAll();

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
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
//								nameField.selectAll();
							}

							public void onSuccess(DcmFileProxy[] result) {

								for (int i = 0; i < result.length; i++) {
									DcmFileProxy proxy = result[i];
									Label l = new Label(proxy.getPatientName());
									RootPanel.get("resultContainer").add(l);

									for (Iterator<Integer> it = proxy
											.getImagesIds().iterator(); it
											.hasNext();) {
										Integer id = it.next();
										
										Image image = new Image("images/" + id);
										image.addStyleName("Image");
										image.setTitle("Щелкните здесь чтобы увеличить изображение");
										image.setWidth("200px");
										// image.setSize("150px", "150px");
										System.out.println("!!! image SIZE: "
												+ image.getWidth() + ";"
												+ image.getHeight());

										RootPanel.get("resultContainer").add(image);
										
										
										final Image imageFull = new Image("images/" + id);
										imageFull.addStyleName("Image");
										imageFull.setTitle("Щелкните здесь чтобы закрыть изображение");
										imageFull.setWidth("600px");
										
//										Hyperlink link = new Hyperlink();
//										link.setText("Открыть в новом окне");
										
										HTML link = new HTML();
										link.setHTML("<a href='"+"images/" + id+"' target='new'> Открыть в новом окне </a>");
										RootPanel.get("resultContainer").add(link);
										//
										
										Button b = new Button("Увеличить...");
										RootPanel.get("resultContainer").add(b);
										
										
										
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
												imageFull.addClickHandler(clickCloseHandler);

												

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
//								nameField.selectAll();
							}
						});
			}
		}

		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);

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
	    public boolean isDisplayStringHTML() { return true; }

	    public void requestSuggestions(SuggestOracle.Request req,
	SuggestOracle.Callback callback) {
	        ItemSuggestService.Util.getInstance().getSuggestions(req, new
	ItemSuggestCallback(req, callback));
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
	            callback.onSuggestionsReady(req, new
	SuggestOracle.Response());
	        }

	        public void onSuccess(Object retValue) {
	            callback.onSuggestionsReady(req,
	(SuggestOracle.Response)retValue);
	        }
	    }

	} 
}
