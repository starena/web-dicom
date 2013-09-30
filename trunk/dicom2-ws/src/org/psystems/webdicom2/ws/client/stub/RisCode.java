
package org.psystems.webdicom2.ws.client.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for risCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="risCode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="risCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modality" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "risCode", propOrder = {
    "risCode",
    "modality",
    "description"
})
public class RisCode {

    protected String risCode;
    protected String modality;
    protected String description;

    /**
     * Gets the value of the risCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRisCode() {
        return risCode;
    }

    /**
     * Sets the value of the risCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRisCode(String value) {
        this.risCode = value;
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

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
