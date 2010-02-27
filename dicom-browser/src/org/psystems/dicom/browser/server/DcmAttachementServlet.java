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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DcmAttachementServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(DcmAttachementServlet.class
			.getName());

	static {
		// PropertyConfigurator.configure("WEB-INF/log4j.properties");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String path = req.getPathInfo().replaceFirst("/", "");

		Matcher matcher = Pattern.compile("^(.*).dcm$").matcher(path);
		if (matcher.matches()) {
			path = matcher.group(1);
		}

		String fileName = null;
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/dicom");

		String dcmRootDir = getServletContext().getInitParameter(
				"webdicom.dir.src");

		// Смотрим, если передан Integer, зачит ищем по ID
		int dcmId = 0;
		try {
			dcmId = Integer.valueOf(path);
		} catch (NumberFormatException ex) {
			fileName = dcmRootDir + File.separator + path;
		}

		PreparedStatement psSelect = null;
		try {

			Connection connection = Util.getConnection(getServletContext());

			if (fileName == null) {
				// ищем по ID
				psSelect = connection
						.prepareStatement("SELECT ID,  DCM_FILE_NAME "
								+ " FROM WEBDICOM.DCMFILE WHERE ID = ? ");
				psSelect.setInt(1, dcmId);
			} else {
				psSelect = connection
						.prepareStatement("SELECT ID, DCM_FILE_NAME "
								+ " FROM WEBDICOM.DCMFILE WHERE DCM_FILE_NAME = ? ");
				psSelect.setString(1, path);
			}
			ResultSet rs = psSelect.executeQuery();
			int index = 0;
			while (rs.next()) {
				String file = rs.getString("DCM_FILE_NAME");
				dcmId = rs.getInt("ID");
				fileName = dcmRootDir + File.separator + file;
				index++;
				break;
			}
			if (index == 0) {
				resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
				resp
						.sendError(HttpServletResponse.SC_NOT_FOUND,
								"Dcm file not found! id=" + dcmId + " file="
										+ fileName);
				return;
			}

		} catch (SQLException e) {
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
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Dcm not found! "
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
