package org.psystems.dicom.pdfview.client.ui;

import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;

public class FormRadioButton extends RadioButton implements IFormInput {

	FormFieldDto formField = new FormFieldDto();
	String label;

	public FormRadioButton(String name, final String label) {
		super(name, label);
		this.label = label;
		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (FormRadioButton.this.getValue()) {
					formField.setValue(label);
				}
			}
		});
	}

	public FormFieldDto getFormField() {
		return formField;
	}

	public void setFormField(FormFieldDto formField) {
		this.formField = formField;

		if (label.equals(formField.getValue())) {
			setValue(true);
		}

	}

}
