/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    Russian translation <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
     
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
 */
package org.psystems.dicom.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;


/**
 * Утилитный класс
 * 
 * @author dima_d
 * 
 */
public class Util {

	// static Connection connection;
	// static String connectionStr =
	// "jdbc:derby://localhost:1527//WORKDB/WEBDICOM";
	
	// Версия ПО (используется для проверки на стороне сервере при обновлении клиента)
	public static String version = "0.1a"; //TODO Взять из конфига?

	private static Logger logger = Logger.getLogger(Util.class
			.getName());
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
			
			 connection = DriverManager.getConnection(
					 connectionUrl + ";create=true", props);
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
