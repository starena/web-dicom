package org.psystems.dicom.sheduler.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ShedulerServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback);

	void startDB(AsyncCallback<String> callback);

	void stopDB(AsyncCallback<String> callback);

}
