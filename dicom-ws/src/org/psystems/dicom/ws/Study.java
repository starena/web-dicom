package org.psystems.dicom.ws;

public abstract class Study {

	private int id;
	private String description;

	public abstract void setID(int id);

	public abstract void setDescription(String name);

	public abstract int getID();

	public abstract String getDescription();
}