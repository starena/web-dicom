package org.psystems.dicom.pdfview.dto;

import java.io.Serializable;
import java.util.Date;

import org.psystems.dicom.pdfview.client.ui.FormWidgetFactory;

/**
 * Поле PDF-формы (Список выбора)
 * 
 * @author dima_d
 * 
 */
public class FormFieldDateDto extends FormFieldDto implements Serializable {

	private static final long serialVersionUID = 1L;
	// формат даты - dd.mm.yyyy
	private String value;

	private Date valueAsDate;

	public String getValue() {
		return value;
	}

	public Date getValueAsDate() {
		return valueAsDate;
	}

	/*
	 * формат даты - dd.mm.yyyy
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.psystems.dicom.pdfview.dto.FormFieldDto#setValue(java.lang.String)
	 */
	public void setValue(String value) {
		this.value = value;
		// this.valueAsDate = FormWidgetFactory.dateFormatUser.parse(value);
	}

	/**
	 * Установка значения через обхект java.util.Date
	 * 
	 * @param date
	 */
	public void setValue(Date date) {
		valueAsDate = date;
		this.value = FormWidgetFactory.dateFormatUser.format(date);
	}

	/**
	 * Проверка перез сериализацией на стороне клиента. Сделана так, потому что
	 * из пакетов *.server.* нельзя вызывать методы *.clint.* (GWT.create())
	 * 
	 * формат даты - dd.mm.yyyy
	 */
	public void chekBeforeClientSerialization() throws IllegalArgumentException{
		try {
		this.valueAsDate = FormWidgetFactory.dateFormatUser.parse(value);
		}catch (Exception e) {
			throw new IllegalArgumentException("Wrond date format! ["+value+"]", e);
		}
	}

}
