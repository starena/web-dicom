package sng.asuneft.atp.okocits3.ws.ueh.dto;

import java.util.Date;

/**
 * Расчетная группа (что из SAP PI приходит)
 * 
 * @author dima_d
 * 
 */
public class CalcGroupPI {

	public int idSAPPIGroup; // id расчетной группы SAP
	public int idSAPPIGroupLimit; // id лимитной расчетной группы
	public Date date;
	public String name;// наименование
	public String type; // тип группы P|Q|WP|WQ
	// public String owner; // структурное подразделение
	public Integer ownerId;// Id структурного подразделения
	// public String typeLimit; // имя порядка лимитирования
	public Integer typeLimitId; // ID порядка лимитирования

	@Override
	public String toString() {
		return "CalcGroupPI [idSAPPIGroup=" + idSAPPIGroup
				+ ", idSAPPIGroupLimit=" + idSAPPIGroupLimit + ", date=" + date
				+ ", name=" + name + ", type=" + type + ", ownerId=" + ownerId
				+ ", typeLimitId=" + typeLimitId + "]";
	}

}
