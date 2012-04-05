package org.psystems.dicom.sheduler.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.derby.drda.NetworkServerControl;
import org.psystems.dicom.sheduler.client.ShedulerService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ShedulerServiceImpl extends RemoteServiceServlet implements ShedulerService {

	// http://db.apache.org/derby/docs/dev/adminguide/adminguide-single.html#cadminov17524
	// http://db.apache.org/derby/javadoc/publishedapi/jdbc4/org/apache/derby/drda/NetworkServerControl.html
	// private String framework = "embedded";
	// private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String driver = "org.apache.derby.jdbc.ClientDriver";
	// private String protocol = "jdbc:derby:ttt/";

	private String protocol = "jdbc:derby://localhost:1527/ttttt/";
	private String rootDicomFilesDir = "/WORK/workspace/dicom-sheduler/test/testdata/2009-12-16/2009-12-16";// FIXME

	// Считать
	// из
	// конфига

	public String greetServer(String input) {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		startDB();
		loadDriver();
		try {
			createDb();

			File rootDir = new File(rootDicomFilesDir);
			if (rootDir.isDirectory()) {

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
				File[] files = rootDir.listFiles(filter);
				for (int i = 0; i < files.length; i++) {
					System.out.println("FILE=" + files[i]);
					try {

						// DCMUtil.convert(files[i], new
						// DCMUtil.printTags(files[i]);

						DicomObjectWrapper proxy = DCMUtil.getDCMObject(rootDir, files[i]);

						insertData(proxy.getDCM_FILE_NAME(), proxy.getPATIENT_NAME(), proxy
								.getPATIENT_BIRTH_DATE(), proxy.getSTUDY_DATE());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} catch (SQLException sqle) {
			printSQLException(sqle);
		}

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;

	}

	@Override
	public String startDB() {
		NetworkServerControl server;
		try {
			server = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
			server.start(null);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "STARTED";
	}

	@Override
	public String stopDB() {
		NetworkServerControl server;
		try {
			server = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
			server.shutdown();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "stopped";
	}

	private void createDb() throws SQLException {

		try {
			Reader reader = new InputStreamReader(new FileInputStream("db.sql"), "WINDOWS-1251");
			BufferedReader d = new BufferedReader(reader);
			String sql = "";
			String s1 = d.readLine();
			while (s1 != null) {
				s1 = d.readLine();
				if (s1 != null)
					sql += s1 + "\n";
			}

			createSchema(sql);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void insertData(String DCM_FILE_NAME, String PATIENT_NAME, Date PATIENT_BIRTH_DATE,
			Date STUDY_DATE) throws SQLException {
		Connection conn = null;
		PreparedStatement psInsert = null;

		Properties props = new Properties(); // connection properties
		props.put("user", "user1");
		props.put("password", "user1");
		String dbName = "derbyDBTEST"; // the name of the database
		conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);
		conn.setAutoCommit(false);

		psInsert = conn.prepareStatement("insert into webdicom.dcmfile"
				+ " (DCM_FILE_NAME, PATIENT_BIRTH_DATE, PATIENT_NAME, STUDY_DATE)" + " values (?, ?, ?, ?)");

		psInsert.setString(1, DCM_FILE_NAME);
		psInsert.setDate(2, PATIENT_BIRTH_DATE);
		psInsert.setString(3, PATIENT_NAME);
		psInsert.setDate(4, STUDY_DATE);

		psInsert.executeUpdate();

		conn.commit();
	}

	private void createSchema(String sql) throws SQLException {
		Connection conn = null;
		Statement s = null;

		Properties props = new Properties(); // connection properties
		// providing a user name and password is optional in the embedded
		// and derbyclient frameworks
		props.put("user", "user1"); // FIXME Считать из конфига
		props.put("password", "user1"); // FIXME Считать из конфига

		String dbName = "derbyDBTEST"; // the name of the database
		conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);

		conn.setAutoCommit(false);
		s = conn.createStatement();
		s.execute(sql);

		conn.commit();
	}

	private void loadDriver() {

		try {
			Class.forName(driver).newInstance();
			System.out.println("Loaded the appropriate driver");
		} catch (ClassNotFoundException cnfe) {
			System.err.println("\nUnable to load the JDBC driver " + driver);
			System.err.println("Please check your CLASSPATH.");
			cnfe.printStackTrace(System.err);
		} catch (InstantiationException ie) {
			System.err.println("\nUnable to instantiate the JDBC driver " + driver);
			ie.printStackTrace(System.err);
		} catch (IllegalAccessException iae) {
			System.err.println("\nNot allowed to access the JDBC driver " + driver);
			iae.printStackTrace(System.err);
		}
	}

	public static void printSQLException(SQLException e) {
		// Unwraps the entire exception chain to unveil the real cause of the
		// Exception.
		while (e != null) {
			System.err.println("\n----- SQLException -----");
			System.err.println("  SQL State:  " + e.getSQLState());
			System.err.println("  Error Code: " + e.getErrorCode());
			System.err.println("  Message:    " + e.getMessage());
			// for stack traces, refer to derby.log or uncomment this:
			// e.printStackTrace(System.err);
			e = e.getNextException();
		}
	}
}
