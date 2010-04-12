 package org.psystems.dicom.webservice;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.axis2.AxisFault;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudies;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByTypeResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.GetStudy;
import org.psystems.dicom.webservice.DicomArchiveStub.GetStudyResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.NewStudy;
import org.psystems.dicom.webservice.DicomArchiveStub.NewStudyResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.Study;

public class DicomArchiveClient {

	public static void main(String[] args) {
		try {

			try {
//				testNewStudy();
//				testGetStudy();
//				findStudies();
				findStudiesByType();
			} catch (DicomWebServiceExceptionException0 e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			// DicomDBStub stub = new DicomDBStub();
			//                	
			//                	
			// DicomDBStub.GetStudy getstudy = new GetStudy();
			// getstudy.setI(11);
			// DicomDBStub.GetStudyResponse responce = stub.getStudy(getstudy);
			// Study result = responce.get_return();
			//                	
			// System.out.println("RESULT : "+result.getId()+" = "+result.getDescription());
			//					
			// DicomDBStub.FindStudies findstudies = new FindStudies();
			// findstudies.setS("поиск searche");
			// FindStudiesResponse responce1 = stub.findStudies(findstudies);
			// Study[] result1 = responce1.get_return();
			//					
			// for(int i = 0; i<result1.length; i++) {
			// System.out.println(" R : "+result1[i].getId() + " = " +
			// result1[i].getDescription());
			// }
			//					
			// DicomDBStub.StartTransaction transaction = new
			// StartTransaction();
			// Study sendedStudy = new Study();
			// sendedStudy.setId(333);
			// sendedStudy.setDescription("TTT Тест TTT");
			// transaction.setStudy(sendedStudy );
			// StartTransactionResponse transactionResponce =
			// stub.startTransaction(transaction);
			// int resultTrans = transactionResponce.get_return();
			// System.out.println("RESULT : "+resultTrans);
			//					

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
	
	private static void findStudies() throws AxisFault, RemoteException, DicomWebServiceExceptionException0 {

		DicomArchiveStub stub = new DicomArchiveStub();
		DicomArchiveStub.FindStudies studySearchedString = new FindStudies();
		studySearchedString.setS("строка поиска");
		FindStudiesResponse responceOfSearcheStudies = stub.findStudies(studySearchedString);
		Study[] result = responceOfSearcheStudies.get_return();
		
		System.out.println("Finded Studies : " + result);
		 for(int i = 0; i<result.length; i++) {
			 printStudy(result[i]);
		 }
	}
	
	private static void findStudiesByType() throws AxisFault, RemoteException, DicomWebServiceExceptionException0 {

		DicomArchiveStub stub = new DicomArchiveStub("http://192.168.88.1:8080/dicom-webservice/services/DicomArchive" );
		
		FindStudiesByType query = new FindStudiesByType();
		query.setStudyType("fluoro");
		query.setPatientName("%");
//		query.setPatientName("Иванов");
		
		FindStudiesByTypeResponse responce = stub.findStudiesByType(query );
		Study[] result = responce.get_return();
		
		System.out.println("Finded Studies : " + result);
		if(result==null) return;
		 for(int i = 0; i<result.length; i++) {
			 printStudy(result[i]);
		 }
	}

	private static void testGetStudy() throws AxisFault, RemoteException, DicomWebServiceExceptionException0 {

		DicomArchiveStub stub = new DicomArchiveStub();
		DicomArchiveStub.GetStudy studySearchedId = new GetStudy();
		studySearchedId.setI(10);
		GetStudyResponse responceOfSearcheStudy = stub
				.getStudy(studySearchedId);
		Study findedStudy = responceOfSearcheStudy.get_return();
		printStudy(findedStudy);

	}

	

	private static void testNewStudy() throws AxisFault, RemoteException, DicomWebServiceExceptionException0 {
		DicomArchiveStub stub = new DicomArchiveStub();

		DicomArchiveStub.NewStudy newStudy = new NewStudy();
		newStudy.setPatientName("Иванов Иван");
		newStudy.setPatientDateBirthday(Calendar.getInstance());
		newStudy.setPatientId("ИВАН123");
		newStudy.setPatientSex("M");
		newStudy.setStudyType("Рентгенография");

		NewStudyResponse responceOfNewStudy = stub.newStudy(newStudy);
		int newStudyId = responceOfNewStudy.get_return();
		System.out.println("newStudyId=" + newStudyId);
	}
	
	private static void printStudy(Study findedStudy) {
		
		if(findedStudy == null) return;
		
		System.out.println("findedStudy=" + findedStudy.getId() + ";"
				+ findedStudy.getStudyViewprotocol() + ";"
				+ findedStudy.getManufacturerModelName() + ";"
				+ findedStudy.getStudyDoctor() + ";" + findedStudy.getStudyId()
				+ ";" + findedStudy.getPatientName() + ";"
				+ findedStudy.getPatientId() + ";"
				+ findedStudy.getStudyResult() + ";"
				+ findedStudy.getStudyType() + ";" + findedStudy.getStudyUrl()
				+ ";" + findedStudy.getStudyDate());
	}
}
