package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Медицинский аппарат
 * 
 * @author dima_d
 * 
 */
public class ManufacturerDevice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2089006109459477701L;
	// Модель аппарат (по тегу из Dicom-файла)
	private String ManufacturerModelName;
	// CR,ES,...
	private String ManufacturerModelType;
	// Описание типа исследования
	private String ManufacturerModelTypeDescription;
	// Описание аппарата (на русском)
	private String ManufacturerModelDescription;


	public ManufacturerDevice() {
		super();
	}
	
	/**
	 * Получение списка доступных аппаратов
	 * TODO !!! Реализовать!!!
	 * @return
	 */
	public static ManufacturerDevice[] getAllManufacturerDevices () {
		
		//TODO Взять из БД!!!
		ManufacturerDevice device = new ManufacturerDevice();
		device.setManufacturerModelName("testdevice");
		device.setManufacturerModelDescription("Тестовы девайс");
		device.setManufacturerModelType("CR");
		device.setManufacturerModelTypeDescription("Флюорограф");
		
		ArrayList<ManufacturerDevice> result = new ArrayList<ManufacturerDevice>();
		result.add(device);
		return result.toArray(new ManufacturerDevice[result.size()]);
	}

	public String getManufacturerModelName() {
		return ManufacturerModelName;
	}

	public void setManufacturerModelName(String manufacturerModelName) {
		ManufacturerModelName = manufacturerModelName;
	}

	public String getManufacturerModelType() {
		return ManufacturerModelType;
	}

	public void setManufacturerModelType(String manufacturerModelType) {
		ManufacturerModelType = manufacturerModelType;
	}

	public String getManufacturerModelTypeDescription() {
		return ManufacturerModelTypeDescription;
	}

	public void setManufacturerModelTypeDescription(
			String manufacturerModelTypeDescription) {
		ManufacturerModelTypeDescription = manufacturerModelTypeDescription;
	}

	public String getManufacturerModelDescription() {
		return ManufacturerModelDescription;
	}

	public void setManufacturerModelDescription(
			String manufacturerModelDescription) {
		ManufacturerModelDescription = manufacturerModelDescription;
	}

	@Override
	public String toString() {
		return "ManufacturerDevice [ManufacturerModelName="
				+ ManufacturerModelName + ", ManufacturerModelDescription="
				+ ManufacturerModelDescription + ", ManufacturerModelType="
				+ ManufacturerModelType + ", ManufacturerModelTypeDescription="
				+ ManufacturerModelTypeDescription + "]";
	}
	
	

}
