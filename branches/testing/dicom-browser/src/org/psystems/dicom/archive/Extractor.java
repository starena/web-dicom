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
package org.psystems.dicom.archive;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.filecache.FileCache;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.net.Association;
import org.dcm4che2.util.CloseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class Extractor {

	private static Logger LOG = LoggerFactory.getLogger(Extractor.class);

	private FileCache cache;

	public static String dcmFileExt = ".dcm"; // Расширение для сохраняемых
	// файлов
	public static String dcmFileBacupExt = ".bak"; // Расширение для сохраняемых

	public static String imageDirPrefix = ".images";

	static String connectionStr = "jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM";

	public static String configStr = "dcmarcive-conf.xml";
	private Connection connection;// соединение с БД

	private int frame = 1;
	private float center;
	private float width;
	private String vlutFct;
	private boolean autoWindowing;
	private DicomObject prState;
	private short[] pval2gray;
	private String imageFileExt = ".jpg";

	private String imageContentType = "image/jpeg";

	public Extractor(FileCache cache) {
		super();
		this.cache = cache;
		// TODO Auto-generated constructor stub
	}

	/**
	 * Проверка-установка соединения
	 * 
	 * @throws SQLException
	 */
	void checkMakeConnection() throws SQLException {

		if (connection != null && connection.isValid(0)) {
			return;
		}

		Properties props = new Properties(); // connection properties
		// providing a user name and password is optional in the embedded
		// and derbyclient frameworks
		props.put("user", "user1"); // FIXME Взять из конфига
		props.put("password", "user1"); // FIXME Взять из конфига

		System.out.println("!!!! conn !!!!! [" + connectionStr + "]");
		
		try {
		Connection conn = DriverManager.getConnection(connectionStr
				+ ";create=true", props);
		
		// conn.setAutoCommit(false);
		// s = conn.createStatement();
		// s.execute(sql);
		//
		// conn.commit();

		// return conn;
		connection = conn;
		}catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * @param metric
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	private long checkDayMetric(String metric, java.sql.Date date)
			throws SQLException {
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

	/**
	 * Получение пути к файлу относительно корня архива
	 * 
	 * @param file
	 * @return
	 */
	public String getRelativeFilePath(File file) {
		File dir = cache.getCacheRootDir();
		String s = file.getPath().replaceFirst(
				Matcher.quoteReplacement(dir.getPath() + File.separator), "");
		return s;
	}

	/**
	 * Получение имени DCM-файла в архиве
	 * 
	 * @param file
	 * @return
	 */
	public String getRelativeDcmFileName(File file) {
		Matcher matcher = Pattern.compile(
				".*" + getFileSeparatorPattern() + "(.*)$").matcher(
				file.getPath());
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * Получение разделителя директорий для регулярного выражения
	 * 
	 * @return
	 */
	private String getFileSeparatorPattern() {
		if (File.separator.equals("\\")) {// windows
			return "\\\\";
		}
		if (File.separator.equals("/")) {// unix
			return "/";
		}
		return null;
	}

	/**
	 * Получение имени JPEG-файла в архиве
	 * 
	 * @param file
	 * @return
	 */
	public String getRelativeImageFileName(File file) {
		Matcher matcher = Pattern.compile(
				".*" + getFileSeparatorPattern() + "(.*)"
						+ getFileSeparatorPattern() + "(.*)$").matcher(
				file.getPath());
		if (matcher.matches()) {
			return matcher.group(1) + File.separator + matcher.group(2);
		}
		return null;
	}

	/**
	 * Получение папки относительного пути папки внутри архива
	 * 
	 * @return
	 */
	String getRelativeIternalDirPath() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatLevel1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatLevel2 = new SimpleDateFormat("H");
		String level1 = formatLevel1.format(calendar.getTime());
		String level2 = formatLevel2.format(calendar.getTime());
		return level1 + File.separator + level2;
	}

	/**
	 * Проверка на наличии информации о DCM-файле в БД
	 * 
	 * @param fileName
	 * @return старое местоположение
	 * @throws SQLException
	 */
	String getDCMFileNamefromDB(String fileName) throws SQLException {

		// String name = getRelativeDcmFileName(new File(fileName));
		// String rerativeName = getRelativeFilePath(new File(fileName));

		PreparedStatement psSelect = connection
				.prepareStatement("SELECT DCM_FILE_NAME FROM WEBDICOM.DCMFILE WHERE NAME = ?");
		try {
			psSelect.setString(1, fileName);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getString("DCM_FILE_NAME");
			}

		} finally {
			if (psSelect != null)
				psSelect.close();
		}
		return null;
	}

	/**
	 * Извлечение картинки
	 * 
	 * @param dcmFile
	 * @throws IOException
	 */
	public DCMImage extractImage(File dcmFile) throws IOException {

		File dest = new File(dcmFile.getPath() + imageDirPrefix);
		dest.mkdirs();

		dest = new File(dest, "fullsize" + imageFileExt);

		Iterator<ImageReader> iter = ImageIO
				.getImageReadersByFormatName("DICOM");

		ImageReader reader = iter.next();
		DicomImageReadParam param = (DicomImageReadParam) reader
				.getDefaultReadParam();
		param.setWindowCenter(center);
		// param.setWindowWidth(width);
		// param.setWindowWidth(1000);
		param.setVoiLutFunction(vlutFct);
		param.setPresentationState(prState);
		param.setPValue2Gray(pval2gray);
		param.setAutoWindowing(autoWindowing);
		ImageInputStream iis = ImageIO.createImageInputStream(dcmFile);
		BufferedImage bi;
		OutputStream out = null;
		String imagePath;
		DCMImage im;
		try {
			reader.setInput(iis, false);
			if (reader.getNumImages(false) <= 0) {
				// System.out.println("\nError: " + dcmFile
				// + " - Don't have any images!");
				return null;
			}
			bi = reader.read(frame - 1, param);
			if (bi == null) {
				System.out.println("\nError: " + dcmFile + " - couldn't read!");
				return null;
			}
			out = new BufferedOutputStream(new FileOutputStream(dest));
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
			enc.encode(bi);

			im = new DCMImage(bi.getWidth(), bi.getHeight(), dest.length());
			// System.out
			// .println("!! w:" + bi.getWidth() + " h:" + bi.getHeight() +
			// " s:"+dest.length());
		} finally {
			CloseUtils.safeClose(iis);
			CloseUtils.safeClose(out);
			imagePath = dest.getPath();
		}

		// Делаем мелкие копии картинок
		resize(imagePath, dcmFile.getPath() + imageDirPrefix + "/100x100.jpg",
				100, 100);
		resize(imagePath, dcmFile.getPath() + imageDirPrefix + "/200x200.jpg",
				200, 200);

		return im;
	}

	/**
	 * Запись информации в БД
	 * 
	 * @param as 
	 * @param dcmFile
	 * @param imageFile
	 * @throws SQLException
	 * @throws IOException
	 */
	public void updateDataBase(Association as, File dcmFile, DCMImage image)
			throws SQLException, IOException, WebDicomDataException {

		
		LOG.info("Calling AE Title: ["+as.getCallingAET()+" : "+as.getLocalAET()+"]; Remote AE Title : ["+as.getRemoteAET()+"]");
		connection.setAutoCommit(false);

		DicomObject dcmObj;
		DicomInputStream din = null;
		SpecificCharacterSet cs = null;

		if (image == null)
			LOG.info("No image");

		try {
			long DCM_FILE_SIZE = dcmFile.length();
			din = new DicomInputStream(dcmFile);
			dcmObj = din.readDicomObject();

			String DCM_FILE_NAME = getRelativeFilePath(dcmFile);
			String NAME = getRelativeDcmFileName(dcmFile);
			
			
			Study study = null;
			//TODO Нужно подробне глянуть. можно-ли еще глубже перекинуть
			//Эксепшн.. внутрь класса Study
			try {
				study = Study.getInstance(dcmObj);
			}catch(Exception ex) {
				throw new WebDicomDataException("Error instantination Study", ex);
			}
			
			String MIME_TYPE = study.getMimeType();
			long DOCUMENT_SIZE = study.getEncapsulatedDocSize();
			
			long IMAGE_FILE_SIZE = 0;
			int IMAGE_WIDTH = 0;
			int IMAGE_HEIGHT = 0;

			if (image != null) {
				IMAGE_FILE_SIZE = image.getHeight(); // TODO Реализовать!!!
				IMAGE_WIDTH = image.getWidth(); // TODO Реализовать!!!
				IMAGE_HEIGHT = image.getHeight();// TODO Реализовать!!!
				MIME_TYPE = "image/jpg";
			}
			
			
			
			
			

			// ----------- Вставка в БД ------------------

			PreparedStatement stmt = null;
			Long directionID = null;
			//TODO Ищем соответствующее направление (по StudyId)
			PreparedStatement psSelect = connection
			.prepareStatement("SELECT ID FROM WEBDICOM.DIRECTION  WHERE DIRECTION_ID = ?");
			try {
				psSelect.setString(1, study.getStudyID());
				ResultSet rs = psSelect.executeQuery();
				while (rs.next()) {
					directionID = rs.getLong("ID");
				}

			} finally {
				if (psSelect != null)
					psSelect.close();
			}

			// STUDY

			long studyInternalID = getStudyInternalIdFomDB(study.getStudyInstanceUID());
			LOG.info("Internal study ID " + studyInternalID);

			if (studyInternalID > 0) {// Есть такое исследование в БД

				LOG.info("Study already in database [" + studyInternalID
						+ "] [" + DCM_FILE_NAME + "]");
				LOG.info("update data in database [" + DCM_FILE_NAME + "]");

				stmt = connection
						.prepareStatement("update WEBDICOM.STUDY SET " +
										"STUDY_ID = ? ," +
										"STUDY_MODALITY = ?," +
										"STUDY_MANUFACTURER_UID = ?, " +
										"STUDY_DATE = ?," +
										"STUDY_TYPE = ?," +
										"STUDY_DESCRIPTION = ?," +
										"STUDY_DOCTOR =?," +
										"STUDY_OPERATOR = ?," +
										"STUDY_RESULT =?," +
										"STUDY_VIEW_PROTOCOL =?," +
										"STUDY_VIEW_PROTOCOL_DATE =?," +
										"STUDY_MANUFACTURER_MODEL_NAME = ?," +
										"PATIENT_ID =?," +
										"PATIENT_NAME =?," +
										"PATIENT_SHORTNAME =?," +
										"PATIENT_BIRTH_DATE =?, " +
										"PATIENT_SEX =?," +
										"DATE_MODIFIED =?, " +
										"FID_DIRECTION = ?"
								+ " where ID = ?");

				stmt.setString(1, study.getStudyID());
				stmt.setString(2, study.getModality());
				stmt.setString(3, study.getManufacturerUID());
				stmt.setDate(4, study.getStudyDate());
				
				//TODO Убрать или передалать!
				LOG.info("UPDATE DB tag: StudyDate: "+study.getStudyDate().toString()+" "+study.getStudyDate().getTime());
				
				stmt.setString(5, study.getStudyType());
				stmt.setString(6, study.getStudyDescription());
				stmt.setString(7, study.getStudyDoctor());
				stmt.setString(8, study.getStudyOperator());
				stmt.setString(9, study.getStudyResult());
				stmt.setString(10, study.getStudyViewProtocol());
				stmt.setDate(11, study.getStudyViewProtocolDate());
				stmt.setString(12, study.getManufacturerModelName());
				stmt.setString(13, study.getPatientID());
				stmt.setString(14, study.getPatientName());
				stmt.setString(15, study.getPatientShortName());
				stmt.setDate(16, study.getPatientBirthDate());
				stmt.setString(17, study.getPatientSex());
				stmt.setTimestamp(18, new Timestamp(new java.util.Date().getTime()));
				
				if(directionID!=null)
					stmt.setLong(19, directionID);
				else
					stmt.setNull(19, java.sql.Types.BIGINT);
				
				stmt.setLong(20, studyInternalID);
				stmt.executeUpdate();
				stmt.close();

			} else {
				// Делаем вставку
				LOG.info("insert data in database [" + DCM_FILE_NAME + "]");
				stmt = connection
						.prepareStatement("insert into WEBDICOM.STUDY ("
								+ "STUDY_UID,"
								+ "STUDY_MODALITY,"
								+ "STUDY_MANUFACTURER_UID,"
								+ "STUDY_ID,"
								+ "STUDY_DATE,"
								+ "STUDY_TYPE,"
								+ "STUDY_DESCRIPTION,"
								+ "STUDY_DOCTOR,"
								+ "STUDY_OPERATOR,"
								+ "STUDY_RESULT,"
								+ "STUDY_VIEW_PROTOCOL,"
								+ "STUDY_VIEW_PROTOCOL_DATE,"
								+ "STUDY_MANUFACTURER_MODEL_NAME,"
								+ "PATIENT_ID,"
								+ "PATIENT_NAME, "
								+ "PATIENT_SHORTNAME, "
								+ "PATIENT_BIRTH_DATE, "
								+ "PATIENT_SEX,"
								+ "DATE_MODIFIED,"
								+ "FID_DIRECTION)"
								+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				stmt.setString(1, study.getStudyInstanceUID());
				stmt.setString(2, study.getModality());
				stmt.setString(3, study.getManufacturerUID());
				stmt.setString(4, study.getStudyID());
				stmt.setDate(5, study.getStudyDate());
				stmt.setString(6, study.getStudyType());
				stmt.setString(7, study.getStudyDescription());
				stmt.setString(8, study.getStudyDoctor());
				stmt.setString(9, study.getStudyOperator());
				stmt.setString(10, study.getStudyResult());
				stmt.setString(11, study.getStudyViewProtocol());
				stmt.setDate(12, study.getStudyViewProtocolDate());
				stmt.setString(13, study.getManufacturerModelName());
				stmt.setString(14, study.getPatientID());
				stmt.setString(15, study.getPatientName());
				stmt.setString(16, study.getPatientShortName());
				stmt.setDate(17, study.getPatientBirthDate());
				stmt.setString(18, study.getPatientSex());
				stmt.setTimestamp(19, new Timestamp(new java.util.Date().getTime()));
				
				if(directionID!=null)
					stmt.setLong(20, directionID);
				else
					stmt.setNull(20, java.sql.Types.BIGINT);

				stmt.executeUpdate();

				// Обновляем статистику
				updateDayStatInc(study.getStudyDate(), "ALL_STUDIES_COUNT", 1);
				stmt.close();
			}

			// DICOM Файл

			LOG.info("[" + DCM_FILE_NAME + "][" + study.getPatientName() + "]["
					+ study.getPatientBirthDate() + "][" + study.getStudyDate() + "]");

			long id = getDCMInternalIdFromDB(DCM_FILE_NAME);
			studyInternalID = getStudyInternalIdFomDB(study.getStudyInstanceUID());
			
			if (id > 0) {// Есть такой файл в БД

				LOG.info("File already in database [" + id + "] ["
						+ DCM_FILE_NAME + "]");
				LOG.info("update data in database [" + DCM_FILE_NAME + "]");

				stmt = connection
						.prepareStatement("update WEBDICOM.DCMFILE SET "
								+ "FID_STUDY = ?,"
								+ "TYPE = ?,"
								+ "DCM_FILE_NAME = ?,"
								+ "NAME = ?,"
								+ "DCM_FILE_SIZE = ?," 
								+ "IMAGE_FILE_SIZE = ?," 
								+ "IMAGE_WIDTH = ?," 
								+ "IMAGE_HEIGHT =?, DATE_MODIFIED =?, MIME_TYPE =?, DOCUMENT_SIZE =? "
								+ " where ID = ?");

				stmt.setLong(1, studyInternalID);
				stmt.setString(2, study.getDcmType());
				stmt.setString(3, DCM_FILE_NAME);
				stmt.setString(4, NAME);
				stmt.setLong(5, DCM_FILE_SIZE);
				stmt.setLong(6, IMAGE_FILE_SIZE);
				stmt.setInt(7, IMAGE_WIDTH);
				stmt.setInt(8, IMAGE_HEIGHT);
				stmt.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				stmt.setString(10, MIME_TYPE);
				stmt.setLong(11, DOCUMENT_SIZE);
				stmt.setLong(12, id);
				stmt.executeUpdate();
				stmt.close();

			} else {
				
				// Делаем вставку
				LOG.info("insert data in database [" + DCM_FILE_NAME + "]");
				stmt = connection
				.prepareStatement("insert into WEBDICOM.DCMFILE ("
						+ "FID_STUDY,"
						+ "TYPE,"
						+ "DCM_FILE_NAME,"
						+ "NAME,"
						+ "DCM_FILE_SIZE," 
						+ "IMAGE_FILE_SIZE," 
						+ "IMAGE_WIDTH," 
						+ "IMAGE_HEIGHT, DATE_MODIFIED, MIME_TYPE,DOCUMENT_SIZE )"
						+ "values (?,?,?,?,?,?,?,?,?,?,?)");

				stmt.setLong(1, studyInternalID);
				stmt.setString(2, study.getDcmType());
				stmt.setString(3, DCM_FILE_NAME);
				stmt.setString(4, NAME);
				stmt.setLong(5, DCM_FILE_SIZE);
				stmt.setLong(6, IMAGE_FILE_SIZE);
				stmt.setInt(7, IMAGE_WIDTH);
				stmt.setInt(8, IMAGE_HEIGHT);
				stmt.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				stmt.setString(10, MIME_TYPE);
				stmt.setLong(11, DOCUMENT_SIZE);
				
				stmt.executeUpdate();

				// Обновляем статистику
				updateDayStatInc(study.getStudyDate(), "ALL_DCM_SIZE", DCM_FILE_SIZE);
				stmt.close();
			}

			long dcmId = getDCMInternalIdFromDB(DCM_FILE_NAME);
			updateTags(dcmObj, dcmId);

			// Вставка в БД информации о картинке
			// insertImageData(dcmFile, new File(imageFile), STUDY_DATE, WIDTH,
			// HEIGHT);

		} catch (org.dcm4che2.data.ConfigurationError e) {
			if (e.getCause() instanceof UnsupportedEncodingException) {
				// TODO Дать возможность получения кодировки из коммандной
				// строки
				LOG.error("Unsupported character set " + e);
				// LOG.fatal("Unsupported character set" + charsetStr + " " +
				// e);
			}
			LOG.error("" + e);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("" + e);
			throw e;

		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}

		connection.commit();

	}

	/**
	 * Обновление информации о тегах
	 * 
	 * @param dcmObj
	 * @param dcmId
	 * @throws SQLException
	 */
	private void updateTags(DicomObject dcmObj, long dcmId) throws SQLException {

		// TODO Удаляем старые теги!!!

		PreparedStatement psDelete = connection
				.prepareStatement("delete from WEBDICOM.DCMFILE_TAG where FID_DCMFILE = ?");
		psDelete.setLong(1, dcmId);
		psDelete.executeUpdate();
		psDelete.close();

		// FIXME Задаем жестко кодировку
		SpecificCharacterSet cs = new SpecificCharacterSet("ISO-8859-5");

		// читаем кодировку из dcm-файла
		if (dcmObj.get(Tag.SpecificCharacterSet) != null) {
			SpecificCharacterSet cs1 = SpecificCharacterSet.valueOf(dcmObj.get(
					Tag.SpecificCharacterSet).getStrings(null, false));
		}

		PreparedStatement psInsert = null;

		psInsert = connection
				.prepareStatement("insert into WEBDICOM.DCMFILE_TAG "
						+ " (FID_DCMFILE, TAG, TAG_TYPE, VALUE_STRING)"
						+ " values (?, ?, ?,  ?)");

		int maxLength = 1000;

		DecimalFormat format = new DecimalFormat("0000");

		// System.out.println("!!! " + dcmObj);

		// Раскручиваем теги
		for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
			DicomElement element = it.next();
			int tag = element.tag();
			
//			System.out.println("!!! VR[" + element.vr() + "]"+ element);

			// не пишем бинарные данные
			if (element.vr().equals(VR.OW) || element.vr().equals(VR.OB)
					|| element.vr().equals(VR.SQ)) {
				continue;
			}

			// String type = TagUtils.toString(tag);
			String type = "" + element.vr().toString();
			if (type.length() > 2) {
				type = type.substring(0, 2);
			}

			short ma = (short) (tag >> 16);
			String major = format.format(ma);
			short mi = (short) (tag);
			String minor = format.format(mi);

			String name = dcmObj.nameOf(tag);
			int length = element.length();
			if (length > maxLength)
				length = maxLength;

//			System.out.println("!!! element vr=["+element.vr()+"]");
			String value = element.getValueAsString(cs, length);
			if (value == null)
				continue;
			psInsert.setLong(1, dcmId);
			psInsert.setInt(2, tag);
			psInsert.setString(3, type);
			psInsert.setString(4, value);

			psInsert.executeUpdate();

			LOG.info("insert tag (" + major + "," + minor + ") [" + type
					+ "] [" + name + "] " + value);
			// System.out.println("insert tag (" + major + ","+minor+") ["+
			// type+"] [" + name + "] " + value);
		}
		psInsert.close();

	}

	/**
	 * Проверка на наличии информации о DCM-файле в БД
	 * 
	 * @param fileName
	 * @return
	 * @throws SQLException
	 */
	private long getDCMInternalIdFromDB(String fileName) throws SQLException {

		String name = getRelativeDcmFileName(new File(fileName));
		// String rerativeName = getRelativeFilePath(new File(fileName));

		PreparedStatement psSelect = connection
				.prepareStatement("SELECT ID FROM WEBDICOM.DCMFILE WHERE NAME = ?");
		try {
			psSelect.setString(1, name);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getLong("ID");
			}

		} finally {
			if (psSelect != null)
				psSelect.close();
		}
		return -1;
	}

	/**
	 * Получение из БД внктренного ID
	 * 
	 * @param uid
	 * @return если не найден, то '-1'
	 * @throws SQLException
	 */
	private long getStudyInternalIdFomDB(String uid) throws SQLException {

		PreparedStatement psSelect = connection
				.prepareStatement("SELECT ID FROM WEBDICOM.STUDY WHERE STUDY_UID = ?");
		try {
			psSelect.setString(1, uid);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getLong("ID");
			}

		} finally {
			if (psSelect != null)
				psSelect.close();
		}
		return -1;
	}

	/**
	 * Проверка на наличии информации о Картинке-файле в БД
	 * 
	 * @param fileName
	 * @return
	 * @throws SQLException
	 */
	private int checkDbImageFile(String fileName) throws SQLException {

		String name = getRelativeImageFileName(new File(fileName));
		// String rerativeName = getRelativeFilePath(new File(fileName));

		PreparedStatement psSelect = connection
				.prepareStatement("SELECT ID FROM WEBDICOM.IMAGES WHERE NAME = ?");
		try {
			psSelect.setString(1, name);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getInt("ID");
			}

		} finally {
			if (psSelect != null)
				psSelect.close();
		}
		throw new NoDataFoundException("No data");
	}

	/**
	 * Вставка информации о картинках в БД
	 * 
	 * @param dcmFile
	 * @param imageFile
	 * @throws SQLException
	 */
	// private void insertImageData(File dcmFile, File imageFile,
	// java.sql.Date STUDY_DATE, int WIDTH, int HEIGHT)
	// throws SQLException {
	//
	// long FID_DCMFILE = checkDbDCMFile(getRelativeFilePath(dcmFile));
	// String IMAGE_FILE_NAME = getRelativeFilePath(imageFile);
	// String NAME = getRelativeImageFileName(imageFile);
	// long IMAGE_FILE_SIZE = imageFile.length();
	// String CONTENT_TYPE = imageContentType;
	//
	// try {
	// int idImage = checkDbImageFile(IMAGE_FILE_NAME);
	//
	// PreparedStatement psUpdate = null;
	//
	// LOG.info("update data in database [" + FID_DCMFILE + "] image ["
	// + IMAGE_FILE_NAME + "]");
	// psUpdate = connection.prepareStatement("update WEBDICOM.IMAGES"
	// + " set FID_DCMFILE = ? ,"
	// + " CONTENT_TYPE = ? , IMAGE_FILE_NAME =? , NAME = ?, "
	// + " IMAGE_FILE_SIZE = ?, WIDTH = ?, HEIGHT = ?"
	// + " where ID = ?");
	//
	// psUpdate.setLong(1, FID_DCMFILE);
	// psUpdate.setString(2, CONTENT_TYPE);
	// psUpdate.setString(3, IMAGE_FILE_NAME);
	// psUpdate.setString(4, NAME);
	// psUpdate.setLong(5, IMAGE_FILE_SIZE);
	// psUpdate.setInt(6, WIDTH);
	// psUpdate.setInt(7, HEIGHT);
	// psUpdate.setInt(8, idImage);
	// psUpdate.executeUpdate();
	// psUpdate.close();
	//
	// } catch (NoDataFoundException ex) {
	// PreparedStatement psInsert = null;
	//
	// LOG.info("insert data in database [" + FID_DCMFILE + "] image ["
	// + IMAGE_FILE_NAME + "]");
	//
	// psInsert = connection
	// .prepareStatement("insert into WEBDICOM.IMAGES"
	// +
	// " (FID_DCMFILE, CONTENT_TYPE, IMAGE_FILE_NAME, NAME,  IMAGE_FILE_SIZE, WIDTH, HEIGHT)"
	// + " values (?, ?, ?, ?, ?, ?, ?)");
	//
	// psInsert.setLong(1, FID_DCMFILE);
	// psInsert.setString(2, CONTENT_TYPE);
	// psInsert.setString(3, IMAGE_FILE_NAME);
	// psInsert.setString(4, NAME);
	// psInsert.setLong(5, IMAGE_FILE_SIZE);
	// psInsert.setInt(6, WIDTH);
	// psInsert.setInt(7, HEIGHT);
	// psInsert.executeUpdate();
	// psInsert.close();
	//
	// }
	//
	// // Обновление статистики
	// updateDayStatInc(STUDY_DATE, "ALL_IMAGE_SIZE", IMAGE_FILE_SIZE);
	//
	// }

	/**
	 * Обновление метрики дневной статистики (инкремент)
	 * 
	 * @param date
	 * @param metric
	 * @param value
	 * @throws SQLException
	 */
	private void updateDayStatInc(java.util.Date date, String metric, long value)
			throws SQLException {

		PreparedStatement stmt = null;

		// Calendar calendar = Calendar.getInstance();
		// long time = calendar.getTimeInMillis();
		// time = time - (time % (60 * 60 * 24 * 1000));
		// // calendar.setTimeInMillis(time);

		long time = date.getTime();
		time = time - (time % (60 * 60 * 24 * 1000));
		// date = new Date(time);

		LOG.info(metric + "=" + value + " of " + date);

		// Проверка на наличии этого файла в БД
		try {
			long valueOld = checkDayMetric(metric, new java.sql.Date(time));
			LOG.info("metric already in database [" + metric + "][" + date
					+ "][" + valueOld + "]");

			stmt = connection.prepareStatement("update WEBDICOM.DAYSTAT "
					+ " SET METRIC_VALUE_LONG = ? "
					+ " where METRIC_NAME = ? AND METRIC_DATE = ?");

			long sumVal = value + valueOld;
			stmt.setLong(1, sumVal);
			stmt.setString(2, metric);
			stmt.setDate(3, new java.sql.Date(time));
			stmt.executeUpdate();

		} catch (NoDataFoundException ex) {
			// Делаем вставку
			LOG.info("insert data in database [" + metric + "][" + date + "]["
					+ value + "]");
			stmt = connection.prepareStatement("insert into WEBDICOM.DAYSTAT "
					+ " (METRIC_NAME, METRIC_DATE, METRIC_VALUE_LONG)"
					+ " values (?, ?, ?)");

			stmt.setString(1, metric);
			stmt.setDate(2, new java.sql.Date(time));
			stmt.setLong(3, value);
			stmt.executeUpdate();

		} finally {
			if (stmt != null)
				stmt.close();
		}

	}

	private void resize(String srcFile, String dstfile, int width, int height)
			throws IOException {

		BufferedImage image = ImageIO.read(new File(srcFile));

		// BufferedImage resizedImage = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_ARGB);
		BufferedImage resizedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();

		ImageIO.write(resizedImage, "jpg", new File(dstfile));

	}
	
	/**
	 * Получение КБП
	 * @param PatientName
	 * @param PatientBirthDate
	 * @return
	 */
	public static String makeShortName (String PatientName, Date PatientBirthDate) {
		
		String result = null;
		PatientName = PatientName.replaceAll("\\^", " ");//Заменяем ^
		Matcher matcher = Pattern.compile("\\s*(...).*?\\s+(.).*?\\s+(.).*?").matcher(PatientName.toUpperCase());
		if (matcher.matches()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(PatientBirthDate);
			
			int year = (cal.get(Calendar.YEAR) - 1900);
			if( year >= 100) year -= 100;
			String yearS = "" + year;
			if(year<10) yearS = "0" + yearS;
				
			result = matcher.group(1)+matcher.group(2)+matcher.group(3)+yearS; 
//			System.out.println("!=["+matcher.group(1)+"]["+matcher.group(2)+"]["+matcher.group(3)+"]");
		}
		return result;
		
	}

}
