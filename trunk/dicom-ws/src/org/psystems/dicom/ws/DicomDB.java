package org.psystems.dicom.ws;

public abstract class DicomDB {

	private org.psystems.dicom.ws.Study[] study;

	public abstract org.psystems.dicom.ws.Study getStudy(int i);
}