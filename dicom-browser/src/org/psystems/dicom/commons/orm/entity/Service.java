package org.psystems.dicom.commons.orm.entity;

import java.io.Serializable;

/**
 * Услуга
 * 
 * @author dima_d
 */
public class Service implements Serializable {

    private static final long serialVersionUID = 5146546219455184305L;
    private String serviceCode;// Код
    private String serviceAlias;// краткий код
    private String serviceDescription;// Описание
    private int serviceCount = 1;// количество услуг
    private long studyInternalId;// Исследование к которому привязана услуга

    public String getServiceCode() {
	return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
	this.serviceCode = serviceCode;
    }

    public String getServiceAlias() {
	return serviceAlias;
    }

    public void setServiceAlias(String serviceAlias) {
	this.serviceAlias = serviceAlias;
    }

    public String getServiceDescription() {
	return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
	this.serviceDescription = serviceDescription;
    }

    public int getServiceCount() {
	return serviceCount;
    }

    public void setServiceCount(int serviceCount) {
	this.serviceCount = serviceCount;
    }

    public long getStudyInternalId() {
	return studyInternalId;
    }

    public void setStudyInternalId(long studyInternalId) {
	this.studyInternalId = studyInternalId;
    }

    @Override
    public String toString() {
	return "Service [serviceCode=" + serviceCode + ", serviceAlias=" + serviceAlias + ", serviceDescription="
		+ serviceDescription + ", serviceCount=" + serviceCount + ", studyInternalId=" + studyInternalId + "]";
    }

}
