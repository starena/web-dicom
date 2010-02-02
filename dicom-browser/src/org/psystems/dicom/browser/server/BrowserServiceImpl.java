package org.psystems.dicom.browser.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.psystems.dicom.browser.client.BrowserService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BrowserServiceImpl extends RemoteServiceServlet implements
		BrowserService {
	
	private String connectionStr = "jdbc:derby://localhost:1527//WORKDB/WEBDICOM";

	public String test(String input) {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		Connection conn;
		try {
			conn = getConnection();
			return "Hello, " + input + "!<br><br>I am running " + serverInfo + " conn=" + conn
			+ ".<br><br>It looks like you are using:<br>" + userAgent;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userAgent;
	
	}
	
	private Connection getConnection() throws SQLException {

		Properties props = new Properties(); // connection properties
		props.put("user", "user1"); // FIXME Взять из конфига
		props.put("password", "user1"); // FIXME Взять из конфига

		Connection conn = DriverManager.getConnection(
				connectionStr + ";create=true", props);
		
		return conn;
	}
}
