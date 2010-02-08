package org.psystems.dicom.browser.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.ItemSuggestService;
import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ItemSuggestServiceImpl extends RemoteServiceServlet implements
		ItemSuggestService {
	
	private static Logger logger = Logger.getLogger(ItemSuggestServiceImpl.class
			.getName());
	
	public SuggestOracle.Response getSuggestions(SuggestOracle.Request req) {
		SuggestOracle.Response resp = new SuggestOracle.Response();

		// Create a list to hold our suggestions (pre-set the lengthto the limit
		// specified by the request)
		
		int limit = req.getLimit();
		String queryStr = req.getQuery();
		
//		System.out.println("!!! queryStr=["+queryStr+"]");
		List<Suggestion> suggestions = new ArrayList<Suggestion>(req.getLimit());

		// Replace the code below with something to create and popular
		// ItemSuggestion objects
		// This is where all your magic should happen to find matches and create
		// the suggestion list, usually from a database
		// The dummy code below creates bogus suggestions "Suggestion1",
		// "Suggestion 2", etc..
		
//		System.out.println("!!!!! " + req.getQuery());
		
//		for (int i = 1; i < 11; i++) {
//			suggestions.add(new ItemSuggestion("Suggestion " + i, "Suggestion "
//					+ i));
//		}
//		
		
		PreparedStatement psSelect = null;

		try {

			Connection connection = org.psystems.dicom.browser.server.Util.getConnection(getServletContext());
			//

			
			psSelect = connection
					.prepareStatement("SELECT ID, DCM_FILE_NAME, PATIENT_NAME, PATIENT_BIRTH_DATE, "
							+ " STUDY_DATE FROM WEBDICOM.DCMFILE WHERE UPPER(PATIENT_NAME) like UPPER(? || '%')");

			psSelect.setString(1, queryStr);
			ResultSet rs = psSelect.executeQuery();
			ArrayList<DcmFileProxy> data = new ArrayList<DcmFileProxy>();
			int index = 0;
			while (rs.next()) {
				
				String name = rs.getString("PATIENT_NAME");
				String date = ""+rs.getDate("PATIENT_BIRTH_DATE");
				suggestions.add(new ItemSuggestion(name + " [" + date + "]", name));
			
				if (index++ > limit) {
					break;
				}

			}
			rs.close();


		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
//			throw new DefaultGWTRPCException(e.getMessage());
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
//				throw new DefaultGWTRPCException(e.getMessage());
			}
		}

		// Now set the suggestions in the response
		resp.setSuggestions(suggestions);

		// Send the response back to the client
		return resp;
	}

}