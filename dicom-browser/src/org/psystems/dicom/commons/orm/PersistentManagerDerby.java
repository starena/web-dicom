package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.commons.httpclient.methods.GetMethod;
import org.psystems.dicom.browser.client.component.WorkListPanel;

public class PersistentManagerDerby implements IPersistentManager {

	private Connection connection;
	//TODO Убрать в конфиг!!!
	private int maxResponsRecords = 300;//Максимальное количество возвращаемых записей
	
	
	

	/**
	 * @param connection
	 */
	public PersistentManagerDerby(Connection connection) {
		super();
		this.connection = connection;
	}

	
	@Override
	public long pesistentDirection(Serializable Direction) throws DataException {

		// сохранение направления
		if (Direction instanceof Direction) {
			return makePesistentDirection((Direction) Direction);
		}
		// сохранение направления
		if (Direction instanceof Study) {
			return makePesistentStudy((Study) Direction);
		}
		return 0;

	}

	/**
	 * Сохранение исследования
	 * TODO Не реализовано !!!
	 * 
	 * @param study
	 * @return
	 */
	private long makePesistentStudy(Study study) {
		// TODO Auto-generated method stub
		return 0;
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
		
		drn.chechEntity();
		
		if (drn.getPatient() == null) {
			throw new DataException("Patient could not be null.");
		} else {
			if (drn.getPatient().getPatientName() == null)
				throw new DataException("Patient Name could not be null.");
			if (drn.getPatient().getPatientSex() == null)
				throw new DataException("Patient Sex could not be null.");
			if (drn.getPatient().getPatientBirthDate() == null)
				throw new DataException("Patient Birth Date could not be null.");
			if (drn.getPatient().getPatientName() == null)
				throw new DataException("Patient Name could not be null.");
			
		}

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
					pstmt.setString(2, drn.getDoctorDirect().getEmployeeName());
				else
					pstmt.setNull(2, java.sql.Types.VARCHAR);

				if (drn.getDoctorDirect() != null)
					pstmt.setString(3, drn.getDoctorDirect().getEmployeeCode());
				else
					pstmt.setNull(3, java.sql.Types.VARCHAR);

				if (drn.getDiagnosisDirect() != null)
					pstmt.setString(4, Diagnosis
							.toPersistentCollectionString(drn
									.getDiagnosisDirect()));
				else
					pstmt.setNull(4, java.sql.Types.VARCHAR);

				if (drn.getDateDirection() != null)
					pstmt.setDate(5, java.sql.Date.valueOf(drn
							.getDateDirection()));
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

				try {
					if (drn.getDatePlanned() != null)
						pstmt.setDate(8, java.sql.Date.valueOf(drn
								.getDatePlanned()));
					else
						pstmt.setNull(8, java.sql.Types.DATE);

				} catch (IllegalArgumentException ex) {
					throw new DataException("field Date Planned wrong format: "
							+ drn.getDatePlanned(), ex);
				}

				if (drn.getDoctorPerformed() != null) {
					pstmt.setString(9, drn.getDoctorPerformed()
							.getEmployeeName());
					pstmt.setString(10, drn.getDoctorPerformed()
							.getEmployeeCode());
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

				try {
					if (drn.getDatePerformed() != null)
						pstmt.setDate(15, java.sql.Date.valueOf(drn
								.getDatePerformed()));
					else
						pstmt.setNull(15, java.sql.Types.DATE);

				} catch (IllegalArgumentException ex) {
					throw new DataException("field Date Performed wrong format: "
							+ drn.getDatePerformed(), ex);
				}


				if (drn.getPatient() != null) {
					pstmt.setString(16, drn.getPatient().getPatientId());
					pstmt.setString(17, drn.getPatient().getPatientName());
					pstmt.setString(19, drn.getPatient().getPatientSex());
				} else {
					pstmt.setNull(16, java.sql.Types.VARCHAR);
					pstmt.setNull(17, java.sql.Types.VARCHAR);
					pstmt.setNull(19, java.sql.Types.VARCHAR);
				}
				
				try {
					if (drn.getPatient() != null
							&& drn.getPatient().getPatientBirthDate() != null) {
						pstmt.setDate(18, java.sql.Date.valueOf(drn
								.getPatient().getPatientBirthDate()));
					} else {
						pstmt.setNull(18, java.sql.Types.DATE);
					}
				} catch (IllegalArgumentException ex) {
					throw new DataException(
							"Patient field Birth Date wrong format: "
									+ drn.getPatient().getPatientBirthDate(),
							ex);
				}

				//TODO проверить, нужно ли делать TIMESTAMP
				pstmt.setTimestamp(20, new Timestamp(new java.util.Date().getTime()));//sysdate
//				pstmt.setDate(20, java.sql.Date.valueOf(formatSQL
//						.format(new java.util.Date())));// sysdate

				try {
					if (drn.getDateRemoved() != null)
						pstmt.setTimestamp(21, new Timestamp(java.sql.Date
								.valueOf(drn.getDateRemoved()).getTime()));
					else
						pstmt.setNull(21, java.sql.Types.TIMESTAMP);
				} catch (IllegalArgumentException ex) {
					throw new DataException("field Date Removed wrong format: "
							+ drn.getDateRemoved(), ex);
				}
				
				connection.setAutoCommit(false);
				int count = pstmt.executeUpdate();
				
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("values IDENTITY_VAL_LOCAL()");
				ResultSetMetaData rsmd = rs.getMetaData();
				while (rs.next()) {
					
//					for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
//						String columnName = rsmd.getColumnName(i);
//						// Get the name of the column's table name
//						String tableName = rsmd.getTableName(i);
//						System.out.println("!!!!!!! ID = columnName [" + columnName + "]["+tableName+"]");
//					}

					resultId = rs.getLong("1");
//					System.out.println("!!!!!!! ID  [" + resultId + "]");
					
				}
				
				connection.commit();
				
			} else { //Делаем update
				
				
				//TODO Сделать корректную проверку на оюновление направления в БД.
				//по каким полям. по ID? или directionId?...
				
				sql = "UPDATE WEBDICOM.DIRECTION SET "
					+ "DIRECTION_ID = ?," //1
					+ "DOCTOR_DIRECT_NAME = ?," //2
					+ "DOCTOR_DIRECT_CODE = ?," //3
					+ "DIAGNOSIS_DIRECT = ?," //4
					+ "DATE_DIRECTION = ?," //5
					+ "SERVICES_DIRECTED = ?," //6
					+ "DEVICE = ?," //7
					+ "DIRECTION_DATE_PLANNED = ?," //8
					+ "DOCTOR_PERFORMED_NAME = ?," //9
					+ "DOCTOR_PERFORMED_CODE = ?," //10
					+ "DIRECTION_CODE = ?," //11
					+ "DIRECTION_LOCATION = ?," //12
					+ "DIAGNOSIS_PERFORMED = ?," //13
					+ "SERVICES_PERFORMED = ?," //14
					+ "DATE_PERFORMED = ?," //15
					+ "PATIENT_ID = ?," //16
					+ "PATIENT_NAME = ?," //17
					+ "PATIENT_BIRTH_DATE = ?," //18
					+ "PATIENT_SEX = ?," //19
					+ "DATE_MODIFIED = ?," //20
					+ "REMOVED = ?" //21
					+" WHERE ID = ?"; //22
			
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, drn.getDirectionId());

				if (drn.getDoctorDirect() != null)
					pstmt.setString(2, drn.getDoctorDirect().getEmployeeName());
				else
					pstmt.setNull(2, java.sql.Types.VARCHAR);

				if (drn.getDoctorDirect() != null)
					pstmt.setString(3, drn.getDoctorDirect().getEmployeeCode());
				else
					pstmt.setNull(3, java.sql.Types.VARCHAR);

				if (drn.getDiagnosisDirect() != null)
					pstmt.setString(4, Diagnosis
							.toPersistentCollectionString(drn
									.getDiagnosisDirect()));
				else
					pstmt.setNull(4, java.sql.Types.VARCHAR);

				try {
					if (drn.getDateDirection() != null)
						pstmt.setDate(5, java.sql.Date.valueOf(drn
								.getDateDirection()));
					else
						pstmt.setNull(5, java.sql.Types.DATE);

				} catch (IllegalArgumentException ex) {
					throw new DataException(
							"field Date Direction wrong format: "
									+ drn.getDateDirection(), ex);
				}

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

				try {
					if (drn.getDatePlanned() != null)
						pstmt.setDate(8, java.sql.Date.valueOf(drn
								.getDatePlanned()));
					else
						pstmt.setNull(8, java.sql.Types.DATE);
				} catch (IllegalArgumentException ex) {
					throw new DataException("field Date Planned wrong format: "
							+ drn.getDatePlanned(), ex);
				}

				if (drn.getDoctorPerformed() != null) {
					pstmt.setString(9, drn.getDoctorPerformed()
							.getEmployeeName());
					pstmt.setString(10, drn.getDoctorPerformed()
							.getEmployeeCode());
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

				try {
					if (drn.getDatePerformed() != null)
						pstmt.setDate(15, java.sql.Date.valueOf(drn
								.getDatePerformed()));
					else
						pstmt.setNull(15, java.sql.Types.DATE);
				} catch (IllegalArgumentException ex) {
					throw new DataException(
							"field Date Performed wrong format: "
									+ drn.getDatePlanned(), ex);
				}

				if (drn.getPatient() != null) {
					pstmt.setString(16, drn.getPatient().getPatientId());
					pstmt.setString(17, drn.getPatient().getPatientName());
					pstmt.setString(19, drn.getPatient().getPatientSex());
				} else {
					pstmt.setNull(16, java.sql.Types.VARCHAR);
					pstmt.setNull(17, java.sql.Types.VARCHAR);
					pstmt.setNull(19, java.sql.Types.VARCHAR);
				}

				try {
					if (drn.getPatient() != null
							&& drn.getPatient().getPatientBirthDate() != null) {
						pstmt.setDate(18, java.sql.Date.valueOf(drn
								.getPatient().getPatientBirthDate()));
					} else {
						pstmt.setNull(18, java.sql.Types.DATE);
					}
				} catch (IllegalArgumentException ex) {
					throw new DataException(
							"Patient field Birth Date wrong format: "
									+ drn.getPatient().getPatientBirthDate(),
							ex);
				}

				// TODO проверить, нужно ли делать TIMESTAMP
				pstmt.setTimestamp(20, new Timestamp(new java.util.Date()
						.getTime()));// sysdate
				// pstmt.setDate(20, java.sql.Date.valueOf(formatSQL
				// .format(new java.util.Date())));// sysdate

				try {
					if (drn.getDateRemoved() != null)
						pstmt.setTimestamp(21, new Timestamp(java.sql.Date
								.valueOf(drn.getDateRemoved()).getTime()));
					else
						pstmt.setNull(21, java.sql.Types.TIMESTAMP);
				} catch (IllegalArgumentException ex) {
					throw new DataException("field Date Removed wrong format: "
							+ drn.getDateRemoved(), ex);
				}
				
				pstmt.setLong(22, drn.getId());

				connection.setAutoCommit(false);
				int count = pstmt.executeUpdate();
				resultId = drn.getId();

				connection.commit();

			}

			} catch (SQLException e) {
				throw new DataException("SQL Error " + e.getMessage(),e);
			} catch (IllegalArgumentException e) {
				throw new DataException("Internal error "+ e.getMessage(),e);
			}
			finally {

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

	
	/**
	 * Получение экземпляра Направления из Record Set
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Direction getDirectionFromRs(ResultSet rs) throws SQLException {
		Direction drn = new Direction();
		drn.setId(rs.getLong("ID"));
		drn.setDirectionId(rs.getString("DIRECTION_ID"));
		Employee doctorDirect = new Employee();
		doctorDirect.setEmployeeCode(rs.getString("DOCTOR_DIRECT_CODE"));
		doctorDirect.setEmployeeName(rs.getString("DOCTOR_DIRECT_NAME"));
		doctorDirect.setEmployeeType(Employee.TYPE_DOCTOR);
		drn.setDoctorDirect(doctorDirect);
		drn.setDiagnosisDirect(Diagnosis.getCollectionFromPersistentString(rs
				.getString("DIAGNOSIS_DIRECT")));
		
		drn.setDateDirection(ORMUtil.utilDateToSQLDateString(rs.getDate("DATE_DIRECTION")));
		drn.setServicesDirect(Service.getCollectionFromPersistentString(rs
				.getString("SERVICES_DIRECTED")));
		ManufacturerDevice dev = new ManufacturerDevice();
		// TODO Остальные поля десериализовать?
		dev.setManufacturerModelName(rs.getString("DEVICE"));
		drn.setDevice(dev);
		drn.setDatePlanned(ORMUtil.utilDateToSQLDateString(rs.getDate("DIRECTION_DATE_PLANNED")) );

		if(rs.getString("DOCTOR_PERFORMED_NAME")!=null) {
			Employee doctorPerformed = new Employee();
			doctorPerformed.setEmployeeCode(rs.getString("DOCTOR_PERFORMED_CODE"));
			doctorPerformed.setEmployeeName(rs.getString("DOCTOR_PERFORMED_NAME"));
			drn.setDoctorPerformed(doctorPerformed);
		}
		
		drn.setDirectionCode(rs.getString("DIRECTION_CODE"));
		drn.setDirectionLocation(rs.getString("DIRECTION_LOCATION"));

		drn.setDiagnosisPerformed(Diagnosis
				.getCollectionFromPersistentString(rs
						.getString("DIAGNOSIS_PERFORMED")));
		drn.setServicesPerformed(Service.getCollectionFromPersistentString(rs
				.getString("SERVICES_PERFORMED")));
		
		drn.setDatePerformed(ORMUtil.utilDateToSQLDateString(rs.getDate("DATE_PERFORMED")));

		Patient patient = new Patient();
		patient.setPatientId(rs.getString("PATIENT_ID"));
		patient.setPatientName(rs.getString("PATIENT_NAME"));
		//TODO проверить, корректно ли возвращается дата !!!
		
		patient.setPatientBirthDate(ORMUtil.utilDateToSQLDateString(rs.getDate("PATIENT_BIRTH_DATE")));
		patient.setPatientSex(rs.getString("PATIENT_SEX"));
		drn.setPatient(patient);

		drn.setDateModified(ORMUtil.utilDateTimeToSQLDateTimeString(rs.getDate("DATE_MODIFIED")));
		drn.setDateRemoved(ORMUtil.utilDateTimeToSQLDateTimeString(rs.getDate("REMOVED")));

		return drn;
	}
	
	
	@Override
	public Direction getDirectionByID(Long id) throws DataException {
		
		Direction drn = new Direction();
		PreparedStatement pstmt = null;

		try {
			pstmt = connection
					.prepareStatement("SELECT * FROM WEBDICOM.DIRECTION WHERE ID = ? ");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				drn = getDirectionFromRs(rs);
			}
			
			return drn;
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
	}

	

	@Override
	public Direction getDirectionByDirectionId(String internalID)
			throws DataException {

		Direction drn = new Direction();
		PreparedStatement pstmt = null;

		try {
			pstmt = connection
					.prepareStatement("SELECT * FROM WEBDICOM.DIRECTION WHERE DIRECTION_ID = ? ");
			pstmt.setString(1, internalID);
			ResultSet rs = pstmt.executeQuery();
		
			while (rs.next()) {
				drn = getDirectionFromRs(rs);
			}
			return drn;
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
	}
	
	@Override
	public Study getStudyByUID(String uid)  throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Direction> queryDirections(QueryDirection request)
			throws DataException {

		if(request==null) throw new IllegalArgumentException("Request could not be null.");
		
		//проверяем валидность направления
		request.chechEntity();
		
		ArrayList<Direction> result = new ArrayList<Direction>();
		PreparedStatement pstmt = null;
		String sql = "SELECT * FROM WEBDICOM.DIRECTION WHERE ";

		try {

			// Разбор полей

			int counterArguments = 0;
			if (request.getId() != null) {
				if (counterArguments++ > 0)
					sql += " AND ";
				sql += " ID = ? ";
			}
			if (request.getDirectionId() != null) {
				if (counterArguments++ > 0)
					sql += " AND ";
				sql += " DIRECTION_ID = ? ";
			}
			if (request.getDateDirection() != null) {
				if (counterArguments++ > 0)
					sql += " AND ";
				sql += " DATE_DIRECTION = ? ";
			}
			if (request.getPatientBirthDate() != null) {
				if (counterArguments++ > 0)
					sql += " AND ";
				sql += " PATIENT_BIRTH_DATE = ? ";
			}
			if (request.getPatientId() != null) {
				if (counterArguments++ > 0)
					sql += " AND ";
				sql += " PATIENT_ID = ? ";
			}
			if (request.getPatientName() != null) {
				if (counterArguments++ > 0)
					sql += " AND ";
				sql += " PATIENT_NAME = ? ";
			}
			if (request.getPatientSex() != null) {
				if (counterArguments++ > 0)
					sql += " AND ";
				sql += " PATIENT_SEX = ? ";
			}
			
			if(counterArguments==0)
				throw new DataException("All query arguments empty! Set any argument's");
			
			
			pstmt = connection.prepareStatement(sql);
			counterArguments = 1;
			
			if (request.getId() != null) {
				pstmt.setLong(counterArguments++, request.getId());
			}
			if (request.getDirectionId() != null) {
				pstmt.setString(counterArguments++, request.getDirectionId());
			}
			if (request.getDateDirection() != null) {
				pstmt.setDate(counterArguments++,
						java.sql.Date.valueOf(request.getDateDirection()));
			}
			if (request.getPatientBirthDate() != null) {
				pstmt.setDate(counterArguments++,
						java.sql.Date.valueOf(request.getPatientBirthDate()));
			}
			if (request.getPatientId() != null) {
				pstmt.setString(counterArguments++, request.getPatientId());
			}
			if (request.getPatientName() != null) {
				pstmt.setString(counterArguments++, request.getPatientName());
			}
			if (request.getPatientSex() != null) {
				pstmt.setString(counterArguments++, request.getPatientSex());
			}
			
			// pstmt.setString(1, internalID);
			ResultSet rs = pstmt.executeQuery();
			int counter = 0;
			while (rs.next()) {
				result.add(getDirectionFromRs(rs));
				if(counter>maxResponsRecords) break;
			}
			return result;
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

	}


	/**
	 * Получение экземпляра исследования из Record Set
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private Study getStudyFromRs(ResultSet rs) throws SQLException {
		
		Study study = new StudyImplDerby();
		study.setId(rs.getLong("ID"));
		study.setStudyInstanceUID(rs.getString("STUDY_UID"));
		study.setStudyModality(rs.getString("STUDY_MODALITY"));
		study.setStudyType(rs.getString("STUDY_TYPE"));
		study.setStudyDescription(rs.getString("STUDY_DESCRIPTION"));
		study.setStudyDate(rs.getDate("STUDY_DATE"));
		study.setManufacturerModelUID(rs
				.getString("STUDY_MANUFACTURER_UID"));
		study.setManufacturerModelName(rs
				.getString("STUDY_MANUFACTURER_MODEL_NAME"));
		study.setStudyDoctor(rs.getString("STUDY_DOCTOR"));
		study.setStudyOperator(rs.getString("STUDY_OPERATOR"));
		study.setStudyViewprotocol(rs.getString("STUDY_VIEW_PROTOCOL"));
		study.setStudyViewprotocolDate(rs.getDate("STUDY_VIEW_PROTOCOL_DATE"));
		study.setStudyId(rs.getString("STUDY_ID"));
		study.setPatientName(rs.getString("PATIENT_NAME"));
		study.setPatientShortName(rs.getString("PATIENT_SHORTNAME"));
		study.setPatientSex(rs.getString("PATIENT_SEX"));
		study.setPatientBirthDate(rs.getDate("PATIENT_BIRTH_DATE"));
		study.setPatientId(rs.getString("PATIENT_ID"));
		study.setStudyResult(rs.getString("STUDY_RESULT"));
		study.setStudyUrl("");// TODO сделать!!
		study.setDcmFiles(new Long[] { 1l, 2l, 3l });// TODO сделать!!
		study.setStudyDateModify(rs.getTimestamp("DATE_MODIFIED"));
		
		study.setStudyDateRemoved(null);
		if(rs.getTimestamp("REMOVED")!=null)
		study.setStudyDateRemoved(rs.getTimestamp("REMOVED"));
		
		return study;
	}
	


	@Override
	public Study getStudyByID(Long id) throws DataException {
		PreparedStatement psSelect = null;
		String sql = "SELECT * FROM WEBDICOM.STUDY WHERE ID = ?";

		try {

			psSelect = connection.prepareStatement(sql);
			psSelect.setLong(1, id);
			ResultSet rs = psSelect.executeQuery();

			while (rs.next()) {
				return getStudyFromRs(rs);
			}
			rs.close();

		} catch (SQLException e) {
			throw new DataException(e);
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();

			} catch (SQLException e) {
				throw new DataException(e);
			}
		}
		return null;
	}
	


	@Override
	public ArrayList<Study> queryStudies(QueryStudy request)
			throws DataException {
		
		if(request==null) throw new IllegalArgumentException("Request could not be null.");
		
		//проверяем валидность направления
		request.chechEntity();
		
		PreparedStatement psSelect = null;

		// TODO Сделать ограничение на количество возвращаемых строк

		// "SELECT * FROM WEBDICOM.STUDY"
		// + " WHERE UPPER(PATIENT_NAME) like UPPER( ? || '%')"
		// + " order by PATIENT_NAME, STUDY_DATE "

		String sqlAddon = "";

		
		if (request.getId() != null) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " ID = ? ";
		}
		
		if (request.getStudyId() != null && request.getStudyId().length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " STUDY_ID = ? ";
		}
		
		if (request.getStudyModality() != null && request.getStudyModality().length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " STUDY_MODALITY = ? ";
		}
		
		if (request.getManufacturerModelName() != null && request.getManufacturerModelName().length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " STUDY_MANUFACTURER_MODEL_NAME = ? ";
		}

		
		
		if (request.getPatientId() != null && request.getPatientId().length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " UPPER(PATIENT_ID) like UPPER( ? || '%') ";
		}
		
		if (request.getPatientName() != null && request.getPatientName().length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " UPPER(PATIENT_NAME) like UPPER( ? || '%') ";
		}
		
		
		if (request.getPatientShortName() != null && request.getPatientShortName().length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " PATIENT_SHORTNAME = UPPER(?) ";
		}

		if (request.getPatientBirthDate() != null && request.getPatientBirthDate().length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " PATIENT_BIRTH_DATE = ? ";
		}

		if (request.getPatientSex() != null && request.getPatientSex().length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " PATIENT_SEX = ? ";
		}

		if (request.getBeginStudyDate() != null && request.getBeginStudyDate().length() > 0
				&& request.getEndStudyDate() != null && request.getEndStudyDate().length() > 0) {

			//TODO Вынести в chekEntity
			if (java.sql.Date.valueOf(request.getBeginStudyDate()).getTime() > java.sql.Date
					.valueOf(request.getEndStudyDate()).getTime()) {
				throw new DataException(new IllegalArgumentException(
						"beginStudyDate > endStudyDate"));
			}

			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " STUDY_DATE BETWEEN ? AND ? ";
		} else {

			if (request.getBeginStudyDate() != null && request.getBeginStudyDate().length() > 0
					&& (request.getEndStudyDate() == null || request.getEndStudyDate().length() > 0)) {
				if (sqlAddon.length() != 0)
					sqlAddon += " AND ";
				sqlAddon += " STUDY_DATE > ? ";
			}

			if (request.getEndStudyDate() != null && request.getEndStudyDate().length() > 0
					&& (request.getBeginStudyDate() == null || request.getBeginStudyDate().length() > 0)) {
				if (sqlAddon.length() != 0)
					sqlAddon += " AND ";
				sqlAddon += " STUDY_DATE < ?  ";
			}
			
			
		}
		
		
		//TODO странная конструкция. пересмотреть..сделать isOld или isNew...
		if (request.getStudyResult() != null && request.getStudyResult().length() > 0) {
			if (sqlAddon.length() != 0)
				
			if(request.getStudyResult().equals("new")) {
				sqlAddon += " AND ";
				sqlAddon += " ( STUDY_VIEW_PROTOCOL is NULL OR STUDY_VIEW_PROTOCOL = '' ) ";
			}
			if(request.getStudyResult().equals("old")) {
				sqlAddon += " AND ";
				sqlAddon += " ( STUDY_VIEW_PROTOCOL IS NOT NULL AND STUDY_VIEW_PROTOCOL != '' )";
			}
			
		}
		String order = "PATIENT_NAME, STUDY_DATE";
		if(request.getSortOrder()!=null) {
			order = request.getSortOrder();
		}

		String sql = "SELECT * FROM WEBDICOM.STUDY" + " WHERE " + sqlAddon
				+ " order by "+order;


	
		
		IllegalArgumentException ex = new IllegalArgumentException(
				"All query arguments empty! Set any argument's");
		if (sqlAddon.length() == 0)
			throw new DataException(ex);

		try {

			psSelect = connection.prepareStatement(sql);
			int index = 1;
			
			if (request.getId() != null) {
				psSelect.setLong(index++, request.getId());
			}

			if (request.getStudyId() != null && request.getStudyId().length() > 0) {
				psSelect.setString(index++, request.getStudyId());
			}

			if (request.getStudyModality() != null && request.getStudyModality().length() > 0) {
				psSelect.setString(index++, request.getStudyModality());
			}
			
			if (request.getManufacturerModelName() != null && request.getManufacturerModelName().length() > 0) {
				psSelect.setString(index++, request.getManufacturerModelName());
			}
			
			if (request.getPatientId() != null && request.getPatientId().length() > 0) {
				psSelect.setString(index++, request.getPatientId());
			}

			if (request.getPatientName() != null && request.getPatientName().length() > 0) {
				psSelect.setString(index++, request.getPatientName());
			}

			if (request.getPatientShortName() != null && request.getPatientShortName().length() > 0) {
				psSelect.setString(index++, request.getPatientShortName());
			}

			if (request.getPatientBirthDate() != null && request.getPatientBirthDate().length() > 0) {
				psSelect.setDate(index++, java.sql.Date
						.valueOf(request.getPatientBirthDate()));
			}

			if (request.getPatientSex() != null && request.getPatientSex().length() > 0) {
				psSelect.setString(index++, request.getPatientSex());
			}

			if (request.getBeginStudyDate() != null && request.getBeginStudyDate().length() > 0
					&& request.getEndStudyDate() != null && request.getEndStudyDate().length() > 0) {
				psSelect
						.setDate(index++, java.sql.Date.valueOf(request.getBeginStudyDate()));
				psSelect.setDate(index++, java.sql.Date.valueOf(request.getEndStudyDate()));
			}else {
				
				if (request.getBeginStudyDate() != null && request.getBeginStudyDate().length() > 0
						&& (request.getEndStudyDate() == null || request.getEndStudyDate().length() > 0)) {
					psSelect
					.setDate(index++, java.sql.Date.valueOf(request.getBeginStudyDate()));
				}

				if (request.getEndStudyDate() != null && request.getEndStudyDate().length() > 0
						&& (request.getBeginStudyDate() == null || request.getBeginStudyDate().length() > 0)) {
					psSelect
					.setDate(index++, java.sql.Date.valueOf(request.getEndStudyDate()));
				}
			}
			
			
		

			ResultSet rs = psSelect.executeQuery();

			ArrayList<Study> data = new ArrayList<Study>();

			
			int counter = 1;
			while (rs.next()) {
				//TODO убрать в конфиг
				if(counter++> WorkListPanel.maxResultCount) break;
				data.add(getStudyFromRs(rs));
			}
			rs.close();

			return data;

		} catch (SQLException e) {
			throw new DataException(e);
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();

			} catch (SQLException e) {
				throw new DataException(e);
			}
		}
	}
	
}
