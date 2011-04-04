package org.psystems.dicom.commons.orm;

import java.io.Serializable;

/**
 * Услуга
 * 
 * @author dima_d
 */
public class Service implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5146546219455184305L;
	private String serviceCode;// Код
	private String serviceAlias;// краткий код для врача
	private String serviceDescription;// Описание

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceAlias() {
		return serviceAlias;
	}

	public void setServiceAlias(String serviceAlias) {
		this.serviceAlias = serviceAlias;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	@Override
	public String toString() {
		return "Service [serviceAlias=" + serviceAlias + ", serviceCode="
				+ serviceCode + ", serviceDescription=" + serviceDescription
				+ "]";
	}

}
