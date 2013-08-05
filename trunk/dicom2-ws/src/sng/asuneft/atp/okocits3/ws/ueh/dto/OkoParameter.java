package sng.asuneft.atp.okocits3.ws.ueh.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

/**
 * Параметр ОКО
 * 
 * @author dima_d
 * 
 */
public class OkoParameter {

	public int objId;
	public int parId;
	public Date date;
	public int status=0;

	@XmlElement(nillable = true)
	public String valueString;
	@XmlElement(nillable = true)
	public Double valueDouble;
	@XmlElement(nillable = true)
	public Date valueDate;

	@Override
	public String toString() {
		return "OkoParameter [objId=" + objId + ", parId=" + parId + ", date="
				+ date + ", valueString=" + valueString + ", valueDouble="
				+ valueDouble + ", valueDate=" + valueDate + "]";
	}

}

