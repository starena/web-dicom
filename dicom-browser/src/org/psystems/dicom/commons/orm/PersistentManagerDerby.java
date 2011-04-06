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
				
				if (drn.getId() == null) { // делаем вставку
					sql = "INSERT INTO WEBDICOM.DIRECTION ("
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
							+") VALUES "
							+ "(?,?,?,?,?,?,?,?,?,?," +
								"?,?,?,?,?,?,?,?,?,?,?)";
					
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, drn.getDirectionId());
				
				if (drn.getDoctorDirect() != null)
					pstmt.setString(2, drn.getDoctorDirect().getEmployeName());
				else
					pstmt.setNull(2, java.sql.Types.VARCHAR);

				if (drn.getDoctorDirect() != null)
					pstmt.setString(3, drn.getDoctorDirect().getEmployeCode());
				else
					pstmt.setNull(3, java.sql.Types.VARCHAR);

				if (drn.getDiagnosisDirect() != null)
					pstmt.setString(4, Diagnosis
							.toPersistentCollectionString(drn
									.getDiagnosisDirect()));
				else
					pstmt.setNull(4, java.sql.Types.VARCHAR);

				if (drn.getDateDirection() != null)
					pstmt.setDate(5, java.sql.Date.valueOf(formatSQL.format(drn
							.getDateDirection())));
				else
					pstmt.setNull(5, java.sql.Types.DATE);

				if (drn.getServicesDirect() != null)
					pstmt.setString(6, Service.toPersistentCollectionString(drn
							.getServicesDirect()));
				else
					pstmt.setNull(6, java.sql.Types.VARCHAR);

				// TODO device Может сделать доп. поля ^...^ как в услугах?
				if (drn.getDevice() != null)
					pstmt.setString(7, drn.getDevice()
							.getManufacturerModelName());
				else
					pstmt.setNull(7, java.sql.Types.VARCHAR);

				if (drn.getDatePlanned() != null)
					pstmt.setDate(8, java.sql.Date.valueOf(formatSQL.format(drn
							.getDatePlanned())));
				else
					pstmt.setNull(8, java.sql.Types.DATE);

				if (drn.getDoctorPerformed() != null) {
					pstmt.setString(9, drn.getDoctorPerformed()
							.getEmployeName());
					pstmt.setString(10, drn.getDoctorPerformed()
							.getEmployeCode());
				} else {
					pstmt.setNull(9, java.sql.Types.VARCHAR);
					pstmt.setNull(10, java.sql.Types.VARCHAR);
				}

				pstmt.setString(11, drn.getDirectionCode());
				pstmt.setString(12, drn.getDirectionLocation());

				if (drn.getDiagnosisPerformed() != null)
					pstmt.setString(13, Diagnosis
							.toPersistentCollectionString(drn
									.getDiagnosisPerformed()));
				else
					pstmt.setNull(13, java.sql.Types.VARCHAR);

				if (drn.getServicesPerformed() != null)
					pstmt.setString(14, Service
							.toPersistentCollectionString(drn
									.getServicesPerformed()));
				else
					pstmt.setNull(14, java.sql.Types.VARCHAR);

				if (drn.getDatePerformed() != null)
					pstmt.setDate(15, java.sql.Date.valueOf(formatSQL
							.format(drn.getDatePerformed())));
				else
					pstmt.setNull(15, java.sql.Types.DATE);

				if (drn.getPatient() != null) {
					pstmt.setString(16, drn.getPatient().getPatientId());
					pstmt.setString(17, drn.getPatient().getPatientName());
					pstmt.setString(19, drn.getPatient().getPatientSex());
				} else {
					pstmt.setNull(16, java.sql.Types.VARCHAR);
					pstmt.setNull(17, java.sql.Types.VARCHAR);
					pstmt.setNull(19, java.sql.Types.VARCHAR);
				}
				
				if (drn.getPatient() != null && drn.getPatient().getPatientBirthDate() != null) {
					pstmt.setDate(18, java.sql.Date
							.valueOf(formatSQL.format(drn.getPatient()
									.getPatientBirthDate())));
				} else {
					pstmt.setNull(18, java.sql.Types.DATE);
				}

				pstmt.setDate(20, java.sql.Date.valueOf(formatSQL
						.format(new java.util.Date())));// sysdate

				if (drn.getDateRemoved() != null)
					pstmt.setDate(21, java.sql.Date.valueOf(formatSQL
							.format(drn.getDateRemoved())));
				else 
					pstmt.setNull(21, java.sql.Types.DATE);
				
				connection.setAutoCommit(false);
				int count = pstmt.executeUpdate();
				connection.commit();
				
				//TODO Не работает!!!
//				ResultSet keys = pstmt.getGeneratedKeys();
//				keys.next();
//				resultId = keys.getLong("ID");
				
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
