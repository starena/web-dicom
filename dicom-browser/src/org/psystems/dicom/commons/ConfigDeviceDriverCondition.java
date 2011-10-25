package org.psystems.dicom.commons;

/**
 * Конфигурация тегов драйвера для аппарата
 * 
 * @author dima_d
 * 
 */
public class ConfigDeviceDriverCondition {
    private String tag;
    private String type;
    private String value;

    public ConfigDeviceDriverCondition(String tag, String type, String value) {
	super();
	this.tag = tag;
	this.type = type;
	this.value = value;
    }

    public String getTag() {
	return tag;
    }

    public String getType() {
	return type;
    }

    public String getValue() {
	return value;
    }

    @Override
    public String toString() {
	return "\t     <Condtiton> tag: [" + tag + "] type: [" + type + "] value: [" + value + "]";
    }

}
