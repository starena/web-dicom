package org.psystems.dicom.commons;

/**
 * Конфигурация врача-лаборанта для аппарата
 * 
 * @author dima_d
 * 
 */
public class ConfigDeviceEmploye {

    public static String TYPE_DOCTOR = "DOCTOR";
    public static String TYPE_LABORANT = "LABORANT";

    private String type;
    private String name;
    private String code;

    public ConfigDeviceEmploye(String type, String name, String code) {
	super();
	this.type = type;
	this.name = name;
	this.code = code;
    }

    public String getType() {
	return type;
    }

    public String getName() {
	return name;
    }

    public String getCode() {
	return code;
    }

    @Override
    public String toString() {
	return "\t     <employe> type: [" + type + "] name: [" + name + "] code: [" + code + "]";
    }

}
