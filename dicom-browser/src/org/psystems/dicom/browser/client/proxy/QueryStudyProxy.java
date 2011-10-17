package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

public class QueryStudyProxy implements Serializable {

    private static final long serialVersionUID = -2840336673833444555L;
    private Long id; // Внутренний ID
    private String studyId; // штрих код
    private String studyModality;// модальность. Проверка согласно списку
				 // ORMUtil.modalityList
    private String beginStudyDate;// Дата начала интервала поиска по дате
				  // исследования. формат "yyyy-mm-dd"
    private String endStudyDate;// Дата окончания интервала поиска по дате
				// исследования. формат "yyyy-mm-dd"

    private String patientId; // ID пациента
    private String patientName; // ФИО пациента
    private String patientSex; // Пол пациента. варианты: M|F
    private String patientBirthDate; // Дата рождения пациента. формат
				     // "yyyy-mm-dd"
    private String patientShortName; // код краткого поиска

    private String beginStudyDateTimeModify; // Дата начала интервала поиска по
					     // дате модификации исследования
    private String endStudyDateTimeModify; // Дата окончания интервала поиска по
					   // дате модификации исследования

    private String manufacturerModelName; // имя аппарата
    private String studyResult;// Результат
    private String studyViewProtocol;// Протокол
    private boolean studyComplite;// Исследование выполнено
    private boolean studyNotComplite;// Исследование НЕвыполнено

    // для фильтра в worklist типа сделать
    private String sortOrder;// TODO странное поле :-( идут перечисления полей в
			     // БД... переделать..

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getStudyId() {
	return studyId;
    }

    public void setStudyId(String studyId) {
	this.studyId = studyId;
    }

    public String getStudyModality() {
	return studyModality;
    }

    public void setStudyModality(String studyModality) {
	this.studyModality = studyModality;
    }

    public String getBeginStudyDate() {
	return beginStudyDate;
    }

    public void setBeginStudyDate(String beginStudyDate) {
	this.beginStudyDate = beginStudyDate;
    }

    public String getEndStudyDate() {
	return endStudyDate;
    }

    public void setEndStudyDate(String endStudyDate) {
	this.endStudyDate = endStudyDate;
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

    public void setPatientBirthDate(String patientBirthDate) {
	this.patientBirthDate = patientBirthDate;
    }

    public String getPatientShortName() {
	return patientShortName;
    }

    public void setPatientShortName(String patientShortName) {
	this.patientShortName = patientShortName;
    }

    public String getBeginStudyDateTimeModify() {
	return beginStudyDateTimeModify;
    }

    public void setBeginStudyDateTimeModify(String beginStudyDateTimeModify) {
	this.beginStudyDateTimeModify = beginStudyDateTimeModify;
    }

    public String getEndStudyDateTimeModify() {
	return endStudyDateTimeModify;
    }

    public void setEndStudyDateTimeModify(String endStudyDateTimeModify) {
	this.endStudyDateTimeModify = endStudyDateTimeModify;
    }

    public String getManufacturerModelName() {
	return manufacturerModelName;
    }

    public void setManufacturerModelName(String manufacturerModelName) {
	this.manufacturerModelName = manufacturerModelName;
    }

    public String getStudyResult() {
	return studyResult;
    }

    public void setStudyResult(String studyResult) {
	this.studyResult = studyResult;
    }

    public String getStudyViewProtocol() {
	return studyViewProtocol;
    }

    public void setStudyViewProtocol(String studyViewProtocol) {
	this.studyViewProtocol = studyViewProtocol;
    }

    public boolean isStudyComplite() {
	return studyComplite;
    }

    public void setStudyComplite(boolean studyComplite) {
	this.studyComplite = studyComplite;
    }

    public boolean isStudyNotComplite() {
	return studyNotComplite;
    }

    public void setStudyNotComplite(boolean studyNotComplite) {
	this.studyNotComplite = studyNotComplite;
    }

    public String getSortOrder() {
	return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
	this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
	return "QueryStudyProxy [id=" + id + ", studyId=" + studyId + ", studyModality=" + studyModality
		+ ", beginStudyDate=" + beginStudyDate + ", endStudyDate=" + endStudyDate + ", patientId=" + patientId
		+ ", patientName=" + patientName + ", patientSex=" + patientSex + ", patientBirthDate="
		+ patientBirthDate + ", patientShortName=" + patientShortName + ", beginStudyDateTimeModify="
		+ beginStudyDateTimeModify + ", endStudyDateTimeModify=" + endStudyDateTimeModify
		+ ", manufacturerModelName=" + manufacturerModelName + ", studyResult=" + studyResult
		+ ", studyViewProtocol=" + studyViewProtocol + ", studyComplite=" + studyComplite
		+ ", studyNotComplite=" + studyNotComplite + ", sortOrder=" + sortOrder + "]";
    }

}
