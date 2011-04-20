package org.psystems.dicom.webservice;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.psystems.dicom.webservice.WebDicomStub.Diagnosis;
import org.psystems.dicom.webservice.WebDicomStub.Direction;
import org.psystems.dicom.webservice.WebDicomStub.Employee;
import org.psystems.dicom.webservice.WebDicomStub.GetDirectionById;
import org.psystems.dicom.webservice.WebDicomStub.GetDirectionBydirectionId;
import org.psystems.dicom.webservice.WebDicomStub.MakeDirection;
import org.psystems.dicom.webservice.WebDicomStub.ManufacturerDevice;
import org.psystems.dicom.webservice.WebDicomStub.Patient;
import org.psystems.dicom.webservice.WebDicomStub.QueryDirection;
import org.psystems.dicom.webservice.WebDicomStub.QueryDirectionE;
import org.psystems.dicom.webservice.WebDicomStub.QueryStudy;
import org.psystems.dicom.webservice.WebDicomStub.QueryStudyE;
import org.psystems.dicom.webservice.WebDicomStub.Service;
import org.psystems.dicom.webservice.WebDicomStub.Study;

public class Test {

	private WebDicomStub stub;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new Test();
	}

	public Test() {

		String url = "/dicom-webservice2/services/WebDicom.WebDicomHttpSoap12Endpoint/";
		String host = "http://localhost:8080" + url;
//		 String host = "http://localhost:38081" + url;
//		 String host = "https://proxy.gp1.psystems.org:38081" + url;

		// Вывод отладочной информации по SSL-соединению
		// System.setProperty("javax.net.debug", "all");

		// "C:\Program Files\Java\jre6\bin\keytool.exe" -genkey -alias asu
		// -keystore client.jks
		// "C:\Program Files\Java\jre6\bin\keytool.exe" -list -v -keystore
		// client.jks
		// "C:\Program Files\Java\jre6\bin\keytool.exe" -keystore client.jks
		// -import -trustcacerts -alias proxy_gp1 -file proxy.cer

		System.setProperty("javax.net.ssl.keyStore", "client.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "derenok");
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStore", "client.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "derenok");

		try {
			stub = new WebDicomStub(host);

			// авторизация
			HttpTransportProperties.Authenticator basicAuth = new HttpTransportProperties.Authenticator();
			basicAuth.setUsername("dicomuser");
			basicAuth.setPassword("dicomtest");
			basicAuth.setPreemptiveAuthentication(true);
			final Options clientOptions = stub._getServiceClient().getOptions();
			clientOptions.setProperty(HTTPConstants.AUTHENTICATE, basicAuth);

//			 testMakeDirection();
			// testgetdirectionById();
			// testetdirectionBydirectionId();
			// testQueryDirection();
			// testFindStudiesByType();
			testQueryStudy();

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DicomWebServiceExceptionException0 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testQueryDirection() throws RemoteException,
			DicomWebServiceExceptionException0 {

		QueryDirectionE query = new QueryDirectionE();
		QueryDirection request = new QueryDirection();

		request.setPatientId("PATID1234");

		query.setQuery(request);
		Direction[] data = stub.queryDirection(query).get_return();
		if (data != null) {
			for (Direction direction : data) {
				System.out.println("direction: " + direction);
			}
		} else {
			System.out.println("No data");
		}
	}

	public void testgetdirectionBydirectionId() throws RemoteException,
			DicomWebServiceExceptionException0 {

		GetDirectionBydirectionId query = new GetDirectionBydirectionId();
		query.setDirectionId("DIRID12312345");

		System.out.println("direction = "
				+ stub.getDirectionBydirectionId(query).get_return().getId());

	}

	public void testgetdirectionById() throws RemoteException,
			DicomWebServiceExceptionException0 {

		GetDirectionById query = new GetDirectionById();
		query.setId(33l);
		System.out.println("direction = "
				+ stub.getDirectionById(query).get_return().getDirectionId());
	}

	public void testMakeDirection() throws RemoteException,
			DicomWebServiceExceptionException0 {

		MakeDirection query = new MakeDirection();

		//
		query.setDirectionId("88273");

		//

		Employee doctor = new Employee();
		doctor.setEmployeeName("Врач doctor1");
		query.setDoctorDirect(doctor);

		//
		Diagnosis dia1 = new Diagnosis();
		dia1.setDiagnosisCode("Z01");
		dia1.setDiagnosisDescription("Диагноз1");
		Diagnosis dia2 = new Diagnosis();

		dia2.setDiagnosisCode("Z02");
		dia2.setDiagnosisDescription("Диагноз2");
		query.setDiagnosisDirect(new Diagnosis[] { dia1, dia2 });

		//
		Service service1 = new Service();
		service1.setServiceCode("code1");
		service1.setServiceDescription("Описание1");

		Service service2 = new Service();
		service2.setServiceCode("code2");
		service2.setServiceDescription("Описание2");

		query.setServicesDirect(new Service[] { service1, service2 });

		//
		query.setDateDirection("2011-05-06");

		//
		ManufacturerDevice device = new ManufacturerDevice();
		device.setManufacturerModelType("CR");
		device.setManufacturerModelName("Электрон");
		query.setDevice(device);

		//
		query.setDatePlanned("2011-05-20");

		//
		query.setDirectionCode("CODE123");

		//
		query.setDirectionLocation("GP1-ROOM515");

		//
		Patient patient = new Patient();
		patient.setPatientName("Артемьева Наталья Александровна");
		patient.setPatientId("PATID1234");
		patient.setPatientSex("F");
		patient.setPatientBirthDate("1974-03-01");
		query.setPatient(patient);

		System.out.println("!!!! " + stub.makeDirection(query).get_return());

	}

	// public void testFindStudiesByType() throws AxisFault, RemoteException,
	// DicomWebServiceExceptionException0 {
	//
	// FindStudiesByType query = new FindStudiesByType();
	// query.setStudyModality("CR");
	// stub.findStudiesByType(query);
	//
	// }

	public void testQueryStudy() throws RemoteException,
			DicomWebServiceExceptionException0 {

		QueryStudyE query = new QueryStudyE();
		QueryStudy request = new QueryStudy();
		
		request.setId(1);
		request.setStudyId("123");
		
		request.setPatientId("123");
		request.setPatientName("1212");
		request.setPatientBirthDate("1974-03-01");
		request.setPatientShortName("SSSS");
		request.setPatientSex("F");//Работает.
		
		request.setStudyModality("US");
		
		request.setBeginStudyDate("2011-05-01");
		request.setEndStudyDate("2011-05-02");
		
	
		
		query.setQuery(request);
		Study[] studies = stub.queryStudy(query ).get_return();
		if(studies!=null) {
			for (Study study : studies) {
				System.out.println("!!! study="+study);
			}
		} else {
			System.out.println("No data");
		}
	}

}
