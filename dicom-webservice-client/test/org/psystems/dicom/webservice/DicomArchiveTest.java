/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */

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
        public  void testgetStudy() throws java.lang.Exception{

        org.psystems.dicom.webservice.DicomArchiveStub stub =
                    new org.psystems.dicom.webservice.DicomArchiveStub();//the default implementation should point to the right endpoint

           org.psystems.dicom.webservice.DicomArchiveStub.GetStudy getStudy12=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.GetStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.GetStudy.class);
                    // TODO : Fill in the getStudy12 here
                
                        assertNotNull(stub.getStudy(
                        getStudy12));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartgetStudy() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.GetStudy getStudy12=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.GetStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.GetStudy.class);
                    // TODO : Fill in the getStudy12 here
                

                stub.startgetStudy(
                         getStudy12,
                    new tempCallbackN65548()
                );
              


        }

        private class tempCallbackN65548  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65548(){ super(null);}

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

           org.psystems.dicom.webservice.DicomArchiveStub.FindStudies findStudies14=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudies)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudies.class);
                    // TODO : Fill in the findStudies14 here
                
                        assertNotNull(stub.findStudies(
                        findStudies14));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartfindStudies() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.FindStudies findStudies14=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.FindStudies)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.FindStudies.class);
                    // TODO : Fill in the findStudies14 here
                

                stub.startfindStudies(
                         findStudies14,
                    new tempCallbackN65586()
                );
              


        }

        private class tempCallbackN65586  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65586(){ super(null);}

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

           org.psystems.dicom.webservice.DicomArchiveStub.NewStudy newStudy16=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.NewStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.NewStudy.class);
                    // TODO : Fill in the newStudy16 here
                
                        assertNotNull(stub.newStudy(
                        newStudy16));
                  



        }
        
         /**
         * Auto generated test method
         */
        public  void testStartnewStudy() throws java.lang.Exception{
            org.psystems.dicom.webservice.DicomArchiveStub stub = new org.psystems.dicom.webservice.DicomArchiveStub();
             org.psystems.dicom.webservice.DicomArchiveStub.NewStudy newStudy16=
                                                        (org.psystems.dicom.webservice.DicomArchiveStub.NewStudy)getTestObject(org.psystems.dicom.webservice.DicomArchiveStub.NewStudy.class);
                    // TODO : Fill in the newStudy16 here
                

                stub.startnewStudy(
                         newStudy16,
                    new tempCallbackN65624()
                );
              


        }

        private class tempCallbackN65624  extends org.psystems.dicom.webservice.DicomArchiveCallbackHandler{
            public tempCallbackN65624(){ super(null);}

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
    