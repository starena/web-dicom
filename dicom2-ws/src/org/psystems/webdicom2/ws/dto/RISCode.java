package org.psystems.webdicom2.ws.dto;


/**
 * Результат исследования
 * 
 * @author derenok_dv
 * 
 */
public class RISCode {

	public String risCode;
	public String modality;
	public String description;

	@Override
	public String toString() {
		return "RISCode [risCode=" + risCode + ", modality=" + modality
				+ ", description=" + description + "]";
	}

}
