package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

public class ManufacturerDeviceProxy implements Serializable {

	private static final long serialVersionUID = 8971937698612066578L;

	private String ManufacturerModelName; // Модель
	private String ManufacturerModelType; // Тип исследования которые аппарат
	// может выполнять. Проверка согласно списку ORMUtil.modalityList

	private String ManufacturerModelTypeDescription; // Описание типа
	// исследования
	private String ManufacturerModelDescription; // Описание аппарата (на

	// русском)

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
		return "ManufacturerDeviceProxy [ManufacturerModelDescription="
				+ ManufacturerModelDescription + ", ManufacturerModelName="
				+ ManufacturerModelName + ", ManufacturerModelType="
				+ ManufacturerModelType + ", ManufacturerModelTypeDescription="
				+ ManufacturerModelTypeDescription + "]";
	}

}
