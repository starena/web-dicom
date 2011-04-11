package org.psystems.dicom.commons.orm;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Запрос на исследование
 * 
 * @author dima_d
 * 
 */
public class QueryDirection {

	private static final long serialVersionUID = -2840335603832244555L;
	private Long id; // Внутренний ID
	private String directionId; // штрих код
	private Date dateDirection;// Дата направления

	public static SimpleDateFormat formatSQL = new SimpleDateFormat(
			"yyyy-MM-dd");

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDirectionId() {
		return directionId;
	}

	public void setDirectionId(String directionId) {
		this.directionId = directionId;
	}

	// public Date getDateDirection() {
	// return dateDirection;
	// }

	/**
	 * @return Формат SQL Date - "гггг.дд.мм"
	 */
	public String getDateDirectionAsString() {
		return formatSQL.format(dateDirection);
	}

	// public void setDateDirection(Date dateDirection) {
	// this.dateDirection = dateDirection;
	// }

	/**
	 * Формат SQL Date - "гггг.дд.мм"
	 * 
	 * @param date
	 */
	public void setDateDirectionAsString(String date) {
		this.dateDirection = java.sql.Date.valueOf(formatSQL.format(date));
	}

}
