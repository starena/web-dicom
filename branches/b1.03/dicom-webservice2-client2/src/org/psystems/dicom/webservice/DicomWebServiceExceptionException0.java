
/**
 * DicomWebServiceExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

package org.psystems.dicom.webservice;

public class DicomWebServiceExceptionException0 extends java.lang.Exception{
    
    private org.psystems.dicom.webservice.WebDicomStub.DicomWebServiceExceptionE faultMessage;
    
    public DicomWebServiceExceptionException0() {
        super("DicomWebServiceExceptionException0");
    }
           
    public DicomWebServiceExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public DicomWebServiceExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(org.psystems.dicom.webservice.WebDicomStub.DicomWebServiceExceptionE msg){
       faultMessage = msg;
    }
    
    public org.psystems.dicom.webservice.WebDicomStub.DicomWebServiceExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    