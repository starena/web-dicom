package org.psystems.dicom.browser.server;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;
import org.psystems.dicom.browser.client.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

public class AttachementServlet extends HttpServlet {
	
	private static Logger logger =Logger.getLogger(AttachementServlet.class.getName());
	static { PropertyConfigurator.configure("WEB-INF/log4j.properties");}//TODO Убрать !!!
	
//	private static final Logger log = Logger.getLogger(AttachementServlet.class
//			.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		
//		URL url = new URL("http://localhost:8080/Project/images/image1.bmp");
//		URLConnection con = url.openConnection();
//		con.setUseCaches(false);
//		 
//		BufferedInputStream in = new BufferedInputStream(con.getInputStream());
//		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//		 
//		byte b[] = new byte[8];
//		int count;
//		while((count=in.read(b)) != -1)
//		{
//			out.write(b,0,count);
//					
//		}
//		out.flush();	 
//		out.close();
//		in.close();
//		}
//	
		String path = req.getPathInfo().replaceFirst("/", "");
		System.out.println("!!! path="+path);
		
		
		PreparedStatement psSelect = null;
		
		try {
			
		if (Util.connection == null)
			Util.getConnection();// FIXME Сделать получение соединения через pool

		psSelect = Util.connection
				.prepareStatement("SELECT ID, DCM_FILE_NAME, PATIENT_NAME, PATIENT_BIRTH_DATE, " +
						" STUDY_DATE FROM WEBDICOM.DCMFILE WHERE UPPER(PATIENT_NAME) like UPPER( '%' || ? || '%')");
		
			 psSelect.setString(1, path);
			ResultSet rs = psSelect.executeQuery();
			ArrayList<DcmFileProxy> data = new ArrayList<DcmFileProxy>();
			int index = 0;
			while (rs.next()) {
				DcmFileProxy proxy = new DcmFileProxy();
				proxy.init(rs.getInt("ID"), rs.getString("DCM_FILE_NAME"), 
						rs.getString("PATIENT_NAME"), rs.getDate("PATIENT_BIRTH_DATE"),
						 rs.getDate("STUDY_DATE"));
				data.add(proxy);
				
				
			}
			
			DcmFileProxy[] result = data.toArray(new DcmFileProxy[data.size()]);

		} catch (SQLException e) {
//			logger.error(e);
			e.printStackTrace();
//			throw new DefaultGWTRPCException(e.getMessage());
		} finally {
			
			try {
				if(psSelect!=null) psSelect.close();
			} catch (SQLException e) {
//				logger.error(e);
				e.printStackTrace();
//				throw new DefaultGWTRPCException(e.getMessage());
			}
		}

		 resp.setContentType("image/jpeg");
		 ServletOutputStream out = resp.getOutputStream();

		// byte[] pic;
		// Blob b = // request Blob from datastore

//		 resp.setContentType("image/"+img.getType()+";charset=utf-8");

		// byte [] pic =b.getBytes();
		// int length = (int) pic.length;
		// out.write(pic, 0, length);
		
		
//		String path = req.getPathInfo().replaceFirst("/", "");
//		System.out.println("path="+path);
//		
//		
////		String keyAsStr = path;
//		String keyAsStr = req.getParameter("key");
//		String size = req.getParameter("size");
////		String idStr = req.getParameter("id");
//
//		
//		
//		if(keyAsStr==null && path==null) {
//			resp.sendError(404, "Attachment not found! request key is null." +
//					" usage: /files?key=key_hash_as_string or /files/key_hash_as_string");
//			return;
//		}
//		if(keyAsStr==null && path!=null) {
//			keyAsStr = path;
//		}
//		
//		Key key = KeyFactory.stringToKey(keyAsStr);
////		Key key = KeyFactory.createKey(Attachement.class.getSimpleName(), Long.valueOf(idStr).longValue());
//
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//
//		try {
//
//			// TODO Попробовать через getObjectById
//			// Attachement attachement = pm.getObjectById
//			// (Attachement.class,key.getId());
//			Attachement attachement = null;
//			Query query = pm.newQuery(Attachement.class);
//			query.setFilter("id == idParam");
//			query.declareParameters("Long idParam");
//
//			try {
//				List<Attachement> results = (List<Attachement>) query.execute(key);
//				if (results.iterator().hasNext()) {
//					for (Attachement e : results) {
//						attachement = e;
//						break;
//					}
//				} else {
//					// ... no results ...
//				}
//			} finally {
//				query.closeAll();
//			}
//
//			if(attachement==null) {
//				resp.sendError(404, "Attachment not found! request key is " +keyAsStr);
//				return;
//			}
//			String type = attachement.getType();
//			Blob b = attachement.getData();
//			resp.setContentType(type);
//			ServletOutputStream out = resp.getOutputStream();
//
//			byte[] pic = b.getBytes();
//			
//			
//
//			// Меняем размер (если нужно)
////			if (size != null) {
//				byte[] oldImageData = pic;
//				ImagesService imagesService = ImagesServiceFactory
//						.getImagesService();
//				Image oldImage = ImagesServiceFactory.makeImage(oldImageData);
//				System.out.println("image size ORIG: " + oldImage.getWidth() + ";"+oldImage.getHeight());
//				
//				int maxSize = 150;
//				
//				int w = oldImage.getWidth();
//				int h = oldImage.getHeight();
//				float k = w/h;
//				
//				Transform resize = ImagesServiceFactory.makeResize(150, (int)(150/k));
//				
//				Image newImage = imagesService.applyTransform(resize, oldImage);
//				
//				byte[] newImageData = newImage.getImageData();
//				pic = newImageData;
////			}
//
//			System.out.println("image size TRANS: " + + newImage.getWidth() + ";"+newImage.getHeight());
//				
//			int length = (int) pic.length;
//			out.write(pic, 0, length);
//
//			
//		} finally {
//			pm.close();
//		}
	}

}
