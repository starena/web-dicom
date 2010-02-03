package org.psystems.dicom.browser.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Утилитный класс
 * 
 * @author dima_d
 * 
 */
public class Util {

	// static Connection connection;
	// static String connectionStr =
	// "jdbc:derby://localhost:1527//WORKDB/WEBDICOM";

	public static Connection getConnection() throws SQLException {

		// Properties props = new Properties(); // connection properties
		// props.put("user", "user1"); // FIXME взять из конфига
		// props.put("password", "user1"); // FIXME взять из конфига
		//
		// connection = DriverManager.getConnection(
		// connectionStr + ";create=true", props);
		//

		// for Tomcat
		Connection connection = null;

		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/webdicom");
			connection = ds.getConnection();
		} catch (NamingException e) {
			throw new SQLException("JNDI error " + e);
		}

		return connection;
	}
}
