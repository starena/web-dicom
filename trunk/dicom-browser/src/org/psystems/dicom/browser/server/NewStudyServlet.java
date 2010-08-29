package org.psystems.dicom.browser.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.psystems.dicom.browser.client.component.StudyManagePanel;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;

public class NewStudyServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(NewStudyServlet.class
			.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		resp.setContentType("text/plain");
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		System.out.println("isMultipart=" + isMultipart);

		// TODO Вынести в конфиг!!
		String imgTmpDir = getServletContext().getInitParameter("webdicom.dir.newdcm.uploadimages");
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory(100000, new File(imgTmpDir));

		// Set factory constraints
		// factory.setSizeThreshold(yourMaxMemorySize);
		// factory.setRepository(yourTempDirectory);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// TODO Вынести в конфиг!!
		long yourMaxRequestSize = 10000000;
		// Set overall request size constraint
		upload.setSizeMax(yourMaxRequestSize);

		// Parse the request
		try {
			List /* FileItem */items = upload.parseRequest(req);
			Properties props = new Properties();
			InputStream stream = null;

			// Process the uploaded items
			Iterator iter = items.iterator();

			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					processFormField(item, props);
				} else {
					stream = processUploadedFile(item);
				}
			}
			
//			for (Iterator<Object> iter1 = props.keySet().iterator(); iter1.hasNext();) {
//				String key = (String) iter1.next();
//				String value = props.getProperty(key);
//				
//				
//				System.out.println("[1]!!! key=" + key + "; value=" + value);
//			}

			makeDicomFile(props, stream);

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resp.setStatus(200);
		resp.getWriter().write("___success___");
//		throw new IOException("EXCEPT!!!");

	}

	private InputStream processUploadedFile(FileItem item) throws IOException {
		// TODO Auto-generated method stub

		String fieldName = item.getFieldName();
		String fileName = item.getName();
		String contentType = item.getContentType();
		boolean isInMemory = item.isInMemory();
		long sizeInBytes = item.getSize();
		// props.put("image", ""+item);
		//
		// File uploadedFile = new File("c:\\temp\\image\\" + fileName);
		// try {
		// item.write(uploadedFile);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		System.out.println("!!! UploadFile: " + fieldName + ";" + fileName
				+ ";" + contentType + ";" + isInMemory + ";" + sizeInBytes);

		return item.getInputStream();
	}

	private void processFormField(FileItem item, Properties props)
			throws UnsupportedEncodingException {
		String name = item.getFieldName();
		// TODO Зделать _авто_ определение кодировки !!!
		String value = item.getString("UTF-8");

		System.out.println("!!! FormFiled: " + name + "=" + value);

		props.put(name, value);
	}

	private void makeDicomFile(Properties props, InputStream stream)
			throws IOException {
		System.out.println("!!!! making dcm...");
		
		String contentType = (String) props.get("content_type");
		props.remove("content_type");
		
		//TODO Убрать дублирование кода в этих условиях!!!
		
		if(contentType.equals("application/pdf")) {
			
			Pdf2Dcm pfg2Dcm = new Pdf2Dcm();
			String cfg = getServletContext().getInitParameter(
					"webdicom.dir.newdcm.pdf.cfg");
			pfg2Dcm.loadConfiguration(new File(cfg));

			for (Iterator<Object> iter = props.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				String value = props.getProperty(key);

				// Заточка пот значение тега = 'норма'
				if (key.equals("00102000") && value != null
						&& value.equals(StudyManagePanel.medicalAlertsTitle)) {
					value = "";
				}
				pfg2Dcm.setCfgProperty(key, value);
				System.out.println("!!!! key=" + key + "; value=" + value);
			}

			// TODO Сделать сохранение даты через attrs.putDate(...)
			// jpg2Dcm.setCfgProperty("00100030", "20010329");
			pfg2Dcm.setCharset("ISO_IR 144");
			// jpg2Dcm.setCharset("ISO_IR 192");//UTF-8

			String dcmDir = getServletContext().getInitParameter(
					"webdicom.dir.newdcm");

			String dcmTmpDir = getServletContext().getInitParameter(
					"webdicom.dir.newdcm.tmp");

			long prefix = new Date().getTime();
			String dcmFileName = dcmDir + "/" + prefix + ".dcm";
			String tmpFileName = dcmTmpDir + "/" + prefix + ".dcm";
			// TODO Задать в конфиге
			File dcmFileTmp = new File(tmpFileName);

			pfg2Dcm.convert(stream, dcmFileTmp);

			File dcmFile = new File(dcmFileName);

			dcmFileTmp.renameTo(dcmFile);
			System.out.println("!!!! making IMAGE dcm SUCCESS!");
		}
		
		//-------------------------------
		
		//TODO Убрать дублирование кода в этих условиях!!!
		if (contentType.equals("image/jpg")) {

			Jpg2Dcm jpg2Dcm = new Jpg2Dcm();
			String cfg = getServletContext().getInitParameter(
					"webdicom.dir.newdcm.jpg.cfg");
			jpg2Dcm.loadConfiguration(new File(cfg), true);

			for (Iterator<Object> iter = props.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				String value = props.getProperty(key);

				// Заточка пот значение тега = 'норма'
				if (key.equals("00102000") && value != null
						&& value.equals(StudyManagePanel.medicalAlertsTitle)) {
					value = "";
				}
				jpg2Dcm.setCfgProperty(key, value);
				System.out.println("!!!! key=" + key + "; value=" + value);
			}

			// TODO Сделать сохранение даты через attrs.putDate(...)
			// jpg2Dcm.setCfgProperty("00100030", "20010329");
			jpg2Dcm.setCharset("ISO_IR 144");
			// jpg2Dcm.setCharset("ISO_IR 192");//UTF-8

			String dcmDir = getServletContext().getInitParameter(
					"webdicom.dir.newdcm");

			String dcmTmpDir = getServletContext().getInitParameter(
					"webdicom.dir.newdcm.tmp");

			long prefix = new Date().getTime();
			String dcmFileName = dcmDir + "/" + prefix + ".dcm";
			String tmpFileName = dcmTmpDir + "/" + prefix + ".dcm";
			// TODO Задать в конфиге
			File dcmFileTmp = new File(tmpFileName);

			jpg2Dcm.convert(stream, dcmFileTmp);

			File dcmFile = new File(dcmFileName);

			dcmFileTmp.renameTo(dcmFile);
			System.out.println("!!!! making IMAGE dcm SUCCESS!");

		}
		

	}

}
