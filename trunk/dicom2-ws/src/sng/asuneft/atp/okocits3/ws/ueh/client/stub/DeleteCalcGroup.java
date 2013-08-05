
package sng.asuneft.atp.okocits3.ws.ueh.client.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteCalcGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteCalcGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idOkoCalcGroup" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteCalcGroup", propOrder = {
    "idOkoCalcGroup"
})
public class DeleteCalcGroup {

    protected int idOkoCalcGroup;

    /**
     * Gets the value of the idOkoCalcGroup property.
     * 
     */
    public int getIdOkoCalcGroup() {
        return idOkoCalcGroup;
    }

    /**
     * Sets the value of the idOkoCalcGroup property.
     * 
     */
    public void setIdOkoCalcGroup(int value) {
        this.idOkoCalcGroup = value;
    }

}
