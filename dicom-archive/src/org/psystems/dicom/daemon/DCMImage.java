package org.psystems.dicom.daemon;

public class DCMImage {

	private int width;
	private int height;
	private long length;

	public DCMImage(int width, int height, long length) {
		super();
		this.width = width;
		this.height = height;
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public long getLength() {
		return length;
	}

}
