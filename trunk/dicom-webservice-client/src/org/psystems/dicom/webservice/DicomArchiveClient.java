 package org.psystems.dicom.webservice;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.axis2.AxisFault;
import org.psystems.dicom.webservice.DicomArchiveStub.DicomArchive_Tag;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudies;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByType;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesByTypeResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.FindStudiesResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.GetManufacturersResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.GetStudy;
import org.psystems.dicom.webservice.DicomArchiveStub.GetStudyResponse;
import org.psystems.dicom.webservice.DicomArchiveStub.ManufacturerDevice;
import org.psystems.dicom.webservice.DicomArchiveStub.NewStudyUniversal;
import org.psystems.dicom.webservice.DicomArchiveStub.Study;

public class DicomArchiveClient {

	public static void main(String[] args) {
		try {

			try {
//				testNewStudy();
//				testGetStudy();
//				findStudies();
				
				String host = "http://localhost:8080/dicom-webservice";
//				String host = "http://localhost:38081/dicom-webservice2";
				
				if(args.length > 0) {
					host = args[0];
				}
				System.out.println("host is "+host);
//				findStudiesByType(host);
				
//				testGetDevices(host);
				testNewStudy(host);
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
	
	private static void findStudiesByType(String host) throws AxisFault, RemoteException, DicomWebServiceExceptionException0 {
		
		
	
		
		DicomArchiveStub stub = new DicomArchiveStub(host+"/services/DicomArchive" );
		
		FindStudiesByType query = new FindStudiesByType();
		
		query.setStudyModality("CR");
		
		
		query.setPatientName(null);
		query.setPatientShortName(null);
		query.setPatientSex(null);
		query.setPatientBirthDate("");
//		query.setPatientBirthDate("1978-12-14");
//		
//		
//		Calendar db = Calendar.getInstance();
//		db.set(2010, 1, 25); //2010-02-25
//		
////		db.set(1961, 3, 9); //1961-04-09
////		query.setPatientBirthDate(db);
//		
//		
		query.setBeginStudyDate("");
		query.setEndStudyDate("");
//		
//		
//		
		query.setPatientName("А%");
////		query.setPatientName("Иванов");
//		
		FindStudiesByTypeResponse responce = stub.findStudiesByType(query );
		Study[] result = responce.get_return();
		
		System.out.println("Finded Studies : " + result);
//		System.out.println("Calendar:"+Calendar.getInstance());
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

	private static void testGetDevices(String host) throws AxisFault, RemoteException, DicomWebServiceExceptionException0 {
		DicomArchiveStub stub = new DicomArchiveStub(host+"/services/DicomArchive" );
		GetManufacturersResponse responce = stub.getManufacturers();
		ManufacturerDevice[] result = responce.get_return();
		for(int i=0; i<result.length; i++) {
			System.out.println("!!! ManufacturerDevice="+result[i].getManufacturerModelName()+
					"; "+result[i].getManufacturerModelType()+ "; "+result[i].getManufacturerModelDescription()+"; "+
					result[i].getManufacturerModelTypeDescription());
		}

	
	}
	
	private static void testNewStudy(String host) throws AxisFault, RemoteException, DicomWebServiceExceptionException0 {
		DicomArchiveStub stub = new DicomArchiveStub(host+"/services/DicomArchive" );
		NewStudyUniversal query = new NewStudyUniversal();
		
		/*
		!!! FormFiled: 00100010=ДЕренок
		!!! FormFiled: 00100040=M
		!!! FormFiled: 00080090=
		!!! FormFiled: 00081070=
		!!! FormFiled: 00081030=
		!!! FormFiled: 00102000=123
		!!! FormFiled: 00324000=
		!!! FormFiled: content_type=image/jpg
		!!! UploadFile: upload;alg_shema.jpg;image/jpeg;true;65491
		!!! FormFiled: 0020000D=1.2.40.0.13.1.40452786674097928919318313426061085423
		!!! FormFiled: 0020000E=1.2.40.0.13.1.40452786674097928919318313426061085423.1288615271530
		!!! FormFiled: 00200010=
		!!! FormFiled: 00100021=
		!!! FormFiled: 00081090=ENDOSCP
		!!! FormFiled: 00080060=ES
		!!! FormFiled: 00100030=19740301
		!!! FormFiled: 00080020=20100917
		!!! FormFiled: 00321050=20101101
		*/
		ArrayList<DicomArchive_Tag> taglist = new ArrayList<DicomArchive_Tag>();
		
	
		maketag(taglist,"00100010","ДереноККК");
		maketag(taglist,"00100040","M");
		maketag(taglist,"00102000","result");
		maketag(taglist,"0020000D","1.2.40.0.13.1.40452786674097928919318313426061085423");
		maketag(taglist,"0020000E","1.2.40.0.13.1.40452786674097928919318313426061085423.1288615271531");
		maketag(taglist,"00081090","ENDOSCP");
		maketag(taglist,"00080060","ES");
		maketag(taglist,"00100030","19740302");
		maketag(taglist,"00080020","20100918");
		maketag(taglist,"00321050","20101102");
		
		DicomArchive_Tag[] tags = new DicomArchive_Tag[taglist.size()];
		tags = taglist.toArray(tags);
		
		query.setTags(tags);
		stub.newStudyUniversal(query);
	

	
	}

	private static void maketag(ArrayList<DicomArchive_Tag> taglist, String key, String val) {
		DicomArchive_Tag t = new DicomArchive_Tag();
		t.setTagName(key);
		t.setTagValue(val);
		taglist.add(t);
	}
	

//	private static void testNewStudy() throws AxisFault, RemoteException, DicomWebServiceExceptionException0 {
//		DicomArchiveStub stub = new DicomArchiveStub();
//
//		DicomArchiveStub.NewStudy newStudy = new NewStudy();
//		newStudy.setPatientName("Иванов Иван");
//		newStudy.setPatientDateBirthday(Calendar.getInstance());
//		newStudy.setPatientId("ИВАН123");
//		newStudy.setPatientSex("M");
//		newStudy.setStudyType("Рентгенография");
//
//		NewStudyResponse responceOfNewStudy = stub.newStudy(newStudy);
//		int newStudyId = responceOfNewStudy.get_return();
//		System.out.println("newStudyId=" + newStudyId);
//	}
	
	private static void printStudy(Study findedStudy) {
		
		if(findedStudy == null) return;
		
		SimpleDateFormat formatLevel = new SimpleDateFormat("yyyy-MM-dd_H-m-s.S");
//		String PatientBirthDate = formatLevel.format(calendar.getTime());
		
		
		System.out.println("findedStudy=" + findedStudy.getId() + ";"
				+ findedStudy.getStudyViewprotocol() + ";"
				+ findedStudy.getManufacturerModelName() + ";"
				+ findedStudy.getStudyDoctor() + ";" + findedStudy.getStudyId()
				+ ";" + findedStudy.getPatientName() + ";"
				+ ";" + findedStudy.getPatientShortName() + ";"
				+ findedStudy.getPatientId() + ";" +
				"PatientBirthDate="+findedStudy.getPatientBirthDateAsString() + 
//				" --- [" + findedStudy.getPatientBirthDate().getTime() +"] -- "+
//				"[" + findedStudy.getPatientBirthDate().getTime().getTime() +"] -- "+
				"{" + findedStudy.getPatientBirthDate() +"};"
				+"Sex="+findedStudy.getPatientSex()+"; "
				+ "StudyDateAsString=" + findedStudy.getStudyDateAsString() + ";"
				+ findedStudy.getStudyResult() + ";"
				+ findedStudy.getStudyType() + ";" + findedStudy.getStudyUrl());
				//+ ";" + findedStudy.getStudyDate());
	}
}
