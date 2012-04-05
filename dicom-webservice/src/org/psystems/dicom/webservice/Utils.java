package org.psystems.dicom.webservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public final static String dateFormatSQL = "yyyy-MM-dd";
	public final  static SimpleDateFormat formatSQL = new SimpleDateFormat(dateFormatSQL);
	public final  static String dateFormatDicom = "yyyyMMdd";
	public final  static SimpleDateFormat formatDicom = new SimpleDateFormat(dateFormatDicom);
	
	/**
	 * Конвертация даты из формата YYYY-MM-DD в форма YYYYMMDD
	 * @param inputDateStr
	 * @return
	 * @throws DicomWebServiceException 
	 */
	public static String SqlDate2DicomDate(String inputDateStr) throws DicomWebServiceException {
		
		try {
			Date date = formatSQL.parse(inputDateStr);
			return formatDicom.format(date);
		} catch(ParseException ex) {
			throw new DicomWebServiceException("Error convert date! format must be (YYYY-MM-DD)",ex);
		}
		
	}
}
