
package org.psystems.webdicom2.ws.client.stub;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "IOException", targetNamespace = "http://ws.webdicom2.psystems.org/")
public class IOException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private IOException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public IOException_Exception(String message, IOException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public IOException_Exception(String message, IOException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.psystems.webdicom2.ws.client.stub.IOException
     */
    public IOException getFaultInfo() {
        return faultInfo;
    }

}
