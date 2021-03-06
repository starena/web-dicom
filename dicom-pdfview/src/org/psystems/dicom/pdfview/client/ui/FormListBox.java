package org.psystems.dicom.pdfview.client.ui;

import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

public class FormListBox extends ListBox implements IFormInput {

	FormFieldDto formField = new FormFieldDto();

	public FormListBox() {
		super(false);
		
		addItem("...", "");
		
		addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				formField.setValue(FormListBox.this.getValue(FormListBox.this.getSelectedIndex()));
			}
		});
	}

	public FormFieldDto getFormField() {
		return formField;
	}

	public void setFormField(FormFieldDto formField) {
		this.formField = formField;
		String val = formField.getValue();
		for(int i=0; i<getItemCount(); i++) {
			if(getValue(i).equals(val)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

}
