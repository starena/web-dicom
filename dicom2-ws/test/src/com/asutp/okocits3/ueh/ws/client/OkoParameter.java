
package com.asutp.okocits3.ueh.ws.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for okoParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="okoParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="parId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="valueString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valueDouble" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="valueDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "okoParameter", propOrder = {
    "objId",
    "parId",
    "date",
    "status",
    "valueString",
    "valueDouble",
    "valueDate"
})
public class OkoParameter {

    protected int objId;
    protected int parId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    protected int status;
    @XmlElementRef(name = "valueString", type = JAXBElement.class)
    protected JAXBElement<String> valueString;
    @XmlElementRef(name = "valueDouble", type = JAXBElement.class)
    protected JAXBElement<Double> valueDouble;
    @XmlElementRef(name = "valueDate", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> valueDate;

    /**
     * Gets the value of the objId property.
     * 
     */
    public int getObjId() {
        return objId;
    }

    /**
     * Sets the value of the objId property.
     * 
     */
    public void setObjId(int value) {
        this.objId = value;
    }

    /**
     * Gets the value of the parId property.
     * 
     */
    public int getParId() {
        return parId;
    }

    /**
     * Sets the value of the parId property.
     * 
     */
    public void setParId(int value) {
        this.parId = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the status property.
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

    /**
     * Gets the value of the valueString property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getValueString() {
        return valueString;
    }

    /**
     * Sets the value of the valueString property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setValueString(JAXBElement<String> value) {
        this.valueString = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the valueDouble property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getValueDouble() {
        return valueDouble;
    }

    /**
     * Sets the value of the valueDouble property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setValueDouble(JAXBElement<Double> value) {
        this.valueDouble = ((JAXBElement<Double> ) value);
    }

    /**
     * Gets the value of the valueDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getValueDate() {
        return valueDate;
    }

    /**
     * Sets the value of the valueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setValueDate(JAXBElement<XMLGregorianCalendar> value) {
        this.valueDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

}
