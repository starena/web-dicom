package org.psystems.dicom.browser.client;

import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("browser")
public interface BrowserService extends RemoteService {
	
	String test(String name) throws DefaultGWTRPCException;
	
	/**
	 * Поиск исследований
	 * 
	 * @param queryStr
	 * @return
	 * @throws DefaultGWTRPCException
	 */
	DcmFileProxy[] findStudy(String queryStr) throws DefaultGWTRPCException;
}
