package org.psystems.dicom.browser.client.service;

import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BrowserServiceAsync {

	void findStudy(String version, String queryStr,
			AsyncCallback<DcmFileProxy[]> callback);

}
