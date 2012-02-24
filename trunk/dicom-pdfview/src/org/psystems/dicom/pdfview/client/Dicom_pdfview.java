package org.psystems.dicom.pdfview.client;

import java.util.ArrayList;

import org.psystems.dicom.pdfview.dto.ConfigTemplateDto;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dicom_pdfview implements EntryPoint {
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
	private final PdfServiceAsync service = GWT.create(PdfService.class);

	private DialogBox previewDdialog;

	private VerticalPanel templatesPanel;
	
	private ArrayList<ConfigTemplateDto> templates = new ArrayList<ConfigTemplateDto>();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		templatesPanel = new VerticalPanel();
		RootPanel.get().add(templatesPanel);
		loadTemplates();
	}
	
	private void loadTemplates() {
		
		templatesPanel.clear();
		
		service.getTemplates(new AsyncCallback<ArrayList<ConfigTemplateDto>>() {
			
			@Override
			public void onSuccess(ArrayList<ConfigTemplateDto> result) {
				templates = result;
				showTemplates();

			}
			
			@Override
			public void onFailure(Throwable caught) {
				RootPanel.get().add(new Label("Load templates fault! "+caught));
				System.err.println("Load templates fault! "+caught);
				caught.printStackTrace();
				
			}
		});
	}

	/**
	 * @param dto 
	 * 
	 */
	private void makePreviewDialog(ConfigTemplateDto dto) {
		
		previewDdialog = new DialogBox();
		previewDdialog.setText("Заполнение шаблона");
		previewDdialog.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		
		
		Frame frame = new Frame("http://localhost:8889/pdf/" + dto.getName());
		frame.setSize("900px", "600px");
		dialogVPanel.add(frame);
		
	
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		previewDdialog.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				previewDdialog.hide();
				previewDdialog.removeFromParent();
			}
		});
		
		
		previewDdialog.center();
		previewDdialog.show();
	}

	private void showTemplates() {
		for (final ConfigTemplateDto dto : templates) {
			Button btn = new Button(dto.getDescription());
			btn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					FormPdf form = new FormPdf(dto.getName());
					templatesPanel.add(form);
//					makePreviewDialog(dto);
				}
			});
			templatesPanel.add(btn);
		}
	}
}
