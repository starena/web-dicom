
package org.psystems.dicom.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for manufacturerDevice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="manufacturerDevice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="manufacturerModelDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="manufacturerModelName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="manufacturerModelTypeDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modality" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "manufacturerDevice", propOrder = {
    "manufacturerModelDescription",
    "manufacturerModelName",
    "manufacturerModelTypeDescription",
    "modality"
})
public class ManufacturerDevice {

    protected String manufacturerModelDescription;
    protected String manufacturerModelName;
    protected String manufacturerModelTypeDescription;
    protected String modality;

    /**
     * Gets the value of the manufacturerModelDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerModelDescription() {
        return manufacturerModelDescription;
    }

    /**
     * Sets the value of the manufacturerModelDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerModelDescription(String value) {
        this.manufacturerModelDescription = value;
    }

    /**
     * Gets the value of the manufacturerModelName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerModelName() {
        return manufacturerModelName;
    }

    /**
     * Sets the value of the manufacturerModelName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerModelName(String value) {
        this.manufacturerModelName = value;
    }

    /**
     * Gets the value of the manufacturerModelTypeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerModelTypeDescription() {
        return manufacturerModelTypeDescription;
    }

    /**
     * Sets the value of the manufacturerModelTypeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerModelTypeDescription(String value) {
        this.manufacturerModelTypeDescription = value;
    }

    /**
     * Gets the value of the modality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModality() {
        return modality;
    }

    /**
     * Sets the value of the modality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModality(String value) {
        this.modality = value;
    }

}
