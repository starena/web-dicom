package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class PersistentManagerDerby implements IPersistentManager {

	private Connection connection;

	/**
	 * @param connection
	 */
	public PersistentManagerDerby(Connection connection) {
		super();
		this.connection = connection;
	}

	// void Test() {
	// makePesistent(Direction.class);
	// }

	@Override
	public long makePesistent(Serializable obj) throws DataException {

		// сохранение направления
		if (obj instanceof Direction) {
			return makePesistentDirection((Direction) obj);
		}
		return 0;
		// TODO Auto-generated method stub

	}

	/**
	 * Сохранение направления
	 * 
	 * @param drn
	 * @throws DataException
	 */
	private long makePesistentDirection(Direction drn) throws DataException {

		PreparedStatement pstmt = null;
		String sql = null;
		SimpleDateFormat formatSQL = new SimpleDateFormat("yyyy-MM-dd");
		
		long resultId = 0;

			try {
				
				if (drn.getId() == 0) { // делаем вставку
					sql = "INSERT ("
							+ "DIRECTION_ID," //1
							+ "DOCTOR_DIRECT_NAME," //2
							
							+ "DOCTOR_DIRECT_CODE," //3
							
							+ "DIAGNOSIS_DIRECT," //4
							+ "DATE_DIRECTION," //5
							+ "SERVICES_DIRECTED," //6
							+ "DEVICE," //7
							+ "DIRECTION_DATE_PLANNED," //8
							+ "DOCTOR_PERFORMED_NAME," //9
							
							+ "DOCTOR_PERFORMED_CODE," //10
							
							+ "DIRECTION_CODE," //11
							+ "DIRECTION_LOCATION," //12
							+ "DIAGNOSIS_PERFORMED," //13
							+ "SERVICES_PERFORMED," //14
							+ "DATE_PERFORMED," //15
							+ "PATIENT_ID," //16
							+ "PATIENT_NAME," //17
							+ "PATIENT_BIRTH_DATE," //18
							+ "PATIENT_SEX," //19
							+ "DATE_MODIFIED," //20
							+ "REMOVED" //21
							+") INTO WEBDICOM.DIRECTION VALUES"
							+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, drn.getDirectionId());
				pstmt.setString(2, drn.getDoctorDirect().getEmployeName());
				pstmt.setString(3, drn.getDoctorDirect().getEmployeCode());
				pstmt.setString(4, Diagnosis.toPersistentCollectionString(drn.getDiagnosisDirect()));
				pstmt.setDate(5, java.sql.Date.valueOf(formatSQL.format(drn.getDateDirection())) );
				pstmt.setString(6, Service.toPersistentCollectionString(drn.getServicesDirect()));
				//TODO device Может сделать доп. поля ^...^ как в услугах?
				pstmt.setString(7, drn.getDevice().getManufacturerModelName());
				pstmt.setDate(8, java.sql.Date.valueOf(formatSQL.format(drn.getDatePlanned())) );
				pstmt.setString(9, drn.getDoctorPerformed().getEmployeName());
				pstmt.setString(10, drn.getDoctorPerformed().getEmployeCode());
				pstmt.setString(11, drn.getDirectionCode());
				pstmt.setString(12, drn.getDirectionLocation());
				pstmt.setString(13, Diagnosis.toPersistentCollectionString(drn.getDiagnosisPerformed()));
				pstmt.setString(14, Service.toPersistentCollectionString(drn.getServicesPerformed()));
				pstmt.setDate(15, java.sql.Date.valueOf(formatSQL.format(drn.getDatePerformed())) );
				pstmt.setString(16, drn.getPatient().getPatientId());
				pstmt.setString(17, drn.getPatient().getPatientName());
				pstmt.setDate(18, java.sql.Date.valueOf(formatSQL.format(drn.getPatient().getPatientBirthDate())) );
				pstmt.setString(19, drn.getPatient().getPatientSex());
				pstmt.setDate(20, java.sql.Date.valueOf(formatSQL.format(new java.util.Date())));// sysdate
				pstmt.setDate(18, java.sql.Date.valueOf(formatSQL.format(drn.getDateRemoved())));
				
				connection.setAutoCommit(false);
				int count = pstmt.executeUpdate();
				ResultSet keys = pstmt.getGeneratedKeys();
				keys.next();
				resultId = keys.getLong("ID");
				connection.commit();
			} else {
				//TODO Сделать корректную проверку на оюновление направления в БД.
				//по каким полям. по ID? или directionId?...
			}

				

			} catch (SQLException e) {
				throw new DataException(e);
			} finally {

				try {
					if (pstmt != null)
						pstmt.close();

				} catch (SQLException e) {
					throw new DataException(e);
				}
			}
		return resultId;
		// TODO Auto-generated method stub

	}

	@Override
	public Serializable getObjectbyID(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getObjectbyUID(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
