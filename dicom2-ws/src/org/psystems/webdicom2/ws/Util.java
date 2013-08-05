package org.psystems.webdicom2.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

public class Util {

	public static Logger logger = Logger.getLogger("UEHGate:" + Util.class.getName());
	private static String NOT_LOGGET_USER = "guest";
	
	/**
	 * Печать сообщения об ошибке
	 * 
	 * @param msg
	 * @param e
	 * @throws WsException
	 */
	public static void throwWsException(Logger logger, String msg, Throwable e)
			throws WsException {
		String marker = Thread.currentThread().getId() + "_" + Math.random()
				+ " - " + new Date();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stack = sw.toString();
		logger.log(Level.WARNING, "WEBSERVICE ERROR [" + marker + "] " + msg
				+ " " + e + " stack trace:\n" + stack);
		throw new WsException("WEBSERVICE ERROR [" + marker + "] " + msg + " "
				+ e, e);
	}

	/**
	 * @param context
	 * @return
	 */
	public static ServletContext getServletContext(WebServiceContext context) {
		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
		return servletContext;
	}
	
	/**
	 * @param context
	 * @return
	 */
	public static HttpServletRequest getServletRequest(WebServiceContext context) {
		HttpServletRequest servletContext = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);
		return servletContext;
	}

	/**
	 * 
	 * Проверка тестовый/не-тестовый режим
	 * 
	 * <Parameter name="okocits3-ueh-ws.testmode" value="yes" override="false"/>
	 * 
	 * @return
	 */
	public static boolean isTestMode(WebServiceContext context) {

		ServletContext servletContext = getServletContext(context);
		if ("yes".equals(servletContext
				.getInitParameter("okocits3-ueh-ws.testmode"))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Получение соединения внутри сервлета
	 * 
	 * @param servletContext
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(ServletContext servletContext)
			throws SQLException {

		Connection connection = null;

		return connection;
	}

	/**
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static Connection getConnection() throws SQLException {

		Connection conn = null;
//		String url = "jdbc:oracle:thin:WebSrv/sweb1501@asu-oko-db:1521:asu";
		String url = "jdbc:oracle:thin:writer/retirw@10.130.213.55:1521:ueh";
		String driver = "oracle.jdbc.driver.OracleDriver";
		try {
			Class.forName(driver).newInstance();
		} catch (Exception e) {
			throw new SQLException("DB load driver Error: " + e.getMessage(), e);
		}

		try {
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			throw new SQLException("DB Connection Error: " + e.getMessage(), e);
		}
		return conn;
	}
	
	
	/**
	 * Закрытие соединния (возврат его в pool)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	public static void silentCloseConnection(Connection conn) {
		
		if(conn!=null)
			try {
				conn.close();
			} catch (SQLException e) {
				try {
					Util.throwWsException(logger, "Close Connection error!",e);
				} catch (WsException e1) {
					// NOP
				}
			}
	}
	
	/**
	 * Закрытие стейтмента
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	public static void silentCloseStatement(Statement stmt) {
		if(stmt!=null)
			try {
				stmt.close();
			} catch (SQLException e) {
				try {
					Util.throwWsException(logger, "Close Connection error!",e);
				} catch (WsException e1) {
					// NOP
				}
			}
	}
	
	public static String getCurrentUser(HttpServletRequest request) {

		String user = NOT_LOGGET_USER;

		// Стандартный метод Tomcat
		if (request.getRemoteUser() != null) {
			user = request.getRemoteUser();
		}

		// Метод ОКОЦ�?ТС3
		// httpd.conf mod_jk опции:
		// JkEnvVar REMOTE_USER и JkEnvVar REMOTE_ADDR
		if ( request.getRemoteUser() == null &&
				(String) request.getAttribute("REMOTE_USER") != null
				&& ((String) request.getAttribute("REMOTE_USER")).length() > 0) {
			user = (String) request.getAttribute("REMOTE_USER");
		}
		return user.toLowerCase();
	}
	
	/**
	 * Возврат полного имени заявки
	 * @param request
	 * @param taskName
	 * @return
	 */
	public static String getFullTaskName(HttpServletRequest request, String taskName) {
		return getCurrentUser(request) + ":" + taskName;
	}

}
