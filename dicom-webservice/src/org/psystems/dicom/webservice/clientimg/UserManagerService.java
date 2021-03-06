
package org.psystems.dicom.webservice.clientimg;

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
@WebServiceClient(name = "UserManagerService", targetNamespace = "http://webservice.dicom.psystems.org/", wsdlLocation = "http://localhost:8080/dicom-webservice/UserManager?wsdl")
public class UserManagerService
    extends Service
{

    private final static URL USERMANAGERSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(org.psystems.dicom.webservice.clientimg.UserManagerService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = org.psystems.dicom.webservice.clientimg.UserManagerService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/dicom-webservice/UserManager?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/dicom-webservice/UserManager?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        USERMANAGERSERVICE_WSDL_LOCATION = url;
    }

    public UserManagerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UserManagerService() {
        super(USERMANAGERSERVICE_WSDL_LOCATION, new QName("http://webservice.dicom.psystems.org/", "UserManagerService"));
    }

    /**
     * 
     * @return
     *     returns UserManager
     */
    @WebEndpoint(name = "UserManagerPort")
    public UserManager getUserManagerPort() {
        return super.getPort(new QName("http://webservice.dicom.psystems.org/", "UserManagerPort"), UserManager.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns UserManager
     */
    @WebEndpoint(name = "UserManagerPort")
    public UserManager getUserManagerPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.dicom.psystems.org/", "UserManagerPort"), UserManager.class, features);
    }

}
