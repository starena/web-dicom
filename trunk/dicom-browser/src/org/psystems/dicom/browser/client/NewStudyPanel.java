/**
 * 
 */
package org.psystems.dicom.browser.client;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author dima_d
 * 
 *         Редактор статьи
 */
public class NewStudyPanel extends Composite implements
		ValueChangeHandler<String> {

	

	private ManageStydyServiceAsync manageStudyService;
	private TextBox patientName;

	public NewStudyPanel(final ManageStydyServiceAsync manageStudyService) {
	
		this.manageStudyService = manageStudyService;
		
		// История
		History.addValueChangeHandler(this);
//		History.fireCurrentHistoryState();
		
		DecoratorPanel mainPanel = new DecoratorPanel();
		VerticalPanel verticalPanel = new VerticalPanel();
		mainPanel.setWidget(verticalPanel);
		
		patientName = new TextBox();
		verticalPanel.add(patientName);
		
		
		Button submitBtn = new Button("Создать");
		verticalPanel.add(submitBtn);
		
		submitBtn.addClickHandler(new ClickHandler() {
			
			

			@Override
			public void onClick(ClickEvent event) {

				manageStudyService.newStudy(patientName.getText(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						
						
					}
				});
			}
		});
		
		initWidget(mainPanel);
		

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// TODO Auto-generated method stub
		
	}

	
}
