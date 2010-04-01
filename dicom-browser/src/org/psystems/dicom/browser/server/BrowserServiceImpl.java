/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.dicom.browser.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.StringUtils;
import org.dcm4che2.util.TagUtils;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.exception.VersionGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCRequest;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCResponce;
import org.psystems.dicom.browser.client.proxy.RPCDcmProxyEvent;
import org.psystems.dicom.browser.client.proxy.RPCRequestEvent;
import org.psystems.dicom.browser.client.proxy.RPCResponceEvent;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
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
	public RPCDcmProxyEvent findStudy(long transactionId, String version,
			String queryStr) throws DefaultGWTRPCException {

		checkVersion(version);// проверка версии клиента

		PreparedStatement psFiles = null;
		PreparedStatement psSelect = null;

		try {

			Connection connection = Util.getConnection(getServletContext());

			psFiles = connection
					.prepareStatement("SELECT * FROM WEBDICOM.DCMFILE WHERE FID_STUDY = ? ");

			psSelect = connection
					.prepareStatement("SELECT ID, STUDY_UID, PATIENT_ID, PATIENT_NAME, "
							+ " PATIENT_SEX, PATIENT_BIRTH_DATE, STUDY_ID,"
							+ " STUDY_DATE, STUDY_DOCTOR, STUDY_OPERATOR, STUDY_RESULT, STUDY_DESCRIPTION  FROM WEBDICOM.STUDY"
							+ " WHERE UPPER(PATIENT_NAME) like UPPER( ? || '%')"
							+ " order by PATIENT_NAME, STUDY_DATE ");

			psSelect.setString(1, queryStr);
			ResultSet rs = psSelect.executeQuery();

			ArrayList<StudyProxy> data = new ArrayList<StudyProxy>();
			int index = 0;
			while (rs.next()) {

				StudyProxy studyProxy = new StudyProxy();
				data.add(studyProxy);

				Calendar now = Calendar.getInstance();
				// Locale loc_ru = new Locale("ru", "RU");
				now.setTime(rs.getDate("PATIENT_BIRTH_DATE"));

				ArrayList<DcmTagProxy> tags = getTags(rs.getInt("ID"));
				// StringBuffer sb = new StringBuffer();
				String ManufacturerModelName = null;
				String studyType = null;
				String studyDescriptionDate = null;
				String studyViewprotocol = null;
				String studyResult = null;

				// Собираем теги
				for (Iterator<DcmTagProxy> iter = tags.iterator(); iter
						.hasNext();) {
					DcmTagProxy tag = iter.next();

					// TODO Это только для КРТ Электрон !!!!
					// Дата описания снимка
					if (tag.getMajor() == 21
							|| Integer.toHexString(tag.getMinor()).equals(
									"1110")) {
						studyDescriptionDate = tag.getTagValue();
					}

					// «Вид исследования» - 0029 1106+0029 1107
					if (tag.getMajor() == 29
							|| Integer.toHexString(tag.getMinor()).equals(
									"1106")) {
						studyType = tag.getTagValue();
					}

					if (tag.getMajor() == 29
							|| Integer.toHexString(tag.getMinor()).equals(
									"1107")) {
						studyType += " , " + tag.getTagValue();
					}

					if (tag.getMajor() == 21
							|| Integer.toHexString(tag.getMinor()).equals(
									"1103")) {
						studyResult = tag.getTagValue();
					}

					if (tag.getMajor() == 21
							|| Integer.toHexString(tag.getMinor()).equals(
									"1118")) {
						studyViewprotocol = tag.getTagValue();
					}

					if (tag.getIdTag() == Tag.ManufacturerModelName) {
						ManufacturerModelName = tag.getTagValue();
					}
				}

				studyProxy.init(rs.getLong("ID"), rs.getString("STUDY_UID"),
						ManufacturerModelName, rs.getString("PATIENT_NAME"), rs
								.getString("PATIENT_SEX"), rs
								.getString("PATIENT_ID"), rs
								.getDate("PATIENT_BIRTH_DATE"), rs
								.getString("STUDY_ID"), studyType, rs
								.getDate("STUDY_DATE"), studyDescriptionDate,
						rs.getString("STUDY_DOCTOR"), rs
								.getString("STUDY_OPERATOR"), rs
								.getString("STUDY_DESCRIPTION"),
						studyViewprotocol, studyResult);

				// Получаем список файлов
				psFiles.setLong(1, rs.getLong("ID"));
				ResultSet rsFiles = psFiles.executeQuery();

				ArrayList<DcmFileProxy> files = new ArrayList<DcmFileProxy>();
				while (rsFiles.next()) {

					DcmFileProxy dcmfileProxy = new DcmFileProxy();

					dcmfileProxy.init(rsFiles.getLong("ID"), rsFiles
							.getLong("FID_STUDY"), rsFiles.getString("TYPE"),
							rsFiles.getString("DCM_FILE_NAME"), rsFiles
									.getLong("DCM_FILE_SIZE"), rsFiles
									.getLong("IMAGE_FILE_SIZE"), rsFiles
									.getInt("IMAGE_WIDTH"), rsFiles
									.getInt("IMAGE_HEIGHT"));
					files.add(dcmfileProxy);
				}
				rsFiles.close();
				studyProxy.setFiles(files);

			}
			rs.close();

			// DcmFileProxyCortege[] result = data.toArray(new
			// DcmFileProxy[data.size()]);

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

			RPCDcmProxyEvent event = new RPCDcmProxyEvent();
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
				if (psFiles != null)
					psFiles.close();
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

		long idDcm = ((DcmTagsRPCRequest) reqEvent.getData()).getIdDcm();

		PreparedStatement psSelect = null;

		try {
			ArrayList<DcmTagProxy> data = getTags(idDcm);
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
			String version, long idDcmFile) throws DefaultGWTRPCException {

		checkVersion(version);// проверка версии клиента

		String dcmRootDir = getServletContext().getInitParameter(
				"webdicom.dir.src");
		PreparedStatement psSelect = null;

		try {
			String fileName = null;
			Connection connection = Util.getConnection(getServletContext());
			psSelect = connection.prepareStatement("SELECT ID,  DCM_FILE_NAME "
					+ " FROM WEBDICOM.DCMFILE WHERE ID = ? ");
			psSelect.setLong(1, idDcmFile);
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
	private ArrayList<DcmTagProxy> getTagsFromFile(long idDcm, String fileName)
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

			// DecimalFormat format = new DecimalFormat("0000");

			// Раскручиваем теги
			for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
				DicomElement element = it.next();

				int tag = element.tag();
				short major = (short) (tag >> 16);
				short minor = (short) (tag);

				StringBuffer sb = new StringBuffer();
				StringUtils.shortToHex(tag >> 16, sb);
				String majorStr = sb.toString();

				sb = new StringBuffer();
				StringUtils.shortToHex(tag, sb);
				String minorStr = sb.toString();

				String type = element.vr().toString();

				int length = element.length();
				int maxLength = 200;
				if (length > maxLength)
					length = maxLength;

				DcmTagProxy proxy = new DcmTagProxy();
				proxy.init(idDcm, tag, major, majorStr, minor, minorStr, type,
						dcmObj.nameOf(tag), element
								.getValueAsString(cs, length));
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

	/**
	 * Получение тегов
	 * 
	 * @param idDcm
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<DcmTagProxy> getTags(long idDcm) throws SQLException {

		PreparedStatement psSelect = null;

		try {
			Connection connection = Util.getConnection(getServletContext());
			psSelect = connection
					.prepareStatement("SELECT TAG, TAG_TYPE, VALUE_STRING FROM WEBDICOM.DCMFILE_TAG WHERE FID_DCMFILE = ?");

			psSelect.setLong(1, idDcm);
			ResultSet rs = psSelect.executeQuery();
			ArrayList<DcmTagProxy> data = new ArrayList<DcmTagProxy>();
			// DecimalFormat format = new DecimalFormat("0000");
			while (rs.next()) {
				DcmTagProxy proxy = new DcmTagProxy();

				int tag = rs.getInt("TAG");
				short major = (short) (tag >> 16);
				short minor = (short) (tag);

				StringBuffer sb = new StringBuffer();
				StringUtils.shortToHex(tag >> 16, sb);
				String majorStr = sb.toString();

				sb = new StringBuffer();
				StringUtils.shortToHex(tag, sb);
				String minorStr = sb.toString();

				// StringBuffer sb = new StringBuffer();
				// StringUtils.shortToHex(tag >> 16, sb);
				// String major = sb.toString();
				//
				// sb = new StringBuffer();
				// StringUtils.shortToHex(tag, sb);
				// String minor = sb.toString();

				proxy.init(idDcm, tag, major, majorStr, minor, minorStr, rs
						.getString("TAG_TYPE"), TagUtils.toString(rs
						.getInt("TAG")), rs.getString("VALUE_STRING"));
				data.add(proxy);
			}
			rs.close();
			return data;

		} finally {
			if (psSelect != null)
				psSelect.close();
		}

	}

}
