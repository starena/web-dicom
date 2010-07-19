package org.psystems.dicom.browser.server;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class NewStudyServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(NewStudyServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		resp.setContentType("text/plain");
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		System.out.println("isMultipart="+isMultipart);
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory(100000, new File("c:\\temp\\image"));

		// Set factory constraints
//		factory.setSizeThreshold(yourMaxMemorySize);
//		factory.setRepository(yourTempDirectory);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		long yourMaxRequestSize = 1000000;
		// Set overall request size constraint
		upload.setSizeMax(yourMaxRequestSize );

		// Parse the request
		try {
			List /* FileItem */ items = upload.parseRequest(req);
			
			// Process the uploaded items
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();

			    if (item.isFormField()) {
			        processFormField(item);
			    } else {
			        processUploadedFile(item);
			    }
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void processUploadedFile(FileItem item) {
		// TODO Auto-generated method stub
		
		String fieldName = item.getFieldName();
	    String fileName = item.getName();
	    String contentType = item.getContentType();
	    boolean isInMemory = item.isInMemory();
	    long sizeInBytes = item.getSize();
	 
	    File uploadedFile = new File("c:\\temp\\image\\"+fileName);
	    try {
			item.write(uploadedFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    System.out.println("!!! UploadFile: "+fieldName+";"+fileName+";"+contentType+";"+isInMemory+";"+sizeInBytes);
	}

	private void processFormField(FileItem item) {
		// TODO Auto-generated method stub
		 String name = item.getFieldName();
		    String value = item.getString();

		System.out.println("!!! FormFiled: "+name+"="+value);
	}

	
}
