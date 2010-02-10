package org.psystems.dicom.browser.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.exception.VersionGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.DcmImageProxy;
import org.psystems.dicom.browser.client.service.BrowserService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BrowserServiceImpl extends RemoteServiceServlet implements
		BrowserService {

	private int maxReturnRecords = 20; // Максимальное количество возвращаемых
	// записей

	private static Logger logger = Logger.getLogger(BrowserServiceImpl.class);

	// static {
	// PropertyConfigurator.configure("WEB-INF/log4j.properties");}//TODO Убрать
	// !!!

	@Override
	public DcmFileProxy[] findStudy(String version, String queryStr)
			throws DefaultGWTRPCException {

		// проверка версии клиента
		if (!Util.checkClentkVersion(version)) {
			throw new VersionGWTRPCException(
					"Версия клиента не совпадает с версией сервера! " + version
							+ " != " + Util.version);
		}

		PreparedStatement psSelect = null;
		PreparedStatement psImages = null;

		try {

			Connection connection = Util.getConnection(getServletContext());
			//

			psImages = connection
					.prepareStatement("SELECT ID, CONTENT_TYPE, IMAGE_FILE_NAME, WIDTH, HEIGHT "
							+ " FROM WEBDICOM.IMAGES WHERE FID_DCMFILE = ? ");

			//
			psSelect = connection
					.prepareStatement("SELECT ID, DCM_FILE_NAME, PATIENT_ID, PATIENT_NAME, "
							+ " PATIENT_SEX, PATIENT_BIRTH_DATE, STUDY_ID,"
							+ " STUDY_DATE, STUDY_DOCTOR,STUDY_OPERATOR  FROM WEBDICOM.DCMFILE"
							+ " WHERE UPPER(PATIENT_NAME) like UPPER( ? || '%')"
							+ " order by PATIENT_NAME ");

			psSelect.setString(1, queryStr);
			ResultSet rs = psSelect.executeQuery();
			ArrayList<DcmFileProxy> data = new ArrayList<DcmFileProxy>();
			int index = 0;
			while (rs.next()) {
				DcmFileProxy proxy = new DcmFileProxy();

				Calendar now = Calendar.getInstance();
				Locale loc_ru = new Locale("ru", "RU");
				now.setTime(rs.getDate("PATIENT_BIRTH_DATE"));

				proxy.init(rs.getInt("ID"), rs.getString("DCM_FILE_NAME"), rs
						.getString("PATIENT_NAME"),
						rs.getString("PATIENT_SEX"),
						rs.getString("PATIENT_ID"), rs
								.getDate("PATIENT_BIRTH_DATE"), rs
								.getString("STUDY_ID"), rs
								.getDate("STUDY_DATE"), rs
								.getString("STUDY_DOCTOR"), rs
								.getString("STUDY_OPERATOR"));

				// Получаем список картинок
				psImages.setInt(1, rs.getInt("ID"));
				ResultSet rsImages = psImages.executeQuery();

				ArrayList<DcmImageProxy> images = new ArrayList<DcmImageProxy>();
				while (rsImages.next()) {
					int imageId = rsImages.getInt("ID");
					String contentType = rsImages.getString("CONTENT_TYPE");
					String imFileName = rsImages.getString("IMAGE_FILE_NAME");
					int width = rsImages.getInt("WIDTH");
					int height = rsImages.getInt("HEIGHT");

					DcmImageProxy imageProxy = new DcmImageProxy();
					imageProxy.init(imageId, imFileName, contentType, width,
							height);
					images.add(imageProxy);
				}
				rsImages.close();
				proxy.setImagesIds(images);

				data.add(proxy);
				if (index++ > maxReturnRecords) {
					break;
				}

			}
			rs.close();

			DcmFileProxy[] result = data.toArray(new DcmFileProxy[data.size()]);
			
			
			Calendar calendar = Calendar.getInstance();
	        
	        int tzoffset = calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
//			System.out.println("!!!!! "+tzoffset );
	        
	        long time = calendar.getTimeInMillis();
			time = time - (time % (60 * 60 * 24 * 1000))- tzoffset;
			calendar.setTimeInMillis(time);
			
			Date sqlDate = new java.sql.Date(time);
			updateDayStatInc(sqlDate,"CLIENT_CONNECTIONS",(long)1);
//			System.out.println("!!! sqlDate="+sqlDate);
			
			return result;

		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
				if (psImages != null)
					psImages.close();
			} catch (SQLException e) {
				logger.error(e);
				throw new DefaultGWTRPCException(e.getMessage());
			}
		}

	}
	
	/**
	 * Обновление метрики дневной статистики (инкремент)
	 * 
	 * @param date
	 * @param metric
	 * @param value
	 * @throws SQLException
	 */
	private void updateDayStatInc(Date date, String metric, long value)
			throws SQLException {

		Connection connection = Util.getConnection(getServletContext());
		
		PreparedStatement stmt = null;
		
//		Calendar calendar = Calendar.getInstance();
//		long time = calendar.getTimeInMillis();
//		time = time - (time % (60 * 60 * 24 * 1000));
//		// calendar.setTimeInMillis(time);
		
		long time = date.getTime();
		time = time - (time % (60 * 60 * 24 * 1000));
		date = new Date(time);

		logger.info(metric + "=" + value + " of " + date);

		// Проверка на наличии этого файла в БД
		try {
			long valueOld = checkDayMetric(metric, date);
			logger.info("metric already in database [" + metric + "][" + date
					+ "][" + valueOld + "]");

			stmt = connection.prepareStatement("update WEBDICOM.DAYSTAT "
					+ " SET METRIC_VALUE_LONG = ? "
					+ " where METRIC_NAME = ? AND METRIC_DATE = ?");

			long sumVal = value + valueOld;
			stmt.setLong(1, sumVal);
			stmt.setString(2, metric);
			stmt.setDate(3, date);
			stmt.executeUpdate();
			
//			System.out.println("!!!! [U] [" + date + "][" + metric + "]="+ sumVal + " valueOld="+valueOld);

		} catch (NoDataFoundException ex) {
			// Делаем вставку
			logger.info("insert data in database [" + metric + "][" + date
					+ "][" + value + "]");
			stmt = connection.prepareStatement("insert into WEBDICOM.DAYSTAT "
					+ " (METRIC_NAME, METRIC_DATE, METRIC_VALUE_LONG)"
					+ " values (?, ?, ?)");

			stmt.setString(1, metric);
			stmt.setDate(2, date);
			stmt.setLong(3, value);
			stmt.executeUpdate();
			
//			System.out.println("!!!! [I]  [" + date + "][" + metric + "]="+ value);
		}

	}

	/**
	 * Проверка на наличии этого файла в БД
	 * 
	 * @param dcm_file_name
	 * @return
	 * @throws SQLException
	 */
	private long checkDayMetric(String metric, Date date) throws SQLException {
		
		Connection connection = Util.getConnection(getServletContext());
		
		PreparedStatement psSelect = connection
				.prepareStatement("SELECT METRIC_VALUE_LONG FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ? and METRIC_DATE =? ");
		try {
			psSelect.setString(1, metric);
			psSelect.setDate(2, date);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getLong("METRIC_VALUE_LONG");
			}

		} finally {
			psSelect.close();
		}
		throw new NoDataFoundException("No data");
	}

}
