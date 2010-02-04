package org.psystems.dicom.browser.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

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

	private static Logger logger = Logger.getLogger(AttachementServlet.class
			.getName());
	/**
	 * @param servletContext
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(ServletContext servletContext)
			throws SQLException {

		Connection connection = null;

	
		//
		String connectionUrl = servletContext
				.getInitParameter("webdicom.connection.url");
		if (connectionUrl != null) {
			 Properties props = new Properties(); // connection properties
			 props.put("user", "user1"); // FIXME взять из конфига
			 props.put("password", "user1"); // FIXME взять из конфига
			
			 connection = DriverManager.getConnection(
					 connectionUrl + ";create=true", props);
		} else {
			// for Tomcat
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				DataSource ds = (DataSource) envCtx.lookup("jdbc/webdicom");
				connection = ds.getConnection();
			} catch (NamingException e) {
				throw new SQLException("JNDI error " + e);
			}
		}

		return connection;
	}
}
