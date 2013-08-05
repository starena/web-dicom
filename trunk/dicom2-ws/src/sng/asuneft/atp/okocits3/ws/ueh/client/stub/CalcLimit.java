
package sng.asuneft.atp.okocits3.ws.ueh.client.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for calcLimit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="calcLimit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idCalcgroup" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idCalcgroupLimit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="hour" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="maxR" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="maxY" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="minR" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="minY" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="typeProf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calcLimit", propOrder = {
    "idCalcgroup",
    "idCalcgroupLimit",
    "date",
    "hour",
    "maxR",
    "maxY",
    "minR",
    "minY",
    "typeProf"
})
public class CalcLimit {

    protected int idCalcgroup;
    protected int idCalcgroupLimit;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    protected int hour;
    protected Double maxR;
    protected Double maxY;
    protected Double minR;
    protected Double minY;
    protected String typeProf;

    /**
     * Gets the value of the idCalcgroup property.
     * 
     */
    public int getIdCalcgroup() {
        return idCalcgroup;
    }

    /**
     * Sets the value of the idCalcgroup property.
     * 
     */
    public void setIdCalcgroup(int value) {
        this.idCalcgroup = value;
    }

    /**
     * Gets the value of the idCalcgroupLimit property.
     * 
     */
    public int getIdCalcgroupLimit() {
        return idCalcgroupLimit;
    }

    /**
     * Sets the value of the idCalcgroupLimit property.
     * 
     */
    public void setIdCalcgroupLimit(int value) {
        this.idCalcgroupLimit = value;
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
     * Gets the value of the hour property.
     * 
     */
    public int getHour() {
        return hour;
    }

    /**
     * Sets the value of the hour property.
     * 
     */
    public void setHour(int value) {
        this.hour = value;
    }

    /**
     * Gets the value of the maxR property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxR() {
        return maxR;
    }

    /**
     * Sets the value of the maxR property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxR(Double value) {
        this.maxR = value;
    }

    /**
     * Gets the value of the maxY property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxY() {
        return maxY;
    }

    /**
     * Sets the value of the maxY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxY(Double value) {
        this.maxY = value;
    }

    /**
     * Gets the value of the minR property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMinR() {
        return minR;
    }

    /**
     * Sets the value of the minR property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMinR(Double value) {
        this.minR = value;
    }

    /**
     * Gets the value of the minY property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMinY() {
        return minY;
    }

    /**
     * Sets the value of the minY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMinY(Double value) {
        this.minY = value;
    }

    /**
     * Gets the value of the typeProf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeProf() {
        return typeProf;
    }

    /**
     * Sets the value of the typeProf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeProf(String value) {
        this.typeProf = value;
    }

}
