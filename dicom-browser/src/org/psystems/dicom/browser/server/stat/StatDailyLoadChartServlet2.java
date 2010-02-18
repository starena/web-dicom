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
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.psystems.dicom.browser.server.Util;

public class StatDailyLoadChartServlet2 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Font labelFont = new Font("Helvetica", Font.PLAIN, 20);
	
	private static Logger logger = Logger
			.getLogger(StatDailyLoadChartServlet2.class);

	private Paint color1 = new Color(68,99,156);
	private Paint color2 = new Color(176,202,250);
	
	

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();

		CategoryDataset dataset = createDataset();
		JFreeChart chart = getChart(dataset);
		int width = 800;
		int height = 250;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);

	}

	
	
	private JFreeChart getChart(CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createStackedBarChart(
            "Stacked Bar Chart Demo 4",  // chart title
            "Category",                  // domain axis label
            "Value",                     // range axis label
            dataset,                     // data
            PlotOrientation.VERTICAL,    // the plot orientation
            true,                        // legend
            true,                        // tooltips
            false                        // urls
        );
        
        
        TextTitle title =  new TextTitle("Динамика загрузки данных",labelFont);
		title.setPaint(new Color(68,99,156));
		chart.setTitle(title);
		
		
		
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap("G1");
        map.mapKeyToGroup("Product 1 (US)", "G1");
        map.mapKeyToGroup("Product 1 (Europe)", "G1");
   
        renderer.setSeriesToGroupMap(map); 
        
        
        BarPainter b = new StandardBarPainter();
//        BarPainter b = new GradientBarPainter(0,0.9,0.9);
        
        renderer.setBarPainter(b);
        
        renderer.setSeriesPaint(0, color1 );
        renderer.setSeriesPaint(1, color2 ); 
        
       
        
        SubCategoryAxis domainAxis = new SubCategoryAxis("Product / Month");
        domainAxis.setCategoryMargin(0.05);
        domainAxis.addSubCategory("Product 1");
  
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainAxis(domainAxis);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.red);
		plot.setRangeGridlinePaint(Color.lightGray);
        
        //plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        plot.setRenderer(renderer);
        plot.setFixedLegendItems(createLegendItems());
        return chart;
        
    }

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	private CategoryDataset createDataset() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();

        result.addValue(20.3, "Product 1 (US)", "Jan 04");
        result.addValue(27.2, "Product 1 (US)", "Feb 04");
        result.addValue(19.7, "Product 1 (US)", "Mar 04");
        result.addValue(19.7, "Product 1 (US)", "Apr 04");
        result.addValue(19.7, "Product 1 (US)", "May 04");
        
        result.addValue(19.4, "Product 1 (Europe)", "Jan 04");
        result.addValue(10.9, "Product 1 (Europe)", "Feb 04");
        result.addValue(18.4, "Product 1 (Europe)", "Mar 04");
        result.addValue(19.7, "Product 1 (Europe)", "Apr 04");
        result.addValue(19.7, "Product 1 (Europe)", "May 04");
        

   

        return result;
    }
	
	private LegendItemCollection createLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        LegendItem item1 = new LegendItem("US", color1);
        LegendItem item2 = new LegendItem("Europe", color2);

        result.add(item1);
        result.add(item2);
      
        return result;
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
