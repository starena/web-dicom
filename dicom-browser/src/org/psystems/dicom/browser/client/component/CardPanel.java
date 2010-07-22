package org.psystems.dicom.browser.client.component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CardPanel extends Composite {

	public CardPanel() {
		
		FlexTable container = new FlexTable();
//		container.setWidth("100%");
		DOM.setStyleAttribute(container.getElement(), "background", "green");
		
		for(int i=0; i<7; i++) {
			container.setWidget(0, i, makeItemLabel("!!!"));
		}
		
		
		container.getColumnFormatter().setWidth(0, "100px");
		container.getColumnFormatter().setWidth(1, "100px");
		container.getColumnFormatter().setWidth(2, "100px");
		container.getColumnFormatter().setWidth(3, "100px");
		container.getColumnFormatter().setWidth(4, "100px");
		container.getColumnFormatter().setWidth(5, "100px");
//		container.getColumnFormatter().setWidth(6, "50%");
		
		
		initWidget(container);
	}
	
	
	/**
	 * @param title
	 * @return
	 */
	Widget makeItemLabel(String title) {
		Label label = new Label(title);
		DOM.setStyleAttribute(label.getElement(), "background", "yellow");
		return label;
	}
	
	

	
}
