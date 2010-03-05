package org.psystems.dicom.browser.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.TagUtils;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.exception.VersionGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.DcmFileProxyCortege;
import org.psystems.dicom.browser.client.proxy.DcmImageProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCRequest;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCResponce;
import org.psystems.dicom.browser.client.proxy.RPCDcmFileProxyEvent;
import org.psystems.dicom.browser.client.proxy.RPCRequestEvent;
import org.psystems.dicom.browser.client.proxy.RPCResponceEvent;
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
	public RPCDcmFileProxyEvent findStudy(long transactionId, String version,
			String queryStr) throws DefaultGWTRPCException {

		checkVersion(version);// проверка версии клиента

		// System.out.println("BEGIN SLEEP");
		// try { //TODO Убрать!!!
		// Thread.sleep(25*1000);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		// System.out.println("END SLEEP");

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
							+ " STUDY_DATE, STUDY_DOCTOR,STUDY_OPERATOR, STUDY_DESCRIPTION  FROM WEBDICOM.DCMFILE"
							+ " WHERE UPPER(PATIENT_NAME) like UPPER( ? || '%')"
							+ " order by PATIENT_NAME ");

			psSelect.setString(1, queryStr);
			ResultSet rs = psSelect.executeQuery();
			
			ArrayList<DcmFileProxyCortege> data = new ArrayList<DcmFileProxyCortege>();
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
								.getString("STUDY_OPERATOR"), rs
								.getString("STUDY_DESCRIPTION"));

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

				DcmFileProxyCortege cortege = new DcmFileProxyCortege();
				cortege.init(proxy.getStudyId());
				
				ArrayList<DcmFileProxy> r = new ArrayList<DcmFileProxy>();
				r.add(proxy);
				cortege.setDcmProxies(r);
				
				
				data.add(cortege);
				if (index++ > maxReturnRecords) {
					break;
				}

			}
			rs.close();

//			DcmFileProxyCortege[] result = data.toArray(new DcmFileProxy[data.size()]);

			Calendar calendar = Calendar.getInstance();

			int tzoffset = calendar.getTimeZone().getOffset(
					calendar.getTimeInMillis());
			// System.out.println("!!!!! "+tzoffset );

			long time = calendar.getTimeInMillis();
			time = time - (time % (60 * 60 * 24 * 1000)) - tzoffset;
			calendar.setTimeInMillis(time);

			Date sqlDate = new java.sql.Date(time);
			updateDayStatInc(sqlDate, "CLIENT_CONNECTIONS", (long) 1);
			// System.out.println("!!! sqlDate="+sqlDate);

			RPCDcmFileProxyEvent event = new RPCDcmFileProxyEvent();
			event.init(transactionId, data);
			return event;

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
	 * Метод проверки версии клиента
	 * 
	 * @param version
	 * @throws VersionGWTRPCException
	 */
	private void checkVersion(String version) throws VersionGWTRPCException {
		if (!Util.checkClentVersion(version)) {
			throw new VersionGWTRPCException(
					"Версия клиента не совпадает с версией сервера! " + version
							+ " != " + Util.version);
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

		// Calendar calendar = Calendar.getInstance();
		// long time = calendar.getTimeInMillis();
		// time = time - (time % (60 * 60 * 24 * 1000));
		// // calendar.setTimeInMillis(time);

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

			// System.out.println("!!!! [U] [" + date + "][" + metric + "]="+
			// sumVal + " valueOld="+valueOld);

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

			// System.out.println("!!!! [I]  [" + date + "][" + metric + "]="+
			// value);
		}

	}

	/**
	 * Проверка на наличии этого файла в БД TODO Как-то заморочено
	 * получилось....
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

	@Override
	public RPCResponceEvent getDcmTags(RPCRequestEvent reqEvent)
			throws DefaultGWTRPCException {

		// проверка версии клиента
		if (!Util.checkClentVersion(reqEvent)) {
			throw new VersionGWTRPCException(
					"Версия клиента не совпадает с версией сервера! "
							+ reqEvent.getVersion() + " != " + Util.version);
		}

		int idDcm = ((DcmTagsRPCRequest) reqEvent.getData()).getIdDcm();

		PreparedStatement psSelect = null;

		try {
			Connection connection = Util.getConnection(getServletContext());
			psSelect = connection
					.prepareStatement("SELECT TAG, TAG_TYPE, VALUE_STRING FROM WEBDICOM.DCMFILE_TAGS WHERE FID_DCMFILE = ?");

			psSelect.setInt(1, idDcm);
			ResultSet rs = psSelect.executeQuery();
			ArrayList<DcmTagProxy> data = new ArrayList<DcmTagProxy>();
			DecimalFormat format = new DecimalFormat("0000");
			while (rs.next()) {
				DcmTagProxy proxy = new DcmTagProxy();

				int tag = rs.getInt("TAG");
				short ma = (short) (tag >> 16);
				short mi = (short) (tag);
				String major = format.format(ma);
				String minor = format.format(mi);

				proxy.init(idDcm, tag, major, minor, rs.getString("TAG_TYPE"),
						TagUtils.toString(rs.getInt("TAG")), rs
								.getString("VALUE_STRING"));
				data.add(proxy);

			}
			rs.close();
			DcmTagsRPCResponce responce = new DcmTagsRPCResponce();
			responce.setTagList(data);

			RPCResponceEvent respEvent = new RPCResponceEvent();
			respEvent.init(reqEvent.getTransactionId(), reqEvent.getVersion(),
					responce);
			return respEvent;

		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
				throw new DefaultGWTRPCException(e.getMessage());
			}
		}

	}

	@Override
	public ArrayList<DcmTagProxy> getDcmTagsFromFile(long transactionId,
			String version, int idDcmFile) throws DefaultGWTRPCException {

		checkVersion(version);// проверка версии клиента

		String dcmRootDir = getServletContext().getInitParameter(
				"webdicom.dir.src");
		PreparedStatement psSelect = null;

		try {
			String fileName = null;
			Connection connection = Util.getConnection(getServletContext());
			psSelect = connection.prepareStatement("SELECT ID,  DCM_FILE_NAME "
					+ " FROM WEBDICOM.DCMFILE WHERE ID = ? ");
			psSelect.setInt(1, idDcmFile);
			ResultSet rs = psSelect.executeQuery();
			int index = 0;
			while (rs.next()) {
				String file = rs.getString("DCM_FILE_NAME");
				fileName = dcmRootDir + File.separator + file;
				index++;
				break;
			}
			rs.close();
			if (index == 0) {
				throw new DefaultGWTRPCException("Dcm file not found! id="
						+ idDcmFile);
			}
			try {
				return getTagsFromFile(idDcmFile, fileName);
			} catch (IOException e) {
				throw new DefaultGWTRPCException("IOException ! id="
						+ idDcmFile + " file=" + fileName + " " + e);
			}

			//

			//
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
				throw new DefaultGWTRPCException(e.getMessage());
			}
		}

	}

	/**
	 * @param idDcm
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private ArrayList<DcmTagProxy> getTagsFromFile(int idDcm, String fileName)
			throws IOException {
		DicomObject dcmObj;
		DicomInputStream din = null;
		SpecificCharacterSet cs = new SpecificCharacterSet("ISO-8859-5");
		ArrayList<DcmTagProxy> data = new ArrayList<DcmTagProxy>();

		try {

			File f = new File(fileName);
			long fileSize = f.length();
			din = new DicomInputStream(f);
			dcmObj = din.readDicomObject();
			// System.out.println("dcmObj=" + dcmObj);

			// читаем кодировку из dcm-файла
			if (dcmObj.get(Tag.SpecificCharacterSet) != null) {
				SpecificCharacterSet cs1 = SpecificCharacterSet.valueOf(dcmObj
						.get(Tag.SpecificCharacterSet).getStrings(null, false));
			}

			DecimalFormat format = new DecimalFormat("0000");

			// Раскручиваем теги
			for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
				DicomElement element = it.next();

				int tag = element.tag();
				short ma = (short) (tag >> 16);
				String major = format.format(ma);
				short mi = (short) (tag);
				String minor = format.format(mi);

				String type = element.vr().toString();

				int length = element.length();
				int maxLength = 200;
				if (length > maxLength)
					length = maxLength;

				DcmTagProxy proxy = new DcmTagProxy();
				proxy.init(idDcm, tag, major, minor, type, dcmObj.nameOf(tag),
						element.getValueAsString(cs, length));
				data.add(proxy);

			}

		} catch (org.dcm4che2.data.ConfigurationError e) {
			if (e.getCause() instanceof UnsupportedEncodingException) {
				logger.fatal("Unsupported character set" + cs + " " + e);
			}
			logger.fatal("" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("" + e);
		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}
		return data;
	}

}
