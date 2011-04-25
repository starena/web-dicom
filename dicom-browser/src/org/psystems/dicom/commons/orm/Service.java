package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		return ORMUtil.toPersistString(serviceCode) + "^"
				+ ORMUtil.toPersistString(serviceAlias) + "^"
				+ ORMUtil.toPersistString(serviceDescription);
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

		if (data == null || data.length() == 0)
			return null;

		Service srv = new Service();

		Matcher matcher = Pattern.compile("^(.+)\\^(.+)\\^(.+)$").matcher(data);
		if (matcher.matches()) {
			srv.setServiceCode(ORMUtil.fromPersistString(matcher.group(1)));
			srv.setServiceAlias(ORMUtil.fromPersistString(matcher.group(2)));
			srv.setServiceDescription(ORMUtil.fromPersistString(matcher
					.group(3)));

		} else {
			throw new IllegalArgumentException(
					"Wrong string pattern [serviceCode^serviceAlias^serviceDescription] for ["
							+ data + "]");
		}

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
