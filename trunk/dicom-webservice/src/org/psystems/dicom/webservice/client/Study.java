
package org.psystems.dicom.webservice.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for study complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="study">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dcmFilesId" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="direction" type="{http://webservice.dicom.psystems.org/}direction" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="manufacturerModelName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="manufacturerModelUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientBirthDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientSex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientShortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="servicesPerformed" type="{http://webservice.dicom.psystems.org/}service" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="studyDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyDateTimeModify" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyDateTimeRemoved" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyDoctor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyInstanceUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyModality" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyOperator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyViewprotocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyViewprotocolDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "study", propOrder = {
    "dcmFilesId",
    "direction",
    "id",
    "manufacturerModelName",
    "manufacturerModelUID",
    "patientBirthDate",
    "patientId",
    "patientName",
    "patientSex",
    "patientShortName",
    "servicesPerformed",
    "studyDate",
    "studyDateTimeModify",
    "studyDateTimeRemoved",
    "studyDescription",
    "studyDoctor",
    "studyId",
    "studyInstanceUID",
    "studyModality",
    "studyOperator",
    "studyResult",
    "studyType",
    "studyUrl",
    "studyViewprotocol",
    "studyViewprotocolDate"
})
public class Study {

    @XmlElement(nillable = true)
    protected List<Long> dcmFilesId;
    protected Direction direction;
    protected Long id;
    protected String manufacturerModelName;
    protected String manufacturerModelUID;
    protected String patientBirthDate;
    protected String patientId;
    protected String patientName;
    protected String patientSex;
    protected String patientShortName;
    @XmlElement(nillable = true)
    protected List<Service> servicesPerformed;
    protected String studyDate;
    protected String studyDateTimeModify;
    protected String studyDateTimeRemoved;
    protected String studyDescription;
    protected String studyDoctor;
    protected String studyId;
    protected String studyInstanceUID;
    protected String studyModality;
    protected String studyOperator;
    protected String studyResult;
    protected String studyType;
    protected String studyUrl;
    protected String studyViewprotocol;
    protected String studyViewprotocolDate;

    /**
     * Gets the value of the dcmFilesId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dcmFilesId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDcmFilesId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getDcmFilesId() {
        if (dcmFilesId == null) {
            dcmFilesId = new ArrayList<Long>();
        }
        return this.dcmFilesId;
    }

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link Direction }
     *     
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Direction }
     *     
     */
    public void setDirection(Direction value) {
        this.direction = value;
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
     * Gets the value of the manufacturerModelUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerModelUID() {
        return manufacturerModelUID;
    }

    /**
     * Sets the value of the manufacturerModelUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerModelUID(String value) {
        this.manufacturerModelUID = value;
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
     * Gets the value of the servicesPerformed property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the servicesPerformed property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServicesPerformed().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Service }
     * 
     * 
     */
    public List<Service> getServicesPerformed() {
        if (servicesPerformed == null) {
            servicesPerformed = new ArrayList<Service>();
        }
        return this.servicesPerformed;
    }

    /**
     * Gets the value of the studyDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyDate() {
        return studyDate;
    }

    /**
     * Sets the value of the studyDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyDate(String value) {
        this.studyDate = value;
    }

    /**
     * Gets the value of the studyDateTimeModify property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyDateTimeModify() {
        return studyDateTimeModify;
    }

    /**
     * Sets the value of the studyDateTimeModify property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyDateTimeModify(String value) {
        this.studyDateTimeModify = value;
    }

    /**
     * Gets the value of the studyDateTimeRemoved property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyDateTimeRemoved() {
        return studyDateTimeRemoved;
    }

    /**
     * Sets the value of the studyDateTimeRemoved property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyDateTimeRemoved(String value) {
        this.studyDateTimeRemoved = value;
    }

    /**
     * Gets the value of the studyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyDescription() {
        return studyDescription;
    }

    /**
     * Sets the value of the studyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyDescription(String value) {
        this.studyDescription = value;
    }

    /**
     * Gets the value of the studyDoctor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyDoctor() {
        return studyDoctor;
    }

    /**
     * Sets the value of the studyDoctor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyDoctor(String value) {
        this.studyDoctor = value;
    }

    /**
     * Gets the value of the studyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyId() {
        return studyId;
    }

    /**
     * Sets the value of the studyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyId(String value) {
        this.studyId = value;
    }

    /**
     * Gets the value of the studyInstanceUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyInstanceUID() {
        return studyInstanceUID;
    }

    /**
     * Sets the value of the studyInstanceUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyInstanceUID(String value) {
        this.studyInstanceUID = value;
    }

    /**
     * Gets the value of the studyModality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyModality() {
        return studyModality;
    }

    /**
     * Sets the value of the studyModality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyModality(String value) {
        this.studyModality = value;
    }

    /**
     * Gets the value of the studyOperator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyOperator() {
        return studyOperator;
    }

    /**
     * Sets the value of the studyOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyOperator(String value) {
        this.studyOperator = value;
    }

    /**
     * Gets the value of the studyResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyResult() {
        return studyResult;
    }

    /**
     * Sets the value of the studyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyResult(String value) {
        this.studyResult = value;
    }

    /**
     * Gets the value of the studyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyType() {
        return studyType;
    }

    /**
     * Sets the value of the studyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyType(String value) {
        this.studyType = value;
    }

    /**
     * Gets the value of the studyUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyUrl() {
        return studyUrl;
    }

    /**
     * Sets the value of the studyUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyUrl(String value) {
        this.studyUrl = value;
    }

    /**
     * Gets the value of the studyViewprotocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyViewprotocol() {
        return studyViewprotocol;
    }

    /**
     * Sets the value of the studyViewprotocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyViewprotocol(String value) {
        this.studyViewprotocol = value;
    }

    /**
     * Gets the value of the studyViewprotocolDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyViewprotocolDate() {
        return studyViewprotocolDate;
    }

    /**
     * Sets the value of the studyViewprotocolDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyViewprotocolDate(String value) {
        this.studyViewprotocolDate = value;
    }

}
