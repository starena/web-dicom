
package com.asutp.okocits3.ueh.ws.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subscribeItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="subscribeItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="parId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subscribeItem", propOrder = {
    "objId",
    "parId"
})
public class SubscribeItem {

    protected int objId;
    protected int parId;

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

}
