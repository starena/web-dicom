package org.psystems.webdicom2.ws.dto;

import java.util.Arrays;

/**
 * Результат исследования
 * 
 * @author derenok_dv
 * 
 */
public class StudyResult {

	public String result;
	public String[] imageUrls;
	public String[] pdfUrls;

	@Override
	public String toString() {
		return "StudyResult [result=" + result + ", imageUrls="
				+ Arrays.toString(imageUrls) + ", pdfUrls="
				+ Arrays.toString(pdfUrls) + "]";
	}

}
