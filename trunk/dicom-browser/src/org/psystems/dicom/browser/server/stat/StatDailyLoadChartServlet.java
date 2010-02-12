package org.psystems.dicom.browser.server.stat;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.psystems.dicom.browser.server.Util;

public class StatDailyLoadChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger
			.getLogger(StatDailyLoadChartServlet.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();

		CategoryDataset dataset = createDataset();
		JFreeChart chart = getChart(dataset);
		int width = 700;
		int height = 450;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);

	}

	public JFreeChart getChart(CategoryDataset dataset) {
		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(
				"Динамика загрузки даннных", // chart title
				"Дата", // domain axis label
				"Значение", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final IntervalMarker target = new IntervalMarker(4.5, 7.5);
		target.setLabel("Ожидаемый диапазон");
		target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
		target.setLabelAnchor(RectangleAnchor.LEFT);
		target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		target.setPaint(new Color(222, 222, 255, 128));
		plot.addRangeMarker(target, Layer.BACKGROUND);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(0.10);

		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);
		// renderer.setSeriesPaint(0, gp0);
		// renderer.setSeriesPaint(1, gp1);
		// renderer.setSeriesPaint(2, gp2);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(0)
		// CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
				);
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	private CategoryDataset createDataset() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// row keys...
		String series1 = "Исследования (DCM-файлы)";
		String series2 = "Изображения (JPG-файлы)";
		String series3 = "Общее";

		Calendar calendar = Calendar.getInstance();

		int tzoffset = calendar.getTimeZone().getOffset(
				calendar.getTimeInMillis());
		// System.out.println("!!!!! "+tzoffset );

		long time = calendar.getTimeInMillis();
		time = time - (time % (60 * 60 * 24 * 1000)) - tzoffset;
		calendar.setTimeInMillis(time);

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

		PreparedStatement psSelect = null;

		try {
			Connection connection = Util.getConnection(getServletContext());
			psSelect = connection
					.prepareStatement("SELECT METRIC_VALUE_LONG, METRIC_DATE"
							+ " FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ? and " +
									" METRIC_DATE BETWEEN ? AND ? ");
			
			Calendar calendar1 = (Calendar) calendar.clone();
			calendar1.add(Calendar.DAY_OF_MONTH, - 7);
			

				
				String dateStr = format.format(calendar.getTime());
				 System.out.println("!!! " + dateStr);
				
				psSelect.setString(1, "ALL_DCM_SIZE");//ALL_DCM_SIZE ALL_IMAGE_SIZE CLIENT_CONNECTIONS
				psSelect.setDate(2, new java.sql.Date(calendar1.getTimeInMillis()));
				psSelect.setDate(3, new java.sql.Date(calendar.getTimeInMillis()));
				ResultSet rs = psSelect.executeQuery();
				
				while (rs.next()) {
					long value = rs.getLong("METRIC_VALUE_LONG");
					Date date = rs.getDate("METRIC_DATE");
					dateStr = format.format(date.getTime());
					String category = dateStr;
					dataset.addValue(value, series1, category);
					System.out.println("!!!! ALL_DCM_SIZE="+dateStr+"="+value);
				}
				rs.close();
				
				psSelect.setString(1, "ALL_IMAGE_SIZE");//ALL_DCM_SIZE ALL_IMAGE_SIZE CLIENT_CONNECTIONS
				psSelect.setDate(2, new java.sql.Date(calendar1.getTimeInMillis()));
				psSelect.setDate(3, new java.sql.Date(calendar.getTimeInMillis()));
				rs = psSelect.executeQuery();
				
				while (rs.next()) {
					long value = rs.getLong("METRIC_VALUE_LONG");
					Date date = rs.getDate("METRIC_DATE");
					dateStr = format.format(date.getTime());
					String category = dateStr;
					dataset.addValue(value, series2, category);
					System.out.println("!!!! ALL_IMAGE_SIZE= "+dateStr+"="+value);
				}
				rs.close();
				
//				psSelect.setString(1, "CLIENT_CONNECTIONS");//ALL_DCM_SIZE ALL_IMAGE_SIZE CLIENT_CONNECTIONS
//				psSelect.setDate(2, new java.sql.Date(calendar1.getTimeInMillis()));
//				psSelect.setDate(3, new java.sql.Date(calendar.getTimeInMillis()));
//				rs = psSelect.executeQuery();
//				
//				while (rs.next()) {
//					long value = rs.getLong("METRIC_VALUE_LONG");
//					Date date = rs.getDate("METRIC_DATE");
//					dateStr = format.format(date.getTime());
//					String category = dateStr;
//					dataset.addValue(value, series3, category);
//					System.out.println("!!!! CLIENT_CONNECTIONS= "+dateStr+"="+value);
//				}
//				rs.close();
				
//				dataset.addValue(Math.random() * 100, series1, category);
//				dataset.addValue(Math.random() * 100, series2, category);
//				dataset.addValue(Math.random() * 100, series3, category);


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

		return dataset;

	}

}
