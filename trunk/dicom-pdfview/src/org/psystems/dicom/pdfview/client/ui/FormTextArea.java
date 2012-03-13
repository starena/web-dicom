package org.psystems.dicom.pdfview.client.ui;

import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextArea;

public class FormTextArea extends TextArea implements IFormInput {

	FormFieldDto formField = new FormFieldDto();
	
	// Ширина однострочной панели (для определения какой виджет ставить TextArea или TextBox)
	public final static int singlePanelMaxHeight = 20;

	public FormTextArea() {

		addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				formField.setValue(FormTextArea.this.getValue());
			}
		});

	}

	/**
	 * Интелектуальное изменение размера
	 */
	protected void resizeIntelegent() {
		String val = this.getValue();
		String[] s = val.split("\n");
		int lines = this.getVisibleLines();

		if (s.length > lines)
			FormTextArea.this.setVisibleLines(s.length);
	}

	public FormFieldDto getFormField() {
		return formField;
	}

	public void setFormField(FormFieldDto formField) {
		this.formField = formField;
		setValue(formField.getValue());
	}

}
