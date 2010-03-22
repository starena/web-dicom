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
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.dicom.webservice;

import java.util.Date;

public class Study {

	private Integer id; // Внутренний ID
	private String patientId; // ID пациента (0016,0032) LO	'Patient ID' = LAB_ID64936
	private String patientName; // ФИО пациента (0016,0016)	PN	'Patient's Name' = 	Ги****тди***ва И Ф
	private String patientSex; // Пол пациента (0016,0064) CS "Patient's Sex" = M/F
	private Date patientBirthDate; // Дата рождения пациента (0016,0048) DA	"Patient's Birth Date" = 19670811
	private String studyId; // ID исследования (0032,0016)	SH	'Study ID' = 89729
	private String studyInstanceUID; // UID исследования (0032,0013) UI 'Study Instance UID' = 1.2.826.0.1.3680043.2.634.30.1.89729.20100305113905 
	private Date studyDate; // Дата исследования (0008,0032) DA	'Study Date' = 	20100225
	private String studyDoctor; // Врач исследования (0008,0144) PN	"Referring Physician's Name" = Куницкий В.Н
	private String studyOperator; // Оператор исследования (0008,4208)	PN	"Operators' Name" = Гаврилова Н.Г.
	private Date studyDescriptionDate;// Дата описания исследования.
	private String studyType;// Вид исследования.
	private String studyViewprotocol;// Протокол осмотра
	private String studyResult;// Результат исследования.
	private String ManufacturerModelUID; // UID Аппарата (0002,0003)	UI	"Media Storage SOP Instance UID" = 1.2.826.0.1.3680043.2.634.0.18669.2010225.1416.1  
	private String ManufacturerModelName; // Имя Аппарата (0008,4240) LO	"Manufacturer's Model Name" = КРТ-Электрон
	private String studyUrl; // URL для открытия в обозревателе
	private String dcmFileName; // Имя DCM файла

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public Date getPatientBirthDate() {
		return patientBirthDate;
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

	public Date getStudyDescriptionDate() {
		return studyDescriptionDate;
	}

	public void setStudyDescriptionDate(Date studyDescriptionDate) {
		this.studyDescriptionDate = studyDescriptionDate;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
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

	public String getDcmFileName() {
		return dcmFileName;
	}

	public void setDcmFileName(String dcmFileName) {
		this.dcmFileName = dcmFileName;
	}

}