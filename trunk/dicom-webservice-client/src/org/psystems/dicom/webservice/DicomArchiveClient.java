package org.psystems.dicom.webservice;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.axis2.AxisFault;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudies;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.GetStudy;
import org.psystems.dicom.webservice.DicomArchiveStub.GetStudyResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.NewStudy;
import org.psystems.dicom.webservice.DicomArchiveStub.NewStudyResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.Study;

public class DicomArchiveClient {

	public static void main(String[] args) {
		try {

			testNewStudy();
			testGetStudy();
			findStudies();

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
	
	private static void findStudies() throws AxisFault, RemoteException {

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

	private static void testGetStudy() throws AxisFault, RemoteException {

		DicomArchiveStub stub = new DicomArchiveStub();
		DicomArchiveStub.GetStudy studySearchedId = new GetStudy();
		studySearchedId.setI(10);
		GetStudyResponse responceOfSearcheStudy = stub
				.getStudy(studySearchedId);
		Study findedStudy = responceOfSearcheStudy.get_return();
		printStudy(findedStudy);

	}

	

	private static void testNewStudy() throws AxisFault, RemoteException {
		DicomArchiveStub stub = new DicomArchiveStub();

		DicomArchiveStub.NewStudy newStudy = new NewStudy();
		newStudy.setPatient("Иванов Иван");
		newStudy.setPatientDateBirthday(Calendar.getInstance());
		newStudy.setPatientId("ИВАН123");
		newStudy.setPatientSex("M");
		newStudy.setStudyType("Рентгенография");

		NewStudyResponse responceOfNewStudy = stub.newStudy(newStudy);
		int newStudyId = responceOfNewStudy.get_return();
		System.out.println("newStudyId=" + newStudyId);
	}
	
	private static void printStudy(Study findedStudy) {
		System.out.println("findedStudy=" + findedStudy.getId() + ";"
				+ findedStudy.getStudyDescription() + ";"
				+ findedStudy.getStudyDevice() + ";"
				+ findedStudy.getStudyDoctor() + ";" + findedStudy.getStudyId()
				+ ";" + findedStudy.getStudyPatient() + ";"
				+ findedStudy.getStudyPatientId() + ";"
				+ findedStudy.getStudyResult() + ";"
				+ findedStudy.getStudyType() + ";" + findedStudy.getStudyUrl()
				+ ";" + findedStudy.getStudyDate());
	}
}
