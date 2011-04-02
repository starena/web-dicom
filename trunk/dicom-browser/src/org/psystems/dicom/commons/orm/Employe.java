package org.psystems.dicom.commons.orm;

/**
 * Сотрудник
 * 
 * @author dima_d
 */
public class Employe {

	private String employeType;// Тип сотрудника (врач,оператор,..)
	private String employeName;// Имя
	private String employeCode;// Код, таб.номер

	public String getEmployeType() {
		return employeType;
	}

	public void setEmployeType(String employeType) {
		this.employeType = employeType;
	}

	public String getEmployeName() {
		return employeName;
	}

	public void setEmployeName(String employeName) {
		this.employeName = employeName;
	}

	public String getEmployeCode() {
		return employeCode;
	}

	public void setEmployeCode(String employeCode) {
		this.employeCode = employeCode;
	}

	@Override
	public String toString() {
		return "Employe [employeCode=" + employeCode + ", employeName="
				+ employeName + ", employeType=" + employeType + "]";
	}

}
