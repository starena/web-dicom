
/**
 * DicomWebServiceExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

package org.psystems.dicom.webservice;

public class DicomWebServiceExceptionException0 extends java.lang.Exception{
    
    private org.psystems.dicom.webservice.DicomArchiveStub.DicomWebServiceExceptionE faultMessage;
    
    public DicomWebServiceExceptionException0() {
        super("DicomWebServiceExceptionException0");
    }
           
    public DicomWebServiceExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public DicomWebServiceExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(org.psystems.dicom.webservice.DicomArchiveStub.DicomWebServiceExceptionE msg){
       faultMessage = msg;
    }
    
    public org.psystems.dicom.webservice.DicomArchiveStub.DicomWebServiceExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    