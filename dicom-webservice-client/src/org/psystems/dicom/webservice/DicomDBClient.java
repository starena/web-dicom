package org.psystems.dicom.webservice;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.psystems.dicom.webservice.DicomDBStub.FindStudies;
import org.psystems.dicom.webservice.DicomDBStub.FindStudiesResponse;
import org.psystems.dicom.webservice.DicomDBStub.FindStudy;
import org.psystems.dicom.webservice.DicomDBStub.GetStudy;
import org.psystems.dicom.webservice.DicomDBStub.StartTransaction;
import org.psystems.dicom.webservice.DicomDBStub.StartTransactionResponse;
import org.psystems.dicom.webservice.DicomDBStub.Study;

public class DicomDBClient {

        public static void main(String[] args) {
                try {
                	
                	DicomDBStub stub = new DicomDBStub();
                	
                	
                	DicomDBStub.GetStudy getstudy = new GetStudy();
                	getstudy.setI(11);
                	DicomDBStub.GetStudyResponse responce = stub.getStudy(getstudy);
                	Study result = responce.get_return();
                	
					System.out.println("RESULT : "+result.getId()+" = "+result.getDescription());
					
					DicomDBStub.FindStudies findstudies = new FindStudies();
					findstudies.setS("поиск searche");
					FindStudiesResponse responce1 = stub.findStudies(findstudies);
					Study[] result1 = responce1.get_return();
					
					for(int i = 0; i<result1.length; i++) {
						System.out.println(" R : "+result1[i].getId() + " = " + result1[i].getDescription());
					}
					
					DicomDBStub.StartTransaction transaction = new StartTransaction();
					Study sendedStudy = new Study();
					sendedStudy.setId(333);
					sendedStudy.setDescription("TTT Тест TTT");
					transaction.setStudy(sendedStudy );
					StartTransactionResponse transactionResponce = stub.startTransaction(transaction);
					int resultTrans = transactionResponce.get_return();
					System.out.println("RESULT : "+resultTrans);
					
					

                } catch (AxisFault e) {
                        e.printStackTrace();
                } catch (RemoteException e) {
                        e.printStackTrace();
                }

        }
}
