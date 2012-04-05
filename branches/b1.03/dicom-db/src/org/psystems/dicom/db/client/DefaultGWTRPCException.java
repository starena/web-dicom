package org.psystems.dicom.db.client;

import java.io.Serializable;

public class DefaultGWTRPCException extends Exception implements Serializable {

	private static final long serialVersionUID = -996480671051412057L;
	private String text;

	public DefaultGWTRPCException() {
	}

	public DefaultGWTRPCException(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}