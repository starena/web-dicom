package org.psystems.dicom.browser.server;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleInsets;

public class ChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Изображения (JPG-файлы)", 1);
		dataset.setValue("Исследования (DCM-файлы)", 1.5);
		dataset.setValue("Свободно", 95.5);

		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart(
				"Использование дискового пространства на сервере", dataset, legend,
				tooltips, urls);

		chart.setBorderPaint(Color.GREEN);
		chart.setBorderStroke(new BasicStroke(5.0f));
		// chart.setBorderVisible(true);
//		chart.setPadding(new RectangleInsets(20	,20,20,20));
		
		return chart;
	}

}
