package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

/**
 * @author dima_d
 * 
 */
public class DcmImageProxy implements Serializable {

	private static final long serialVersionUID = -7977302129675187420L;

	private Integer id; // ID
	private String fileName; // Имя файла
	private String contentType; // Тип контента
	private Integer width; // Ширина
	private Integer height; // Высота

	/**
	 * Инициализация класса
	 * 
	 * @param id
	 * @param contentType
	 * @param width
	 * @param height
	 */
	public void init(Integer id, String fileName, String contentType,
			Integer width, Integer height) {
		this.id = id;
		this.fileName = fileName;
		this.contentType = contentType;
		this.width = width;
		this.height = height;
	}

	public Integer getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "DcmImageProxy " + id + ";" + fileName + ";" + contentType + ";"
				+ width + ";" + height;
	}

}
