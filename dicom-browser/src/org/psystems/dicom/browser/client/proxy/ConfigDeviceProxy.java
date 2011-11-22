package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Конфигурация аппарата
 * 
 * @author dima_d
 * 
 */
public class ConfigDeviceProxy implements Serializable {

    private static final long serialVersionUID = -3306597326380764155L;
    private String modality;
    private String name;
    private String description;
    private ArrayList<ConfigDeviceEmployeProxy> employes = new ArrayList<ConfigDeviceEmployeProxy>();

    public String getModality() {
	return modality;
    }

    public void setModality(String modality) {
	this.modality = modality;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public ArrayList<ConfigDeviceEmployeProxy> getEmployes() {
	return employes;
    }

    public void setEmployes(ArrayList<ConfigDeviceEmployeProxy> employes) {
	this.employes = employes;
    }

    public void addEmploye(ConfigDeviceEmployeProxy emp) {
	employes.add(emp);
    }

}
