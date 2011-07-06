package org.psystems.test.xforms.server;
/**
* GenerateFDFServlet.java
*
* Copyright (C) 2001-2003 by Adobe Systems Incorporated.
* All rights reserved.
*
* USAGE
* -----
* Requires a running web and Java server at http://localhost/, the
* compiled GenerateFDFServlet.class file to be at http://localhost/servlet/GenerateFDFServlet.class.
*
* The file "GenerateFDF.PDF" should reside at http://localhost/PDFs/GenerateFDF.PDF.
*
* The sample is executed by opening GenerateFDF.PDF, and pressing Get Data,
* which populates the PDF with the data from this servlet.
*/

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.adobe.fdf.*;
import com.adobe.fdf.exceptions.*;

public class GenerateFDFServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		try {
			FDFDoc outputFDF = null;

			/* Create a new FDF. */
			outputFDF = new FDFDoc();

			/*
				Now, take the pointer to the FDFDoc just created and add new field data. This
				sample application sets new values in the FDF file created using the FDFSetValue method.

				You could generate your own values by parsing a database or getting input
				from the user then call the methods below to build the FDF data. In this sample
				we'll generate them using simple strings.

				FDFSetValue uses the following parameters:

					fieldName	= string representing the fully qualified
								name of the field (e.g. "employee.name.last").
					newValue	= string to use as new value.
			*/

			outputFDF.SetValue("status", "Thanks George!");
			outputFDF.SetValue("Date", "December 31 1999");
			outputFDF.SetValue("Name", "John Clay");
			outputFDF.SetValue("Address", "12 Saratoga Ave");
			outputFDF.SetValue("City", "Saratoga");
			outputFDF.SetValue("State", "CA");

			/*
				Set the file for the outgoing fdf. This sets the name of the PDF file
				whose fields will be populated by the field data produced by this servlet.
			*/
//			outputFDF.SetFile("http://localhost:8080/PDFs/GenerateFDF.pdf");
			outputFDF.SetFile("http://localhost:8080/PDFs/oo1.pdf");
			for (String name : outputFDF.GetFieldNames()) {
				System.out.println("!!!! name="+name);
			}

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
			outputFDF.Save(out);

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
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		try {
			FDFDoc outputFDF = null;

			/* Create a new FDF. */
			outputFDF = new FDFDoc();

			/*
				Now, take the pointer to the FDFDoc just created and add new field data. This
				sample application sets new values in the FDF file created using the FDFSetValue method.

				You could generate your own values by parsing a database or getting input
				from the user then call the methods below to build the FDF data. In this sample
				we'll generate them using simple strings.

				FDFSetValue uses the following parameters:

					fieldName	= string representing the fully qualified
								name of the field (e.g. "employee.name.last").
					newValue	= string to use as new value.
			*/

			outputFDF.SetValue("status", "Thanks George!");
			outputFDF.SetValue("Date", "December 31 1999");
			outputFDF.SetValue("Name", "John Clay");
			outputFDF.SetValue("Address", "12 Saratoga Ave");
			outputFDF.SetValue("City", "Saratoga");
			outputFDF.SetValue("State", "CA");

			/*
				Set the file for the outgoing fdf. This sets the name of the PDF file
				whose fields will be populated by the field data produced by this servlet.
			*/
//			outputFDF.SetFile("http://localhost:8080/PDFs/GenerateFDF.pdf");
			outputFDF.SetFile("http://localhost:8080/PDFs/ooform4.pdf");
			for (String name : outputFDF.GetFieldNames()) {
				System.out.println("!!!! name="+name);
			}

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
			outputFDF.Save(out);

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