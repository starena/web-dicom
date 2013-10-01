
package org.psystems.webdicom2.ws.client.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getDCM complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getDCM">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="misId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDCM", propOrder = {
    "misId"
})
public class GetDCM {

    protected String misId;

    /**
     * Gets the value of the misId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMisId() {
        return misId;
    }

    /**
     * Sets the value of the misId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMisId(String value) {
        this.misId = value;
    }

}
