package org.psystems.dicom.db.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.derby.drda.NetworkServerControl;
import org.psystems.dicom.db.client.DBService;
import org.psystems.dicom.db.client.DefaultGWTRPCException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DBServiceImpl extends RemoteServiceServlet implements DBService {

//	private String driver = "org.apache.derby.jdbc.ClientDriver";
	private String protocol = "jdbc:derby://localhost:1527//WORKDB/";
	String dbName = "WEBDICOM"; // the name of the database

	public String greetServer(String input) {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	@Override
	public String startDB() throws DefaultGWTRPCException {
		NetworkServerControl server;
		try {
			server = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
			server.start(null);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		}
		return "DB STARTED";
	}

	@Override
	public String stopDB() throws DefaultGWTRPCException {
		NetworkServerControl server;
		try {
			server = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
			server.shutdown();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		}

		return "DB STOPPED";
	}

	@Override
	public String createDB() throws DefaultGWTRPCException {
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
			throw new DefaultGWTRPCException(e.getMessage());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
			throw new DefaultGWTRPCException(e.getMessage());
		}

		return "DB CREATED";
	}

	private void createSchema(String sql) throws SQLException {
		Connection conn = null;
		Statement s = null;

		Properties props = new Properties(); // connection properties
		// providing a user name and password is optional in the embedded
		// and derbyclient frameworks
		props.put("user", "user1"); //FIXME Взять из конфига
		props.put("password", "user1"); // FIXME Взять из конфига

		
		conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);

		conn.setAutoCommit(false);
		s = conn.createStatement();
		s.execute(sql);

		conn.commit();
	}

//	private void loadDriver() {
//
//		try {
//			Class.forName(driver).newInstance();
//			System.out.println("Loaded the appropriate driver");
//		} catch (ClassNotFoundException cnfe) {
//			System.err.println("\nUnable to load the JDBC driver " + driver);
//			System.err.println("Please check your CLASSPATH.");
//			cnfe.printStackTrace(System.err);
//		} catch (InstantiationException ie) {
//			System.err.println("\nUnable to instantiate the JDBC driver " + driver);
//			ie.printStackTrace(System.err);
//		} catch (IllegalAccessException iae) {
//			System.err.println("\nNot allowed to access the JDBC driver " + driver);
//			iae.printStackTrace(System.err);
//		}
//	}

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
