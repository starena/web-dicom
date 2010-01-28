package org.psystems.dicom.sheduler.daemon;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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

	private String srcDir;
	private String dstDir;

	private final String USAGE = "sheduler [Options] SOURCE DEST\n";

	private final String DESCRIPTION = "Convert DICOM file SOURCE to DEST and "
			+ " verify data in URL dababase.\nOptions:";

	private final String EXAMPLE = "\nExample: dcm2dcm in.dcm out.dcm\n"
			+ " => Decode DICOM object from DICOM file in.dcm and encode it with"
			+ " Implicit VR Little Endian Transfer Syntax to DICOM file out.dcm.";

	private String VERSION = "0.1a";
	
	private String protocol = "jdbc:derby://localhost:1527//WORKDB/";
	String dbName = "WEBDICOM"; // the name of the database
	private Connection connection;



	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new Sheduler();
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

		opts
				.addOption(OptionBuilder.withLongOpt("source-dir")
						.withDescription("use PATH source dir").hasArg()
						.withArgName("PATH").create("s"));

		opts.addOption(OptionBuilder.withLongOpt("dest-dir").withDescription(
				"use PATH destination dir").hasArg().withArgName("PATH")
				.create("d"));

		opts.addOption(OptionBuilder.withLongOpt("connection").withDescription(
				"use URL for JDBC connector").hasArg().withArgName("URL")
				.create("c"));

		opts.addOption(OptionBuilder.withLongOpt("daemon").withDescription(
				"run sheduler as daemon").create());

		opts.addOption("h", "help", false, "print this message");
		opts.addOption("V", "version", false,
				"print the version information and exit");

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

	public Sheduler() {

		String[] args = new String[] { "-d test\\testdata\\out",
				"--source-dir=test\\testdata\\2009-12-16" };

		CommandLine cl = parse(args);

		if (cl.hasOption("source-dir")) {
			srcDir = cl.getOptionValue("source-dir").trim();
			System.out.println("srcDir=[" + srcDir + "]");
		}

		if (cl.hasOption("dest-dir")) {
			dstDir = cl.getOptionValue("dest-dir").trim();
			System.out.println("dstDir=[" + dstDir + "]");
		}

		try {
			connection =  getConnection();
			iterateFiles();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	/**
	 * Обход папок с файлами
	 * @throws SQLException 
	 */
	private void iterateFiles() throws SQLException {

		// FIXME Сделать через календарь
		final String currentDateStr = "2009-12-16";
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
				if (name.endsWith(".dcm")) {
					return true;
				}
				return false;
			}

		};

		// TODO пока 2-х уровневая организация папок, потом сделать это
		// рекурсивно. задается через регулярное выражение
		File rootDir = new File(srcDir);
		if (rootDir.isDirectory()) {
			File[] dirs = rootDir.listFiles(filterDir);
			for (int i = 0; i < dirs.length; i++) {
				if (dirs[i].isDirectory()) {
					String dirName = dirs[i].getName();
					File[] files = dirs[i].listFiles(filter);
					
					//создаем рекурсивно директории
					new File(dstDir + File.separator + dirs[i].getName()).mkdirs();
					
					for (int j = 0; j < files.length; j++) {
						String fileName = dirName + File.separator
								+ files[j].getName();

						System.out.println("FILE:" + fileName);
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
		try {

			// din = new DicomInputStream(new File("demo/Im00001.dcm"));
			// din = new DicomInputStream(new File("demo/6185.bin"));
			din = new DicomInputStream(new File(fileName));
			dcmObj = din.readDicomObject();
			// System.out.println("dcmObj=" + dcmObj);

			DicomObjectToStringParam param = DicomObjectToStringParam
					.getDefaultParam();

			SpecificCharacterSet cs = null;

			for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
				DicomElement element = it.next();

				int tag = element.tag();

				short ma = (short) (tag >> 16);
				String major = Integer.toHexString(ma);

				short mi = (short) (tag);
				String minor = Integer.toHexString(mi);

				// Object major = StringUtils.shortToHex(tag >> 16, sb);
				// sb.append(',');
				// Object minor = StringUtils.shortToHex(tag, sb);

				if (tag == 524293 && tag == Tag.SpecificCharacterSet) {
					cs = SpecificCharacterSet.valueOf(element.getStrings(null,
							false));
					// String charset = element.getValueAsString(cs,
					// element.length());
					// cs = new SpecificCharacterSet("ISO-8859-5");

					System.out.println("DicomElement (" + major + "," + minor
							+ ") {" + tag + "}  " + " [" + dcmObj.nameOf(tag)
							+ "]  = "
							+ element.getValueAsString(cs, element.length()));
				}

				if (tag == 524416 || tag == 524417 || tag == 1048592
						|| tag == 1048608) {

					System.out.println("DicomElement (" + major + "," + minor
							+ ") {" + tag + "}  " + " [" + dcmObj.nameOf(tag)
							+ "]  = "
							+ element.getValueAsString(cs, element.length()));

				}

				// if (tag == 2145386512) {
				//
				// System.out.println("length=" + element.length());
				// System.out.println("DicomElement (" + major + "," + minor
				// + ") {" + tag + "}  " + " [" + dcmObj.nameOf(tag)
				// + "]  = " + element.getValueAsString(cs, 100));
				// // https://jai-imageio.dev.java.net/binary-builds.html
				//
				// // byte[] rlePixelData = element.getFragment(1);
				// // System.out.println("DicomElement IMAGE" +
				// // rlePixelData.length);
				//
				// }
				
				
				
				
				
			

			}
			
			String DCM_FILE_NAME = file;
			cs = SpecificCharacterSet.valueOf(dcmObj.get(Tag.SpecificCharacterSet).getStrings(null, false));
			java.util.Date PATIENT_BIRTH_DATE = dcmObj.get(Tag.PatientBirthDate).getDate(false);
			java.util.Date STUDY_DATE = dcmObj.get(Tag.StudyDate).getDate(false);
			DicomElement element1 = dcmObj.get(Tag.PatientName); 
			String PATIENT_NAME = element1.getValueAsString(cs, element1.length());
			
			
			connection.setAutoCommit(false);
			
			
			insertData(DCM_FILE_NAME, PATIENT_NAME, new java.sql.Date(PATIENT_BIRTH_DATE.getTime()) ,
					new java.sql.Date(STUDY_DATE.getTime()));
			
			connection.commit();
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}

		String srcFileName = srcDir + File.separator + file;
		String dstFileName = dstDir + File.separator + file + fileExt;

		try {
			System.out.print("converting image...");

			// File src = new File("demo/Im00001.dcm");
			// File dest = new File("demo/Im00001.jpg");
			File src = new File(srcFileName);
			File dest = new File(dstFileName);
			
			
			
			convert(src, dest);
			System.out.println("success!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void convert(File src, File dest) throws IOException {
		Iterator<ImageReader> iter = ImageIO
				.getImageReadersByFormatName("DICOM");
		ImageReader reader = iter.next();
		DicomImageReadParam param = (DicomImageReadParam) reader
				.getDefaultReadParam();
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
		}
		System.out.print('.');
	}
	
	private Connection getConnection() throws SQLException {
		

		Properties props = new Properties(); // connection properties
		// providing a user name and password is optional in the embedded
		// and derbyclient frameworks
		props.put("user", "user1"); //FIXME Взять из конфига
		props.put("password", "user1"); // FIXME Взять из конфига

		
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);
//		conn.setAutoCommit(false);
//		s = conn.createStatement();
//		s.execute(sql);
//
//		conn.commit();
		
		return conn;
	}
	
	private void insertData(String DCM_FILE_NAME, String PATIENT_NAME, Date PATIENT_BIRTH_DATE,
			Date STUDY_DATE) throws SQLException {
		PreparedStatement psInsert = null;
		
		System.out.println("!!! ["+DCM_FILE_NAME + "]["+PATIENT_NAME+"]");

		Properties props = new Properties(); // connection properties
		props.put("user", "user1");
		props.put("password", "user1");
		String dbName = "derbyDBTEST"; // the name of the database
	

		psInsert = connection.prepareStatement("insert into WEBDICOM.DCMFILES"
				+ " (DCM_FILE_NAME, PATIENT_BIRTH_DATE, PATIENT_NAME, STUDY_DATE)" + " values (?, ?, ?, ?)");

		psInsert.setString(1, DCM_FILE_NAME);
		psInsert.setDate(2, PATIENT_BIRTH_DATE);
		psInsert.setString(3, PATIENT_NAME);
		psInsert.setDate(4, STUDY_DATE);

		psInsert.executeUpdate();

		
	}

}
