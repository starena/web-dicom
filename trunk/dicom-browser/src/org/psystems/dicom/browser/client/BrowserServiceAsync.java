package org.psystems.dicom.browser.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BrowserServiceAsync {

	void test(String name, AsyncCallback<String> callback);

}
