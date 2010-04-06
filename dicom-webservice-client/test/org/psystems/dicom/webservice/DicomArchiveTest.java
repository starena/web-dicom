

/**
 * DicomArchiveTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */
    package org.psystems.dicom.webservice;

    /*
     *  DicomArchiveTest Junit test case
    */

    public class DicomArchiveTest extends junit.framework.TestCase{

     
        /**
         * Auto generated test method
         */
        public  void testfindStudiesByType() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomArchiveStub stub =
                    new org.psystems.dicom.webservice.DicomArchiveStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType findStudiesByType16=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType.class);
                    // TODO : Fill in the findStudiesByType16 here
                
                        assertNotNull(stub.findStudiesByType(
                        findStudiesByType16));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartfindStudiesByType() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType findStudiesByType16=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType.class);
                    // TODO : Fill in the findStudiesByType16 here
                

                stub.startfindStudiesByType(
                         findStudiesByType16,
                    new tempCallbackN65548()
                );
              


        }

        private class tempCallbackN65548  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65548(){ super(null);}

            public void receiveResultfindStudiesByType(
                         org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByTypeResponse result
                            ) {
                
            }

            public void receiveErrorfindStudiesByType(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetStudy() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomArchiveStub stub =
                    new org.psystems.dicom.webservice.DicomArchiveStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomArchiveStub.GetStudy getStudy18=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.GetStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.GetStudy.class);
                    // TODO : Fill in the getStudy18 here
                
                        assertNotNull(stub.getStudy(
                        getStudy18));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetStudy() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.GetStudy getStudy18=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.GetStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.GetStudy.class);
                    // TODO : Fill in the getStudy18 here
                

                stub.startgetStudy(
                         getStudy18,
                    new tempCallbackN65602()
                );
              


        }

        private class tempCallbackN65602  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65602(){ super(null);}

            public void receiveResultgetStudy(
                         org.psystems.dicom.webservice.DicomArchiveStub.GetStudyResponse result
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

        org.psystems.dicom.webservice.DicomArchiveStub stub =
                    new org.psystems.dicom.webservice.DicomArchiveStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomArchiveStub.FindStudies findStudies20=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudies)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudies.class);
                    // TODO : Fill in the findStudies20 here
                
                        assertNotNull(stub.findStudies(
                        findStudies20));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartfindStudies() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.FindStudies findStudies20=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudies)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudies.class);
                    // TODO : Fill in the findStudies20 here
                

                stub.startfindStudies(
                         findStudies20,
                    new tempCallbackN65656()
                );
              


        }

        private class tempCallbackN65656  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65656(){ super(null);}

            public void receiveResultfindStudies(
                         org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesResponse result
                            ) {
                
            }

            public void receiveErrorfindStudies(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testnewStudy() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomArchiveStub stub =
                    new org.psystems.dicom.webservice.DicomArchiveStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomArchiveStub.NewStudy newStudy22=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.NewStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.NewStudy.class);
                    // TODO : Fill in the newStudy22 here
                
                        assertNotNull(stub.newStudy(
                        newStudy22));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartnewStudy() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.NewStudy newStudy22=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.NewStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.NewStudy.class);
                    // TODO : Fill in the newStudy22 here
                

                stub.startnewStudy(
                         newStudy22,
                    new tempCallbackN65710()
                );
              


        }

        private class tempCallbackN65710  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65710(){ super(null);}

            public void receiveResultnewStudy(
                         org.psystems.dicom.webservice.DicomArchiveStub.NewStudyResponse result
                            ) {
                
            }

            public void receiveErrornewStudy(java.lang.Exception e) {
                fail();
            }

        }
      
        //Create an ADBBean and provide it as the test object
        public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception{
           return (org.apache.axis2.databinding.ADBBean) type.newInstance();
        }

        
        

    }
    