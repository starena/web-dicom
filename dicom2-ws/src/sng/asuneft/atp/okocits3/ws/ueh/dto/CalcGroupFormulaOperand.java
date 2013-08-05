package sng.asuneft.atp.okocits3.ws.ueh.dto;

/**
 * Элемент формулы расчетной группы
 * 
 * @author dima_d
 * 
 */
public class CalcGroupFormulaOperand {

	public int objId;
	public int parId;
	public int typePar;
	public String operator; // +|-

	@Override
	public String toString() {
		return "CalcGroupFormulaOperand [objId=" + objId + ", parId=" + parId
				+ ", operator=" + operator + "]";
	}

}
