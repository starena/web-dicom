package org.psystems.test.xforms.server;
/**
* HTMLtoPDFServlet.java
*
* Copyright (C) 2001-2003 by Adobe Systems Incorporated.
* All rights reserved.
*
* USAGE
* -----
* Requires a running web and Java server at http://localhost/, the
* compiled HTMLtoPDFServlet.class file to be at http://localhost/servlet/HTMLtoPDFServlet.class.
*
* The file "HTMLtoPDF.pdf" should reside at http://localhost/PDFs/HTMLtoPDF.pdf.
*
* The sample is executed by opening the HTMLtoPDF.htm webpage, filling out the appropriate data,
* and submitting the form.
*
* PURPOSE
* -------
* This servlet parses submitted forms data from an HTML form and writes
* out an FDF with the same fields and values. The FDF points to another PDF Form
* that has the same fields as in the HTML form. The end result is a PDF Form
* that has the same fields and values as the originating HTML form.
*/

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.adobe.fdf.*;
import com.adobe.fdf.exceptions.*;

public class HTMLtoPDFServlet extends HttpServlet {


	/* Fetch this pdf file from fdf. Path should be a fully specified url */
	String HRDEMO_PDF =	"http://localhost:8080/PDFs/HTMLtoPDF.pdf";

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		try {
			// Create the FDF that will be sent back to the client
			FDFDoc outFDF = new FDFDoc();

			/*
			 * Get the enumeration of parameters passed in the HTML submission.
			 * These are the the name segments of name-value pairs passed in the format:
			 * name1=value1&name2=value2
			 */
			Enumeration paramNames = req.getParameterNames();
			/*
			 * We loop through each parameter name, pulling out its value,
			 * and creating a corresponding field in the output FDF.
			 */
			String paramValue, paramName;

			while(paramNames.hasMoreElements()) {
			 	// parameter name (e.g. name1)
			 	paramName = (String)paramNames.nextElement();
			 	// its value (e.g. value1)
			 	paramValue = req.getParameter(paramName);

			 	outFDF.SetValue(paramName, paramValue);
			}

			/*
			  The FDF gives (as the value of the /F key) the name of the AcroForm that
			  needs to be fetched in order to import the FDF data into it.
			 */
			outFDF.SetFile(HRDEMO_PDF);

	    	/*
			 Next we'll do three things:

			1)  Set everything up to emit the correct HTTP header for the MIME type. In
				the case with FDF, the MIME needs to be set to "application/vnd.fdf".
			2)	Obtain the OutputStream to which FDF data will be written
			3)	Write the FDF data to the OutputStream
			*/

			// 1
			res.setContentType("application/vnd.fdf");

			// 2
			OutputStream out = res.getOutputStream();

			// 3
			outFDF.Save(out);

			out.close();
	} catch(FDFException e) {
			/* We handle an error by emitting an html header */
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.println("Caught FDF exception");
			out.println(e.toString());
			e.printStackTrace(out);
		}
	}
}