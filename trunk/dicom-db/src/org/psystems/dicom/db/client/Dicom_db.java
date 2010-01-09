package org.psystems.dicom.db.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dicom_db implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DBServiceAsync dbService = GWT.create(DBService.class);

	private DialogBox errorDialogBox;

	private HTML errorResponseLabel;

	private Button createDBButton;

	private Button stopDBButton;

	private Button startDBButton;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		
		VerticalPanel vp = new VerticalPanel();
		RootPanel.get("testContainer").add(vp);
		
		startDBButton = new Button("DB start");
		vp.add(startDBButton);
		startDBButton.addStyleName("sendButton");
		
		
		startDBButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				startDBButton.setEnabled(false);
				
				dbService.startDB(new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						showErrorDlg((DefaultGWTRPCException) caught);
						startDBButton.setEnabled(true);
					}

					public void onSuccess(String result) {
						startDBButton.setEnabled(true);
//						System.out.println("RESULT: " + result);
					}
				});

			}

		});

		stopDBButton = new Button("DB stop");
		vp.add(stopDBButton);
		stopDBButton.addStyleName("sendButton");
		
		
		stopDBButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				stopDBButton.setEnabled(false);
				
				dbService.stopDB(new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						showErrorDlg((DefaultGWTRPCException) caught);
						stopDBButton.setEnabled(true);
					}

					public void onSuccess(String result) {
//						System.out.println("RESULT: " + result);
						stopDBButton.setEnabled(true);
					}
				});

			}

		});
		
		createDBButton = new Button("DB create");
		vp.add(createDBButton);
		createDBButton.addStyleName("sendButton");
		
		
		createDBButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				createDBButton.setEnabled(false);
				
				dbService.createDB(new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						showErrorDlg((DefaultGWTRPCException) caught);
						createDBButton.setEnabled(true);
					}

					public void onSuccess(String result) {
//						System.out.println("RESULT: " + result);
						createDBButton.setEnabled(true);
					}
				});

			}

		});

		createDlg(sendButton, nameField);
		createErorrDlg();
		
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

	private void createDlg(final Button sendButton, final TextBox nameField) {
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

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
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				dbService.greetServer(textToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(result);
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
