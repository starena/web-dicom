
package org.psystems.dicom.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryDirection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryDirection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dateDirection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateTimePlannedBegin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateTimePlannedEnd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="directionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="directionLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="doctorDirectCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="doctorDirectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="doctorPerformedCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="doctorPerformedName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="manufacturerDevice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientBirthDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientSex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientShortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="senderLPU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryDirection", propOrder = {
    "dateDirection",
    "dateTimePlannedBegin",
    "dateTimePlannedEnd",
    "directionId",
    "directionLocation",
    "doctorDirectCode",
    "doctorDirectName",
    "doctorPerformedCode",
    "doctorPerformedName",
    "id",
    "manufacturerDevice",
    "patientBirthDate",
    "patientId",
    "patientName",
    "patientSex",
    "patientShortName",
    "senderLPU"
})
public class QueryDirection {

    protected String dateDirection;
    protected String dateTimePlannedBegin;
    protected String dateTimePlannedEnd;
    protected String directionId;
    protected String directionLocation;
    protected String doctorDirectCode;
    protected String doctorDirectName;
    protected String doctorPerformedCode;
    protected String doctorPerformedName;
    protected Long id;
    protected String manufacturerDevice;
    protected String patientBirthDate;
    protected String patientId;
    protected String patientName;
    protected String patientSex;
    protected String patientShortName;
    protected String senderLPU;

    /**
     * Gets the value of the dateDirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateDirection() {
        return dateDirection;
    }

    /**
     * Sets the value of the dateDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateDirection(String value) {
        this.dateDirection = value;
    }

    /**
     * Gets the value of the dateTimePlannedBegin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTimePlannedBegin() {
        return dateTimePlannedBegin;
    }

    /**
     * Sets the value of the dateTimePlannedBegin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTimePlannedBegin(String value) {
        this.dateTimePlannedBegin = value;
    }

    /**
     * Gets the value of the dateTimePlannedEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTimePlannedEnd() {
        return dateTimePlannedEnd;
    }

    /**
     * Sets the value of the dateTimePlannedEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTimePlannedEnd(String value) {
        this.dateTimePlannedEnd = value;
    }

    /**
     * Gets the value of the directionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectionId() {
        return directionId;
    }

    /**
     * Sets the value of the directionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectionId(String value) {
        this.directionId = value;
    }

    /**
     * Gets the value of the directionLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectionLocation() {
        return directionLocation;
    }

    /**
     * Sets the value of the directionLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectionLocation(String value) {
        this.directionLocation = value;
    }

    /**
     * Gets the value of the doctorDirectCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoctorDirectCode() {
        return doctorDirectCode;
    }

    /**
     * Sets the value of the doctorDirectCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoctorDirectCode(String value) {
        this.doctorDirectCode = value;
    }

    /**
     * Gets the value of the doctorDirectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoctorDirectName() {
        return doctorDirectName;
    }

    /**
     * Sets the value of the doctorDirectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoctorDirectName(String value) {
        this.doctorDirectName = value;
    }

    /**
     * Gets the value of the doctorPerformedCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoctorPerformedCode() {
        return doctorPerformedCode;
    }

    /**
     * Sets the value of the doctorPerformedCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoctorPerformedCode(String value) {
        this.doctorPerformedCode = value;
    }

    /**
     * Gets the value of the doctorPerformedName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoctorPerformedName() {
        return doctorPerformedName;
    }

    /**
     * Sets the value of the doctorPerformedName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoctorPerformedName(String value) {
        this.doctorPerformedName = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the manufacturerDevice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerDevice() {
        return manufacturerDevice;
    }

    /**
     * Sets the value of the manufacturerDevice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerDevice(String value) {
        this.manufacturerDevice = value;
    }

    /**
     * Gets the value of the patientBirthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientBirthDate() {
        return patientBirthDate;
    }

    /**
     * Sets the value of the patientBirthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientBirthDate(String value) {
        this.patientBirthDate = value;
    }

    /**
     * Gets the value of the patientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the value of the patientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientId(String value) {
        this.patientId = value;
    }

    /**
     * Gets the value of the patientName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * Sets the value of the patientName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientName(String value) {
        this.patientName = value;
    }

    /**
     * Gets the value of the patientSex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientSex() {
        return patientSex;
    }

    /**
     * Sets the value of the patientSex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientSex(String value) {
        this.patientSex = value;
    }

    /**
     * Gets the value of the patientShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientShortName() {
        return patientShortName;
    }

    /**
     * Sets the value of the patientShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientShortName(String value) {
        this.patientShortName = value;
    }

    /**
     * Gets the value of the senderLPU property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderLPU() {
        return senderLPU;
    }

    /**
     * Sets the value of the senderLPU property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderLPU(String value) {
        this.senderLPU = value;
    }

}
