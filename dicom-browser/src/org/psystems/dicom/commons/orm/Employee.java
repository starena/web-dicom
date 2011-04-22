package org.psystems.dicom.commons.orm;

import java.io.Serializable;

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
	
	String persistentDelimeter = "|";//разделитель структур

	/**
	 * формат строки Код^ФИО
	 * @return
	 */
	public String toPersistentString() {
		return employeeCode + "^" + employeeType + "^" + employeeName;
	}
	
	/**
	 * Создание экземпляра из строки
	 * @param data
	 * @return
	 */
	public static Employee getFromPersistentString (String data) {
		
		String[] d = data.split("\\^");
		Employee emp = new Employee();
		emp.setEmployeeCode(d[0]);
		emp.setEmployeeName(d[2]);
		emp.setEmployeeType(d[1]);
		
		
//		Matcher matcher = Pattern.compile("^\\s{0,}(\\D+\\s+\\D+\\s+\\D+)\\s(\\d{1,2})\\.(\\d{1,2})\\.(\\d{4})\\s{0,}$").matcher(queryStr);
//		boolean fullSearch = matcher.matches();
//		String fio = null,day = null,month = null,year = null;
//		if (fullSearch) {
//			fio = matcher.group(1);
//			day = matcher.group(2);
//			month = matcher.group(3);
//			year = matcher.group(4);
//		}
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
							+ TYPE_OPERATOR);
	}

	@Override
	public String toString() {
		return "Employee [employeeCode=" + employeeCode + ", employeeName="
				+ employeeName + ", employeeType=" + employeeType + "]";
	}
	

	

}
