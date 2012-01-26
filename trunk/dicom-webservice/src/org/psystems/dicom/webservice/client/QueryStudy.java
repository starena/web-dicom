
package org.psystems.dicom.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryStudy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryStudy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="beginStudyDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="beginStudyDateTimeModify" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="endStudyDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="endStudyDateTimeModify" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="manufacturerModelName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientBirthDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientSex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patientShortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sortOrder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyComplite" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="studyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyModality" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyNotComplite" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="studyRemoved" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="studyResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studyViewProtocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryStudy", propOrder = {
    "beginStudyDate",
    "beginStudyDateTimeModify",
    "endStudyDate",
    "endStudyDateTimeModify",
    "id",
    "manufacturerModelName",
    "patientBirthDate",
    "patientId",
    "patientName",
    "patientSex",
    "patientShortName",
    "sortOrder",
    "studyComplite",
    "studyId",
    "studyModality",
    "studyNotComplite",
    "studyRemoved",
    "studyResult",
    "studyViewProtocol"
})
public class QueryStudy {

    protected String beginStudyDate;
    protected String beginStudyDateTimeModify;
    protected String endStudyDate;
    protected String endStudyDateTimeModify;
    protected Long id;
    protected String manufacturerModelName;
    protected String patientBirthDate;
    protected String patientId;
    protected String patientName;
    protected String patientSex;
    protected String patientShortName;
    protected String sortOrder;
    protected boolean studyComplite;
    protected String studyId;
    protected String studyModality;
    protected boolean studyNotComplite;
    protected boolean studyRemoved;
    protected String studyResult;
    protected String studyViewProtocol;

    /**
     * Gets the value of the beginStudyDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeginStudyDate() {
        return beginStudyDate;
    }

    /**
     * Sets the value of the beginStudyDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginStudyDate(String value) {
        this.beginStudyDate = value;
    }

    /**
     * Gets the value of the beginStudyDateTimeModify property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeginStudyDateTimeModify() {
        return beginStudyDateTimeModify;
    }

    /**
     * Sets the value of the beginStudyDateTimeModify property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginStudyDateTimeModify(String value) {
        this.beginStudyDateTimeModify = value;
    }

    /**
     * Gets the value of the endStudyDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndStudyDate() {
        return endStudyDate;
    }

    /**
     * Sets the value of the endStudyDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndStudyDate(String value) {
        this.endStudyDate = value;
    }

    /**
     * Gets the value of the endStudyDateTimeModify property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndStudyDateTimeModify() {
        return endStudyDateTimeModify;
    }

    /**
     * Sets the value of the endStudyDateTimeModify property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndStudyDateTimeModify(String value) {
        this.endStudyDateTimeModify = value;
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
     * Gets the value of the sortOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the value of the sortOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSortOrder(String value) {
        this.sortOrder = value;
    }

    /**
     * Gets the value of the studyComplite property.
     * 
     */
    public boolean isStudyComplite() {
        return studyComplite;
    }

    /**
     * Sets the value of the studyComplite property.
     * 
     */
    public void setStudyComplite(boolean value) {
        this.studyComplite = value;
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
     * Gets the value of the studyNotComplite property.
     * 
     */
    public boolean isStudyNotComplite() {
        return studyNotComplite;
    }

    /**
     * Sets the value of the studyNotComplite property.
     * 
     */
    public void setStudyNotComplite(boolean value) {
        this.studyNotComplite = value;
    }

    /**
     * Gets the value of the studyRemoved property.
     * 
     */
    public boolean isStudyRemoved() {
        return studyRemoved;
    }

    /**
     * Sets the value of the studyRemoved property.
     * 
     */
    public void setStudyRemoved(boolean value) {
        this.studyRemoved = value;
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
     * Gets the value of the studyViewProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudyViewProtocol() {
        return studyViewProtocol;
    }

    /**
     * Sets the value of the studyViewProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudyViewProtocol(String value) {
        this.studyViewProtocol = value;
    }

}
