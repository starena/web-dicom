package org.psystems.dicom.commons.orm;

import java.io.Serializable;
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
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMakePesistent() {

		try {
			Properties props = new Properties(); // connection properties
			props.put("user", "user1"); // FIXME взять из конфига
			props.put("password", "user1"); // FIXME взять из конфига
			Connection connection;

			connection = DriverManager.getConnection(
					"jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM"
							+ ";create=true", props);

			PersistentManagerDerby pm = new PersistentManagerDerby(connection);

			String rndId = ""+Math.random();
			Direction drn = new Direction();
			drn.setDateDirection(new Date());

			ManufacturerDevice device = new ManufacturerDevice();
			device.setManufacturerModelName("TestModel");
			device.setManufacturerModelDescription("Тестовый аппарат");
			device.setManufacturerModelType("ES");
			device.setManufacturerModelTypeDescription("Эндоскоп");
			drn.setDevice(device);

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

			drn.setDiagnosisDirect(diagnosisDirect);

			drn.setDirectionCode("Test code");
			drn.setDirectionId(rndId);
			drn.setDirectionLocation("605");

			Employe doctorDirect = new Employe();
			doctorDirect.setEmployeCode("123");
			doctorDirect.setEmployeName("Врач Петров И.И.");
			doctorDirect.setEmployeType(Employe.TYPE_DOCTOR);
			drn.setDoctorDirect(doctorDirect);

			Patient patient = new Patient();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

			try {
				patient.setPatientBirthDate(format.parse("01-03-1974"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			patient.setPatientId("123123");
			patient.setPatientName("Иванов Иван Иванович");
			patient.setPatientSex("M");
			patient.setPatientShortName("ИВАИВ74");
			drn.setPatient(patient);

			ArrayList<Service> servicesDirect = new ArrayList<Service>();
			Service srv = new Service();
			srv.setServiceCode("A.03.16.001.01");
			srv.setServiceAlias("ЭГДС");
			srv
					.setServiceDescription("Эзофагогастродуоденоскопия диагностическая");
			servicesDirect.add(srv);

			srv = new Service();
			srv.setServiceCode("A.02.12.002.02");
			srv.setServiceAlias("СМАД");
			srv
					.setServiceDescription("Суточное мониторирование артериального давления");
			servicesDirect.add(srv);

			drn.setServicesDirect(servicesDirect);
			long newId = pm.makePesistent(drn);
			
			Direction newDrn = (Direction) pm.getObjectbyID(newId);
			assertEquals( rndId, newDrn.getDirectionId());
			
			

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
