package org.psystems.dicom.browser.server;

import java.sql.SQLException;

public class NoDataFoundException extends SQLException {

	public NoDataFoundException(String reason) {
		super(reason);
	}

	
}
