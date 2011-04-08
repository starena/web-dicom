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
	static String persistentDelimeter = "|";// разделитель структур

	/**
	 * формат строки диагнозов: Тип^подтип^МКБ^Описание|...;
	 * 
	 * @return
	 */
	public String toPersistentString() {
		return serviceCode + "^" + serviceAlias + "^" + serviceDescription;
	}

	/**
	 * Получение коллекции в строковом варианте для сохранения
	 * 
	 * @param servicelist
	 * @return
	 */
	public static String toPersistentCollectionString(
			Service[] servicelist) {
		StringBuffer sb = new StringBuffer();
		for (Service service : servicelist) {
			if (sb.length() != 0)
				sb.append(persistentDelimeter);
			sb.append(service.toPersistentString());
		}
		return sb.toString();
	}

	/**
	 * Создание экземпляра из строки
	 * 
	 * @param data
	 * @return
	 */
	public static Service getFromPersistentString(String data) {
		String[] d = data.split("\\^");
		Service srv = new Service();
		srv.setServiceCode(d[0]);
		srv.setServiceAlias(d[1]);
		srv.setServiceDescription(d[2]);
		return srv;
	}

	/**
	 * Создание коллекции экземпляров из строки
	 * 
	 * @param data
	 * @return
	 */
	public static Service[] getCollectionFromPersistentString(
			String data) {
		if(data==null) return null;
		ArrayList<Service> srvs = new ArrayList<Service>();
		String[] d = data.split("\\" + persistentDelimeter);
		for (int i = 0; i < d.length; i++) {
			srvs.add(getFromPersistentString(d[i]));
		}
		Service[] result = new Service[srvs.size()];
		return srvs.toArray(result);
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
