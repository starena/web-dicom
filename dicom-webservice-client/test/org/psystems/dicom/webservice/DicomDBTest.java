

/**
 * DicomDBTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */
    package org.psystems.dicom.webservice;

    /*
     *  DicomDBTest Junit test case
    */

    public class DicomDBTest extends junit.framework.TestCase{

     
        /**
         * Auto generated test method
         */
        public  void teststartTransaction() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomDBStub stub =
                    new org.psystems.dicom.webservice.DicomDBStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomDBStub.StartTransaction startTransaction16=
                                                        (org.psystems.dicom.webservice.DicomDBStub.StartTransaction)getTestObject(org.psystems.dicom.webservice.DicomDBStub.StartTransaction.class);
                    // TODO : Fill in the startTransaction16 here
                
                        assertNotNull(stub.startTransaction(
                        startTransaction16));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartstartTransaction() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomDBStub stub = new org.psystems.dicom.webservice.DicomDBStub();
             org.psystems.dicom.webservice.DicomDBStub.StartTransaction startTransaction16=
                                                        (org.psystems.dicom.webservice.DicomDBStub.StartTransaction)getTestObject(org.psystems.dicom.webservice.DicomDBStub.StartTransaction.class);
                    // TODO : Fill in the startTransaction16 here
                

                stub.startstartTransaction(
                         startTransaction16,
                    new tempCallbackN65548()
                );
              


        }

        private class tempCallbackN65548  extends org.psystems.dicom.webservice.DicomDBCallbackHandler{
            public tempCallbackN65548(){ super(null);}

            public void receiveResultstartTransaction(
                         org.psystems.dicom.webservice.DicomDBStub.StartTransactionResponse result
                            ) {
                
            }

            public void receiveErrorstartTransaction(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testfindStudy() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomDBStub stub =
                    new org.psystems.dicom.webservice.DicomDBStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomDBStub.FindStudy findStudy18=
                                                        (org.psystems.dicom.webservice.DicomDBStub.FindStudy)getTestObject(org.psystems.dicom.webservice.DicomDBStub.FindStudy.class);
                    // TODO : Fill in the findStudy18 here
                
                        assertNotNull(stub.findStudy(
                        findStudy18));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartfindStudy() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomDBStub stub = new org.psystems.dicom.webservice.DicomDBStub();
             org.psystems.dicom.webservice.DicomDBStub.FindStudy findStudy18=
                                                        (org.psystems.dicom.webservice.DicomDBStub.FindStudy)getTestObject(org.psystems.dicom.webservice.DicomDBStub.FindStudy.class);
                    // TODO : Fill in the findStudy18 here
                

                stub.startfindStudy(
                         findStudy18,
                    new tempCallbackN65586()
                );
              


        }

        private class tempCallbackN65586  extends org.psystems.dicom.webservice.DicomDBCallbackHandler{
            public tempCallbackN65586(){ super(null);}

            public void receiveResultfindStudy(
                         org.psystems.dicom.webservice.DicomDBStub.FindStudyResponse result
                            ) {
                
            }

            public void receiveErrorfindStudy(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetStudy() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomDBStub stub =
                    new org.psystems.dicom.webservice.DicomDBStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomDBStub.GetStudy getStudy20=
                                                        (org.psystems.dicom.webservice.DicomDBStub.GetStudy)getTestObject(org.psystems.dicom.webservice.DicomDBStub.GetStudy.class);
                    // TODO : Fill in the getStudy20 here
                
                        assertNotNull(stub.getStudy(
                        getStudy20));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetStudy() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomDBStub stub = new org.psystems.dicom.webservice.DicomDBStub();
             org.psystems.dicom.webservice.DicomDBStub.GetStudy getStudy20=
                                                        (org.psystems.dicom.webservice.DicomDBStub.GetStudy)getTestObject(org.psystems.dicom.webservice.DicomDBStub.GetStudy.class);
                    // TODO : Fill in the getStudy20 here
                

                stub.startgetStudy(
                         getStudy20,
                    new tempCallbackN65624()
                );
              


        }

        private class tempCallbackN65624  extends org.psystems.dicom.webservice.DicomDBCallbackHandler{
            public tempCallbackN65624(){ super(null);}

            public void receiveResultgetStudy(
                         org.psystems.dicom.webservice.DicomDBStub.GetStudyResponse result
                            ) {
                
            }

            public void receiveErrorgetStudy(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testfindStudies() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomDBStub stub =
                    new org.psystems.dicom.webservice.DicomDBStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomDBStub.FindStudies findStudies22=
                                                        (org.psystems.dicom.webservice.DicomDBStub.FindStudies)getTestObject(org.psystems.dicom.webservice.DicomDBStub.FindStudies.class);
                    // TODO : Fill in the findStudies22 here
                
                        assertNotNull(stub.findStudies(
                        findStudies22));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartfindStudies() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomDBStub stub = new org.psystems.dicom.webservice.DicomDBStub();
             org.psystems.dicom.webservice.DicomDBStub.FindStudies findStudies22=
                                                        (org.psystems.dicom.webservice.DicomDBStub.FindStudies)getTestObject(org.psystems.dicom.webservice.DicomDBStub.FindStudies.class);
                    // TODO : Fill in the findStudies22 here
                

                stub.startfindStudies(
                         findStudies22,
                    new tempCallbackN65662()
                );
              


        }

        private class tempCallbackN65662  extends org.psystems.dicom.webservice.DicomDBCallbackHandler{
            public tempCallbackN65662(){ super(null);}

            public void receiveResultfindStudies(
                         org.psystems.dicom.webservice.DicomDBStub.FindStudiesResponse result
                            ) {
                
            }

            public void receiveErrorfindStudies(java.lang.Exception e) {
                fail();
            }

        }
      
        //Create an ADBBean and provide it as the test object
        public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
        }

        
        

    }
    