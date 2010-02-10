package org.psystems.dicom.browser.client.service;

import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("browser")
public interface BrowserService extends RemoteService {

	/**
	 * Поиск исследований
	 * @param version TODO
	 * @param queryStr
	 * 
	 * @return
	 * @throws DefaultGWTRPCException
	 */
	DcmFileProxy[] findStudy(String version, String queryStr) throws DefaultGWTRPCException;
}
