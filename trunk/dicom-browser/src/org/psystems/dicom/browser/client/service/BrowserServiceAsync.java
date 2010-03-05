package org.psystems.dicom.browser.client.service;

import java.util.ArrayList;

import org.psystems.dicom.browser.client.proxy.DcmTagProxy;
import org.psystems.dicom.browser.client.proxy.RPCDcmFileProxyEvent;
import org.psystems.dicom.browser.client.proxy.RPCRequestEvent;
import org.psystems.dicom.browser.client.proxy.RPCResponceEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BrowserServiceAsync {

	void findStudy(long transactionId, String version, String queryStr,
			AsyncCallback<RPCDcmFileProxyEvent> callback);

	void getDcmTags(RPCRequestEvent event,
			AsyncCallback<RPCResponceEvent> callback);

	void getDcmTagsFromFile(long transactionId, String version, int idDcmFile,
			AsyncCallback<ArrayList<DcmTagProxy>> callback);

}
