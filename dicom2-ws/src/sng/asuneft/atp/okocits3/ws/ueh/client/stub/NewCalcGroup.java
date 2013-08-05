
package sng.asuneft.atp.okocits3.ws.ueh.client.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for newCalcGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="newCalcGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="calcgroup" type="{http://ueh.ws.okocits3.atp.asuneft.sng/}calcGroupPI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "newCalcGroup", propOrder = {
    "calcgroup"
})
public class NewCalcGroup {

    protected CalcGroupPI calcgroup;

    /**
     * Gets the value of the calcgroup property.
     * 
     * @return
     *     possible object is
     *     {@link CalcGroupPI }
     *     
     */
    public CalcGroupPI getCalcgroup() {
        return calcgroup;
    }

    /**
     * Sets the value of the calcgroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link CalcGroupPI }
     *     
     */
    public void setCalcgroup(CalcGroupPI value) {
        this.calcgroup = value;
    }

}
