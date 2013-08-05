package sng.asuneft.atp.okocits3.ws.ueh.dto;

import java.util.ArrayList;

/**
 * 
 * Расчетная группа (лимитная)
 * 
 * @author dima_d
 * 
 */
public class CalcGroupLimit {

	public int id; // id расчетной группы
	public String name;// наименование
	public String type; // тип группы P|Q|WP|WQ
	public Integer ownerId;// Id структурного подразделения
	public Integer typeLimitId;// Id порядка лимитирования

	/**
	 * Получение клона
	 * 
	 * @param cg
	 */
	public static CalcGroupLimit cloneFromCalcGroup(CalcGroup cg) {
		if (cg == null)
			return null;
		CalcGroupLimit cgl = new CalcGroupLimit();
		cgl.id = cg.id;
		cgl.name = cg.name;
		cgl.type = "L";
		cgl.ownerId = cg.ownerId;
		return cgl;
	}

	@Override
	public String toString() {
		return "CalcGroupLimit [id=" + id + ", name=" + name + ", type=" + type
				+ ", ownerId=" + ownerId + ", typeLimitId=" + typeLimitId + "]";
	}

	

}
