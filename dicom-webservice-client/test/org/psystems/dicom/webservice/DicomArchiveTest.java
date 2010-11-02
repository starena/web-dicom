

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

           org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType findStudiesByType27=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType.class);
                    // TODO : Fill in the findStudiesByType27 here
                
                        assertNotNull(stub.findStudiesByType(
                        findStudiesByType27));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartfindStudiesByType() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType findStudiesByType27=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType.class);
                    // TODO : Fill in the findStudiesByType27 here
                

                stub.startfindStudiesByType(
                         findStudiesByType27,
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

           org.psystems.dicom.webservice.DicomArchiveStub.GetStudy getStudy29=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.GetStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.GetStudy.class);
                    // TODO : Fill in the getStudy29 here
                
                        assertNotNull(stub.getStudy(
                        getStudy29));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetStudy() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.GetStudy getStudy29=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.GetStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.GetStudy.class);
                    // TODO : Fill in the getStudy29 here
                

                stub.startgetStudy(
                         getStudy29,
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

           org.psystems.dicom.webservice.DicomArchiveStub.FindStudies findStudies31=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudies)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudies.class);
                    // TODO : Fill in the findStudies31 here
                
                        assertNotNull(stub.findStudies(
                        findStudies31));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartfindStudies() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.FindStudies findStudies31=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudies)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudies.class);
                    // TODO : Fill in the findStudies31 here
                

                stub.startfindStudies(
                         findStudies31,
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
        public  void testnewStudyUniversal() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomArchiveStub stub =
                    new org.psystems.dicom.webservice.DicomArchiveStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomArchiveStub.NewStudyUniversal newStudyUniversal33=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.NewStudyUniversal)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.NewStudyUniversal.class);
                    // TODO : Fill in the newStudyUniversal33 here
                
                    
                    //There is no output to be tested!
                    stub.newStudyUniversal(
                        newStudyUniversal33);
                    



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartnewStudyUniversal() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.NewStudyUniversal newStudyUniversal33=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.NewStudyUniversal)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.NewStudyUniversal.class);
                    // TODO : Fill in the newStudyUniversal33 here
                

                stub.startnewStudyUniversal(
                         newStudyUniversal33,
                    new tempCallbackN65710()
                );
              


        }

        private class tempCallbackN65710  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65710(){ super(null);}

            public void receiveResultnewStudyUniversal(
                         ) {
                
            }

            public void receiveErrornewStudyUniversal(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testgetManufacturers() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomArchiveStub stub =
                    new org.psystems.dicom.webservice.DicomArchiveStub();//the default implementation should point to the right endpoint

           
                        assertNotNull(stub.getManufacturers(
                        ));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetManufacturers() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             

                stub.startgetManufacturers(
                         
                    new tempCallbackN65761()
                );
              


        }

        private class tempCallbackN65761  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65761(){ super(null);}

            public void receiveResultgetManufacturers(
                         org.psystems.dicom.webservice.DicomArchiveStub.GetManufacturersResponse result
                            ) {
                
            }

            public void receiveErrorgetManufacturers(java.lang.Exception e) {
                fail();
            }

        }
      
        /**
         * Auto generated test method
         */
        public  void testnewStudy() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomArchiveStub stub =
                    new org.psystems.dicom.webservice.DicomArchiveStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomArchiveStub.NewStudy newStudy37=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.NewStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.NewStudy.class);
                    // TODO : Fill in the newStudy37 here
                
                    
                    //There is no output to be tested!
                    stub.newStudy(
                        newStudy37);
                    



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartnewStudy() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.NewStudy newStudy37=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.NewStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.NewStudy.class);
                    // TODO : Fill in the newStudy37 here
                

                stub.startnewStudy(
                         newStudy37,
                    new tempCallbackN65796()
                );
              


        }

        private class tempCallbackN65796  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65796(){ super(null);}

            public void receiveResultnewStudy(
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
    