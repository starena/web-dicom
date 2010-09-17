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
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.psystems.dicom.browser.client.component.StudyManagePanel;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.Session;

public class NewStudyServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(NewStudyServlet.class
			.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		System.out.println("isMultipart=" + isMultipart);
		HttpSession session = req.getSession();

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


			makeDicomFile(session, props, stream);
			
//			if(true) throw new Exception(" Это тест Test Exception ");
			
			
			resp.setStatus(200);
			resp.getWriter().write("___success___");

			
			
		} catch (FileUploadException e) {
			
			//TODO Сделать через LOG4J
			e.printStackTrace();
			resp.setStatus(200);
			DefaultGWTRPCException ex = Util.throwPortalException("Ошибка загрузки вложения", e);
			resp.getWriter().write("TRACECODE ["+ex.getLogMarker()+"] \n "+ex.getMessage()+" \n "+ex.getStack());
		}  catch (Exception e) {
			//TODO Сделать через LOG4J
			e.printStackTrace();
			resp.setStatus(200);
			DefaultGWTRPCException ex = Util.throwPortalException("Ошибка сохранения исследования", e);
			resp.getWriter().write("TRACECODE ["+ex.getLogMarker()+"] \n "+ex.getMessage()+" \n "+ex.getStack());
		}
		

	}

	private InputStream processUploadedFile(FileItem item) throws IOException {
		// TODO Auto-generated method stub

		String fieldName = item.getFieldName();
		String fileName = item.getName();
		String contentType = item.getContentType();
		boolean isInMemory = item.isInMemory();
		long sizeInBytes = item.getSize();
		

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

	private void makeDicomFile(HttpSession session, Properties props, InputStream stream)
			throws IOException, DefaultGWTRPCException {
		System.out.println("!!!! making dcm...");
		
		String contentType = (String) props.get("content_type");
		props.remove("content_type");
		
		//TODO Убрать дублирование кода в этих условиях!!!
		
		File dcmFileTmp = null;
		
		try {
		
		String prefix = "" + new Date().getTime()+ "_" + (int)(Math.random()*10000000l);
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
				
				
				//  *********** Работа с сессией ***************
				
				Session sessionObject = (Session)session.getAttribute(Util.sessionAttrName);
				if(sessionObject==null) {
					sessionObject = new Session();
				}
				
				// ManufacturerModelName
				if (key.equals("00081090")) {
					sessionObject.setStudyManagePanel_ManufacturerModelName(value);
				}
				
				// Modality
				if (key.equals("00080060")) {
					sessionObject.setStudyManagePanel_Modality(value);
				}
				
				session.setAttribute(Util.sessionAttrName,sessionObject);
				
				
				
			}

			// TODO Сделать сохранение даты через attrs.putDate(...)
			// jpg2Dcm.setCfgProperty("00100030", "20010329");
			pfg2Dcm.setCharset("ISO_IR 144");
			// jpg2Dcm.setCharset("ISO_IR 192");//UTF-8

//			String dcmDir = getServletContext().getInitParameter(
//					"webdicom.dir.newdcm");

			String dcmTmpDir = getServletContext().getInitParameter(
					"webdicom.dir.newdcm.tmp");

			
//			String dcmFileName = dcmDir + "/" + prefix + ".dcm";
			String tmpFileName = dcmTmpDir + "/" + prefix + ".dcm";
			// TODO Задать в конфиге
			dcmFileTmp = new File(tmpFileName);

			pfg2Dcm.convert(stream, dcmFileTmp);

//			dcmFile = new File(dcmFileName);

//			dcmFileTmp.renameTo(dcmFile);
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

//			String dcmDir = getServletContext().getInitParameter(
//					"webdicom.dir.newdcm");

			String dcmTmpDir = getServletContext().getInitParameter(
					"webdicom.dir.newdcm.tmp");

//			String dcmFileName = dcmDir + "/" + prefix + ".dcm";
			String tmpFileName = dcmTmpDir + "/" + prefix + ".dcm";
			// TODO Задать в конфиге
			dcmFileTmp = new File(tmpFileName);

			jpg2Dcm.convert(stream, dcmFileTmp);

//			dcmFile = new File(dcmFileName);

//			dcmFileTmp.renameTo(dcmFile);
			System.out.println("!!!! making IMAGE dcm SUCCESS!");
			
			

		}
		
		
		String connectionStr = getServletContext().getInitParameter(
			"webdicom.archive.connection");
				
		DcmSnd.sendToArchive(connectionStr,dcmFileTmp);
		 
		
		
		} finally {
			if(dcmFileTmp!=null)
			dcmFileTmp.delete();
		}
		
		System.out.println("!!!! Sending IMAGE dcm SUCCESS!");
		

	}

}
