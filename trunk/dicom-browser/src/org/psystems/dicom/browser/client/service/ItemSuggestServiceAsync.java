package org.psystems.dicom.browser.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;

public interface ItemSuggestServiceAsync {

	void getSuggestions(String version, Request req,
			AsyncCallback<Response> callback);

}