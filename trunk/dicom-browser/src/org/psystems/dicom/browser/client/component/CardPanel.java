package org.psystems.dicom.browser.client.component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

public class CardPanel extends Composite {

	public CardPanel() {
		Label label = new Label("!!!!!!!!!");
		FlexTable container = new FlexTable();
		container.setWidth("100%");
		DOM.setStyleAttribute(container.getElement(), "background", "green");
		DOM.setStyleAttribute(label.getElement(), "background", "red");
		container.setWidget(0, 0, label);
		container.getCellFormatter().setWidth(0, 0, "50%");
		
		initWidget(container);
	}

	
}
