package org.psystems.webdicom2.ws.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.ws.WebServiceException;

import org.psystems.webdicom2.ws.WsException;

/**
 * Направление
 * 
 * @author ddv
 * 
 */
public class Direction {

	private String barCode;
	private String misId;
	private String patientId;
	private String patientName;
	private String patientNameTranslit;
	private String sex;
	private String dateBirsday;
	private String modality;
	private String dateStudy;
	private String serviceName;

	/**
	 * @param msg
	 * @return
	 */
	private String getExceptoinMessage(String msg) {
		StringBuffer sb = new StringBuffer();
		return sb.append("[").append(this.getClass().getSimpleName())
				.append("] ").append(msg).toString();
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getMisId() {
		return misId;
	}

	public void setMisId(String misId) {
		this.misId = misId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) throws WsException {
		Pattern pattern = Pattern.compile("(\\w+)\\^(\\w+)\\^(\\w+)");
		Matcher matcher = pattern.matcher(patientName);
		if (!matcher.find())
			throw new WsException(
					getExceptoinMessage("field [patientName] valid format: Surname^Name^Patroname"));
		this.patientName = patientName;
	}

	public String getPatientNameTranslit() {
		return patientNameTranslit;
	}

	public void setPatientNameTranslit(String patientNameTranslit) {
		this.patientNameTranslit = patientNameTranslit;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) throws WsException {
		if (!("M".equals(sex)) && !("F".equals(sex)))
			throw new WsException(
					getExceptoinMessage("field [sex] available values: F|M"));
		this.sex = sex;
	}

	public String getDateBirsday() {
		return dateBirsday;
	}

	public String getDateBirsdayDicom() {

		// System.out.println("!!!! dateBirsday="+dateBirsday);
		Pattern pattern = Pattern.compile("(\\d{4})/(\\d{2})/(\\d{2})");
		Matcher matcher = pattern.matcher(dateBirsday);
		if (matcher.find()) {
			// return matcher.group(0);
			// System.out.println("!! count="+matcher.groupCount());
			// System.out.println("!!! g0="+matcher.group(0));
			// System.out.println("!!! g1="+matcher.group(1));
			// System.out.println("!!! g2="+matcher.group(2));
			// System.out.println("!!! g3="+matcher.group(3));
			return matcher.group(1) + matcher.group(2) + matcher.group(3);
		}
		return null;

	}

	public void setDateBirsday(String dateBirsday) throws WsException {
		
		Pattern pattern = Pattern.compile("(\\d{4})/(\\d{2})/(\\d{2})");
		Matcher matcher = pattern.matcher(dateBirsday);
		if (!matcher.find())
			throw new WsException(
					getExceptoinMessage("field [dateBirsday] valid format: YYYY/MM/DD"));
		this.dateBirsday = dateBirsday;
	}

	public void setDateBirsdayDicom(String dateBirsday) throws WsException {
		Pattern pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
		Matcher matcher = pattern.matcher(dateBirsday);
		
//		System.out.println("!!! dateBirsday="+dateBirsday);
		if (matcher.find()) {
//			 return matcher.group(0);
//			 System.out.println("!! count="+matcher.groupCount());
//			 System.out.println("!!! g0="+matcher.group(0));
			// System.out.println("!!! g1="+matcher.group(1));
			// System.out.println("!!! g2="+matcher.group(2));
			// System.out.println("!!! g3="+matcher.group(3));
			this.dateBirsday = matcher.group(1) + "/" + matcher.group(2) + "/"+ matcher.group(3);
		} else throw new WsException(
				getExceptoinMessage("field [dateBirsday] valid DICOM format: YYYYMMDD"));

	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) throws WsException {
		if(modality.length()!=2)
			throw new WsException(
					getExceptoinMessage("field [modality] valid size: 2"));
		this.modality = modality;
	}

	public String getDateStudy() {
		return dateStudy;
	}

	public void setDateStudy(String dateStudy) throws WsException {
		Pattern pattern = Pattern.compile("(\\d{4})/(\\d{2})/(\\d{2})");
		Matcher matcher = pattern.matcher(dateStudy);
		if (!matcher.find())
			throw new WsException(
					getExceptoinMessage("field [dateStudy] valid format: YYYY/MM/DD"));
		this.dateStudy = dateStudy;
	}
	
	public void setDateStudyDicom(String dateStudy) throws WsException {
		Pattern pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
		Matcher matcher = pattern.matcher(dateStudy);
		
		if (matcher.find()) {
			this.dateStudy = matcher.group(1) + "/" + matcher.group(2) + "/"+ matcher.group(3);
		} else throw new WsException(
				getExceptoinMessage("field [dateStudy] valid DICOM format: YYYYMMDD"));
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "Direction [barCode=" + barCode + ", misId=" + misId
				+ ", patientId=" + patientId + ", patientName=" + patientName
				+ ", patientNameTranslit=" + patientNameTranslit + ", sex="
				+ sex + ", dateBirsday=" + dateBirsday + ", modality="
				+ modality + ", dateStudy=" + dateStudy + ", serviceName="
				+ serviceName + "]";
	}

}
