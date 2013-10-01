
package org.psystems.webdicom2.ws.client.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dcm complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dcm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="misId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dcmId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imageId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pdfId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idMis" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dcm", propOrder = {
    "misId",
    "dcmId",
    "imageId",
    "pdfId",
    "idMis"
})
public class Dcm {

    protected String misId;
    protected String dcmId;
    protected String imageId;
    protected String pdfId;
    protected boolean idMis;

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

    /**
     * Gets the value of the dcmId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDcmId() {
        return dcmId;
    }

    /**
     * Sets the value of the dcmId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDcmId(String value) {
        this.dcmId = value;
    }

    /**
     * Gets the value of the imageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * Sets the value of the imageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageId(String value) {
        this.imageId = value;
    }

    /**
     * Gets the value of the pdfId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdfId() {
        return pdfId;
    }

    /**
     * Sets the value of the pdfId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdfId(String value) {
        this.pdfId = value;
    }

    /**
     * Gets the value of the idMis property.
     * 
     */
    public boolean isIdMis() {
        return idMis;
    }

    /**
     * Sets the value of the idMis property.
     * 
     */
    public void setIdMis(boolean value) {
        this.idMis = value;
    }

}
