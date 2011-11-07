package org.psystems.dicom.pdf;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author dima_d
 * 
 *         Сервлет управления формирования PDF шаблонов
 * 
 */
public class ListPdfServlet extends HttpServlet {

    private static final long serialVersionUID = 8911247236211732365L;
    private static Logger logger = Logger.getLogger(ListPdfServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	printFiles(req, resp, false);
    }

   

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	req.setCharacterEncoding("cp1251");
//	changePDFContent(req, resp, true);
    }
    
    private void printFiles(HttpServletRequest req, HttpServletResponse resp, boolean b) {
	// TODO Auto-generated method stub
	try {
	    resp.getWriter().println("<h1> TEST </h1>");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }


}