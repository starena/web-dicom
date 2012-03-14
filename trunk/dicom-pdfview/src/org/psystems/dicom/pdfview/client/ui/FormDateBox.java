package org.psystems.dicom.pdfview.client.ui;

import java.util.Date;

import org.psystems.dicom.pdfview.dto.FormFieldDateDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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
		// Date date = new Date();
		setValue(null);
		// formField.setValue(FormWidgetFactory.dateFormatDicom
		// .format(date));

		addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				formField.setValue(FormWidgetFactory.dateFormatUser
						.format(date));

				try {
					((FormFieldDateDto) formField)
							.chekBeforeClientSerialization();
				} catch (IllegalArgumentException caught) {
					RootPanel.get().add(
							new Label("Select date fault! " + caught));
					System.err.println("Select date fault! " + caught);
					caught.printStackTrace();
				}

			}
		});

	}

	public FormFieldDto getFormField() {
		return formField;
	}

	public void setFormField(FormFieldDto formField) {
		this.formField = formField;

		// if (formField.getValue() != null && formField.getValue().length() >
		// 0)
		// setValue(FormWidgetFactory.dateFormatDicom.parse(formField
		// .getValue()));
		// else
		// setValue(new Date());

	}

}
