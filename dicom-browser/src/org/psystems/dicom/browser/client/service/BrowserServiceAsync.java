package org.psystems.dicom.browser.client.service;

import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.RPCDcmFileProxyEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BrowserServiceAsync {

	void findStudy(long transactionId, String version, String queryStr,
			AsyncCallback<RPCDcmFileProxyEvent> callback);

	

}
