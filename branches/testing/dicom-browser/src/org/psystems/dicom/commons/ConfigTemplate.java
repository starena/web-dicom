package org.psystems.dicom.commons;

/**
 * Конфигурация шаблона описаний
 * 
 * @author dima_d
 * 
 */
public class ConfigTemplate {

    private String modality;
    private String name;
    private String fontsize;
    private String description;

    /**
     * @param modality
     * @param name
     * @param description
     */
    public ConfigTemplate(String modality, String name, String fontsize, String description) {
	this.modality = modality;
	this.name = name;
	this.fontsize = fontsize;
	this.description = description;
    }

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

    public String getFontsize() {
	return fontsize;
    }

    public void setFontsize(String fontsize) {
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

	return "\t <Template> modality:[" + modality + "] name:[" + name + "] description:[" + description
		+ "] fontsize:[" + fontsize + "]\n";

    }

}
