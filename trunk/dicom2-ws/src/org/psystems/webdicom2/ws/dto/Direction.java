package org.psystems.webdicom2.ws.dto;

/**
 * Направление
 * 
 * @author ddv
 * 
 */
public class Direction {

	public String barCode;
	public String patientId;
	public String patientName;
	public String patientNameTranslit;
	public String sex;
	public String dateBirsday;
	public String modality;
	public String dateStudy;
	public String serviceName;

	@Override
	public String toString() {
		return "Direction [barCode=" + barCode + ", patientId=" + patientId
				+ ", patientName=" + patientName + ", patientNameTranslit="
				+ patientNameTranslit + ", sex=" + sex + ", dateBirsday="
				+ dateBirsday + ", modality=" + modality + ", dateStudy="
				+ dateStudy + ", serviceName=" + serviceName + "]";
	}

}
