
package org.psystems.dicom.webservice.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for direction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="direction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dateDirection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="datePerformed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateTimeModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateTimePlanned" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateTimeRemoved" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="device" type="{http://webservice.dicom.psystems.org/}manufacturerDevice" minOccurs="0"/>
 *         &lt;element name="diagnosisDirect" type="{http://webservice.dicom.psystems.org/}diagnosis" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="diagnosisPerformed" type="{http://webservice.dicom.psystems.org/}diagnosis" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="directionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="directionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="directionLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="doctorDirect" type="{http://webservice.dicom.psystems.org/}employee" minOccurs="0"/>
 *         &lt;element name="doctorPerformed" type="{http://webservice.dicom.psystems.org/}employee" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="patient" type="{http://webservice.dicom.psystems.org/}patient" minOccurs="0"/>
 *         &lt;element name="servicesDirect" type="{http://webservice.dicom.psystems.org/}service" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="servicesPerformed" type="{http://webservice.dicom.psystems.org/}service" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "direction", propOrder = {
    "dateDirection",
    "datePerformed",
    "dateTimeModified",
    "dateTimePlanned",
    "dateTimeRemoved",
    "device",
    "diagnosisDirect",
    "diagnosisPerformed",
    "directionCode",
    "directionId",
    "directionLocation",
    "doctorDirect",
    "doctorPerformed",
    "id",
    "patient",
    "servicesDirect",
    "servicesPerformed"
})
public class Direction {

    protected String dateDirection;
    protected String datePerformed;
    protected String dateTimeModified;
    protected String dateTimePlanned;
    protected String dateTimeRemoved;
    protected ManufacturerDevice device;
    @XmlElement(nillable = true)
    protected List<Diagnosis> diagnosisDirect;
    @XmlElement(nillable = true)
    protected List<Diagnosis> diagnosisPerformed;
    protected String directionCode;
    protected String directionId;
    protected String directionLocation;
    protected Employee doctorDirect;
    protected Employee doctorPerformed;
    protected Long id;
    protected Patient patient;
    @XmlElement(nillable = true)
    protected List<Service> servicesDirect;
    @XmlElement(nillable = true)
    protected List<Service> servicesPerformed;

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
     * Gets the value of the datePerformed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatePerformed() {
        return datePerformed;
    }

    /**
     * Sets the value of the datePerformed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatePerformed(String value) {
        this.datePerformed = value;
    }

    /**
     * Gets the value of the dateTimeModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTimeModified() {
        return dateTimeModified;
    }

    /**
     * Sets the value of the dateTimeModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTimeModified(String value) {
        this.dateTimeModified = value;
    }

    /**
     * Gets the value of the dateTimePlanned property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTimePlanned() {
        return dateTimePlanned;
    }

    /**
     * Sets the value of the dateTimePlanned property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTimePlanned(String value) {
        this.dateTimePlanned = value;
    }

    /**
     * Gets the value of the dateTimeRemoved property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTimeRemoved() {
        return dateTimeRemoved;
    }

    /**
     * Sets the value of the dateTimeRemoved property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTimeRemoved(String value) {
        this.dateTimeRemoved = value;
    }

    /**
     * Gets the value of the device property.
     * 
     * @return
     *     possible object is
     *     {@link ManufacturerDevice }
     *     
     */
    public ManufacturerDevice getDevice() {
        return device;
    }

    /**
     * Sets the value of the device property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManufacturerDevice }
     *     
     */
    public void setDevice(ManufacturerDevice value) {
        this.device = value;
    }

    /**
     * Gets the value of the diagnosisDirect property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the diagnosisDirect property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiagnosisDirect().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Diagnosis }
     * 
     * 
     */
    public List<Diagnosis> getDiagnosisDirect() {
        if (diagnosisDirect == null) {
            diagnosisDirect = new ArrayList<Diagnosis>();
        }
        return this.diagnosisDirect;
    }

    /**
     * Gets the value of the diagnosisPerformed property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the diagnosisPerformed property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiagnosisPerformed().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Diagnosis }
     * 
     * 
     */
    public List<Diagnosis> getDiagnosisPerformed() {
        if (diagnosisPerformed == null) {
            diagnosisPerformed = new ArrayList<Diagnosis>();
        }
        return this.diagnosisPerformed;
    }

    /**
     * Gets the value of the directionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectionCode() {
        return directionCode;
    }

    /**
     * Sets the value of the directionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectionCode(String value) {
        this.directionCode = value;
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
     * Gets the value of the doctorDirect property.
     * 
     * @return
     *     possible object is
     *     {@link Employee }
     *     
     */
    public Employee getDoctorDirect() {
        return doctorDirect;
    }

    /**
     * Sets the value of the doctorDirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link Employee }
     *     
     */
    public void setDoctorDirect(Employee value) {
        this.doctorDirect = value;
    }

    /**
     * Gets the value of the doctorPerformed property.
     * 
     * @return
     *     possible object is
     *     {@link Employee }
     *     
     */
    public Employee getDoctorPerformed() {
        return doctorPerformed;
    }

    /**
     * Sets the value of the doctorPerformed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Employee }
     *     
     */
    public void setDoctorPerformed(Employee value) {
        this.doctorPerformed = value;
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
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link Patient }
     *     
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Patient }
     *     
     */
    public void setPatient(Patient value) {
        this.patient = value;
    }

    /**
     * Gets the value of the servicesDirect property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the servicesDirect property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServicesDirect().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Service }
     * 
     * 
     */
    public List<Service> getServicesDirect() {
        if (servicesDirect == null) {
            servicesDirect = new ArrayList<Service>();
        }
        return this.servicesDirect;
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

}
