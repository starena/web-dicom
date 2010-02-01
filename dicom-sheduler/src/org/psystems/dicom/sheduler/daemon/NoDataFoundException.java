package org.psystems.dicom.sheduler.daemon;

import java.sql.SQLException;

public class NoDataFoundException extends SQLException {

	public NoDataFoundException(String reason) {
		super(reason);
	}

	
}
