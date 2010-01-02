package org.psystems.dicom.sheduler.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("sheduler")
public interface ShedulerService extends RemoteService {
	String greetServer(String name);

	String startDB();
	
	String stopDB();
}
