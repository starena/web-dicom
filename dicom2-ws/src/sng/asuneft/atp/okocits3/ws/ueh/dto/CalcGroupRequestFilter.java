package sng.asuneft.atp.okocits3.ws.ueh.dto;

import javax.xml.bind.annotation.XmlElement;

public class CalcGroupRequestFilter {

	@XmlElement(nillable = true)
	public Integer id; // id расчетной

	@XmlElement(nillable = true)
	public String type; // тип группы P|Q|WP|WQ

	@XmlElement(nillable = true)
	public String owner; // структурное подразделение

	@Override
	public String toString() {
		return "CalcGroupRequestFilter [id=" + id + ", type=" + type
				+ ", owner=" + owner + "]";
	}

}
