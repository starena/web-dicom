package org.psystems.dicom.pdfview.client.ui;

import org.psystems.dicom.pdfview.dto.FormFieldCheckboxDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;

public class FormCheckBox extends CheckBox implements IFormInput {

	FormFieldDto formField = new FormFieldDto();

	public FormCheckBox(String label) {
		super(label);

		addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (FormCheckBox.this.getValue()) {
					formField.setValue("On");
				} else {
					formField.setValue("Off");
				}

				((FormFieldCheckboxDto) formField).setValue(FormCheckBox.this
						.getValue());
			}
		});

	}

	public FormFieldDto getFormField() {
		return formField;
	}

	public void setFormField(FormFieldDto formField) {
		this.formField = formField;
		if (formField.getValue() != null
				&& (formField.getValue().equalsIgnoreCase("YES") || formField
						.getValue().equalsIgnoreCase("ON"))) {
			FormCheckBox.this.setValue(true);
		}
		
	}

}
