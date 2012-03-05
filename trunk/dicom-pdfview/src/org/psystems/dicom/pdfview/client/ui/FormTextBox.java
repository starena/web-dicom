package org.psystems.dicom.pdfview.client.ui;

import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author dima_d
 * 
 */
public class FormTextBox extends TextBox implements IFormInput {

	FormFieldDto formField = new FormFieldDto();

	/**
	 * 
	 */
	public FormTextBox() {

		addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				formField.setValue(FormTextBox.this.getValue());

			}
		});
	}

	public FormFieldDto getFormField() {
		return formField;
	}

	public void setFormField(FormFieldDto formField) {
		this.formField = formField;
		setValue(formField.getValue());
	}

}
