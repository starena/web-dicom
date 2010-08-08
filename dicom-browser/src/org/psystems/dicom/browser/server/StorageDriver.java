package org.psystems.dicom.browser.server;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;

import org.psystems.dicom.browser.server.drv.Dicom;
import org.psystems.dicom.browser.server.drv.OMITS;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class StorageDriver {

	/**
	 * Метод возврата результатов поиска в выпадающем поисковом списке
	 * 
	 * @param context
	 * @param queryStr
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	public static List<Suggestion> getSuggestions(ServletContext context,
			String queryStr, int limit) throws SQLException {

		List<Suggestion> suggestions = Dicom.getSuggestions(context, queryStr,
				limit);
		
//		List<Suggestion> suggestions1 = OMITS.getSuggestions(context, queryStr,
//				limit);
//		
//		suggestions.addAll(suggestions1);

		return suggestions;
	}

}
