package org.psystems.dicom.browser.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AttachementServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(AttachementServlet.class
			.getName());
	static {
//		PropertyConfigurator.configure("WEB-INF/log4j.properties");
	}// TODO Убрать !!!
	// FIXME Взять imagesRootDir из конфига
	private String imagesRootDir = "C:\\WORK\\workspace\\dicom-sheduler\\test\\testdata\\out";

	// private static final Logger log =
	// Logger.getLogger(AttachementServlet.class
	// .getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String teamColor = getServletConfig().getInitParameter("teamColor");
		System.out.println("!!! teamColor "+teamColor);
		
		String value = getServletContext().getInitParameter("name_of_context_initialization_parameter");
		System.out.println("!!! value "+value);
		
		String osVersion = System.getProperty("myapp.notify-url");
		System.out.println("!!! prop "+System.getenv("DEFAULT_ENCODING_DDV"));

		
		String path = req.getPathInfo().replaceFirst("/", "");
		String fileName = null;
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("image/jpeg");// По умолчанию
		
		//Смотрим, если передан Integer, зачит ищем по ID
		int imageId = 0;
		try {
			imageId = Integer.valueOf(path);
		}catch(NumberFormatException ex) {
			fileName = imagesRootDir + File.separator + path;
		}

		PreparedStatement psSelect = null;
		try {
			// FIXME Сделать получение соединения через pool
//			if (Util.connection == null)
//				Util.getConnection();
			
			
		    
//		    InitialContext ic = new InitialContext();
//		    DataSource myDS = (DataSource)ic.lookup("java:comp/env/jdbc/webdicom");
//		    Connection connection = myDS.getConnection();


			Connection connection = Util.getConnection();

			if (fileName == null) {
				//ищем по ID
				psSelect = connection
				.prepareStatement("SELECT ID, CONTENT_TYPE, IMAGE_FILE_NAME "
						+ " FROM WEBDICOM.IMAGES WHERE ID = ? ");
				psSelect.setInt(1, imageId);
			} else {
				psSelect = connection
					.prepareStatement("SELECT ID, CONTENT_TYPE, IMAGE_FILE_NAME "
							+ " FROM WEBDICOM.IMAGES WHERE IMAGE_FILE_NAME = ? ");
				psSelect.setString(1, path);
			}
			ResultSet rs = psSelect.executeQuery();
			int index = 0;
			while (rs.next()) {
				String contentType = rs.getString("CONTENT_TYPE");
				String file = rs.getString("IMAGE_FILE_NAME");
				imageId = rs.getInt("ID");
				fileName = imagesRootDir + File.separator + file;
				resp.setContentType(contentType);
				index++;
				break;
			}
			if(index == 0) {
				resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Image no found! id=" + imageId + " file=" 
						+ fileName );
				return;
			}

		} 
		catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			try {
				if (psSelect != null)
					psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
		} catch (FileNotFoundException ex) {
			resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Image no found! "
					+ fileName);
			ex.printStackTrace();
			return;
		}
		BufferedOutputStream out = new BufferedOutputStream(resp
				.getOutputStream());

		byte b[] = new byte[8];
		int count;
		while ((count = in.read(b)) != -1) {
			out.write(b, 0, count);

		}
		out.flush();
		out.close();
		in.close();
	}

}
