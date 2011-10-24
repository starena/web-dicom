package org.psystems.dicom.commons;

/**
 * Конфигурация аппарата
 * 
 * @author dima_d
 * 
 */
public class ConfigDevice {

    private String modality;
    private String name;
    private String description;
    private ConfigDeviceDriver driver;

    /**
     * @param modality
     * @param name
     * @param description
     */
    public ConfigDevice(String modality, String name, String description) {
	this.modality = modality;
	this.name = name;
	this.description = description;
    }

    public String getModality() {
	return modality;
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    public void setDriver(ConfigDeviceDriver driver) {
	this.driver = driver;
    }

    public ConfigDeviceDriver getDriver() {
	return driver;
    }

    @Override
    public String toString() {
	return "ConfigDevice [modality=" + modality + ", name=" + name + ", description=" + description + ", device="
		+ driver + "]";
    }

}
