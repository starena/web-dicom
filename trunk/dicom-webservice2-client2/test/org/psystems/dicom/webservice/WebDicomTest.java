

/**
 * WebDicomTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
    package org.psystems.dicom.webservice;

    /*
     *  WebDicomTest Junit test case
    */

    public class WebDicomTest extends junit.framework.TestCase{

     
        /**
         * Auto generated test method
         */
        public  void testmakeDirection() throws java.lang.Exception{

        org.psystems.dicom.webservice.WebDicomStub stub =
                    new org.psystems.dicom.webservice.WebDicomStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.WebDicomStub.MakeDirection makeDirection16=
                                                        (org.psystems.dicom.webservice.WebDicomStub.MakeDirection)getTestObject(org.psystems.dicom.webservice.WebDicomStub.MakeDirection.class);
                    // TODO : Fill in the makeDirection16 here
                
                        assertNotNull(stub.makeDirection(
                        makeDirection16));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartmakeDirection() throws java.lang.Exception{
            org.psystems.dicom.webservice.WebDicomStub stub = new org.psystems.dicom.webservice.WebDicomStub();
             org.psystems.dicom.webservice.WebDicomStub.MakeDirection makeDirection16=
                                                        (org.psystems.dicom.webservice.WebDicomStub.MakeDirection)getTestObject(org.psystems.dicom.webservice.WebDicomStub.MakeDirection.class);
                    // TODO : Fill in the makeDirection16 here
                

                stub.startmakeDirection(
                         makeDirection16,
                    new tempCallbackN65548()
                );
              


        }

        private class tempCallbackN65548  extends org.psystems.dicom.webservice.WebDicomCallbackHandler{
            public tempCallbackN65548(){ super(null);}

            public void receiveResultmakeDirection(
                         org.psystems.dicom.webservice.WebDicomStub.MakeDirectionResponse result
                            ) {
                
            }

            public void receiveErrormakeDirection(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testqueryDirection() throws java.lang.Exception{

        org.psystems.dicom.webservice.WebDicomStub stub =
                    new org.psystems.dicom.webservice.WebDicomStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.WebDicomStub.QueryDirectionE queryDirection18=
                                                        (org.psystems.dicom.webservice.WebDicomStub.QueryDirectionE)getTestObject(org.psystems.dicom.webservice.WebDicomStub.QueryDirectionE.class);
                    // TODO : Fill in the queryDirection18 here
                
                        assertNotNull(stub.queryDirection(
                        queryDirection18));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartqueryDirection() throws java.lang.Exception{
            org.psystems.dicom.webservice.WebDicomStub stub = new org.psystems.dicom.webservice.WebDicomStub();
             org.psystems.dicom.webservice.WebDicomStub.QueryDirectionE queryDirection18=
                                                        (org.psystems.dicom.webservice.WebDicomStub.QueryDirectionE)getTestObject(org.psystems.dicom.webservice.WebDicomStub.QueryDirectionE.class);
                    // TODO : Fill in the queryDirection18 here
                

                stub.startqueryDirection(
                         queryDirection18,
                    new tempCallbackN65602()
                );
              


        }

        private class tempCallbackN65602  extends org.psystems.dicom.webservice.WebDicomCallbackHandler{
            public tempCallbackN65602(){ super(null);}

            public void receiveResultqueryDirection(
                         org.psystems.dicom.webservice.WebDicomStub.QueryDirectionResponse result
                            ) {
                
            }

            public void receiveErrorqueryDirection(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetDirectionBydirectionId() throws java.lang.Exception{

        org.psystems.dicom.webservice.WebDicomStub stub =
                    new org.psystems.dicom.webservice.WebDicomStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionId getDirectionBydirectionId20=
                                                        (org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionId)getTestObject(org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionId.class);
                    // TODO : Fill in the getDirectionBydirectionId20 here
                
                        assertNotNull(stub.getDirectionBydirectionId(
                        getDirectionBydirectionId20));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetDirectionBydirectionId() throws java.lang.Exception{
            org.psystems.dicom.webservice.WebDicomStub stub = new org.psystems.dicom.webservice.WebDicomStub();
             org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionId getDirectionBydirectionId20=
                                                        (org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionId)getTestObject(org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionId.class);
                    // TODO : Fill in the getDirectionBydirectionId20 here
                

                stub.startgetDirectionBydirectionId(
                         getDirectionBydirectionId20,
                    new tempCallbackN65656()
                );
              


        }

        private class tempCallbackN65656  extends org.psystems.dicom.webservice.WebDicomCallbackHandler{
            public tempCallbackN65656(){ super(null);}

            public void receiveResultgetDirectionBydirectionId(
                         org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionIdResponse result
                            ) {
                
            }

            public void receiveErrorgetDirectionBydirectionId(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetDirectionById() throws java.lang.Exception{

        org.psystems.dicom.webservice.WebDicomStub stub =
                    new org.psystems.dicom.webservice.WebDicomStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.WebDicomStub.GetDirectionById getDirectionById22=
                                                        (org.psystems.dicom.webservice.WebDicomStub.GetDirectionById)getTestObject(org.psystems.dicom.webservice.WebDicomStub.GetDirectionById.class);
                    // TODO : Fill in the getDirectionById22 here
                
                        assertNotNull(stub.getDirectionById(
                        getDirectionById22));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetDirectionById() throws java.lang.Exception{
            org.psystems.dicom.webservice.WebDicomStub stub = new org.psystems.dicom.webservice.WebDicomStub();
             org.psystems.dicom.webservice.WebDicomStub.GetDirectionById getDirectionById22=
                                                        (org.psystems.dicom.webservice.WebDicomStub.GetDirectionById)getTestObject(org.psystems.dicom.webservice.WebDicomStub.GetDirectionById.class);
                    // TODO : Fill in the getDirectionById22 here
                

                stub.startgetDirectionById(
                         getDirectionById22,
                    new tempCallbackN65710()
                );
              


        }

        private class tempCallbackN65710  extends org.psystems.dicom.webservice.WebDicomCallbackHandler{
            public tempCallbackN65710(){ super(null);}

            public void receiveResultgetDirectionById(
                         org.psystems.dicom.webservice.WebDicomStub.GetDirectionByIdResponse result
                            ) {
                
            }

            public void receiveErrorgetDirectionById(java.lang.Exception e) {
                fail();
            }

        }
      
        //Create an ADBBean and provide it as the test object
        public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
        }

        
        

    }
    