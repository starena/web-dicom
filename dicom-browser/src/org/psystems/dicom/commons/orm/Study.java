/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    Russian translation <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
     
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
 */

package org.psystems.dicom.commons.orm;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class Study {

	private Long id; // Внутренний ID
	private String patientId; // ID пациента (0016,0032) LO 'Patient ID' =
	// LAB_ID64936
	private String patientName; // ФИО пациента (0016,0016) PN 'Patient's Name'
	// = Ги****тди***ва И Ф
	private String patientShortName; // КБП пациента

	private String patientSex; // Пол пациента (0016,0064) CS "Patient's Sex" =
	// M/F
	private Date patientBirthDate; // Дата рождения пациента (0016,0048) DA
	// "Patient's Birth Date" = 19670811
	private String studyId; // ID исследования (0032,0016) SH 'Study ID' = 89729
	private String studyInstanceUID; // UID исследования (0032,0013) UI 'Study
	// Instance UID' =
	// 1.2.826.0.1.3680043.2.634.30.1.89729.20100305113905
	private Date studyDate; // Дата исследования (0008,0032) DA 'Study Date' =
	// 20100225
	private String studyDoctor; // Врач исследования (0008,0144) PN
	// "Referring Physician's Name" = Куницкий В.Н
	private String studyOperator; // Оператор исследования (0008,4208) PN
	// "Operators' Name" = Гаврилова Н.Г.
	private Date studyViewprotocolDate;// Дата описания исследования.
	private String studyType;// Вид исследования.
	private String studyDescription;// Описание исследования.
	private String studyModality;// модальность (0008,0060).
	private String studyViewprotocol;// Протокол осмотра
	private String studyResult;// Результат исследования.
	private String ManufacturerModelUID; // UID Аппарата (0002,0003) UI
	// "Media Storage SOP Instance UID"
	// =
	// 1.2.826.0.1.3680043.2.634.0.18669.2010225.1416.1
	private String ManufacturerModelName; // Имя Аппарата (0008,4240) LO
	// "Manufacturer's Model Name" =
	// КРТ-Электрон
	private String studyUrl; // URL для открытия в обозревателе
	private Long[] dcmFiles; // Связанные DCM-файлы
	private String dateFormat = "yyyy-MM-dd";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPatientShortName() {
		return patientShortName;
	}

	public void setPatientShortName(String patientShortName) {
		this.patientShortName = patientShortName;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public Date getPatientBirthDate() {
		return patientBirthDate;
	}

	/**
	 * @return 'YYYY-MM-DD'
	 */
	public String getPatientBirthDateAsString() {
		if (patientBirthDate == null)
			return null;
		SimpleDateFormat formatLevel = new SimpleDateFormat(dateFormat);
		return formatLevel.format(patientBirthDate);
	}

	public void setPatientBirthDate(Date patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}

	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}

	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}

	public Date getStudyDate() {
		return studyDate;
	}

	/**
	 * @return 'YYYY-MM-DD'
	 */
	public String getStudyDateAsString() {
		if (studyDate == null)
			return null;
		SimpleDateFormat formatLevel = new SimpleDateFormat(dateFormat);
		return formatLevel.format(studyDate);
	}

	public void setStudyDate(Date studyDate) {
		this.studyDate = studyDate;
	}

	public String getStudyDoctor() {
		return studyDoctor;
	}

	public void setStudyDoctor(String studyDoctor) {
		this.studyDoctor = studyDoctor;
	}

	public String getStudyOperator() {
		return studyOperator;
	}

	public void setStudyOperator(String studyOperator) {
		this.studyOperator = studyOperator;
	}


	public Date getStudyViewprotocolDate() {
		return studyViewprotocolDate;
	}

	/**
	 * @return 'YYYY-MM-DD'
	 */
	public String getStudyViewprotocolDateAsString() {
		if (studyViewprotocolDate == null)
			return null;
		SimpleDateFormat formatLevel = new SimpleDateFormat(dateFormat);
		return formatLevel.format(studyViewprotocolDate);
	}

	

	public void setStudyViewprotocolDate(Date studyViewprotocolDate) {
		this.studyViewprotocolDate = studyViewprotocolDate;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public String getStudyModality() {
		return studyModality;
	}

	public void setStudyModality(String studyModality) {
		this.studyModality = studyModality;
	}
	
	

	public String getStudyDescription() {
		return studyDescription;
	}

	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String getStudyViewprotocol() {
		return studyViewprotocol;
	}

	public void setStudyViewprotocol(String studyViewprotocol) {
		this.studyViewprotocol = studyViewprotocol;
	}

	public String getStudyResult() {
		return studyResult;
	}

	public void setStudyResult(String studyResult) {
		this.studyResult = studyResult;
	}

	public String getManufacturerModelUID() {
		return ManufacturerModelUID;
	}

	public void setManufacturerModelUID(String manufacturerModelUID) {
		ManufacturerModelUID = manufacturerModelUID;
	}

	public String getManufacturerModelName() {
		return ManufacturerModelName;
	}

	public void setManufacturerModelName(String manufacturerModelName) {
		ManufacturerModelName = manufacturerModelName;
	}

	public String getStudyUrl() {
		return studyUrl;
	}

	public void setStudyUrl(String studyUrl) {
		this.studyUrl = studyUrl;
	}

	public Long[] getDcmFiles() {
		return dcmFiles;
	}

	public void setDcmFiles(Long[] dcmFiles) {
		this.dcmFiles = dcmFiles;
	}

	/**
	 * Получение экземпляра
	 * 
	 * @param id
	 * @return
	 */
	public static Study getInstance(long id) {
		return StudyImpl.getInstance(id);
	}

	/**
	 * Получение списка исследований
	 * http://lucene.apache.org/java/3_0_0/queryparsersyntax.html
	 * 
	 * @param query
	 *            поисковая строка
	 * @return
	 */
	public static List<Study> getStudues(String query) {
		return StudyImpl.getStudues(query);
	}

	/**
	 * Получение списка исследований
	 * 
	 * TODO сделано для WEB-сервиса. заточка под "МИС"
	 * 
	 * @param connection
	 * @param studyModality
	 * @param patientName
	 * @param patientShortName
	 * @param patientBirthDate
	 * @param patientSex
	 * @param beginStudyDate
	 * @param endStudyDate
	 * @return
	 * @throws DataException
	 */
	public static Study[] getStudues(Connection connection,
			String studyModality, String patientName, String patientShortName,
			String patientBirthDate, String patientSex, String beginStudyDate,
			String endStudyDate) throws DataException {
		return StudyImpl.getStudues(connection, studyModality, patientName,
				patientShortName, patientBirthDate, patientSex, beginStudyDate,
				endStudyDate);
	}

}