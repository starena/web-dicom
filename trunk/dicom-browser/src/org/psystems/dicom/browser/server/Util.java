package org.psystems.dicom.browser.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Утилитный класс
 * @author dima_d
 *
 */
public class Util {

	static Connection connection;
	static String connectionStr = "jdbc:derby://localhost:1527//WORKDB/WEBDICOM";

	public static Connection getConnection() throws SQLException {

		Properties props = new Properties(); // connection properties
		props.put("user", "user1"); // FIXME взять из конфига
		props.put("password", "user1"); // FIXME взять из конфига

		connection = DriverManager.getConnection(
				connectionStr + ";create=true", props);

		return connection;
	}
}
