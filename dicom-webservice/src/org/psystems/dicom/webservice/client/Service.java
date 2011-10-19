
package org.psystems.dicom.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for service complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="service">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceAlias" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="serviceDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyInternalId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service", propOrder = {
    "serviceAlias",
    "serviceCode",
    "serviceCount",
    "serviceDescription",
    "studyInternalId"
})
public class Service {

    protected String serviceAlias;
    protected String serviceCode;
    protected int serviceCount;
    protected String serviceDescription;
    protected long studyInternalId;

    /**
     * Gets the value of the serviceAlias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceAlias() {
        return serviceAlias;
    }

    /**
     * Sets the value of the serviceAlias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceAlias(String value) {
        this.serviceAlias = value;
    }

    /**
     * Gets the value of the serviceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * Sets the value of the serviceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceCode(String value) {
        this.serviceCode = value;
    }

    /**
     * Gets the value of the serviceCount property.
     * 
     */
    public int getServiceCount() {
        return serviceCount;
    }

    /**
     * Sets the value of the serviceCount property.
     * 
     */
    public void setServiceCount(int value) {
        this.serviceCount = value;
    }

    /**
     * Gets the value of the serviceDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceDescription() {
        return serviceDescription;
    }

    /**
     * Sets the value of the serviceDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceDescription(String value) {
        this.serviceDescription = value;
    }

    /**
     * Gets the value of the studyInternalId property.
     * 
     */
    public long getStudyInternalId() {
        return studyInternalId;
    }

    /**
     * Sets the value of the studyInternalId property.
     * 
     */
    public void setStudyInternalId(long value) {
        this.studyInternalId = value;
    }

}
