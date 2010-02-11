package org.psystems.dicom.browser.client.service;

import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;

public interface ItemSuggestServiceAsync {

	void getSuggestions(long transactionId, String version, Request req,
			AsyncCallback<SuggestTransactedResponse> callback);

}