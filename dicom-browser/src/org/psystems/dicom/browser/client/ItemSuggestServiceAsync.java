package org.psystems.dicom.browser.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public interface ItemSuggestServiceAsync {

	public void getSuggestions(SuggestOracle.Request req, AsyncCallback callback);

}