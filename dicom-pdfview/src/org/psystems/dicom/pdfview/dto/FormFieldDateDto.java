package org.psystems.dicom.pdfview.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Поле PDF-формы (Список выбора)
 * 
 * @author dima_d
 * 
 */
public class FormFieldDateDto extends FormFieldDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean value;

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

}
