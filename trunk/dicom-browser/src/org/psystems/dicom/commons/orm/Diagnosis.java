package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.ArrayList;

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
	static String persistentDelimeter = "|";// разделитель структур

	// TODO Доделать типы...
	// Предварительный, Уточненный, Выписки, Направления, Приемного отделения,
	// Клинический, Смерти, Паталогоанатомический.
	public static final String SUBTYPE_MAIN = "";

	private String diagnosisType;// Тип
	private String diagnosisSubType;// Тип
	private String diagnosisCode;// Код (по МКБ)
	private String diagnosisDescription;// Описание

	/**
	 * формат строки диагнозов: Тип^подтип^МКБ^Описание|...;
	 * 
	 * @return
	 */
	public String toPersistentString() {
		return diagnosisType + "^" + diagnosisSubType + "^" + diagnosisCode
				+ "^" + diagnosisDescription;
	}

	/**
	 * Создание экземпляра из строки
	 * 
	 * @param data
	 * @return
	 */
	public static Diagnosis getFromPersistentString(String data) {

		String[] d = data.split("\\^");
		Diagnosis dia = new Diagnosis();
		dia.setDiagnosisType(d[0]);
		dia.setDiagnosisSubType(d[1]);
		dia.setDiagnosisCode(d[2]);
		dia.setDiagnosisDescription(d[3]);

		// Matcher matcher =
		// Pattern.compile("^\\s{0,}(\\D+\\s+\\D+\\s+\\D+)\\s(\\d{1,2})\\.(\\d{1,2})\\.(\\d{4})\\s{0,}$").matcher(queryStr);
		// boolean fullSearch = matcher.matches();
		// String fio = null,day = null,month = null,year = null;
		// if (fullSearch) {
		// fio = matcher.group(1);
		// day = matcher.group(2);
		// month = matcher.group(3);
		// year = matcher.group(4);
		// }
		return dia;
	}

	/**
	 * Создание коллекции экземпляров из строки
	 * 
	 * @param data
	 * @return
	 */
	public static ArrayList<Diagnosis> getCollectionFromPersistentString(
			String data) {
		if(data==null) return null;
		
		ArrayList<Diagnosis> dias = new ArrayList<Diagnosis>();
		String[] d = data.split("\\" + persistentDelimeter);
		for (int i = 0; i < d.length; i++) {
			dias.add(getFromPersistentString(d[i]));
		}
		return dias;
	}

	/**
	 * Получение коллекции в строковом варианте для сохранения
	 * 
	 * @param dialist
	 * @return
	 */
	public static String toPersistentCollectionString(
			ArrayList<Diagnosis> dialist) {
		StringBuffer sb = new StringBuffer();
		for (Diagnosis diagnosis : dialist) {
			if (sb.length() != 0)
				sb.append(persistentDelimeter);
			sb.append(diagnosis.toPersistentString());
		}
		return sb.toString();
	}

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
