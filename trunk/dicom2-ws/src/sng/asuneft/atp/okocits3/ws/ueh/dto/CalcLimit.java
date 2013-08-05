package sng.asuneft.atp.okocits3.ws.ueh.dto;

import java.util.Date;

/**
 * Лимит расчетной группы
 * 
 * @author dima_d
 * 
 */
public class CalcLimit {

	public int idCalcgroup; // id группы
	public int idCalcgroupLimit; // id _лимитной_ группы
	public Date date;
	public int hour; // Номер часа 0..23 (номер таймзоны)
	public Double maxR; // Значения скорректированного планового профиля
						// (Красный)
	public Double maxY; // Значения скорректированного планового профиля минус 1
						// МВт (Желтый)
	public Double minR; // Значения скорректированного планового профиля
	// (Красный)
	public Double minY; // Значения скорректированного планового профиля минус 1
	// МВт (Желтый)

	public String typeProf; // P|Q|WP|WQ типа значения (профиля)

	@Override
	public String toString() {
		return "CalcLimit [idCalcgroup=" + idCalcgroup + ", idCalcgroupLimit="
				+ idCalcgroupLimit + ", date=" + date + ", hour=" + hour
				+ ", maxR=" + maxR + ", minY=" + minY + ", maxY=" + maxY
				+ ", maxR=" + maxR + ", typeProf=" + typeProf + "]";
	}

}
