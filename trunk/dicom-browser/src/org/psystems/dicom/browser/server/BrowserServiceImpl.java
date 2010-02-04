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

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BrowserServiceImpl extends RemoteServiceServlet implements
		BrowserService {

	
	private int maxReturnRecords = 20; //Максимальное количество возвращаемых записей
	
	private static Logger logger =Logger.getLogger(BrowserServiceImpl.class);
//	static { PropertyConfigurator.configure("WEB-INF/log4j.properties");}//TODO Убрать !!!

	public String test(String input) throws DefaultGWTRPCException {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		Connection conn;
		try {
			conn = Util.getConnection(getServletContext());
			return "Hello, " + input + "!<br><br>I am running " + serverInfo
					+ " conn=" + conn
					+ ".<br><br>It looks like you are using:<br>" + userAgent;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DefaultGWTRPCException(e.getMessage());
		}

	}



	@Override
	public DcmFileProxy[] findStudy(String queryStr)
			throws DefaultGWTRPCException {

		
		
		PreparedStatement psSelect = null;
		
		try {
			
			Connection connection = Util.getConnection(getServletContext());
			

		psSelect = connection
				.prepareStatement("SELECT ID, DCM_FILE_NAME, PATIENT_NAME, PATIENT_BIRTH_DATE, " +
						" STUDY_DATE FROM WEBDICOM.DCMFILE WHERE UPPER(PATIENT_NAME) like UPPER( '%' || ? || '%')");
		
			 psSelect.setString(1, queryStr);
			ResultSet rs = psSelect.executeQuery();
			ArrayList<DcmFileProxy> data = new ArrayList<DcmFileProxy>();
			int index = 0;
			while (rs.next()) {
				DcmFileProxy proxy = new DcmFileProxy();
				proxy.init(rs.getInt("ID"), rs.getString("DCM_FILE_NAME"), 
						rs.getString("PATIENT_NAME"), rs.getDate("PATIENT_BIRTH_DATE"),
						 rs.getDate("STUDY_DATE"));
				data.add(proxy);
				if(index++ > maxReturnRecords) { break; }
				
			}
			
			DcmFileProxy[] result = data.toArray(new DcmFileProxy[data.size()]);
			return result;

		} catch (SQLException e) {
			logger.error(e);
			throw new DefaultGWTRPCException(e.getMessage());
		} finally {
			
			try {
				if(psSelect!=null) psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
				throw new DefaultGWTRPCException(e.getMessage());
			}
		}

	}

}
