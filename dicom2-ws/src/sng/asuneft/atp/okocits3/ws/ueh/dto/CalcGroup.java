package sng.asuneft.atp.okocits3.ws.ueh.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;

/**
 * Расчетная группа
 * 
 * @author dima_d
 * 
 */
public class CalcGroup {

	public int id; // id расчетной группы
	// лимитные группы
	public ArrayList<CalcGroupLimit> calcGroupLimits = new ArrayList<CalcGroupLimit>();

	public String name;// наименование
	public String type; // тип группы P|Q|WP|WQ
	public Integer ownerId;// Id структурного подразделения

	@XmlElement(nillable = true)
	public Date date;
	@XmlElement(nillable = true)
	public Double value;

	public ArrayList<CalcGroupFormulaOperand> formula; // формула

	@Override
	public String toString() {
		return "CalcGroup [id=" + id + ", calcGroupLimits=" + calcGroupLimits
				+ ", name=" + name + ", type=" + type + ", ownerId=" + ownerId
				+ ", date=" + date + ", value=" + value + ", formula="
				+ formula + "]";
	}

}
