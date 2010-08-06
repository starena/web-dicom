/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.Date;

import org.psystems.dicom.browser.client.service.ManageStydyServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author dima_d
 * 
 */
public class StudyManagePanel extends Composite implements
		ValueChangeHandler<String> {

	private ManageStydyServiceAsync manageStudyService;
	private HTML submitResult;
	private Button submitBtn;
	private TextBox patientName;
	private DateBox birstdayDox;
	private DateBox studydateBox;
	private FileUpload fileUpload;
	
	DateTimeFormat dateFormatBox = DateTimeFormat.getFormat("dd.MM.yyyy");
	

	public StudyManagePanel(final ManageStydyServiceAsync manageStudyService) {

		this.manageStudyService = manageStudyService;
		

		// История
		History.addValueChangeHandler(this);
		// History.fireCurrentHistoryState();

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
//				System.out.println("!!! onSubmitComplete [" + event.getResults()+"]");
				
					if(!event.getResults().matches(".+___success___.+")) {
						submitResult.setHTML(""+event.toDebugString()+"<HR>"+event.getResults());
						submitError();
					} else {
						submitSuccess();	
					}
					
				
			}

		});
		
		formPanel.addSubmitHandler(new SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				// TODO Auto-generated method stub
//				event.cancel();
				submitBtn.setEnabled(false);
			}
		});

		

		FlexTable flexTable = new FlexTable();
		//TODO Убрать в css
		DOM.setStyleAttribute(formPanel.getElement(), "background", "#E9EDF5");
		
		formPanel.add(flexTable);

		flexTable.setWidget(0, 0, makeItemLabel("ФИО"));
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		patientName = new TextBox();
		patientName.setName("00100010");
		flexTable.setWidget(0, 1, patientName);

		flexTable.setWidget(1, 0, makeItemLabel("Пол"));
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		ListBox lbSex = new ListBox();
		lbSex.setName("00100040");
		lbSex.addItem("Муж", "M");
		lbSex.addItem("Жен", "F");
		
		flexTable.setWidget(1, 1, lbSex);
		
		

		flexTable.setWidget(2, 0, makeItemLabel("Дата рождения"));
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		final Hidden patientBirthDate = new Hidden();
		flexTable.setWidget(2, 2, patientBirthDate);
		patientBirthDate.setName("00100030");
		
		birstdayDox = new DateBox();
		birstdayDox.setFormat(new DateBox.DefaultFormat(dateFormatBox));
		flexTable.setWidget(2, 1, birstdayDox);
		// DateTimeFormat dateFormat = DateTimeFormat.getLongDateFormat();
		
		
		birstdayDox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyyMMdd");
				String d = dateFormat.format(event.getValue());
				patientBirthDate.setValue(d);
			}
		});
		
		
		flexTable.setWidget(3, 0, makeItemLabel("Дата исследования"));
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		final Hidden studyDate = new Hidden();
		
		flexTable.setWidget(3, 2, studyDate);
		studyDate.setName("00080020");
		
		studydateBox = new DateBox();
		studydateBox.setValue(new Date());
		flexTable.setWidget(3, 1, studydateBox);
		
		
		DateTimeFormat dateFormat1 = DateTimeFormat.getFormat("yyyyMMdd");
		String d = dateFormat1.format(studydateBox.getValue());
		studyDate.setValue(d);
		
		// DateTimeFormat dateFormat = DateTimeFormat.getLongDateFormat();
		
		studydateBox.setFormat(new DateBox.DefaultFormat(dateFormatBox));
		
		studydateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyyMMdd");
				String d = dateFormat.format(event.getValue());
				studyDate.setValue(d);
			}
		});
	

		
		
//		Date d = (Date)event;
//		System.out.println("!!! date="+event.getValue().getClass());
//		DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy.mm.dd");
//		dateFormat.format((Date)event);
		
		flexTable.setWidget(4, 0, makeItemLabel("Снимок"));
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		fileUpload = new FileUpload();
		fileUpload.setName("upload");
		flexTable.setWidget(4, 1, fileUpload);

		submitBtn = new Button("Создать");
		flexTable.setWidget(5, 0, submitBtn);
		flexTable.getFlexCellFormatter().setColSpan(5, 0, 3);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_CENTER);

		submitBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				formPanel.submit();
			}
		});
		
		submitResult = new HTML("");
		flexTable.setWidget(6, 0, submitResult);
		flexTable.getFlexCellFormatter().setColSpan(6, 0, 3);
		flexTable.getFlexCellFormatter().setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_CENTER);

		initWidget(mainPanel);
		

	}
	
	private void clearForm() {
		submitBtn.setEnabled(true);
		resetForm();
		
		studydateBox.setValue(new Date());
	
		/*
		patientName.setText("");
		birstdayDox.setValue(null);
		studydateBox.setValue(null);
		*/
	}
	
	private native void resetForm() /*-{
    $doc.forms[0].reset();
	}-*/; 
	
	/**
	 * НЕуспешное завершение сохранения исследования
	 */
	protected void submitError() {
		submitBtn.setEnabled(true);
	}

	/**
	 * Успешное завершение сохранения исследования
	 */
	protected void submitSuccess() {
		clearForm();
		submitBtn.setEnabled(true);
		
	}

	/**
	 * @param title
	 * @return
	 */
	Widget makeItemLabel(String title) {
		Label label = new Label(title);
		//TODO Убрать в css
		DOM.setStyleAttribute(label.getElement(), "font", "1.5em/ 150% normal Verdana, Tahoma, sans-serif");
		return label;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// TODO Auto-generated method stub

	}

}
