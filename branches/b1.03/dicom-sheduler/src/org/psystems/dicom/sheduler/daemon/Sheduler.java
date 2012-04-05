package org.psystems.dicom.sheduler.daemon;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.DicomObjectToStringParam;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.CloseUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class Sheduler {

	private int frame = 1;
	private float center;
	private float width;
	private String vlutFct;
	private boolean autoWindowing;
	private DicomObject prState;
	private short[] pval2gray;
	private String fileExt = ".jpg";

	public static Logger logger = Logger.getLogger(Sheduler.class);
	// public static Logger logger = Logger.getRootLogger();

	private String srcDir;
	private String dstDir;

	private final String USAGE = "sheduler [Options] SOURCE DEST\n";

	private final String DESCRIPTION = "Convert DICOM file SOURCE to DEST and "
			+ " verify data in URL dababase.\nOptions:";

	private final String EXAMPLE = "\nExample: dcm2dcm in.dcm out.dcm\n"
			+ " => Decode DICOM object from DICOM file in.dcm and encode it with"
			+ " Implicit VR Little Endian Transfer Syntax to DICOM file out.dcm.";

	private String VERSION = "0.1a";

	// private String protocol = "jdbc:derby://localhost:1527//WORKDB/WEBDICOM";
	private Connection connection;
	private String srcDate;
	private String charsetStr;
	private String cStr;
	private String connectionStr = "jdbc:derby://localhost:1527//DICOM/DB/WEBDICOM";
	private String extensionStr = ".dcm";// Расширение входных файлов

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// for(int i=0; i<args.length; i++) {
		// System.out.println("ARG ["+i+"]["+args[i]+"]");
		// }
		new Sheduler(args);
	}

	/**
	 * Разборщик коммандной строки
	 * 
	 * @param args
	 * @return
	 */
	private CommandLine parse(String[] args) {
		// create the command line parser
		CommandLineParser parser = new PosixParser();

		// create the Options
		Options opts = new Options();

		opts.addOption(OptionBuilder.withLongOpt("extension").withDescription(
				"use file PREFIX for input files").withArgName("PREFIX").create("e"));

		opts.addOption(OptionBuilder.withLongOpt("source-dir").withDescription("use PATH source dir")
				.hasArg().withArgName("PATH").isRequired().create("s"));

		opts.addOption(OptionBuilder.withLongOpt("dest-dir").withDescription("use PATH destination dir")
				.hasArg().withArgName("PATH").isRequired().create("d"));

		opts.addOption(OptionBuilder.withLongOpt("connection").withDescription("use URL for JDBC connector")
				.hasArg().withArgName("URL").isRequired().create("c"));

		opts.addOption(OptionBuilder.withLongOpt("date").withDescription("use DATE for check").hasArg()
				.withArgName("DATE").isRequired().create("dd"));

		opts.addOption(OptionBuilder.withLongOpt("charset").withDescription("use CHARSET for parsing")
				.hasArg().withArgName("CHARSET").create("cs"));

		opts
				.addOption(OptionBuilder.withLongOpt("daemon").withDescription("run sheduler as daemon")
						.create());

		opts.addOption("h", "help", false, "print this message");
		opts.addOption("V", "version", false, "print the version information and exit");

		CommandLine cl = null;
		try {
			cl = new PosixParser().parse(opts, args);
		} catch (ParseException e) {
			exit("sheduler: " + e.getMessage());
			throw new RuntimeException("unreachable");
		}
		if (cl.hasOption('V')) {
			System.out.println("sheduler version " + VERSION);
			// TODO Взять нормально версию
			System.exit(0);
		}

		if (cl.hasOption('h')) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(USAGE, DESCRIPTION, opts, EXAMPLE);
			System.exit(0);
		}

		return cl;

		// HelpFormatter formatter = new HelpFormatter();
		// formatter.printHelp(USAGE, DESCRIPTION, options, EXAMPLE);
	}

	private void exit(String msg) {
		System.err.println(msg);
		System.err.println("Try 'sheduler -h' for more information.");
		System.exit(1);
	}

	/**
	 * @param args
	 */
	public Sheduler(String[] args) {

		// String[] args = new String[] { "-d test\\testdata\\out",
		// "--source-dir=test\\testdata\\2009-12-16" };

		// TODO убрать!!!
		PropertyConfigurator.configure("log4j.properties");

		CommandLine cl = parse(args);

		if (cl.hasOption("source-dir")) {
			srcDir = cl.getOptionValue("source-dir").trim();
			logger.info("srcDir=[" + srcDir + "]");
		}

		if (cl.hasOption("dest-dir")) {
			dstDir = cl.getOptionValue("dest-dir").trim();
			logger.info("dstDir=[" + dstDir + "]");
		}

		if (cl.hasOption("date")) {
			srcDate = cl.getOptionValue("date").trim();
			logger.info("srcDate=[" + srcDate + "]");
		}

		if (cl.hasOption("charset")) {
			charsetStr = cl.getOptionValue("charset").trim();
			logger.info("CharacterSet=[" + charsetStr + "]");
		}

		if (cl.hasOption("connection")) {
			connectionStr = cl.getOptionValue("connection").trim();
			logger.info("connectionURL=[" + connectionStr + "]");
		}

		if (cl.hasOption("extension")) {
			System.out.println("!!! " + cl.getOptionValue("extension"));
			extensionStr = cl.getOptionValue("extension").trim();
			logger.info("extensionStr=[" + extensionStr + "]");
		}

		try {
			connection = getConnection();
			iterateFiles();
		} catch (SQLException e) {
			logger.fatal("" + e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.fatal("" + e);
		}

	}

	/**
	 * Обход папок с файлами
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	private void iterateFiles() throws SQLException, FileNotFoundException {

		final String currentDateStr = srcDate;

		FilenameFilter filterDir = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.equals(currentDateStr)) {
					return true;
				}
				return false;
			}

		};

		// filter files for extension *.dcm
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				if (name.endsWith(".svn"))
					return false;
				else
					return true;
				// if (name.endsWith(extensionStr)) { // .dcm
				// return true;
				// }
				// return false;
				// FIXME Сделать через командную строку!!!
			}

		};

		// TODO пока 2-х уровневая организация папок, потом сделать это
		// рекурсивно. задается через регулярное выражение
		File rootDir = new File(srcDir);

		if (!rootDir.exists()) {
			throw new FileNotFoundException(" No source directory found! " + srcDir);
		}
		if (!rootDir.canRead()) {
			throw new FileNotFoundException(" No access for source directory! " + srcDir);
		}

		if (rootDir.isDirectory()) {
			File[] dirs = rootDir.listFiles(filterDir);
			for (int i = 0; i < dirs.length; i++) {
				if (dirs[i].isDirectory()) {
					String dirName = dirs[i].getName();
					File[] files = dirs[i].listFiles(filter);

					// создаем рекурсивно директории
					File f = new File(dstDir + File.separator + dirs[i].getName());
					if (!rootDir.canWrite()) {
						throw new FileNotFoundException(" No write access for destination directory! "
								+ dstDir);
					}

					f.mkdirs();

					for (int j = 0; j < files.length; j++) {
						// TODO Делаем UNIX-сепаратор
						String fileName = dirName + "/" /* File.separator */
								+ files[j].getName();

						logger.info("parsing dicom file " + fileName);
						extractData(fileName, true, true);
					}

				}
			}

		}
	}

	/**
	 * Извлечение данных
	 * 
	 * @param needTags
	 * @param needImages
	 * @throws SQLException
	 */
	private void extractData(String file, boolean needTags, boolean needImages) throws SQLException {

		String fileName = srcDir + File.separator + file;

		DicomObject dcmObj;
		DicomInputStream din = null;
		SpecificCharacterSet cs = null;

		try {
			File f = new File(fileName);
			long fileSize = f.length();
			din = new DicomInputStream(f);
			dcmObj = din.readDicomObject();
			// System.out.println("dcmObj=" + dcmObj);

			if (charsetStr != null) {
				cs = new SpecificCharacterSet(charsetStr);
			}
			// читаем кодировку из dcm-файла
			if (charsetStr == null) {
				cs = SpecificCharacterSet.valueOf(dcmObj.get(Tag.SpecificCharacterSet)
						.getStrings(null, false));
			}

			// for (Iterator<DicomElement> it = dcmObj.iterator();
			// it.hasNext();) {
			// DicomElement element = it.next();
			//
			// int tag = element.tag();
			//
			// short ma = (short) (tag >> 16);
			// String major = Integer.toHexString(ma);
			//
			// short mi = (short) (tag);
			// String minor = Integer.toHexString(mi);
			//
			// // Object major = StringUtils.shortToHex(tag >> 16, sb);
			// // sb.append(',');
			// // Object minor = StringUtils.shortToHex(tag, sb);
			//
			// // if (tag == 524293 && tag == Tag.SpecificCharacterSet) {
			// // cs = SpecificCharacterSet.valueOf(element.getStrings(null,
			// // false));
			// // // String charset = element.getValueAsString(cs,
			// // // element.length());
			// // // cs = new SpecificCharacterSet("ISO-8859-5");
			// //
			// // System.out.println("DicomElement (" + major + "," + minor
			// // + ") {" + tag + "}  " + " [" + dcmObj.nameOf(tag)
			// // + "]  = "
			// // + element.getValueAsString(cs, element.length()));
			// // }
			//
			// if (tag == 524416 || tag == 524417 || tag == 1048592
			// || tag == 1048608) {
			//
			// System.out.println("DicomElement (" + major + "," + minor
			// + ") {" + tag + "}  " + " [" + dcmObj.nameOf(tag)
			// + "]  = "
			// + element.getValueAsString(cs, element.length()));
			//
			// }
			//
			// }

			String DCM_FILE_NAME = file;

			java.util.Date PATIENT_BIRTH_DATE;

			if (dcmObj.get(Tag.PatientBirthDate) != null) {
				PATIENT_BIRTH_DATE = dcmObj.get(Tag.PatientBirthDate).getDate(false);
			} else {
				PATIENT_BIRTH_DATE = new Date(0);
				logger.warn("Patient Birth Date (tag: PatientBirthDate) is empty!");
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
				logger.warn("Patient sex (tag: PatientSex) is empty!");
			} else {
				PATIENT_SEX = element1.getValueAsString(cs, element1.length());
				if (PATIENT_SEX.length() > 1) {
					logger.warn("PATIENT_SEX to long [" + PATIENT_SEX + "]");
					PATIENT_SEX = PATIENT_SEX.substring(0, 1);
				}
			}

			element1 = dcmObj.get(Tag.StudyID);
			String STUDY_ID = "";
			if (element1 == null) {
				logger.warn("Study ID (tag: StudyID) is empty!");
			} else {
				STUDY_ID = element1.getValueAsString(cs, element1.length());
			}

			java.util.Date STUDY_DATE = dcmObj.get(Tag.StudyDate).getDate(false);

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

			int HEIGHT = 1000;
			if(dcmObj.get(Tag.Rows) == null) {
				logger.warn("tag Rows is empty.");
			} else {
				HEIGHT = dcmObj.get(Tag.Rows).getInt(false);
			}
			
			int WIDTH = 1000;
			if(dcmObj.get(Tag.Columns) == null) {
				logger.warn("tag Columns is empty!");
			} else {
				WIDTH = dcmObj.get(Tag.Columns).getInt(false);
			}

			connection.setAutoCommit(false);
			insertUpdateCommonData(file, DCM_FILE_NAME, fileSize, PATIENT_ID, PATIENT_NAME, PATIENT_SEX,
					new java.sql.Date(PATIENT_BIRTH_DATE.getTime()), STUDY_ID, new java.sql.Date(STUDY_DATE
							.getTime()), STUDY_DOCTOR, STUDY_OPERATOR, WIDTH, HEIGHT);

			connection.commit();

		} catch (org.dcm4che2.data.ConfigurationError e) {
			if (e.getCause() instanceof UnsupportedEncodingException) {
				logger.fatal("Unsupported character set" + charsetStr + " " + e);
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

	}

	/**
	 * @param STUDY_DATE
	 * @param DCM_FILE_NAME
	 * @param IMAGE_FILE_NAME
	 * @param IMAGE_FILE_SIZE
	 * @param WIDTH
	 * @param HEIGHT
	 * @param src
	 * @param dest
	 * @throws IOException
	 * @throws SQLException
	 */
	public void convert(Date STUDY_DATE, String DCM_FILE_NAME, String IMAGE_FILE_NAME, long IMAGE_FILE_SIZE,
			int WIDTH, int HEIGHT, File src, File dest) throws IOException, SQLException {
		Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
		ImageReader reader = iter.next();
		DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
		param.setWindowCenter(center);
		param.setWindowWidth(width);
		param.setVoiLutFunction(vlutFct);
		param.setPresentationState(prState);
		param.setPValue2Gray(pval2gray);
		param.setAutoWindowing(autoWindowing);
		ImageInputStream iis = ImageIO.createImageInputStream(src);
		BufferedImage bi;
		OutputStream out = null;
		try {
			reader.setInput(iis, false);
			bi = reader.read(frame - 1, param);
			if (bi == null) {
				System.out.println("\nError: " + src + " - couldn't read!");
				return;
			}
			out = new BufferedOutputStream(new FileOutputStream(dest));
			JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(out);
			enc.encode(bi);
		} finally {
			CloseUtils.safeClose(iis);
			CloseUtils.safeClose(out);

			connection.setAutoCommit(false);
			insertImageData(DCM_FILE_NAME, "image/jpeg", IMAGE_FILE_NAME, IMAGE_FILE_SIZE, WIDTH, HEIGHT);
			updateDayStatInc(STUDY_DATE, "ALL_IMAGE_SIZE", IMAGE_FILE_SIZE);
			connection.commit();
		}
		System.out.print('.');
	}

	private Connection getConnection() throws SQLException {

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

		return conn;
	}

	/**
	 * @param file
	 *            - короткое имя файла (без корневой диретокрии)
	 * @param DCM_FILE_NAME
	 * @param DCM_FILE_SIZE
	 * @param PATIENT_ID
	 * @param PATIENT_NAME
	 * @param PATIENT_SEX
	 * @param PATIENT_BIRTH_DATE
	 * @param STUDY_ID_AS_STRING
	 *            TODO Воздвращается другим типом (не int)
	 * @param STUDY_DATE
	 * @param STUDY_DOCTOR
	 * @param STUDY_OPERATOR
	 * @param WIDTH
	 * @param HEIGHT
	 * @throws SQLException
	 * @throws IOException
	 */
	private void insertUpdateCommonData(String file, String DCM_FILE_NAME, long DCM_FILE_SIZE,
			String PATIENT_ID, String PATIENT_NAME, String PATIENT_SEX, Date PATIENT_BIRTH_DATE,
			String STUDY_ID_AS_STRING, Date STUDY_DATE, String STUDY_DOCTOR, String STUDY_OPERATOR,
			int WIDTH, int HEIGHT) throws SQLException, IOException {

		PreparedStatement stmt = null;

		logger.info("[" + DCM_FILE_NAME + "][" + PATIENT_NAME + "][" + PATIENT_BIRTH_DATE + "][" + STUDY_DATE
				+ "]");

		int STUDY_ID = -1;
		try {
			STUDY_ID = Integer.valueOf(STUDY_ID_AS_STRING);
		} catch (NumberFormatException e) {
			logger.warn("STUDY_ID not valid format (not integer) = [" + STUDY_ID + "]");
		}

		// Проверка на наличии этого файла в БД
		try {
			int id = checkDbDCMFile(DCM_FILE_NAME);
			logger.info("File already in database [" + id + "] [" + DCM_FILE_NAME + "]");
			logger.info("update data in database [" + DCM_FILE_NAME + "]");

			stmt = connection
					.prepareStatement("update WEBDICOM.DCMFILE"
							+ " SET DCM_FILE_SIZE = ? , PATIENT_NAME = ?, PATIENT_SEX = ?, PATIENT_BIRTH_DATE = ?, "
							+ " STUDY_ID =? , STUDY_DATE = ?, STUDY_DOCTOR =? , STUDY_OPERATOR = ?"
							+ " where ID = ?");

			stmt.setLong(1, DCM_FILE_SIZE);
			stmt.setString(2, PATIENT_NAME);
			stmt.setString(3, PATIENT_SEX);
			stmt.setDate(4, PATIENT_BIRTH_DATE);
			stmt.setInt(5, STUDY_ID);
			stmt.setDate(6, STUDY_DATE);
			stmt.setString(7, STUDY_DOCTOR);
			stmt.setString(8, STUDY_OPERATOR);

			stmt.setInt(9, id);

			stmt.executeUpdate();

			logger.info("skip converting image.");

		} catch (NoDataFoundException ex) {
			// Делаем вставку
			logger.info("insert data in database [" + DCM_FILE_NAME + "]");
			stmt = connection
					.prepareStatement("insert into WEBDICOM.DCMFILE"
							+ " (DCM_FILE_NAME, DCM_FILE_SIZE, PATIENT_ID, PATIENT_NAME, PATIENT_SEX, PATIENT_BIRTH_DATE,"
							+ " STUDY_ID, STUDY_DATE, STUDY_DOCTOR, STUDY_OPERATOR)"
							+ " values (?, ?, ?, ?, ?, ?, ?, ?, ? ,?)");

			stmt.setString(1, DCM_FILE_NAME);
			stmt.setLong(2, DCM_FILE_SIZE);
			stmt.setString(3, PATIENT_ID);
			stmt.setString(4, PATIENT_NAME);
			stmt.setString(5, PATIENT_SEX);
			stmt.setDate(6, PATIENT_BIRTH_DATE);
			stmt.setInt(7, STUDY_ID);
			stmt.setDate(8, STUDY_DATE);
			stmt.setString(9, STUDY_DOCTOR);
			stmt.setString(10, STUDY_OPERATOR);

			stmt.executeUpdate();

			String srcFileName = srcDir + File.separator + file;
			String dstFileName = dstDir + File.separator + file + fileExt;
			String IMAGE_FILE_NAME = file + fileExt;

			logger.info("converting image(s)... " + srcFileName + "][" + dstFileName + "]");

			File src = new File(srcFileName);
			File dest = new File(dstFileName);
			long imageFileSize = dest.length();

			updateDayStatInc(STUDY_DATE, "ALL_DCM_SIZE", DCM_FILE_SIZE);

			try {
			convert(STUDY_DATE, DCM_FILE_NAME, IMAGE_FILE_NAME, imageFileSize, WIDTH, HEIGHT, src, dest);
			} catch(Exception e) {
				logger.warn("Image not converted! " + e);
				return;
			}
				logger.info("converting image(s) success!");
			
			
		}

	}

	/**
	 * Проверка на наличии этого файла в БД
	 * 
	 * @param dcm_file_name
	 * @return
	 * @throws SQLException
	 */
	private int checkDbDCMFile(String dcm_file_name) throws SQLException {
		PreparedStatement psSelect = connection
				.prepareStatement("SELECT ID FROM WEBDICOM.DCMFILE WHERE DCM_FILE_NAME = ?");
		try {
			psSelect.setString(1, dcm_file_name);
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				return rs.getInt("ID");
			}

		} finally {
			psSelect.close();
		}
		throw new NoDataFoundException("No data");
	}

	/**
	 * Вставка информации о картинках в БД
	 * 
	 * @param dcm_file
	 * @param CONTENT_TYPE
	 * @param IMAGE_FILE_NAME
	 * @throws SQLException
	 */
	private void insertImageData(String dcm_file, String CONTENT_TYPE, String IMAGE_FILE_NAME,
			long IMAGE_FILE_SIZE, int WIDTH, int HEIGHT) throws SQLException {

		// Integer FID_DCMFILE = 0;
		//
		// PreparedStatement psSelect = connection
		// .prepareStatement("SELECT ID FROM WEBDICOM.DCMFILE WHERE DCM_FILE_NAME = ?");
		//
		// try {
		// psSelect.setString(1, dcm_file);
		// ResultSet rs = psSelect.executeQuery();
		// while (rs.next()) {
		// FID_DCMFILE = rs.getInt("ID");
		// }
		//
		// logger.info("insert data in database  [" + dcm_file + "] ID = "
		// + FID_DCMFILE);
		// } finally {
		// psSelect.close();
		// }

		Integer FID_DCMFILE = checkDbDCMFile(dcm_file);

		PreparedStatement psInsert = null;

		logger.info("insert data in database [" + FID_DCMFILE + "] image [" + IMAGE_FILE_NAME + "]");

		psInsert = connection.prepareStatement("insert into WEBDICOM.IMAGES"
				+ " (FID_DCMFILE, CONTENT_TYPE, IMAGE_FILE_NAME, IMAGE_FILE_SIZE, WIDTH, HEIGHT)"
				+ " values (?, ?, ?, ?, ?, ?)");

		psInsert.setInt(1, FID_DCMFILE);
		psInsert.setString(2, CONTENT_TYPE);
		psInsert.setString(3, IMAGE_FILE_NAME);
		psInsert.setLong(4, IMAGE_FILE_SIZE);
		psInsert.setInt(5, WIDTH);
		psInsert.setInt(6, HEIGHT);
		psInsert.executeUpdate();

	}

	/**
	 * Обновление метрики дневной статистики (инкремент)
	 * 
	 * @param date
	 * @param metric
	 * @param value
	 * @throws SQLException
	 */
	private void updateDayStatInc(Date date, String metric, long value) throws SQLException {

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
			logger.info("metric already in database [" + metric + "][" + date + "][" + valueOld + "]");

			stmt = connection.prepareStatement("update WEBDICOM.DAYSTAT " + " SET METRIC_VALUE_LONG = ? "
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
			logger.info("insert data in database [" + metric + "][" + date + "][" + value + "]");
			stmt = connection.prepareStatement("insert into WEBDICOM.DAYSTAT "
					+ " (METRIC_NAME, METRIC_DATE, METRIC_VALUE_LONG)" + " values (?, ?, ?)");

			stmt.setString(1, metric);
			stmt.setDate(2, date);
			stmt.setLong(3, value);
			stmt.executeUpdate();

			// System.out.println("!!!! [I]  [" + date + "][" + metric + "]="+
			// value);
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
