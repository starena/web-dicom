package org.psystems.dicom.commons.orm;


/**
 * Запрос направлений
 * 
 * @author dima_d
 * 
 */
public class QueryDirection {

	private static final long serialVersionUID = -2840335603832244555L;
	private Long id; // Внутренний ID
	private String directionId; // штрих код
	private String dateDirection;// Дата направления

	private String patientId; // ID пациента
	private String patientName; // ФИО пациента
	private String patientSex; // Пол пациента (M/F)
	private String patientBirthDate; // Дата рождения пациента (0016,0048) DA

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

	public String getDateDirection() {
		return dateDirection;
	}

	
	/**
	 * @param dateDirection Формат SQL Date - "гггг.дд.мм"
	 */
	public void setDateDirection(String dateDirection) {
		ORMUtil.dateSQLToUtilDate(dateDirection);
		this.dateDirection = dateDirection;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	
	public String getPatientBirthDate() {
		return patientBirthDate;
	}

	
	/**
	 * @param patientBirthDate Формат SQL Date - "гггг.дд.мм"
	 */
	public void setPatientBirthDate(String patientBirthDate) {
		ORMUtil.dateSQLToUtilDate(patientBirthDate);
		this.patientBirthDate = patientBirthDate;
	}

	@Override
	public String toString() {
		return "QueryDirection [dateDirection=" + dateDirection
				+ ", directionId=" + directionId + ", id=" + id
				+ ", patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + "]";
	}
	
	

}
