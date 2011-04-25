package org.psystems.dicom.commons.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

public class ORMUtil {
	
	public static SimpleDateFormat dateFormatSQL = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	public static SimpleDateFormat dateTimeFormatSQL = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Конвертация Util даты
	 * 
	 * @param date
	 * @return
	 */
	public static String utilDateToSQLDateString(Date date) {
		if(date==null) return null;
		return dateFormatSQL.format(date);
	}
	
	
	
	/**
	 * Конвертация SQL даты с проверкой строки с датой на валидность
	 * @param dateStr
	 * @return
	 */
	public static Date dateSQLToUtilDate(String dateStr) {
		if(dateStr==null) return null;
		try {
			return dateFormatSQL.parse(dateStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException("string with a date has wrong format " +
					dateFormatSQL.toPattern() + " input string: [" + dateStr + "]", e);
		}
	}
	
	/**
	 * Конвертация Util даты+время
	 * 
	 * @param date
	 * @return
	 */
	public static String utilDateTimeToSQLDateTimeString(Date date) {
		if(date==null) return null;
		return dateTimeFormatSQL.format(date);
	}
	
	/**
	 * Конвертация SQL даты+время с проверкой строки с датой на валидность
	 * @param dateStr
	 * @return
	 */
	public static Date dateTimeSQLToUtilDate(String dateStr) {
		if(dateStr==null) return null;
		try {
			return dateTimeFormatSQL.parse(dateStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException("string with a date and time has wrong format " +
					dateTimeFormatSQL.toPattern() + " input string: [" + dateStr + "]", e);
		}
	}
	
	
	/**
	 * Экранирование спец символов ^ и |
	 * @param s
	 * @return
	 */
	public static String toPersistString (String s) {
		s = s.replaceAll("\\^", "#####");
		s = s.replaceAll("\\|", "@@@@@");
		return s;
	}

	/**
	 * Де-Экранирование спец символов ^ и |
	 * @param s
	 * @return
	 */
	public static String fromPersistString (String s) {
		s = s.replaceAll("#####", "\\^");
		s = s.replaceAll("@@@@@","\\|");
		return s;
		
	}
	
	
	
	/**
	 * Получение КБП
	 * @param PatientName
	 * @param PatientBirthDate
	 * @return
	 */
	public static String makeShortName (String PatientName, String PatientBirthDate) {
		
		String result = null;
		Matcher matcher = Pattern.compile("\\s*(...).*?\\s+(.).*?\\s+(.).*?").matcher(PatientName.toUpperCase());
		if (matcher.matches()) {
			Calendar cal = Calendar.getInstance();
			Date date = dateSQLToUtilDate(PatientBirthDate);
			cal.setTime(date );
			
			int year = (cal.get(Calendar.YEAR) - 1900);
			if( year >= 100) year -= 100;
			String yearS = "" + year;
			if(year<10) yearS = "0" + yearS;
				
			result = matcher.group(1)+matcher.group(2)+matcher.group(3)+yearS; 
		}
		return result;
		
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

		//
		String connectionUrl = servletContext
				.getInitParameter("webdicom.connection.url");
		if (connectionUrl != null) {
			Properties props = new Properties(); // connection properties
			props.put("user", "user1"); // FIXME взять из конфига
			props.put("password", "user1"); // FIXME взять из конфига

			connection = DriverManager.getConnection(connectionUrl
					+ ";create=true", props);
		} else {
			// for Tomcat
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				DataSource ds = (DataSource) envCtx.lookup("jdbc/webdicom");
				connection = ds.getConnection();
			} catch (NamingException e) {
				throw new SQLException("JNDI error " + e);
			}
		}

		return connection;
	}

}
