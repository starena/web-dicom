package org.psystems.dicom.commons;

import java.util.ArrayList;

/**
 * Конфигурация драйвера для аппарата
 * 
 * @author dima_d
 * 
 */
public class ConfigDeviceDriver {

    private String aet;
    private String javaclass;
    ArrayList<ConfigDeviceDriverCondition> conditions = new ArrayList<ConfigDeviceDriverCondition>();

    public ConfigDeviceDriver(String aet, String javaclass) {
	this.aet = aet;
	this.javaclass = javaclass;
    }

    public void addCondition(ConfigDeviceDriverCondition condition) {
	conditions.add(condition);
    }

    public String getAet() {
	return aet;
    }

    public String getJavaclass() {
	return javaclass;
    }

    public ArrayList<ConfigDeviceDriverCondition> getConditions() {
	return conditions;
    }

    public void setConditions(ArrayList<ConfigDeviceDriverCondition> conditions) {
	this.conditions = conditions;
    }

    @Override
    public String toString() {
	return "ConfigDeviceDriver [aet=" + aet + ", javaclass=" + javaclass + ", conditions=" + conditions + "]";
    }

}
