package org.psystems.dicom.commons;

import java.util.ArrayList;

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
    private ArrayList<ConfigDeviceEmploye> employes = new ArrayList<ConfigDeviceEmploye>();

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

    public void addEmploye(ConfigDeviceEmploye emp) {
	employes.add(emp);
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

    public ArrayList<ConfigDeviceEmploye> getEmployes() {
	return employes;
    }

    public void setEmployes(ArrayList<ConfigDeviceEmploye> employes) {
	this.employes = employes;
    }

    @Override
    public String toString() {
	String driver = "no driver's\n";
	String employe = "no employe's\n";
	if (getDriver() != null)
	    driver = getDriver().toString();
	
	
	for (ConfigDeviceEmploye emp : employes) {
	    employe += emp.toString() + "\n";
	}

	return "\t <Device> modality:[" + modality + "] name:[" + name + "] description:[" + description + "]\n"
		+ "\t   " + driver + "\t   " + employe;

    }

}
