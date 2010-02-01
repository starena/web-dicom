package org.psystems.dicom.sheduler.daemon;

import java.sql.SQLException;

public class NoDataFoundEsception extends SQLException {

	public NoDataFoundEsception(String reason) {
		super(reason);
	}

	
}
