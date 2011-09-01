
package org.psystems.dicom.webservice.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for makeDirection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="makeDirection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="directionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="doctorDirect" type="{http://webservice.dicom.psystems.org/}employee" minOccurs="0"/>
 *         &lt;element name="diagnosisDirect" type="{http://webservice.dicom.psystems.org/}diagnosis" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="servicesDirect" type="{http://webservice.dicom.psystems.org/}service" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dateDirection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="device" type="{http://webservice.dicom.psystems.org/}manufacturerDevice" minOccurs="0"/>
 *         &lt;element name="dateTimePlanned" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="directionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="directionLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patient" type="{http://webservice.dicom.psystems.org/}patient" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "makeDirection", propOrder = {
    "directionId",
    "doctorDirect",
    "diagnosisDirect",
    "servicesDirect",
    "dateDirection",
    "device",
    "dateTimePlanned",
    "directionCode",
    "directionLocation",
    "patient"
})
public class MakeDirection {

    protected String directionId;
    protected Employee doctorDirect;
    @XmlElement(nillable = true)
    protected List<Diagnosis> diagnosisDirect;
    @XmlElement(nillable = true)
    protected List<Service> servicesDirect;
    protected String dateDirection;
    protected ManufacturerDevice device;
    protected String dateTimePlanned;
    protected String directionCode;
    protected String directionLocation;
    protected Patient patient;

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

}
