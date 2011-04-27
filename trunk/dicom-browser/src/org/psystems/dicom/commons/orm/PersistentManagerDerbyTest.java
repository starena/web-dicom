package org.psystems.dicom.commons.orm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

public class PersistentManagerDerbyTest extends TestCase {

	// private String dbPath = "test/junit/DB";
	private String dbPath = "memory:DB";
	private Connection connection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		connection = DriverManager.getConnection("jdbc:derby:" + dbPath
				+ ";create=true;drop=true");
		FileInputStream sqlInput = new FileInputStream(
				"../dicom-archive/database/derby/db.sql");

		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				// TODO Auto-generated method stub
			}
		};

		// создание/заливка инстанции БД
		org.apache.derby.tools.ij.runScript(connection, sqlInput, "UTF-8", out,
				"UTF-8");

		connection = DriverManager.getConnection("jdbc:derby:" + dbPath
				+ ";create=true;drop=true");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		connection.close();
	}

	/**
	 * Удаление папки
	 * 
	 * @param dir
	 * @return
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Получение "эталонного" направления
	 * 
	 * @return
	 */
	private Direction getNewOriginalDirection() {

		String randomDirectionId = "" + Math.random();
		Direction drn = new Direction();
		drn.setDateDirection("2011-04-13");

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

		drn.setDiagnosisDirect(diagnosisDirect
				.toArray(new Diagnosis[diagnosisDirect.size()]));

		drn.setDirectionCode("Test code");
		drn.setDirectionId(randomDirectionId);
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

		ArrayList<Service> serviceDirect = new ArrayList<Service>();

		Service srv = new Service();
		srv.setServiceCode("A.03.16.001.01");
		srv.setServiceAlias("ЭГДС");
		srv.setServiceDescription("Эзофагогастродуоденоскопия диагностическая");
		serviceDirect.add(srv);

		srv = new Service();
		srv.setServiceCode("A.02.12.002.02");
		srv.setServiceAlias("СМАД");
		srv
				.setServiceDescription("Суточное мониторирование артериального давления");
		serviceDirect.add(srv);

		drn.setServicesDirect(serviceDirect.toArray(new Service[serviceDirect
				.size()]));
		return drn;

	}

	/**
	 * Тест сохранение направления
	 */
	public void testMakePesistent() {
		try {
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			pm.pesistentDirection(getNewOriginalDirection());
		} catch (DataException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Тест получения эталоного Направления по ID
	 */
	public void testGetOriginalDirectionByID() {
		try {
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			Direction drnOriginal = getNewOriginalDirection();
			long id = pm.pesistentDirection(drnOriginal);

			Direction drn = pm.getDirectionByID(id);
			assertEquals(drnOriginal.getDirectionId(), drn.getDirectionId());

			// Diagnosis

			assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisCode(),
					drnOriginal.getDiagnosisDirect()[0].getDiagnosisCode());

			assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisType(),
					drnOriginal.getDiagnosisDirect()[0].getDiagnosisType());

			assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisSubType(),
					drnOriginal.getDiagnosisDirect()[0].getDiagnosisSubType());

			assertEquals(drn.getDiagnosisDirect()[0].getDiagnosisDescription(),
					drnOriginal.getDiagnosisDirect()[0]
							.getDiagnosisDescription());

			assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisCode(),
					drnOriginal.getDiagnosisDirect()[1].getDiagnosisCode());

			assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisType(),
					drnOriginal.getDiagnosisDirect()[1].getDiagnosisType());

			assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisSubType(),
					drnOriginal.getDiagnosisDirect()[1].getDiagnosisSubType());

			assertEquals(drn.getDiagnosisDirect()[1].getDiagnosisDescription(),
					drnOriginal.getDiagnosisDirect()[1]
							.getDiagnosisDescription());

			// Service

			assertEquals(drn.getServicesDirect()[0].getServiceAlias(),
					drnOriginal.getServicesDirect()[0].getServiceAlias());

			assertEquals(drn.getServicesDirect()[0].getServiceCode(),
					drnOriginal.getServicesDirect()[0].getServiceCode());

			assertEquals(drn.getServicesDirect()[0].getServiceDescription(),
					drnOriginal.getServicesDirect()[0].getServiceDescription());

			assertEquals(drn.getServicesDirect()[1].getServiceAlias(),
					drnOriginal.getServicesDirect()[1].getServiceAlias());

			assertEquals(drn.getServicesDirect()[1].getServiceCode(),
					drnOriginal.getServicesDirect()[1].getServiceCode());

			assertEquals(drn.getServicesDirect()[1].getServiceDescription(),
					drnOriginal.getServicesDirect()[1].getServiceDescription());

			connection.rollback();

		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Тест обновления направления
	 */
	public void testUpdateDirection() {

		try {

			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			Direction drnOriginal = getNewOriginalDirection();
			long id = pm.pesistentDirection(drnOriginal);

			Direction drn = pm.getDirectionByID(id);

			// Проверка обновления направления
			ArrayList<Diagnosis> diagnosisDirect = new ArrayList<Diagnosis>();
			Diagnosis dia = new Diagnosis();
			dia.setDiagnosisCode("K11.2");
			dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);// сопутствующий
			dia.setDiagnosisDescription("И Еще одно заболевание такое-то...");
			dia.setDiagnosisSubType("Непонятный");
			diagnosisDirect.add(dia);

			drn.setDiagnosisDirect(diagnosisDirect
					.toArray(new Diagnosis[diagnosisDirect.size()]));

			pm.pesistentDirection(drn);

			//
			Direction drn1 = (Direction) pm.getDirectionByID(drn.getId());
			assertEquals(drn1.getDiagnosisDirect().length, 1);
			assertEquals(drn1.getDiagnosisDirect()[0].getDiagnosisCode(), drn
					.getDiagnosisDirect()[0].getDiagnosisCode());
			assertEquals(drn1.getDiagnosisDirect()[0].getDiagnosisType(), drn
					.getDiagnosisDirect()[0].getDiagnosisType());
			assertEquals(drn1.getDiagnosisDirect()[0].getDiagnosisSubType(),
					drn.getDiagnosisDirect()[0].getDiagnosisSubType());
			assertEquals(
					drn1.getDiagnosisDirect()[0].getDiagnosisDescription(), drn
							.getDiagnosisDirect()[0].getDiagnosisDescription());

			// Проверка добавления диагноза
			Direction newDrn1 = pm.getDirectionByID(id);
			diagnosisDirect = new ArrayList<Diagnosis>();
			diagnosisDirect.addAll(Arrays.asList(newDrn1.getDiagnosisDirect()));

			dia = new Diagnosis();
			dia.setDiagnosisCode("K12.2");
			dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);// сопутствующий
			dia.setDiagnosisDescription("Заболевание K12.2");
			dia.setDiagnosisSubType("Непонятный");
			diagnosisDirect.add(dia);

			newDrn1.setDiagnosisDirect(diagnosisDirect
					.toArray(new Diagnosis[diagnosisDirect.size()]));
			pm.pesistentDirection(newDrn1);

			drn1 = (Direction) pm.getDirectionByID(newDrn1.getId());
			assertEquals(drn1.getDiagnosisDirect().length, 2);

			assertEquals(drn1.getDiagnosisDirect()[0].getDiagnosisCode(),
					newDrn1.getDiagnosisDirect()[0].getDiagnosisCode());
			assertEquals(drn1.getDiagnosisDirect()[0].getDiagnosisType(),
					newDrn1.getDiagnosisDirect()[0].getDiagnosisType());
			assertEquals(drn1.getDiagnosisDirect()[0].getDiagnosisSubType(),
					newDrn1.getDiagnosisDirect()[0].getDiagnosisSubType());
			assertEquals(
					drn1.getDiagnosisDirect()[0].getDiagnosisDescription(),
					newDrn1.getDiagnosisDirect()[0].getDiagnosisDescription());

			assertEquals(drn1.getDiagnosisDirect()[1].getDiagnosisCode(),
					"K12.2");
			assertEquals(drn1.getDiagnosisDirect()[1].getDiagnosisType(),
					newDrn1.getDiagnosisDirect()[0].getDiagnosisType());
			assertEquals(drn1.getDiagnosisDirect()[1].getDiagnosisSubType(),
					newDrn1.getDiagnosisDirect()[0].getDiagnosisSubType());
			assertEquals(
					drn1.getDiagnosisDirect()[1].getDiagnosisDescription(),
					"Заболевание K12.2");

			connection.rollback();

		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
