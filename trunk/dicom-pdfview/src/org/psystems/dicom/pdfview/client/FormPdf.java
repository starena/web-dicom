package org.psystems.dicom.pdfview.client;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FormPdf extends Composite {

	
	
	private VerticalPanel mainPanel;

	public FormPdf(String tmlName) {
		mainPanel = new VerticalPanel();
		mainPanel.add(new Button("!!"));
		mainPanel.add(new Button("!!"));
		mainPanel.add(new Button("!!"));
		initWidget(mainPanel);
		initPanel(tmlName);
	}
	
	private void initPanel(String tmlName) {
		
	}

	
}
