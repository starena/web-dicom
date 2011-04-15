package org.psystems.dicom.commons.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

public class PersistentManagerDerbyTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		//TODO Сделать старт тестовой НЕСЕТЕВОЙ инстанции БД
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMakePesistent() {

		try {
			
			//TODO Сделать старт тестовой НЕСЕТЕВОЙ инстанции БД
			
			//TODO Доделать тест по всем полям и разить тест на несколько функций
			
			Properties props = new Properties(); // connection properties
			props.put("user", "user1"); // FIXME взять из конфига
			props.put("password", "user1"); // FIXME взять из конфига
			Connection connection;

			connection = DriverManager.getConnection(
					"jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM"
							+ ";create=true", props);

			PersistentManagerDerby pm = new PersistentManagerDerby(connection);

			String rndId = "" + Math.random();
			Direction drn = new Direction();
			drn.setDateDirection("2011-04-13");

			ManufacturerDevice device = new ManufacturerDevice();
			device.setManufacturerModelName("TestModel");
			device.setManufacturerModelDescription("Тестовый аппарат");
			device.setManufacturerModelType("ES");
			device.setManufacturerModelTypeDescription("Эндоскоп");
			drn.setDevice(device);


			Diagnosis dia = new Diagnosis();
			dia.setDiagnosisCode("M01.1");
			dia.setDiagnosisType(Diagnosis.TYPE_MAIN);// основной
			dia.setDiagnosisDescription("Заболевание такое-то...");
			dia.setDiagnosisSubType("Предварительный");

			Diagnosis dia1 = new Diagnosis();
			dia1.setDiagnosisCode("K01.1");
			dia1.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);// сопутствующий
			dia1.setDiagnosisDescription("Еще одно заболевание такое-то...");
			dia1.setDiagnosisSubType("Предварительный");

			drn.setDiagnosisDirect(new Diagnosis[] {dia,dia1});

			drn.setDirectionCode("Test code");
			drn.setDirectionId(rndId);
			drn.setDirectionLocation("605");

			Employee doctorDirect = new Employee();
			doctorDirect.setEmployeeCode("123");
			doctorDirect.setEmployeeName("Врач Петров И.И.");
			doctorDirect.setEmployeeType(Employee.TYPE_DOCTOR);
			drn.setDoctorDirect(doctorDirect);

			Patient patient = new Patient();
			

			patient.setPatientBirthDate("1974-03-01");
			
			patient.setPatientId("123123");
			patient.setPatientName("Иванов Иван Иванович");
			patient.setPatientSex("M");
			patient.setPatientShortName("ИВАИВ74");
			drn.setPatient(patient);

			Service srv = new Service();
			srv.setServiceCode("A.03.16.001.01");
			srv.setServiceAlias("ЭГДС");
			srv
					.setServiceDescription("Эзофагогастродуоденоскопия диагностическая");

			Service srv1 = new Service();
			srv1.setServiceCode("A.02.12.002.02");
			srv1.setServiceAlias("СМАД");
			srv1
					.setServiceDescription("Суточное мониторирование артериального давления");

			drn.setServicesDirect(new Service[] {srv,srv1});
			long newId = pm.makePesistent(drn);

			// **************************
			// Десериализация
			// *************************

			Direction newDrn = (Direction) pm.getObjectbyID(newId);
			assertEquals(rndId, newDrn.getDirectionId());

			// Diagnosis

			Diagnosis[] newDiaList = newDrn.getDiagnosisDirect();

			Diagnosis newDia = newDiaList[0];
			assertEquals(newDia.getDiagnosisCode(), dia.getDiagnosisCode());
			assertEquals(newDia.getDiagnosisType(), dia.getDiagnosisType());
			assertEquals(newDia.getDiagnosisSubType(), dia
					.getDiagnosisSubType());
			assertEquals(newDia.getDiagnosisDescription(), dia
					.getDiagnosisDescription());

			Diagnosis newDia1 = newDiaList[1];
			assertEquals(newDia1.getDiagnosisCode(), dia1.getDiagnosisCode());
			assertEquals(newDia1.getDiagnosisType(), dia1.getDiagnosisType());
			assertEquals(newDia1.getDiagnosisSubType(), dia1
					.getDiagnosisSubType());
			assertEquals(newDia1.getDiagnosisDescription(), dia1
					.getDiagnosisDescription());

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
			
			
			// Проверка обновления направления
			
//			newDia1 = new Diagnosis();
			newDia1.setDiagnosisCode("K11.2");
			newDia1.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);// сопутствующий
			newDia1.setDiagnosisDescription("И Еще одно заболевание такое-то...");
			newDia1.setDiagnosisSubType("Непонятный");
			
			pm.makePesistent(newDrn);
			
			Direction drn3 = (Direction) pm.getObjectbyID(newDrn.getId());
			Diagnosis[] diaList3 = drn3.getDiagnosisDirect();
			
			Diagnosis newDia3 = diaList3[1];
			assertEquals(newDia3.getDiagnosisCode(), newDia1.getDiagnosisCode());
			assertEquals(newDia3.getDiagnosisType(), newDia1.getDiagnosisType());
			assertEquals(newDia3.getDiagnosisSubType(), newDia1
					.getDiagnosisSubType());
			assertEquals(newDia3.getDiagnosisDescription(), newDia1
					.getDiagnosisDescription());


		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
