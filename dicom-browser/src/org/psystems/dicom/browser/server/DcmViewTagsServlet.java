package org.psystems.dicom.browser.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;

public class DcmViewTagsServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(DcmViewTagsServlet.class
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
		resp.setContentType("text/html");

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

		try {
			showTags(fileName, resp);
		} catch (FileNotFoundException ex) {
			resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Dcm not found! "
					+ fileName);
			ex.printStackTrace();
			return;
		}

	}

	private void showTags(String fileName, HttpServletResponse resp)
			throws IOException {
		// TODO Auto-generated method stub
		DicomObject dcmObj;
		DicomInputStream din = null;
		SpecificCharacterSet cs = new SpecificCharacterSet("ISO-8859-5");

		try {

			File f = new File(fileName);
			long fileSize = f.length();
			din = new DicomInputStream(f);
			dcmObj = din.readDicomObject();
			// System.out.println("dcmObj=" + dcmObj);

			// читаем кодировку из dcm-файла
			if (dcmObj.get(Tag.SpecificCharacterSet) != null) {
				SpecificCharacterSet cs1 = SpecificCharacterSet.valueOf(dcmObj
						.get(Tag.SpecificCharacterSet).getStrings(null, false));
			}

			resp.getWriter().write("<h1> Теги: </h1>");

			resp.getWriter().write("<table border=1>");
			// Раскручиваем теги
			for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
				DicomElement element = it.next();

				int tag = element.tag();
				short ma = (short) (tag >> 16);
				String major = Integer.toHexString(ma);
				short mi = (short) (tag);
				String minor = Integer.toHexString(mi);

				int length = element.length();
				int maxLength = 200;
				if (length > maxLength)
					length = maxLength;

				resp.getWriter().write(
						"<tr> " + " <td>" + major + " <td>" + minor +  "<td>" + dcmObj.nameOf(tag)
								+ " <td> "
								+ element.getValueAsString(cs, length)
								+ "</tr>");
			}
			resp.getWriter().write("</table>");

		} catch (org.dcm4che2.data.ConfigurationError e) {
			if (e.getCause() instanceof UnsupportedEncodingException) {
				logger.fatal("Unsupported character set" + cs + " " + e);
			}
			logger.fatal("" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("" + e);
		} finally {
			try {
				if (din != null)
					din.close();
			} catch (IOException ignore) {
			}
		}
	}

}
