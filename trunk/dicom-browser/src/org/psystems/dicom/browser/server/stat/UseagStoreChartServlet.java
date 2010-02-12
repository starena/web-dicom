package org.psystems.dicom.browser.server.stat;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.psystems.dicom.browser.server.Util;

public class UseagStoreChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(UseagStoreChartServlet.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();

		JFreeChart chart = getChart();
		int width = 700;
		int height = 450;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);

	}

	public JFreeChart getChart() {

		PreparedStatement psSelect = null;
		
		try {

			Connection connection = Util.getConnection(getServletContext());
			long dcmSizes = 0;
			long imgSizes = 0;
			//
			// ALL_IMAGE_SIZE
			// ALL_DCM_SIZE

			// psSelect = connection
			// .prepareStatement("SELECT ID, METRIC_NAME, METRIC_DATE, METRIC_VALUE_LONG "
			// + " FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ?");

			psSelect = connection
					.prepareStatement("SELECT SUM(METRIC_VALUE_LONG) S "
							+ " FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ?");

			psSelect.setString(1, "ALL_DCM_SIZE");
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				dcmSizes = rs.getLong("S");
			}
			rs.close();
			
			psSelect.setString(1, "ALL_IMAGE_SIZE");
			rs = psSelect.executeQuery();
			while (rs.next()) {
				imgSizes = rs.getLong("S");
			}
			rs.close();
			
			
			String dcmRootDir = getServletContext().getInitParameter(
			"webdicom.dir.src");
			long totalSize  = new File(dcmRootDir).getTotalSpace(); //TODO Убрать!
			long freeSize  = new File(dcmRootDir).getFreeSpace();
			long busySize = totalSize - freeSize;
			
//			System.out.println("!!! totalSize=" + totalSize + " freeSize="+freeSize);
			
		
			long diskEmpty = freeSize - imgSizes - dcmSizes;
			
//			double KdiskEmpty = (double)diskEmpty/(double)totalSize;
//			System.out.println("!!! " + KdiskEmpty);
			
			double KdiskBusi = (double)busySize/(double)totalSize * 100;
//			System.out.println("!!! KdiskBusi=" + KdiskBusi);
			
			double KdcmSizes= (double)dcmSizes/(double)totalSize * 100;
//			System.out.println("!!! KdcmSizes=" + KdcmSizes);
			
			double KimgSizes = (double)imgSizes/(double)totalSize * 100;
//			System.out.println("!!! KimgSizes=" + KimgSizes);
			
			double KdiskFree = (double)freeSize/(double)(totalSize  - KdcmSizes - KimgSizes) * 100;
//			System.out.println("!!! KdiskFree=" + KdiskFree);
			
			
			
			DefaultPieDataset dataset = new DefaultPieDataset();
			dataset.setValue("Исследования (DCM-файлы) " + dcmSizes / 1000 + " кб.", KdcmSizes);
			dataset.setValue("Изображения (JPG-файлы) " + imgSizes / 1000 + " кб.", KimgSizes);
			dataset.setValue("Занянто другими " + busySize / 1000 + " кб.", KdiskBusi);
			dataset.setValue("Свободно " + freeSize / 1000 + " кб.", KdiskFree);
			

			boolean legend = true;
			boolean tooltips = false;
			boolean urls = false;

			JFreeChart chart = ChartFactory.createPieChart(
					"Использование дискового пространства на сервере", dataset,
					legend, tooltips, urls);

			chart.setBorderPaint(Color.GREEN);
			chart.setBorderStroke(new BasicStroke(5.0f));
			// chart.setBorderVisible(true);
			// chart.setPadding(new RectangleInsets(20 ,20,20,20));

			
			return chart;

		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		return null;

	
	}

}
