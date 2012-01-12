package org.psystems.dicom.pdfview.dto;

import java.io.Serializable;

/**
 * Конфигурация шаблона описаний
 * 
 * @author dima_d
 * 
 */
public class ConfigTemplateDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String modality;
	private String name;
	private int fontsize;
	private String description;

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

	public int getFontsize() {
		return fontsize;
	}

	public void setFontsize(int fontsize) {
		this.fontsize = fontsize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {

		return "\t <Template> modality:[" + modality + "] name:[" + name
				+ "] description:[" + description + "] fontsize:[" + fontsize
				+ "]\n";

	}

}
