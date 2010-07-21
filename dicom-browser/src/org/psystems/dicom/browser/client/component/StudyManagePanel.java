/**
 * 
 */
package org.psystems.dicom.browser.client.component;

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
 * @author dima_d
 * 
 */
public class StudyManagePanel extends Composite implements
		ValueChangeHandler<String> {

	

	private ManageStydyServiceAsync manageStudyService;

	public StudyManagePanel(final ManageStydyServiceAsync manageStudyService) {
	
		this.manageStudyService = manageStudyService;
		
		// История
		History.addValueChangeHandler(this);
//		History.fireCurrentHistoryState();
		
		DecoratorPanel mainPanel = new DecoratorPanel();
		
		final FormPanel formPanel = new FormPanel();
		formPanel.setAction("/newstudy/upload");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		mainPanel.setWidget(formPanel);
		
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub
				System.out.println("!!! onSubmitComplete" + event.getResults());
			}

		});

		formPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				// TODO Auto-generated method stub
				System.out.println("!!!onSubmit" + event);
			}

		});
		
		VerticalPanel verticalPanel = new VerticalPanel();
		formPanel.add(verticalPanel);
		
		TextBox patientName = new TextBox();
		patientName.setName("00100010");
		verticalPanel.add(patientName);
		
		TextBox patientBirthDate = new TextBox();
		patientBirthDate.setName("00100030");
		verticalPanel.add(patientBirthDate);
		
		
		
		
		
		
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("upload");
		verticalPanel.add(fileUpload);
		
		Button submitBtn = new Button("Создать");
		verticalPanel.add(submitBtn);
		
		submitBtn.addClickHandler(new ClickHandler() {
			
			

			@Override
			public void onClick(ClickEvent event) {
				
				formPanel.submit();

//				manageStudyService.newStudy(patientName.getText(), new AsyncCallback<Void>() {
//
//					@Override
//					public void onFailure(Throwable caught) {
//						// TODO Auto-generated method stub
//						
//					}
//
//					@Override
//					public void onSuccess(Void result) {
//						// TODO Auto-generated method stub
//						
//						
//					}
//				});
			}
		});
		
		
		
		initWidget(mainPanel);
		

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// TODO Auto-generated method stub
		
	}

	
}
