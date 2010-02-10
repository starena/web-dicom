package org.psystems.dicom.browser.client.service;

import org.psystems.dicom.browser.client.DefaultGWTRPCException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.SuggestOracle;

public interface ItemSuggestService extends RemoteService {

	public static final String SERVICE_URI = "services/itemsuggestservice";

	public static class Util {

		public static ItemSuggestServiceAsync getInstance() throws DefaultGWTRPCException {

			ItemSuggestServiceAsync instance = (ItemSuggestServiceAsync) GWT
					.create(ItemSuggestService.class);
			ServiceDefTarget target = (ServiceDefTarget) instance;
			target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
			return instance;
		}
	}

	public SuggestOracle.Response getSuggestions(SuggestOracle.Request req) throws DefaultGWTRPCException;

}