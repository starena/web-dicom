
package sng.asuneft.atp.okocits3.ws.ueh.client.stub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for calcGroupFormulaOperand complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="calcGroupFormulaOperand">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="parId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="typePar" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calcGroupFormulaOperand", propOrder = {
    "objId",
    "parId",
    "typePar",
    "operator"
})
public class CalcGroupFormulaOperand {

    protected int objId;
    protected int parId;
    protected int typePar;
    protected String operator;

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
     * Gets the value of the typePar property.
     * 
     */
    public int getTypePar() {
        return typePar;
    }

    /**
     * Sets the value of the typePar property.
     * 
     */
    public void setTypePar(int value) {
        this.typePar = value;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperator(String value) {
        this.operator = value;
    }

}
