package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		long id = 0;
		

			try {
				
				if (drn.getId() == 0) { // делаем вставку
					sql = "INSERT ("
							+ "DIRECTION_ID,"
							+ "DOCTOR_DIRECT_NAME,"
							+ "DIAGNOSIS_DIRECT,"
							+ "DATE_DIRECTION,"
							+ "SERVICES_DIRECTED,"
							+ "DEVICE,"
							+ "DIRECTION_DATE_PLANNED,"
							+ "DOCTOR_PERFORMED_NAME,"
							+ "DIRECTION_CODE,"
							+ "DIRECTION_CABINET,"
							+ "DIAGNOSIS_PERFORMED,"
							+ "SERVICES_PERFORMED," 
							+ "DATE_PERFORMED," 
							+ "PATIENT_ID,"
							+ "PATIENT_NAME,"
							+ "PATIENT_BIRTH_DATE,"
							+ "PATIENT_SEX,"
							+ "DATE_MODIFIED,"
							+ "REMOVED"
							+") INTO WEBDICOM.DIRECTION VALUES"
							+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, drn.getDirectionId());
				pstmt.setString(2, drn.getDoctorDirect().toPersistentString());
				pstmt.setString(3, Diagnosis.toPersistentCollectionString(drn.getDiagnosisDirect()));

				connection.setAutoCommit(false);
				int count = pstmt.executeUpdate();
				ResultSet keys = pstmt.getGeneratedKeys();
				keys.next();
				id = keys.getLong("ID");
				connection.commit();
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
		return id;
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
