package org.psystems.webdicom2.ws.dto;

/**
 * Тэги DCM Исследования
 * 
 * @author derenok_dv
 * 
 */
public class DCMTag {

	public String id;
	public String name;
	public String type;
	public String value;

	@Override
	public String toString() {
		return "DCMTag [id=" + id + ", name=" + name + ", type=" + type
				+ ", value=" + value + "]";
	}

}
