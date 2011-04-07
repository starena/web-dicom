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
		emp.setEmployeCode(d[0]);
		emp.setEmployeName(d[2]);
		emp.setEmployeType(d[1]);
		
		
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
	

	public String getEmployeType() {
		return employeeType;
	}

	public void setEmployeType(String employeType) {
		this.employeeType = employeType;
	}

	public String getEmployeName() {
		return employeeName;
	}

	public void setEmployeName(String employeName) {
		this.employeeName = employeName;
	}

	public String getEmployeCode() {
		return employeeCode;
	}

	public void setEmployeCode(String employeCode) {
		this.employeeCode = employeCode;
	}

	@Override
	public String toString() {
		return "Employee [employeeCode=" + employeeCode + ", employeeName="
				+ employeeName + ", employeeType=" + employeeType + "]";
	}

}
