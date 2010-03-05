package org.psystems.dicom.browser.server.stat;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.psystems.dicom.browser.server.Util;

public class StatDailyLoadChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Font labelFont = new Font("Helvetica", Font.PLAIN, 20);
	
	private static Logger logger = Logger
			.getLogger(StatDailyLoadChartServlet.class);
	
	

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();

		CategoryDataset dataset = createDataset();
		JFreeChart chart = getChart(dataset);
		int width = 400;
		int height = 250;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);

	}

	public JFreeChart getChart(CategoryDataset dataset) {
		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart3D(
				"Загрузка данных", // chart title
				"Дата", // domain axis label
				"Значение (кб.)", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);
		
		
//		#44639C;
		
		TextTitle title =  new TextTitle("Динамика загрузки данных",labelFont);
//		Paint paint = title.getPaint();
		title.setPaint(new Color(68,99,156));
		chart.setTitle(title);
		

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// final IntervalMarker target = new IntervalMarker(2000000, 3000000);
		// target.setLabel("Ожидаемый диапазон");
		// target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
		// target.setLabelAnchor(RectangleAnchor.LEFT);
		// target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		// target.setPaint(new Color(222, 222, 255, 128));
		// plot.addRangeMarker(target, Layer.BACKGROUND);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickLabelFont(labelFont);

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(0.10);

		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
				0.0f, 0.0f, Color.blue);
		final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);
		 renderer.setSeriesPaint(0, gp0);
		 renderer.setSeriesPaint(1, gp1);
		 renderer.setSeriesPaint(2, gp2);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 4.0)
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

		Calendar calendarEnd = Calendar.getInstance();
		int tzoffset = calendarEnd.getTimeZone().getOffset(
				calendarEnd.getTimeInMillis());

		long time = calendarEnd.getTimeInMillis();
		time = time - (time % (60 * 60 * 24 * 1000)) - tzoffset;
		calendarEnd.setTimeInMillis(time);

		try {
			Connection connection = Util.getConnection(getServletContext());

			Calendar calendarBegin = (Calendar) calendarEnd.clone();
			calendarBegin.add(Calendar.DAY_OF_MONTH, -7);

			getMetrics(connection, "Исследования (DCM-файлы)", "ALL_DCM_SIZE",
					calendarBegin.getTimeInMillis(), calendarEnd
							.getTimeInMillis(), dataset);

			getMetrics(connection, "Изображения (JPG-файлы)", "ALL_IMAGE_SIZE",
					calendarBegin.getTimeInMillis(), calendarEnd
							.getTimeInMillis(), dataset);

		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return dataset;
	}

	/**
	 * @param connection
	 * @param series
	 * @param metrica
	 * @param timeBegin
	 * @param timeEnd
	 * @param dataset
	 * @throws SQLException
	 */
	private void getMetrics(Connection connection, String series,
			String metrica, long timeBegin, long timeEnd,
			DefaultCategoryDataset dataset) throws SQLException {

		PreparedStatement stmt = null;
		try {
			// String dateStr = format.format(calendar.getTime());
			// System.out.println("!!! " + dateStr);
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

			stmt = connection
					.prepareStatement("SELECT METRIC_VALUE_LONG, METRIC_DATE"
							+ " FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ? and "
							+ " METRIC_DATE BETWEEN ? AND ? ");

			stmt.setString(1, metrica);
			stmt.setDate(2, new java.sql.Date(timeBegin));
			stmt.setDate(3, new java.sql.Date(timeEnd));
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				long value = rs.getLong("METRIC_VALUE_LONG") / 1000;
				Date date = rs.getDate("METRIC_DATE");
				String dateStr = format.format(date.getTime());
				String category = dateStr;
				dataset.addValue(value, series, category);
				// System.out.println("!!!! ALL_DCM_SIZE="+dateStr+"="+value);
			}
			rs.close();

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

}