package org.psystems.dicom.browser.client.service;

import java.util.ArrayList;

import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmTagProxy;
import org.psystems.dicom.browser.client.proxy.RPCDcmFileProxyEvent;
import org.psystems.dicom.browser.client.proxy.RPCRequestEvent;
import org.psystems.dicom.browser.client.proxy.RPCResponceEvent;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("browser")
public interface BrowserService extends RemoteService {

	/**
	 * Поиск исследований
	 * 
	 * @param transactionId
	 * @param version
	 * @param queryStr
	 * @return
	 * @throws DefaultGWTRPCException
	 */
	RPCDcmFileProxyEvent findStudy(long transactionId, String version,
			String queryStr) throws DefaultGWTRPCException;

	RPCResponceEvent getDcmTags(RPCRequestEvent event) throws DefaultGWTRPCException;
}
