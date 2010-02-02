package org.psystems.dicom.browser.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("browser")
public interface BrowserService extends RemoteService {
	String test(String name) throws DefaultGWTRPCException;
}
