package org.psystems.dicom.browser.client.service;

import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BrowserServiceAsync {

	void test(String name, AsyncCallback<String> callback);

	void findStudy(String queryStr, AsyncCallback<DcmFileProxy[]> callback);

}
