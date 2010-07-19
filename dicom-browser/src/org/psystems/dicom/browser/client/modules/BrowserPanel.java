/**
 * 
 */
package org.psystems.dicom.browser.client.modules;

import org.psystems.dicom.browser.client.service.BrowserServiceAsync;
import org.psystems.dicom.browser.client.service.ManageStydyServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

/**
 * Панель обозревателя
 * 
 * @author dima_d
 *
 */
public class BrowserPanel extends Composite implements
		ValueChangeHandler<String> {

	

	private BrowserServiceAsync browserService;

	public BrowserPanel(BrowserServiceAsync browserService) {
		this.browserService = browserService;
		History.addValueChangeHandler(this); 
		
		DecoratorPanel mainPanel = new DecoratorPanel();
		_construct(mainPanel);
		initWidget(mainPanel);
		

	}

	/**
	 * Размещение компонентов
	 * @param mainPanel
	 */
	private void _construct(DecoratorPanel mainPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// TODO Auto-generated method stub
		
	}

	
}
