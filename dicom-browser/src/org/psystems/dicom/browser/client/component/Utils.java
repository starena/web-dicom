package org.psystems.dicom.browser.client.component;

import com.google.gwt.i18n.client.DateTimeFormat;

public class Utils {

	public static DateTimeFormat dateFormatUser = DateTimeFormat.getFormat("dd.MM.yyyy");
	public static DateTimeFormat dateFormatDicom = DateTimeFormat.getFormat("yyyyMMdd");
	public static DateTimeFormat dateFormatSql = DateTimeFormat.getFormat("yyyy-MM-dd");
	public static DateTimeFormat dateTimeFormatSql = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	public static DateTimeFormat dateTimeFormatUser = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss");
	public static DateTimeFormat dateFormatYEARONLY = DateTimeFormat.getFormat("yyyy");
	
}
