
package org.psystems.webdicom2.ws.client.stub;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "GateService", targetNamespace = "http://ws.webdicom2.psystems.org/", wsdlLocation = "http://localhost:8081/dicom2-ws/ws?wsdl")
public class GateService
    extends Service
{

    private final static URL GATESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(org.psystems.webdicom2.ws.client.stub.GateService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = org.psystems.webdicom2.ws.client.stub.GateService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8081/dicom2-ws/ws?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8081/dicom2-ws/ws?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        GATESERVICE_WSDL_LOCATION = url;
    }

    public GateService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public GateService() {
        super(GATESERVICE_WSDL_LOCATION, new QName("http://ws.webdicom2.psystems.org/", "GateService"));
    }

    /**
     * 
     * @return
     *     returns Gate
     */
    @WebEndpoint(name = "GatePort")
    public Gate getGatePort() {
        return super.getPort(new QName("http://ws.webdicom2.psystems.org/", "GatePort"), Gate.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Gate
     */
    @WebEndpoint(name = "GatePort")
    public Gate getGatePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.webdicom2.psystems.org/", "GatePort"), Gate.class, features);
    }

}
