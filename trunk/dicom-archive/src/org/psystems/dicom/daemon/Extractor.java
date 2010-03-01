package org.psystems.dicom.daemon;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.dcm4che2.filecache.FileCache;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class Extractor {
	
	
	private static Logger LOG = LoggerFactory.getLogger(Extractor.class);
	
	private FileCache cache;
	
	public static String dcmFileExt = ".dcm"; // Расширение для сохраняемых файлов
	public static String imageDirPrefix = ".images";
	
	static String connectionStr = "jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM";
	private Connection connection;//соединение с БД
	
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
	 * Получение пути к файлу относительно корня архива
	 * 
	 * @param file
	 * @return
	 */
	public String getRelativeFilePath(File file) {
		File dir = cache.getCacheRootDir();
		String s = file.getPath().replaceFirst(Matcher.quoteReplacement(dir.getPath() + File.separator), "");
		return s;
	}

	/**
	 * Получение имени DCM-файла в архиве
	 * 
	 * @param file
	 * @return
	 */
	public String getRelativeDcmFileName(File file) {
		Matcher matcher = Pattern.compile(".*" + getFileSeparatorPattern() + "(.*)$").matcher(file.getPath());
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
				".*" + getFileSeparatorPattern() + "(.*)" + getFileSeparatorPattern() + "(.*)$").matcher(
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

		Connection conn = DriverManager.getConnection(connectionStr + ";create=true", props);
		// conn.setAutoCommit(false);
		// s = conn.createStatement();
		// s.execute(sql);
		//
		// conn.commit();

		// return conn;
		connection = conn;
	}

	/**
	 * Извлечение картинок и запись в БД
	 * 
	 * @param file
	 * @throws SQLException
	 * @throws IOException
	 */
	void extractImagesAndSaveDB(File file) throws SQLException, IOException {

		ArrayList<String> images = extractImages(file);
		connection.setAutoCommit(false);
		insertUpdateCommonData(file, images);
		connection.commit();
	}

	/**
	 * Извлечение картинок
	 * 
	 * @param dcmFile
	 * @throws IOException
	 * @throws SQLException
	 */
	public ArrayList<String> extractImages(File dcmFile) throws IOException, SQLException {

		ArrayList<String> resultImages = new ArrayList<String>();
		File dest = new File(dcmFile.getPath() + imageDirPrefix);
		dest.mkdirs();

		dest = new File(dest, "1" + imageFileExt);
		// TODO Тут может быть наверное несколько картинок !!!

		Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");

		ImageReader reader = iter.next();
		DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
		param.setWindowCenter(center);
		param.setWindowWidth(width);
		param.setVoiLutFunction(vlutFct);
		param.setPresentationState(prState);
		param.setPValue2Gray(pval2gray);
		param.setAutoWindowing(autoWindowing);
		ImageInputStream iis = ImageIO.createImageInputStream(dcmFile);
		BufferedImage bi;
		OutputStream out = null;
		try {
			reader.setInput(iis, false);
			bi = reader.read(frame - 1, param);
			if (bi == null) {
				System.out.println("\nError: " + dcmFile + " - couldn't read!");
				return resultImages;
			}
			out = new BufferedOutputStream(new FileOutputStream(dest));
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
			enc.encode(bi);
		} finally {
			CloseUtils.safeClose(iis);
			CloseUtils.safeClose(out);
			resultImages.add(dest.getPath());
		}
		return resultImages;
	}

	/**
	 * @param dcmFile
	 * @throws SQLException
	 * @throws IOException
	 */
	private void insertUpdateCommonData(File dcmFile, ArrayList<String> images) throws SQLException,
			IOException {

		DicomObject dcmObj;
		DicomInputStream din = null;
		SpecificCharacterSet cs = null;

		try {
			long DCM_FILE_SIZE = dcmFile.length();
			din = new DicomInputStream(dcmFile);
			dcmObj = din.readDicomObject();

			// проверки

			// TODO Дать возможность задания с коммандной строки
			String charsetStr = null;
			if (charsetStr != null) {
				cs = new SpecificCharacterSet(charsetStr);
			}

			// читаем кодировку из dcm-файла
			if (charsetStr == null) {
				
				if(dcmObj.get(Tag.SpecificCharacterSet)!=null) {
				cs = SpecificCharacterSet.valueOf(dcmObj.get(Tag.SpecificCharacterSet)
						.getStrings(null, false));
				}else {
					cs = new SpecificCharacterSet("ISO-8859-5");
					LOG.warn("Character Ser (tag: SpecificCharacterSet) is empty!");
				}
			}

			String DCM_FILE_NAME = getRelativeFilePath(dcmFile);
			String NAME = getRelativeDcmFileName(dcmFile);

			java.sql.Date PATIENT_BIRTH_DATE;

			if (dcmObj.get(Tag.PatientBirthDate) != null) {
				PATIENT_BIRTH_DATE = new java.sql.Date(dcmObj.get(Tag.PatientBirthDate).getDate(false)
						.getTime());
			} else {
				PATIENT_BIRTH_DATE = new java.sql.Date(0);
				LOG.warn("Patient Birth Date (tag: PatientBirthDate) is empty!");
			}

			DicomElement element1 = dcmObj.get(Tag.PatientName);
			String PATIENT_NAME = element1.getValueAsString(cs, element1.length());

			element1 = dcmObj.get(Tag.PatientID);
			String PATIENT_ID = element1.getValueAsString(cs, element1.length());

			if (PATIENT_ID == null || PATIENT_ID.length() == 0) {
				PATIENT_ID = "не указан";
			}

			element1 = dcmObj.get(Tag.PatientSex);
			String PATIENT_SEX = "";
			if (element1 == null) {
				LOG.warn("Patient sex (tag: PatientSex) is empty!");
			} else {
				PATIENT_SEX = element1.getValueAsString(cs, element1.length());
				if (PATIENT_SEX.length() > 1) {
					LOG.warn("PATIENT_SEX to long [" + PATIENT_SEX + "]");
					PATIENT_SEX = PATIENT_SEX.substring(0, 1);
				}
			}

			element1 = dcmObj.get(Tag.StudyID);
			String STUDY_ID = "";
			if (element1 == null) {
				LOG.warn("Study ID (tag: StudyID) is empty!");
			} else {
				STUDY_ID = element1.getValueAsString(cs, element1.length());
			}

			java.sql.Date STUDY_DATE = new java.sql.Date(dcmObj.get(Tag.StudyDate).getDate(false).getTime());

			String STUDY_DOCTOR = "не указан";
			element1 = dcmObj.get(Tag.ReferringPhysicianName);
			if (element1 != null) {
				STUDY_DOCTOR = element1.getValueAsString(cs, element1.length());
				if (STUDY_DOCTOR == null || STUDY_DOCTOR.length() == 0) {
					STUDY_DOCTOR = "не указан";
				}
			}

			String STUDY_OPERATOR = "не указан";
			element1 = dcmObj.get(Tag.OperatorsName);
			if (element1 != null) {
				STUDY_OPERATOR = element1.getValueAsString(cs, element1.length());
				if (STUDY_OPERATOR == null || STUDY_OPERATOR.length() == 0) {
					STUDY_OPERATOR = "не указан";
				}
			}
			
			String STUDY_DESCRIPTION = "";
			element1 = dcmObj.get(Tag.MedicalAlerts);
			if (element1 != null) {
				STUDY_DESCRIPTION = element1.getValueAsString(cs, element1.length());
				if (STUDY_DESCRIPTION == null || STUDY_DESCRIPTION.length() == 0) {
					STUDY_DESCRIPTION = "нет";
				}
			}

			int HEIGHT = dcmObj.get(Tag.Rows).getInt(false);
			int WIDTH = dcmObj.get(Tag.Columns).getInt(false);

			// Вставка в БД

			PreparedStatement stmt = null;

			LOG.info("[" + DCM_FILE_NAME + "][" + PATIENT_NAME + "][" + PATIENT_BIRTH_DATE + "]["
					+ STUDY_DATE + "]");

			// Проверка на наличии этого файла в БД
			try {
				int id = checkDbDCMFile(DCM_FILE_NAME);
				LOG.info("File already in database [" + id + "] [" + DCM_FILE_NAME + "]");
				LOG.info("update data in database [" + DCM_FILE_NAME + "]");

				stmt = connection
						.prepareStatement("update WEBDICOM.DCMFILE"
								+ " SET NAME = ? , DCM_FILE_SIZE = ? , PATIENT_NAME = ?, PATIENT_SEX = ?, PATIENT_BIRTH_DATE = ?, "
								+ " STUDY_ID =? , STUDY_DATE = ?, STUDY_DOCTOR =? , STUDY_OPERATOR = ?, STUDY_DESCRIPTION =?"
								+ " where ID = ?");

				stmt.setString(1, NAME);
				stmt.setLong(2, DCM_FILE_SIZE);
				stmt.setString(3, PATIENT_NAME);
				stmt.setString(4, PATIENT_SEX);
				stmt.setDate(5, PATIENT_BIRTH_DATE);
				stmt.setString(6, STUDY_ID);
				stmt.setDate(7, STUDY_DATE);
				stmt.setString(8, STUDY_DOCTOR);
				stmt.setString(9, STUDY_OPERATOR);
				stmt.setString(10, STUDY_DESCRIPTION);
				stmt.setInt(11, id);
				stmt.executeUpdate();

				LOG.info("skip converting image.");

			} catch (NoDataFoundException ex) {
				// Делаем вставку
				LOG.info("insert data in database [" + DCM_FILE_NAME + "]");
				stmt = connection
						.prepareStatement("insert into WEBDICOM.DCMFILE"
								+ " (DCM_FILE_NAME, NAME, DCM_FILE_SIZE, PATIENT_ID, PATIENT_NAME, PATIENT_SEX, PATIENT_BIRTH_DATE,"
								+ " STUDY_ID, STUDY_DATE, STUDY_DOCTOR, STUDY_OPERATOR, STUDY_DESCRIPTION)"
								+ " values (?,?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,?)");

				stmt.setString(1, DCM_FILE_NAME);
				stmt.setString(2, NAME);
				stmt.setLong(3, DCM_FILE_SIZE);
				stmt.setString(4, PATIENT_ID);
				stmt.setString(5, PATIENT_NAME);
				stmt.setString(6, PATIENT_SEX);
				stmt.setDate(7, PATIENT_BIRTH_DATE);
				stmt.setString(8, STUDY_ID);
				stmt.setDate(9, STUDY_DATE);
				stmt.setString(10, STUDY_DOCTOR);
				stmt.setString(11, STUDY_OPERATOR);
				stmt.setString(12, STUDY_DESCRIPTION);

				stmt.executeUpdate();
				// Обновляем статистику
				updateDayStatInc(STUDY_DATE, "ALL_DCM_SIZE", DCM_FILE_SIZE);
			} finally {
				if (stmt != null)
					stmt.close();
			}

			// Вставка в БД информации о картинках

			for (Iterator<String> it = images.iterator(); it.hasNext();) {
				String fileImage = it.next();
				insertImageData(dcmFile, new File(fileImage), STUDY_DATE, WIDTH, HEIGHT);
			}

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
	 * Проверка на наличии информации о DCM-файле в БД
	 * 
	 * @param fileName
	 * @return
	 * @throws SQLException
	 */
	private int checkDbDCMFile(String fileName) throws SQLException {

		String name = getRelativeDcmFileName(new File(fileName));
		// String rerativeName = getRelativeFilePath(new File(fileName));

		PreparedStatement psSelect = connection
				.prepareStatement("SELECT ID FROM WEBDICOM.DCMFILE WHERE NAME = ?");
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
	private void insertImageData(File dcmFile, File imageFile, java.sql.Date STUDY_DATE, int WIDTH, int HEIGHT)
			throws SQLException {

		Integer FID_DCMFILE = checkDbDCMFile(getRelativeFilePath(dcmFile));
		String IMAGE_FILE_NAME = getRelativeFilePath(imageFile);
		String NAME = getRelativeImageFileName(imageFile);
		long IMAGE_FILE_SIZE = imageFile.length();
		String CONTENT_TYPE = imageContentType;

		try {
			int idImage = checkDbImageFile(IMAGE_FILE_NAME);

			PreparedStatement psUpdate = null;

			LOG.info("update data in database [" + FID_DCMFILE + "] image [" + IMAGE_FILE_NAME + "]");
			psUpdate = connection.prepareStatement("update WEBDICOM.IMAGES" + " set FID_DCMFILE = ? ,"
					+ " CONTENT_TYPE = ? , IMAGE_FILE_NAME =? , NAME = ?, "
					+ " IMAGE_FILE_SIZE = ?, WIDTH = ?, HEIGHT = ?" + " where ID = ?");

			psUpdate.setInt(1, FID_DCMFILE);
			psUpdate.setString(2, CONTENT_TYPE);
			psUpdate.setString(3, IMAGE_FILE_NAME);
			psUpdate.setString(4, NAME);
			psUpdate.setLong(5, IMAGE_FILE_SIZE);
			psUpdate.setInt(6, WIDTH);
			psUpdate.setInt(7, HEIGHT);
			psUpdate.setInt(8, idImage);
			psUpdate.executeUpdate();
			psUpdate.close();

		} catch (NoDataFoundException ex) {
			PreparedStatement psInsert = null;

			LOG.info("insert data in database [" + FID_DCMFILE + "] image [" + IMAGE_FILE_NAME + "]");

			psInsert = connection.prepareStatement("insert into WEBDICOM.IMAGES"
					+ " (FID_DCMFILE, CONTENT_TYPE, IMAGE_FILE_NAME, NAME,  IMAGE_FILE_SIZE, WIDTH, HEIGHT)"
					+ " values (?, ?, ?, ?, ?, ?, ?)");

			psInsert.setInt(1, FID_DCMFILE);
			psInsert.setString(2, CONTENT_TYPE);
			psInsert.setString(3, IMAGE_FILE_NAME);
			psInsert.setString(4, NAME);
			psInsert.setLong(5, IMAGE_FILE_SIZE);
			psInsert.setInt(6, WIDTH);
			psInsert.setInt(7, HEIGHT);
			psInsert.executeUpdate();
			psInsert.close();

		}

		// Обновление статистики
		updateDayStatInc(STUDY_DATE, "ALL_IMAGE_SIZE", IMAGE_FILE_SIZE);

	}

	/**
	 * Обновление метрики дневной статистики (инкремент)
	 * 
	 * @param date
	 * @param metric
	 * @param value
	 * @throws SQLException
	 */
	private void updateDayStatInc(java.util.Date date, String metric, long value) throws SQLException {

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
			LOG.info("metric already in database [" + metric + "][" + date + "][" + valueOld + "]");

			stmt = connection.prepareStatement("update WEBDICOM.DAYSTAT " + " SET METRIC_VALUE_LONG = ? "
					+ " where METRIC_NAME = ? AND METRIC_DATE = ?");

			long sumVal = value + valueOld;
			stmt.setLong(1, sumVal);
			stmt.setString(2, metric);
			stmt.setDate(3, new java.sql.Date(time));
			stmt.executeUpdate();

		} catch (NoDataFoundException ex) {
			// Делаем вставку
			LOG.info("insert data in database [" + metric + "][" + date + "][" + value + "]");
			stmt = connection.prepareStatement("insert into WEBDICOM.DAYSTAT "
					+ " (METRIC_NAME, METRIC_DATE, METRIC_VALUE_LONG)" + " values (?, ?, ?)");

			stmt.setString(1, metric);
			stmt.setDate(2, new java.sql.Date(time));
			stmt.setLong(3, value);
			stmt.executeUpdate();

		} finally {
			if (stmt != null)
				stmt.close();
		}

	}

	/**
	 * @param metric
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	private long checkDayMetric(String metric, java.sql.Date date) throws SQLException {
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
