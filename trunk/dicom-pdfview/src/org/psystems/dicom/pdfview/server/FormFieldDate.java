package org.psystems.dicom.pdfview.server;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;

/**
 * Поле хранения даты. формат: dd.mm.yyyy
 * 
 * @author dima_d
 * 
 */
public class FormFieldDate extends FormField {

	private Date valueAsDate;

	public FormFieldDate(String name) throws UnsupportedEncodingException {
		super(name);
	}

	/*
	 * 
	 * Сделана дополнительная проверка на форам строки (dd.mm.yyyy)
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.psystems.dicom.pdfview.server.FormField#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		try {
			if(value==null || value.length()==0) 
				valueAsDate = null;
				else
			valueAsDate = FormFieldFactory.dateFormatUser.parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		super.setValue(value);
	}

	/**
	 * @return Дата в формате YYYYMMDD
	 * @throws ParseException
	 */
	public String getValueAsDicomFmt() throws ParseException {
		Date date = FormFieldFactory.dateFormatUser.parse(this.getValue());
		return FormFieldFactory.dateFormatDicom.format(date);
	}

	/**
	 * @return
	 * @throws ParseException
	 */
	public Date getValueAsDate() throws ParseException {
		return FormFieldFactory.dateFormatUser.parse(this.getValue());
	}

}
