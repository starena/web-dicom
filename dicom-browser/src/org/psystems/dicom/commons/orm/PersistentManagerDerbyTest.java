package org.psystems.dicom.commons.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Properties;

import junit.framework.TestCase;

public class PersistentManagerDerbyTest extends TestCase {

	private Direction drnMaster;
	private long drnMasterID;
	
	private String rndId;
	
	private Diagnosis dia1;
	private Service srv;
	private Service srv1;
	private Connection connection;
	


	protected void setUp() throws Exception {
		super.setUp();
		//TODO Сделать старт тестовой НЕСЕТЕВОЙ инстанции БД
		
		Properties props = new Properties(); // connection properties
		props.put("user", "user1"); // FIXME взять из конфига
		props.put("password", "user1"); // FIXME взять из конфига

		connection = DriverManager.getConnection(
				"jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM"
						+ ";create=true", props);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMakePesistent() {

		try {
			
			//TODO Сделать старт тестовой НЕСЕТЕВОЙ инстанции БД
			
			//TODO Доделать тест по всем полям и разить тест на несколько функций
			
			

			PersistentManagerDerby pm = new PersistentManagerDerby(connection);

			rndId = "" + Math.random();
			drnMaster = new Direction();
			drnMaster.setDateDirection("2011-04-13");

			ManufacturerDevice device = new ManufacturerDevice();
			device.setManufacturerModelName("TestModel");
			device.setManufacturerModelDescription("Тестовый аппарат");
			device.setManufacturerModelType("ES");
			device.setManufacturerModelTypeDescription("Эндоскоп");
			drnMaster.setDevice(device);

			ArrayList<Diagnosis> diagnosisDirect = new ArrayList<Diagnosis>();

			Diagnosis dia = new Diagnosis();
			dia.setDiagnosisCode("M01.1");
			dia.setDiagnosisType(Diagnosis.TYPE_MAIN);// основной
			dia.setDiagnosisDescription("Заболевание такое-то...");
			dia.setDiagnosisSubType("Предварительный");
			diagnosisDirect.add(dia);

			dia = new Diagnosis();
			dia.setDiagnosisCode("K01.1");
			dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);// сопутствующий
			dia.setDiagnosisDescription("Еще одно заболевание такое-то...");
			dia.setDiagnosisSubType("Предварительный");
			diagnosisDirect.add(dia);

			drnMaster.setDiagnosisDirect(diagnosisDirect.toArray(new Diagnosis[diagnosisDirect.size()]));

			drnMaster.setDirectionCode("Test code");
			drnMaster.setDirectionId(rndId);
			drnMaster.setDirectionLocation("605");

			Employee doctorDirect = new Employee();
			doctorDirect.setEmployeeCode("123");
			doctorDirect.setEmployeeName("Врач Петров И.И.");
			doctorDirect.setEmployeeType(Employee.TYPE_DOCTOR);
			drnMaster.setDoctorDirect(doctorDirect);

			Patient patient = new Patient();
			

			patient.setPatientBirthDate("1974-03-01");
			
			patient.setPatientId("123123");
			patient.setPatientName("Иванов Иван Иванович");
			patient.setPatientSex("M");
			patient.setPatientShortName("ИВАИВ74");
			drnMaster.setPatient(patient);

			srv = new Service();
			srv.setServiceCode("A.03.16.001.01");
			srv.setServiceAlias("ЭГДС");
			srv
					.setServiceDescription("Эзофагогастродуоденоскопия диагностическая");

			srv1 = new Service();
			srv1.setServiceCode("A.02.12.002.02");
			srv1.setServiceAlias("СМАД");
			srv1
					.setServiceDescription("Суточное мониторирование артериального давления");

			drnMaster.setServicesDirect(new Service[] {srv,srv1});
			drnMasterID = pm.pesistentDirection(drnMaster);
			
			
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void testGetDirectionByID() {

		try {
			
			testMakePesistent();
			
			//TODO Сделать старт тестовой НЕСЕТЕВОЙ инстанции БД
			
			//TODO Доделать тест по всем полям и разить тест на несколько функций
			

			PersistentManagerDerby pm = new PersistentManagerDerby(connection);

						
			

			// **************************
			// Десериализация
			// *************************

			
			
			Direction newDrn = pm.getDirectionByID(drnMasterID);
			assertEquals(rndId, newDrn.getDirectionId());

			
			// Diagnosis

			
			assertEquals(newDrn.getDiagnosisDirect()[0].getDiagnosisCode(),
					drnMaster.getDiagnosisDirect()[0].getDiagnosisCode());

			assertEquals(newDrn.getDiagnosisDirect()[0].getDiagnosisType(),
					drnMaster.getDiagnosisDirect()[0].getDiagnosisType());

			assertEquals(newDrn.getDiagnosisDirect()[0].getDiagnosisSubType(),
					drnMaster.getDiagnosisDirect()[0].getDiagnosisSubType());

			assertEquals(newDrn.getDiagnosisDirect()[0]
					.getDiagnosisDescription(),
					drnMaster.getDiagnosisDirect()[0].getDiagnosisDescription());
			
			//
			
			assertEquals(newDrn.getDiagnosisDirect()[1].getDiagnosisCode(),
					drnMaster.getDiagnosisDirect()[1].getDiagnosisCode());

			assertEquals(newDrn.getDiagnosisDirect()[1].getDiagnosisType(),
					drnMaster.getDiagnosisDirect()[1].getDiagnosisType());

			assertEquals(newDrn.getDiagnosisDirect()[1].getDiagnosisSubType(),
					drnMaster.getDiagnosisDirect()[1].getDiagnosisSubType());

			assertEquals(newDrn.getDiagnosisDirect()[1]
					.getDiagnosisDescription(),
					drnMaster.getDiagnosisDirect()[1].getDiagnosisDescription());
			




			// Service

			Service[] newServicesDirect = newDrn.getServicesDirect();

			Service newSrv = newServicesDirect[0];
			assertEquals(newSrv.getServiceAlias(), srv.getServiceAlias());
			assertEquals(newSrv.getServiceCode(), srv.getServiceCode());
			assertEquals(newSrv.getServiceDescription(), srv
					.getServiceDescription());

			Service newSrv1 = newServicesDirect[1];
			assertEquals(newSrv1.getServiceAlias(), srv1.getServiceAlias());
			assertEquals(newSrv1.getServiceCode(), srv1.getServiceCode());
			assertEquals(newSrv1.getServiceDescription(), srv1
					.getServiceDescription());
			

		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testUpdateDirection() {

		try {

			testMakePesistent();

			// TODO Сделать старт тестовой НЕСЕТЕВОЙ инстанции БД

			PersistentManagerDerby pm = new PersistentManagerDerby(connection);

			Direction newDrn = pm.getDirectionByID(drnMasterID);
			
			System.out.println("!!! doctorPerformed:" + newDrn.getDoctorPerformed());

			// Проверка обновления направления
			ArrayList<Diagnosis> diagnosisDirect = new ArrayList<Diagnosis>();
			Diagnosis dia = new Diagnosis();
			dia.setDiagnosisCode("K11.2");
			dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);// сопутствующий
			dia.setDiagnosisDescription("И Еще одно заболевание такое-то...");
			dia.setDiagnosisSubType("Непонятный");

			newDrn.setDiagnosisDirect(diagnosisDirect
					.toArray(new Diagnosis[diagnosisDirect.size()]));

			pm.pesistentDirection(newDrn);

			Direction drn3 = (Direction) pm.getDirectionByID(newDrn.getId());
			Diagnosis[] diaList3 = drn3.getDiagnosisDirect();

			Diagnosis newDia3 = diaList3[1];
			assertEquals(newDia3.getDiagnosisCode(), dia.getDiagnosisCode());
			assertEquals(newDia3.getDiagnosisType(), dia.getDiagnosisType());
			assertEquals(newDia3.getDiagnosisSubType(), dia
					.getDiagnosisSubType());
			assertEquals(newDia3.getDiagnosisDescription(), dia
					.getDiagnosisDescription());

		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
