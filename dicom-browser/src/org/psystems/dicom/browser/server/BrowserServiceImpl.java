package org.psystems.dicom.browser.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.psystems.dicom.browser.client.BrowserService;
import org.psystems.dicom.browser.client.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BrowserServiceImpl extends RemoteServiceServlet implements
		BrowserService {

	private String connectionStr = "jdbc:derby://localhost:1527//WORKDB/WEBDICOM";
	private Connection connection;

	public String test(String input) throws DefaultGWTRPCException {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		Connection conn;
		try {
			conn = getConnection();
			return "Hello, " + input + "!<br><br>I am running " + serverInfo
					+ " conn=" + conn
					+ ".<br><br>It looks like you are using:<br>" + userAgent;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		}

	}

	private Connection getConnection() throws SQLException {

		Properties props = new Properties(); // connection properties
		props.put("user", "user1"); // FIXME взять из конфига
		props.put("password", "user1"); // FIXME взять из конфига

		connection = DriverManager.getConnection(
				connectionStr + ";create=true", props);

		return connection;
	}

	@Override
	public DcmFileProxy[] findStudy(String queryStr)
			throws DefaultGWTRPCException {

		PreparedStatement psSelect = null;
		
		try {
			
		if (connection == null)
			getConnection();// FIXME Сделать получение соединения через pool

		psSelect = connection
				.prepareStatement("SELECT ID, DCM_FILE_NAME, PATIENT_NAME, PATIENT_BIRTH_DATE, " +
						" STUDY_DATE FROM WEBDICOM.DCMFILE");
		
			// psSelect.setString(1, dcm_file_name);
			ResultSet rs = psSelect.executeQuery();
			ArrayList<DcmFileProxy> data = new ArrayList<DcmFileProxy>();
			while (rs.next()) {
				DcmFileProxy proxy = new DcmFileProxy();
				proxy.init(rs.getInt("ID"), rs.getString("DCM_FILE_NAME"), 
						rs.getString("PATIENT_NAME"), rs.getDate("PATIENT_BIRTH_DATE"),
						 rs.getDate("STUDY_DATE"));
				data.add(proxy);
				
			}
			
			DcmFileProxy[] result = data.toArray(new DcmFileProxy[data.size()]);
			return result;

		} catch (SQLException e) {
			throw new DefaultGWTRPCException(e.getMessage());
		} finally {
			
			try {
				if(psSelect!=null) psSelect.close();
			} catch (SQLException e) {
				//FIXME Выдать сообщение в logger !!!!
				throw new DefaultGWTRPCException(e.getMessage());
			}
		}

	}

}
