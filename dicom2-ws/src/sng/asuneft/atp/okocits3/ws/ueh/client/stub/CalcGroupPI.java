
package sng.asuneft.atp.okocits3.ws.ueh.client.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for calcGroupPI complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="calcGroupPI">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idSAPPIGroup" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idSAPPIGroupLimit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ownerId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="typeLimitId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calcGroupPI", propOrder = {
    "idSAPPIGroup",
    "idSAPPIGroupLimit",
    "date",
    "name",
    "type",
    "ownerId",
    "typeLimitId"
})
public class CalcGroupPI {

    protected int idSAPPIGroup;
    protected int idSAPPIGroupLimit;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    protected String name;
    protected String type;
    protected Integer ownerId;
    protected Integer typeLimitId;

    /**
     * Gets the value of the idSAPPIGroup property.
     * 
     */
    public int getIdSAPPIGroup() {
        return idSAPPIGroup;
    }

    /**
     * Sets the value of the idSAPPIGroup property.
     * 
     */
    public void setIdSAPPIGroup(int value) {
        this.idSAPPIGroup = value;
    }

    /**
     * Gets the value of the idSAPPIGroupLimit property.
     * 
     */
    public int getIdSAPPIGroupLimit() {
        return idSAPPIGroupLimit;
    }

    /**
     * Sets the value of the idSAPPIGroupLimit property.
     * 
     */
    public void setIdSAPPIGroupLimit(int value) {
        this.idSAPPIGroupLimit = value;
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the ownerId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the value of the ownerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOwnerId(Integer value) {
        this.ownerId = value;
    }

    /**
     * Gets the value of the typeLimitId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTypeLimitId() {
        return typeLimitId;
    }

    /**
     * Sets the value of the typeLimitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTypeLimitId(Integer value) {
        this.typeLimitId = value;
    }

}
