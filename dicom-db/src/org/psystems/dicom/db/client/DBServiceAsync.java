package org.psystems.dicom.db.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DBServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback);

	void startDB(AsyncCallback<String> callback);

	void stopDB(AsyncCallback<String> callback);

	void createDB(AsyncCallback<String> callback);
}
