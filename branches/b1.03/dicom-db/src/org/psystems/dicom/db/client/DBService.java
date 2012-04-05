package org.psystems.dicom.db.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("db")
public interface DBService extends RemoteService {
	String greetServer(String name);

	String startDB() throws DefaultGWTRPCException;

	String stopDB() throws DefaultGWTRPCException;
	
	String createDB() throws DefaultGWTRPCException;
}
