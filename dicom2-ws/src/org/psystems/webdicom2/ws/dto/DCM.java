package org.psystems.webdicom2.ws.dto;


/**
 * Файл DCM Исследования
 * 
 * @author derenok_dv
 * 
 */
public class DCM {

	public String misId;// TODO Это StudyId
	public String dcmId;
	public String modality;
	public String deviceName;
	public String patientName;
	public String physicianName;
	public String imageId;
	public String pdfId;
	public String contentUrl;

	@Override
	public String toString() {
		return "DCM [misId=" + misId + ", dcmId=" + dcmId + ", imageId=" + imageId
				+ ", pdfId=" + pdfId + "]";
	}

}
