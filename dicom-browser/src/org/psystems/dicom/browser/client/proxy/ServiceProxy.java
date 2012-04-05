package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

public class ServiceProxy implements Serializable {

	private static final long serialVersionUID = 7544949749980154946L;

	private String serviceCode;// Код
	private String serviceAlias;// краткий код
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
		return "ServiceProxy [serviceAlias=" + serviceAlias + ", serviceCode="
				+ serviceCode + ", serviceDescription=" + serviceDescription
				+ "]";
	}

}
