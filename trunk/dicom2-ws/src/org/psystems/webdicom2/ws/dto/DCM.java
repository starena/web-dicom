package org.psystems.webdicom2.ws.dto;

import java.util.HashMap;

/**
 * Файл DCM Исследования
 * 
 * @author derenok_dv
 * 
 */
public class DCM {

	public String barCode;// TODO Это StudyId
	public String id;
	public String imageId;
	public String pdfId;
	public HashMap<String, String> tags;

	@Override
	public String toString() {
		return "DCM [barCode=" + barCode + ", id=" + id + ", imageId="
				+ imageId + ", pdfId=" + pdfId + ", tags=" + tags + "]";
	}

}
