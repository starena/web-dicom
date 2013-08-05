package org.psystems.webdicom2.ws;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroup;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroupFormulaOperand;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroupLimit;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroupPI;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcGroupRequestFilter;
import sng.asuneft.atp.okocits3.ws.ueh.dto.CalcLimit;

/**
 * @author dima_d
 * 
 *         http://jax-ws.java.net/
 * 
 *         Типы данных:
 *         http://download.oracle.com/docs/cd/E12840_01/wls/docs103/
 *         webserv/data_types.html
 * 
 *         аттачменты:
 *         http://www.mkyong.com/webservices/jax-ws/jax-ws-attachment-with-mtom/
 */
@javax.xml.ws.soap.MTOM
@WebService
public class UEHGate {

	public static Logger logger = Logger.getLogger(UEHGate.class.getName());

	public boolean testingMode = false; // режим тестирования

	@Resource
	private WebServiceContext context;

	/**
	 * Сценари №1 Передача скорректированных плановых значений (лимитов)
	 * мощности и энергии в ИС «ОКО ЦИТС Энергетика».
	 * 
	 * @param CalcLimit
	 *            TODO Сделать через предачу несколькими аргументами?
	 * @throws WsException
	 */
	public void setLimits(@WebParam(name = "limits") List<CalcLimit> limits)
			throws WsException {

		// if (testingMode) {
		// UEHGateTestImpl test = new UEHGateTestImpl();
		// test.setLimit(limit);
		// }

		Connection conn = null;
		CallableStatement cstmt = null;
		PreparedStatement stmt = null;

		// таймзоны
		HashMap<Integer, Integer> timezones = new HashMap<Integer, Integer>();

		timezones.put(0, 2146999998);
		timezones.put(1, 2146999903);
		timezones.put(2, 2146999812);
		timezones.put(3, 2146999725);
		timezones.put(4, 2146999642);
		timezones.put(5, 2146999563);
		timezones.put(6, 2146999488);
		timezones.put(7, 2146999417);
		timezones.put(8, 2146999350);
		timezones.put(9, 2146999287);
		timezones.put(10, 2146999228);
		timezones.put(11, 2146999173);
		timezones.put(12, 2146999122);
		timezones.put(13, 2146999075);
		timezones.put(14, 2146999032);
		timezones.put(15, 2146998993);
		timezones.put(16, 2146998958);
		timezones.put(17, 2146998927);
		timezones.put(18, 2146998900);
		timezones.put(19, 2146998877);
		timezones.put(20, 2146998858);
		timezones.put(21, 2146998843);
		timezones.put(22, 2146998832);
		timezones.put(23, 2146998825);
		
		//Суточная зона для энергии.
		timezones.put(-1, 2146999953);
		

		try {

			conn = Util.getConnection();

			// определение порядка лимитирования
			String sql = "SELECT VALUE_NUM FROM info_oper io "
					+ "WHERE io.Obj_Id = ? AND io.Par_Id = 7000115 ";

			stmt = conn.prepareStatement(sql);

			for (CalcLimit limit : limits) {

				// Проверки
				checkLimit(limit);

			

				logger.log(Level.WARNING, "setLimit: " + limit);

				Integer tzId = timezones.get(limit.hour);
				logger.log(Level.INFO, "setLimit: timezone id:  " + tzId);

				stmt.setInt(1, limit.idCalcgroupLimit);
				ResultSet rs = stmt.executeQuery();

				// порядок лимитирования
				int idPl = 0;
				while (rs.next()) {
					idPl = rs.getInt("VALUE_NUM");
				}

				logger.log(Level.WARNING, "setLimit: LimitOrder Id:" + idPl);


				// Задание кортежа с лимитами
				// out_result := UEH_MAIN.SET_LIMITS2
				// ( :C_ID_TZ, :C_ID_CG, :C_ID_PL,
				// :C_ALARM_MAX, :C_ALARM_MIN, :C_DANGER_MAX, :C_DANGER_MIN,
				// :C_STOCK_LIMIT, :C_DT_ENTER );
				sql = "{ ? = call  UEH_MAIN.SET_LIMITS2( ?, ?, ?, ?, ?,"
						+ " ?, ?, ?, ?  ) }";
				cstmt = conn.prepareCall(sql);

				// Мин.К - ALARM_MIN
				// Мин.Ж - DANGER_MIN
				// Макс.Ж - DANGER_MAX
				// Макс.К - ALARM_MAX

				cstmt.registerOutParameter(1, Types.VARCHAR);
				cstmt.setInt(2, tzId);
				cstmt.setInt(3, limit.idCalcgroupLimit);
				cstmt.setInt(4, idPl);

				if (limit.maxR != null)
					cstmt.setDouble(5, limit.maxR);
				else
					cstmt.setNull(5, Types.NUMERIC);

				if (limit.minR != null)
					cstmt.setDouble(6, limit.minR);
				else
					cstmt.setNull(6, Types.NUMERIC);

				if (limit.maxY != null)
					cstmt.setDouble(7, limit.maxY);
				else
					cstmt.setNull(7, Types.NUMERIC);

				if (limit.minY != null)
					cstmt.setDouble(8, limit.minY);
				else
					cstmt.setNull(8, Types.NUMERIC);

				cstmt.setNull(9, Types.NUMERIC);
				cstmt.setDate(10, new java.sql.Date(limit.date.getTime()));

				cstmt.execute();
				String result = cstmt.getString(1);// id параметра

			}

			conn.commit();
			// conn.rollback();

		} catch (SQLException e) {
			Util.throwWsException(logger, "SQL error: ", e);
		} finally {
			Util.silentCloseStatement(stmt);
			Util.silentCloseStatement(cstmt);
			Util.silentCloseConnection(conn);
		}
	}

	/**
	 * Проверка валидности лимита
	 * 
	 * @param limit
	 * @throws WsException
	 */
	private void checkLimit(CalcLimit limit) throws WsException {
		
		
		// FIXME сделать проверка на дату!!! стобы была > чем sysdate +
		// 2 часа.
		// хотя в процедуре в БД проверка стоит
		Calendar cal = Calendar.getInstance();
		Date sys = cal.getTime();
		long systime = cal.getTimeInMillis();
		cal.setTime(limit.date);
		cal.set(Calendar.HOUR, limit.hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long limittime = cal.getTimeInMillis();
		
		if(limittime < systime + 2*60*60*1000) 
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException("Time > (sysdate - 2 hour)! limit time: [" + cal.getTime() +"] sysdate: ["+sys+"] limit: " + limit));
		
		if (limit == null) {
			String msg = "limit is null";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

		if (limit.hour < 0 || limit.hour > 23) {
			String msg = "limit hour must be [0..23]";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

		// Значения лимитов

		// minR
		if (limit.minR != null && limit.minY != null
				&& limit.minR.compareTo(limit.minY) > 0) {
			String msg = "limit minR > minY !";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

		if (limit.minR != null && limit.maxY != null
				&& limit.minR.compareTo(limit.maxY) > 0) {
			String msg = "limit minR > maxY !";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

		if (limit.minR != null && limit.maxR != null
				&& limit.minR.compareTo(limit.maxR) > 0) {
			String msg = "limit minR > maxR !";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

		// minY
		if (limit.minY != null && limit.maxY != null
				&& limit.minY.compareTo(limit.maxY) > 0) {
			String msg = "limit minY > maxY !";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

		if (limit.minY != null && limit.maxR != null
				&& limit.minY.compareTo(limit.maxR) > 0) {
			String msg = "limit minY > maxR !";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

		// maxY
		if (limit.maxY != null && limit.maxR != null
				&& limit.maxY.compareTo(limit.maxR) > 0) {
			String msg = "limit maxY > maxR !";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}
	}

	/**
	 * Сценари №2 Передача данных по созданию новой расчетной группы и ее лимита
	 * в ИС «ОКО ЦИТС Энергетика»
	 * 
	 * @param CalcGroupPI
	 * @return
	 * @throws WsException
	 */
	public CalcGroup newCalcGroup(
			@WebParam(name = "calcgroup") CalcGroupPI calcgroup)
			throws WsException {

		logger.log(Level.WARNING, "newCalcGroup: " + calcgroup);

		String pType = "P";
//		String pTypeLimit = "ТЭК";
//		Integer pTypeLimitId = 171002342;
//		String pOwner = "ОАО \"Сургутнефтегаз\"";
//		Integer pOwnerId = 171000000;
		String pName = "SAPPI";

		if (testingMode) {
			UEHGateTestImpl test = new UEHGateTestImpl();
			return test.newCalcGroup(calcgroup);
		}

		// Проверки:

		if (calcgroup == null) {
			String msg = "calcgroup is null";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

//		if (!pOwnerId.equals(calcgroup.ownerId)) {
//			String msg = "calcgroup owner Id must be [" + pOwnerId + "]";
//			Util.throwWsException(logger, "error: ",
//					new IllegalArgumentException(msg));
//		}

		if (!pType.equals(calcgroup.type)) {
			String msg = "calcgroup type must be [" + pType + "]";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

//		if (!pTypeLimitId.equals(calcgroup.typeLimitId)) {
//			String msg = "calcgroup type limit ID must be [" + pTypeLimitId + "]";
//			Util.throwWsException(logger, "error: ",
//					new IllegalArgumentException(msg));
//		}

		if (calcgroup.name == null || calcgroup.name.length() < 2) {
			String msg = "calcgroup name must be 2 letter's an more!";
			Util.throwWsException(logger, "error: ",
					new IllegalArgumentException(msg));
		}

		Connection conn = null;
		CallableStatement cstmt = null;

		try {

			conn = Util.getConnection();

			String groupName = "(" + calcgroup.type + ") " + pName + "_"
					+ calcgroup.name;

			// Создание расчетной группы
			CalcGroup cgMain = createCalcGroup(conn, calcgroup.type, groupName,
					calcgroup.ownerId);

			// Создание лимитной группы

			CalcGroupLimit cgLimit = CalcGroupLimit.cloneFromCalcGroup(
			createCalcGroup(conn, "L", groupName
					+ "-Лимиты", calcgroup.ownerId));

			// Связывание лимитной с главной группой
			linkObject(conn, cgLimit.id, 7000114, cgMain.id);

			cgMain.calcGroupLimits.add(cgLimit);

			// Связывание лимитной с порядком лимитирования
			linkObject(conn, cgLimit.id, 7000115, calcgroup.typeLimitId);

			logger.log(Level.WARNING, "newCalcGroup OKO MAIN CalcGroup: "
					+ cgMain);

			logger.log(Level.WARNING, "newCalcGroup OKO LIMIT CalcGroup: "
					+ cgLimit);

			// TODO Сделать удаление

			conn.commit();
			// conn.rollback();

			return cgMain;

		} catch (SQLException e) {
			Util.throwWsException(logger, "SQL error: ", e);
		} finally {
			Util.silentCloseStatement(cstmt);
			Util.silentCloseConnection(conn);
		}
		return null;

	}

	/**
	 * Удаление расчетной группы (также удаляется лимитная группа и лимиты)
	 * 
	 * @param idOkoCalcGroup
	 *            - Удаляется только группа с именем (P) SAPPI*
	 * @throws WsException
	 */
	public void deleteCalcGroup(
			@WebParam(name = "idOkoCalcGroup") int idOkoCalcGroup) throws WsException {

		Connection conn = null;
		CallableStatement cstmt = null;
		PreparedStatement stmt = null;
		String sql = null;

		// TODO Заглушка на порядок лимитирования
//		String pTypeLimit = "ТЭК";
//		int pTypeLimitId = 171002342;

		// Проверки
//		if (pTypeLimitId != typeLimitId) {
//			String msg = "TypeLimitId must be [" + pTypeLimitId + "]";
//			Util.throwWsException(logger, "error: ",
//					new IllegalArgumentException(msg));
//		}

		try {

			conn = Util.getConnection();

			// проверка на имя (P) SAPPI*
			sql = "SELECT o.ID, o.NAME FROM obj o WHERE o.Id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idOkoCalcGroup);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String name = rs.getString("NAME");
				if (!name.startsWith("(P) SAPPI_")) {
					String msg = "Deleting not SAPPI CalcGroup! current name is ["
							+ name + "]";
					Util.throwWsException(logger, "error: ",
							new IllegalArgumentException(msg));
				}
			}

			Util.silentCloseStatement(stmt);

			// Ищем лимитную группу
			sql = "SELECT OBJ_ID, OBJ_NAME  FROM info_oper io "
					+ " WHERE  io.Par_Id = 7000114 AND io.Value_Num = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idOkoCalcGroup);
			rs = stmt.executeQuery();

			int idLimitGroup = 0;

			while (rs.next()) {

				idLimitGroup = rs.getInt("OBJ_ID");
				logger.log(
						Level.WARNING,
						"deleteCalcGroup find limitCalcGroup id: "
								+ idLimitGroup + " name: ["
								+ rs.getString("OBJ_NAME") + "]");
			}

			Util.silentCloseStatement(stmt);

			// Очищаем таблицу лимитов

			sql = "delete from ueh_limits where ID_CG = ? ";
			stmt = conn.prepareStatement(sql);

			stmt.setInt(1, idLimitGroup);
			int rowsDeleted = stmt.executeUpdate();
			Util.silentCloseStatement(stmt);

			logger.log(Level.WARNING,
					"deleteCalcGroup deleted from table UEH_LIMITS rows: "
							+ rowsDeleted);

			// Удаляем лимитную группу
			// UEH_MAIN.DEL_OBJECT(:C_OBJ_ID)
			sql = "{ call UEH_MAIN.DEL_OBJECT(?) }";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, idLimitGroup);
			cstmt.execute();

			logger.log(Level.WARNING,
					"deleteCalcGroup limit CalcGroup deleted id: "
							+ idLimitGroup);

			// Удаляем расчетную группу
			cstmt.setInt(1, idOkoCalcGroup);
			cstmt.execute();

			logger.log(Level.WARNING, "deleteCalcGroup CalcGroup deleted id: "
					+ idOkoCalcGroup);

			Util.silentCloseStatement(cstmt);

			conn.commit();
			// conn.rollback();

		} catch (SQLException e) {
			Util.throwWsException(logger, "SQL error: ", e);
		} finally {
			Util.silentCloseStatement(stmt);
			Util.silentCloseStatement(cstmt);
			Util.silentCloseConnection(conn);
		}
	}

	/**
	 * 
	 * Связывание ОКО объекта через параметр
	 * 
	 * @param conn
	 * @param objId
	 * @param parId
	 * @param value
	 * @throws SQLException
	 */
	private int linkObject(Connection conn, int objId, int parId, int value)
			throws SQLException {

		// newpar := UEH_MAIN.INS_PARAMETER(:C_OBJ_ID, :C_PAR_ID, SYSDATE,
		// null, null, :C_VALUE_NUM)
		String sql = "{ ? = call UEH_MAIN.INS_PARAMETER(?, ?, SYSDATE, ?, null, null) }";
		CallableStatement cstmt = conn.prepareCall(sql);
		cstmt.registerOutParameter(1, Types.INTEGER);
		cstmt.setInt(2, objId);
		cstmt.setInt(3, parId);
		cstmt.setInt(4, value);
		cstmt.execute();
		int newParId = cstmt.getInt(1);// id параметра
		Util.silentCloseStatement(cstmt);
		return newParId;
	}


	/**
	 * Получение списка лимитных групп
	 * 
	 * @param conn
	 * @param calckgroupId
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<CalcGroupLimit>  getLimitGroups(Connection conn,
			int calckgroupId) throws SQLException {

		ArrayList<CalcGroupLimit> result = new ArrayList<CalcGroupLimit>();

		// Ищем лимитные группы и их порядки лимитирования
		String sql = "SELECT io1.Obj_Id L_ID, io1.Obj_Name L_NAME, io2.Value_Num PL_ID FROM INFO_OPER io1, INFO_OPER io2 "
				+ " WHERE io1.Obj_Type = 1251 AND io1.Value_Num = ? AND io2.Obj_Id = io1.Obj_Id AND io2.Par_Id = 7000115";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, calckgroupId);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {

			CalcGroup cg = getCG(conn, rs.getInt("L_ID"));
			
			CalcGroupLimit cgl =  CalcGroupLimit.cloneFromCalcGroup(cg);
			cgl.typeLimitId = rs.getInt("L_ID");
			
			result.add(cgl);
			
			logger.log(Level.INFO, "getLimitGroups find limitCalcGroup id: "
					+ rs.getInt("L_ID") + " name: [" + rs.getString("L_NAME")
					+ "] plId: [" + rs.getInt("PL_ID") + "] CalcGroup: " + cg);
		}

		Util.silentCloseStatement(stmt);
		return result;

	}

	/**
	 * @param conn
	 * @param type
	 * @param name
	 * @param ownerId
	 * @param ownerName
	 * @return
	 * @throws SQLException
	 */
	private CalcGroup createCalcGroup(Connection conn, String type,
			String name, int ownerId) throws SQLException {

		CallableStatement cstmt = null;

		// Создаем объект
		// newidobj := UEH_MAIN.INS_OBJECT(:C_TYPE, :C_PARENT, :CNAME )
		String sql = "{ ? = call UEH_MAIN.INS_OBJECT(?, ?, ?) }";
		cstmt = conn.prepareCall(sql);
		cstmt.registerOutParameter(1, Types.INTEGER);
		cstmt.setInt(2, 1251);
		cstmt.setInt(3, ownerId);

		cstmt.setString(4, name);
		cstmt.execute();
		int newObjId = cstmt.getInt(1);// id группы
		Util.silentCloseStatement(cstmt);

		logger.log(Level.WARNING, "newCalcGroup created new object with id: "
				+ newObjId);

		// Создаем параметр
		// newpar := UEH_MAIN.INS_PARAMETER(:C_OBJ_ID, :C_PAR_ID, SYSDATE,
		// null, null, :C_VALUE_CHAR)

		sql = "{ ? = call UEH_MAIN.INS_PARAMETER(?, ?, SYSDATE, null, null, ?) }";
		cstmt = conn.prepareCall(sql);
		cstmt.registerOutParameter(1, Types.INTEGER);
		cstmt.setInt(2, newObjId);
		cstmt.setInt(3, 7100039);
		cstmt.setString(4, type);
		cstmt.execute();
		int newParId = cstmt.getInt(1);// id параметра

		logger.log(Level.WARNING, "newCalcGroup add to object id: " + newObjId
				+ " parameter: " + newParId);

		CalcGroup cg = new CalcGroup();
		cg.id = newObjId;
		cg.name = name;
		cg.type = type;
		cg.ownerId = ownerId;
		cg.date = new Date();

		Util.silentCloseStatement(cstmt);

		return cg;
	}

	
	/**
	 * Сценари №3 Передача данных о составе ВСЕХ расчетных групп в SAP ECC 6.0
	 * IS-U
	 * 
	 * @param filters
	 * @param withLimit  Передавать лимитные группы
	 * @return
	 * @throws WsException
	 */
	public ArrayList<CalcGroup> getCalcGroups(
			@WebParam(name = "filters") ArrayList<CalcGroupRequestFilter> filters,
			@WebParam(name = "withlimit") Boolean withLimit)
			throws WsException {

		if (testingMode) {
			UEHGateTestImpl test = new UEHGateTestImpl();
			return test.getCalcGroups(filters);
		}

		ArrayList<CalcGroup> result = new ArrayList<CalcGroup>();

		Connection conn = null;
		PreparedStatement stmt = null;

		try {

			logger.log(Level.WARNING, "getAllCalcGroups");

			conn = Util.getConnection();
			String sql = "select i.Value_Char STYPE  , o.*, o1.Id PARID, o1.Name PARNAME , o1.Id PARID from obj o, obj o1, info_oper i "
					+ " where o.Type = 1251 and o.Id = i.Obj_Id and i.Par_Id = 7100039 and o.Parent = o1.Id(+) "
					+ "and i.Value_Char not like 'L' ";

			// фильтры
			for (CalcGroupRequestFilter filter : filters) {

				logger.log(Level.WARNING, "filter: " + filter);

				if (filter.id != null) {
					sql += " and o.id = ? ";
				}
				if (filter.type != null) {
					sql += " and i.Value_Char = ? ";
				}
				if (filter.owner != null) {
					sql += " and o1.Name = ? ";
				}
			}

			stmt = conn.prepareStatement(sql);

			// фильтры
			int counter = 1;
			for (CalcGroupRequestFilter filter : filters) {

				if (filter.id != null) {
					stmt.setInt(counter++, filter.id);
				}
				if (filter.type != null) {
					stmt.setString(counter++, filter.type);
				}
				if (filter.owner != null) {
					stmt.setString(counter++, filter.owner);
				}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				CalcGroup cg = new CalcGroup();

				cg.id = rs.getInt("ID");
				cg.type = rs.getString("STYPE");
				cg.name = rs.getString("NAME");
//				cg.owner = rs.getString("PARNAME");
				cg.ownerId = rs.getInt("PARID");
				cg.formula = getCalcGroupFormula(cg.id, conn);
				
				if(withLimit) cg.calcGroupLimits = getLimitGroups(conn, cg.id);
				
				result.add(cg);

				logger.log(Level.INFO, "  CalcGroup" + cg);

			}

			Util.silentCloseStatement(stmt);
			return result;

		} catch (SQLException e) {
			Util.throwWsException(logger, "SQL error: ", e);
		} finally {
			Util.silentCloseStatement(stmt);
			Util.silentCloseConnection(conn);
		}

		return null;

	}

	/**
	 * Сценари №3 Передача данных о составе расчетной группы в SAP ECC 6.0 IS-U
	 * 
	 * @return
	 * @throws WsException
	 */
	public CalcGroup getCalcGroup(@WebParam(name = "idGroup") int idGroup)
			throws WsException {

		if (testingMode) {
			UEHGateTestImpl test = new UEHGateTestImpl();
			return test.getCalcGroup(idGroup);
		}

		Connection conn = null;

		try {

			logger.log(Level.WARNING, "getCalcGroup idGroup:" + idGroup);
			conn = Util.getConnection();
			return getCG(conn, idGroup);

		} catch (SQLException e) {
			Util.throwWsException(logger, "SQL error: ", e);
		} finally {
			Util.silentCloseConnection(conn);
		}

		return null;

	}
	
	/**
	 * @param conn
	 * @param idGroup
	 * @return
	 * @throws WsException
	 */
	private CalcGroup getCG(Connection conn, int idGroup) throws SQLException {
		
		PreparedStatement stmt = null;

		try {

			logger.log(Level.WARNING, "getCalcGroup idGroup:" + idGroup);
			conn = Util.getConnection();
			String sql = "select i.Value_Char STYPE  , o.*, o1.Id PARID, o1.Name PARNAME , o1.Id PARID from obj o, obj o1, info_oper i "
					+ " where o.Type = 1251 and o.Id = i.Obj_Id and i.Par_Id = 7100039 and o.Parent = o1.Id(+) "
					+ " /*and i.Value_Char not like 'L'*/ AND o.Id = ?";

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idGroup);
			ResultSet rs = stmt.executeQuery();
			CalcGroup cg = new CalcGroup();
			while (rs.next()) {
				cg.id = rs.getInt("ID");
				cg.type = rs.getString("STYPE");
				cg.name = rs.getString("NAME");
				// cg.owner = rs.getString("PARNAME");
				cg.ownerId = rs.getInt("PARID");

			}

			Util.silentCloseStatement(stmt);

			cg.formula = getCalcGroupFormula(idGroup, conn);
			cg.calcGroupLimits = getLimitGroups(conn, idGroup);

			return cg;
		
		} finally {
			Util.silentCloseStatement(stmt);
		}


	}

	/**
	 * @param idGroup
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<CalcGroupFormulaOperand> getCalcGroupFormula(int idGroup,
			Connection conn) throws SQLException {

		PreparedStatement stmt = null;
		ResultSet rs;
		ArrayList<CalcGroupFormulaOperand> result = new ArrayList<CalcGroupFormulaOperand>();

		try {
			// формула
			String sql = "select OBJ_NAME, VALUE_CHAR from info_oper i "
					+ "where i.Par_Id = 7100028 and i.obj_id = ?";

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idGroup);
			rs = stmt.executeQuery();

			while (rs.next()) {

				String formulaString = rs.getString("VALUE_CHAR");
				if (formulaString == null)
					continue;

				String[] fff = formulaString.split("\\|");
				int index = 0;
				String operator = "";

				for (String s : fff) {
					index++;
					if (s.length() == 0)
						continue;
					if (index % 2 == 0) {
						operator = s;
					} else {
						String[] elts = s.split(";");
						CalcGroupFormulaOperand operand = new CalcGroupFormulaOperand();
						operand.operator = operator;
						operand.objId = Integer.valueOf(elts[0]);
						operand.typePar = Integer.valueOf(elts[1]);
						operand.parId = Integer.valueOf(elts[2]);
						result.add(operand);
					}

				}

			}

			return result;
		} finally {
			Util.silentCloseStatement(stmt);
		}

	}

	/**
	 * Сценари №4 Передача фактических объемов потребления мощности по расчетной
	 * группе в SAP ECC 6.0 IS-U.
	 * 
	 * Использовать DataGate
	 * 
	 */

}
