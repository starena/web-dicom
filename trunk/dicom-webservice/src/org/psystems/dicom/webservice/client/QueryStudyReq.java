
package org.psystems.dicom.webservice.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryStudyReq complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryStudyReq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="query" type="{http://webservice.dicom.psystems.org/}queryStudy" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryStudyReq", namespace = "http://webservice.dicom.psystems.org", propOrder = {
    "query"
})
public class QueryStudyReq {

    protected QueryStudy query;

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link QueryStudy }
     *     
     */
    public QueryStudy getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryStudy }
     *     
     */
    public void setQuery(QueryStudy value) {
        this.query = value;
    }

}
