
package sng.asuneft.atp.okocits3.ws.ueh.client.stub;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCalcGroups complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCalcGroups">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="filters" type="{http://ueh.ws.okocits3.atp.asuneft.sng/}calcGroupRequestFilter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="withlimit" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCalcGroups", propOrder = {
    "filters",
    "withlimit"
})
public class GetCalcGroups {

    protected List<CalcGroupRequestFilter> filters;
    protected Boolean withlimit;

    /**
     * Gets the value of the filters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalcGroupRequestFilter }
     * 
     * 
     */
    public List<CalcGroupRequestFilter> getFilters() {
        if (filters == null) {
            filters = new ArrayList<CalcGroupRequestFilter>();
        }
        return this.filters;
    }

    /**
     * Gets the value of the withlimit property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWithlimit() {
        return withlimit;
    }

    /**
     * Sets the value of the withlimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWithlimit(Boolean value) {
        this.withlimit = value;
    }

}
