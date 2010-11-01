package org.psystems.dicom.commons.orm;

/**
 * Медицинский аппарат
 * 
 * @author dima_d
 * 
 */
public class ManufacturerDevice {

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

}
