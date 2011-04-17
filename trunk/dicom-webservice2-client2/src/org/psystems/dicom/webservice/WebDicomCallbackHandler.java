
/**
 * WebDicomCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package org.psystems.dicom.webservice;

    /**
     *  WebDicomCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class WebDicomCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public WebDicomCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public WebDicomCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for makeDirection method
            * override this method for handling normal response from makeDirection operation
            */
           public void receiveResultmakeDirection(
                    org.psystems.dicom.webservice.WebDicomStub.MakeDirectionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from makeDirection operation
           */
            public void receiveErrormakeDirection(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryDirection method
            * override this method for handling normal response from queryDirection operation
            */
           public void receiveResultqueryDirection(
                    org.psystems.dicom.webservice.WebDicomStub.QueryDirectionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryDirection operation
           */
            public void receiveErrorqueryDirection(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDirectionBydirectionId method
            * override this method for handling normal response from getDirectionBydirectionId operation
            */
           public void receiveResultgetDirectionBydirectionId(
                    org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDirectionBydirectionId operation
           */
            public void receiveErrorgetDirectionBydirectionId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryStudy method
            * override this method for handling normal response from queryStudy operation
            */
           public void receiveResultqueryStudy(
                    org.psystems.dicom.webservice.WebDicomStub.QueryStudyResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryStudy operation
           */
            public void receiveErrorqueryStudy(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDirectionById method
            * override this method for handling normal response from getDirectionById operation
            */
           public void receiveResultgetDirectionById(
                    org.psystems.dicom.webservice.WebDicomStub.GetDirectionByIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDirectionById operation
           */
            public void receiveErrorgetDirectionById(java.lang.Exception e) {
            }
                


    }
    