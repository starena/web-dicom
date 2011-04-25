package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сотрудник
 * 
 * @author dima_d
 */
public class Employee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5003881435162510751L;
	private String employeeType;// Тип сотрудника (врач,оператор,..)
	private String employeeName;// Имя
	private String employeeCode;// Код, таб.номер

	public static String TYPE_DOCTOR = "DOCTOR";
	public static String TYPE_OPERATOR = "OPERATOR";

	String persistentDelimeter = "|";// разделитель структур

	/**
	 * формат строки Код^ФИО
	 * 
	 * @return
	 */
	public String toPersistentString() {
		chechEntity();
		return ORMUtil.toPersistString(employeeCode) + "^" + ORMUtil.toPersistString(employeeType)
		+ "^" + ORMUtil.toPersistString(employeeName);
	}

	/**
	 * Создание экземпляра из строки
	 * 
	 * @param data
	 * @return
	 */
	public static Employee getFromPersistentString(String data) {

		if (data == null || data.length() == 0)
			return null;

		Employee emp = new Employee();

		Matcher matcher = Pattern.compile("^(.+)\\^(.+)\\^(.+)$").matcher(data);
		if (matcher.matches()) {
			emp.setEmployeeCode(ORMUtil.fromPersistString(matcher.group(1)));
			emp.setEmployeeType(ORMUtil.fromPersistString(matcher.group(2)));
			emp.setEmployeeName(ORMUtil.fromPersistString(matcher.group(3)));

		} else {
			throw new IllegalArgumentException(
					"Wrong string pattern [employeeCode^employeeType^employeeName] for ["
							+ data + "]");
		}
		return emp;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/**
	 * Проверка всех полей.
	 */
	public void chechEntity() {
		if (employeeType == null)
			throw new IllegalArgumentException(
					"Employee type argument could not be null.");
		if (!employeeType.equals(TYPE_DOCTOR)
				&& !employeeType.equals(TYPE_OPERATOR))
			throw new IllegalArgumentException(
					"Employee type argument must be " + TYPE_DOCTOR + " or "
							+ TYPE_OPERATOR + "[" + employeeType + "]");
	}

	@Override
	public String toString() {
		return "Employee [employeeCode=" + employeeCode + ", employeeName="
				+ employeeName + ", employeeType=" + employeeType + "]";
	}

}
