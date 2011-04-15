package org.psystems.dicom.webservice;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
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
import org.psystems.dicom.webservice.WebDicomStub.QueryDirectionResponse;
import org.psystems.dicom.webservice.WebDicomStub.Service;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// testMakeDirection();
		// testgetdirectionById();
		// testgetdirectionBydirectionId();
		testqueryDirection();
	}

	private static void testqueryDirection() {
		try {
			WebDicomStub stub = new WebDicomStub();
			QueryDirectionE query = new QueryDirectionE();
			QueryDirection request = new QueryDirection();
			
			request.setPatientId("PATID1234");
			
			query.setQuery(request );
			Direction[] data = stub.queryDirection(query).get_return();
			for (Direction direction : data) {
				System.out.println("direction: "+direction);
			}

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

	private static void testgetdirectionBydirectionId() {
		try {
			WebDicomStub stub = new WebDicomStub();
			GetDirectionBydirectionId query = new GetDirectionBydirectionId();
			query.setDirectionId("DIRID12312345");

			System.out.println("direction = "
					+ stub.getDirectionBydirectionId(query).get_return()
							.getId());

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

	private static void testgetdirectionById() {

		try {
			WebDicomStub stub = new WebDicomStub();
			GetDirectionById query = new GetDirectionById();
			query.setId(33l);
			System.out.println("direction = "
					+ stub.getDirectionById(query).get_return()
							.getDirectionId());

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

	private static void testMakeDirection() {
		try {
			// host+"/services/WebDicom"
			WebDicomStub stub = new WebDicomStub();

			MakeDirection query = new MakeDirection();

			//
			query.setDirectionId("DIRID12312345");

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
			patient.setPatientName("Деренок Derenok");
			patient.setPatientId("PATID1234");
			patient.setPatientSex("M");
			patient.setPatientBirthDate("1974-03-01");
			query.setPatient(patient);

			System.out
					.println("!!!! " + stub.makeDirection(query).get_return());

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

}
