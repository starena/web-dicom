package org.psystems.dicom.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * @author dima_d
 * 
 * http://jax-ws.java.net/
 *
 */
@WebService()
public class UserManager {
 
    /**
     * Web service operation
     */
    @WebMethod(operationName = "addUser")
    public boolean addUser(@WebParam(name = "userName")
    String userName) {
        System.out.println(userName);
        return true;
    }
 
}
