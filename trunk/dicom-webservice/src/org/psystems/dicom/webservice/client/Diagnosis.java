
package org.psystems.dicom.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for diagnosis complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="diagnosis">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="diagnosisCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="diagnosisDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="diagnosisSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="diagnosisType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diagnosis", propOrder = {
    "diagnosisCode",
    "diagnosisDescription",
    "diagnosisSubType",
    "diagnosisType"
})
public class Diagnosis {

    protected String diagnosisCode;
    protected String diagnosisDescription;
    protected String diagnosisSubType;
    protected String diagnosisType;

    /**
     * Gets the value of the diagnosisCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    /**
     * Sets the value of the diagnosisCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosisCode(String value) {
        this.diagnosisCode = value;
    }

    /**
     * Gets the value of the diagnosisDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosisDescription() {
        return diagnosisDescription;
    }

    /**
     * Sets the value of the diagnosisDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosisDescription(String value) {
        this.diagnosisDescription = value;
    }

    /**
     * Gets the value of the diagnosisSubType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosisSubType() {
        return diagnosisSubType;
    }

    /**
     * Sets the value of the diagnosisSubType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosisSubType(String value) {
        this.diagnosisSubType = value;
    }

    /**
     * Gets the value of the diagnosisType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosisType() {
        return diagnosisType;
    }

    /**
     * Sets the value of the diagnosisType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosisType(String value) {
        this.diagnosisType = value;
    }

}
