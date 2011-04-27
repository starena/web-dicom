package org.psystems.dicom.commons.orm;

import java.io.Serializable;

/**
 * Диагноз
 * 
 * @author dima_d
 */
public class Diagnosis implements Serializable {

	private static final long serialVersionUID = -2133521701334810608L;
	public static final String TYPE_MAIN = "ОСНОВНОЙ";
	public static final String TYPE_INVOLVEMENT = "ОСЛОЖНЕНИЕ";
	public static final String TYPE_ACCOMPANYING = "СОПУТСТВУЮЩИЙ";

	// TODO Доделать типы...
	// Предварительный, Уточненный, Выписки, Направления, Приемного отделения,
	// Клинический, Смерти, Паталогоанатомический.
	public static final String SUBTYPE_MAIN = "";

	private String diagnosisType;// Тип
	private String diagnosisSubType;// ПодтипТип
	private String diagnosisCode;// Код (по МКБ)
	private String diagnosisDescription;// Описание


	public String getDiagnosisType() {
		return diagnosisType;
	}

	public void setDiagnosisType(String diagnosisType) {
		this.diagnosisType = diagnosisType;
	}

	public String getDiagnosisSubType() {
		return diagnosisSubType;
	}

	public void setDiagnosisSubType(String diagnosisSubType) {
		this.diagnosisSubType = diagnosisSubType;
	}

	public String getDiagnosisCode() {
		return diagnosisCode;
	}

	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}

	public String getDiagnosisDescription() {
		return diagnosisDescription;
	}

	public void setDiagnosisDescription(String diagnosisDescription) {
		this.diagnosisDescription = diagnosisDescription;
	}

	@Override
	public String toString() {
		return "Diagnosis [diagnosisCode=" + diagnosisCode
				+ ", diagnosisDescription=" + diagnosisDescription
				+ ", diagnosisSubType=" + diagnosisSubType + ", diagnosisType="
				+ diagnosisType + "]";
	}

}
