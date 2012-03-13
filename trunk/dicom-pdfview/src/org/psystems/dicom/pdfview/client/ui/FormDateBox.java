package org.psystems.dicom.pdfview.client.ui;

import java.util.Date;


import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author dima_d
 * 
 */
public class FormDateBox extends DateBox implements IFormInput {

	FormFieldDto formField = new FormFieldDto();

	/**
	 * 
	 */
	public FormDateBox() {
		
		setFormat(new DateBox.DefaultFormat(FormWidgetFactory.dateFormatUser));
		setValue(new Date());
		
		addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				// TODO Auto-generated method stub
				Date date = event.getValue();
				formField.setValue(FormWidgetFactory.dateFormatDicom.format(date));
			}
		});

		
	}

	public FormFieldDto getFormField() {
		return formField;
	}

	public void setFormField(FormFieldDto formField) {
		this.formField = formField;
		setValue(FormWidgetFactory.dateFormatDicom.parse(formField.getValue()));
	}

}
