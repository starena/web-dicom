package org.psystems.dicom.browser.client.exception;


public class VersionGWTRPCException extends DefaultGWTRPCException  {

	private static final long serialVersionUID = -6394105356332764521L;
	private String text;

	public VersionGWTRPCException() {
	}

	public VersionGWTRPCException(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}