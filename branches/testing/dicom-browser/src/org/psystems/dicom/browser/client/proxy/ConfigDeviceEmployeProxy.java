package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

/**
 * Конфигурация врача-лаборанта для аппарата
 * 
 * @author dima_d
 * 
 */
public class ConfigDeviceEmployeProxy implements Serializable {

    private static final long serialVersionUID = 8079746551268054L;
    public static String TYPE_DOCTOR = "DOCTOR";
    public static String TYPE_LABORANT = "LABORANT";

    private String type;
    private String name;

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
