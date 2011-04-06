package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.ArrayList;

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
	static String persistentDelimeter = "|";//разделитель структур
	
	/**
	 * формат строки диагнозов: Тип^подтип^МКБ^Описание|...;
	 * @return
	 */
	public String toPersistentString() {
		return serviceCode + "^" + serviceAlias + "^" + serviceDescription;
	}
	
	/**
	 * Получение коллекции в строковом варианте для сохранения
	 * @param servicelist
	 * @return
	 */
	public static String toPersistentCollectionString(ArrayList<Service> servicelist) {
		StringBuffer sb = new StringBuffer();
		for (Service service : servicelist) {
			if(sb.length()!=0) sb.append(persistentDelimeter);
			sb.append(service.toPersistentString());
		}
		return sb.toString();	
	}

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
