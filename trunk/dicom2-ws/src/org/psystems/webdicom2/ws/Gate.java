package org.psystems.webdicom2.ws;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.MTOM;
import javax.jws.soap.SOAPBinding.Style;

import org.psystems.webdicom2.ws.dto.Direction;
import org.psystems.webdicom2.ws.dto.RISCode;
import org.psystems.webdicom2.ws.dto.DCM;
import org.psystems.webdicom2.ws.dto.StudyResult;

import javax.imageio.ImageIO;

/**
 * @author dima_d
 * 
 *         http://jax-ws.java.net/
 * 
 *         Типы данных:
 *         http://download.oracle.com/docs/cd/E12840_01/wls/docs103/
 *         webserv/data_types.html
 * 
 *         аттачменты:
 *         http://www.mkyong.com/webservices/jax-ws/jax-ws-attachment-with-mtom/
 */
@MTOM
@WebService
@SOAPBinding(style = Style.DOCUMENT)
public class Gate {

	public static Logger logger = Logger.getLogger(Gate.class.getName());

	@Resource
	private WebServiceContext context;

	String testDrnDataDir = "/tmp/webdicom";
	String testDrnDatafile = "direction.xml";

	private static Properties drnProp;

	private void loadTestData(String barCode) throws IOException {

		try {
			FileInputStream fis = new FileInputStream(testDrnDataDir
					+ File.separator + barCode + File.separator
					+ testDrnDatafile);
			drnProp = new Properties();
			drnProp.loadFromXML(fis);
			fis.close();
		} catch (FileNotFoundException ex) {
			drnProp = new Properties();
		}
	}

	private void saveTestData(String barCode) throws IOException {

		Properties tmp = new Properties() {

			@Override
			public Set<Object> keySet() {
				return Collections.unmodifiableSet(new TreeSet<Object>(super
						.keySet()));
			}

		};

		tmp.putAll(drnProp);

		File theDir = new File(testDrnDataDir);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();

			if (result) {
				// System.out.println("DIR created");
			}
		}

		theDir = new File(testDrnDataDir + File.separator + barCode);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();

			if (result) {
				// System.out.println("DIR created");
			}
		}

		FileOutputStream fos = new FileOutputStream(testDrnDataDir
				+ File.separator + barCode + File.separator + testDrnDatafile);
		tmp.storeToXML(fos, "WebdicomProperties File", "UTF-8");
		fos.close();
	}

	/**
	 * Создание исследования
	 * 
	 * @param drn
	 * @return
	 */
	public Direction sendDirection(Direction drn) {

		try {

			loadTestData(drn.barCode);

			drnProp.put("barCode", drn.barCode);
			drnProp.put("dateBirsday", drn.dateBirsday);
			drnProp.put("dateStudy", drn.dateStudy);
			drnProp.put("modality", drn.modality);
			drnProp.put("patientId", drn.patientId);
			drnProp.put("patientName", drn.patientName);
			drnProp.put("serviceName", drn.serviceName);
			drnProp.put("sex", drn.sex);

			saveTestData(drn.barCode);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return drn;
	}

	/**
	 * Удалить направление
	 * 
	 * @param barCode
	 *            - штрих код
	 * @return - количество связанных исследований
	 * 
	 */
	public int removeDirrection(String barCode) {
		// TODO реализовать удаление директории
		return 10;
	}

	/**
	 * Получение списка выполненных сейрий
	 * 
	 * @param barCode
	 * @return
	 */
	public DCM[] getDCM(String barCode) {
		
		File drnDir = new File(testDrnDataDir + File.separator + barCode);
		ArrayList<DCM> result = new ArrayList<DCM>();
		
		
		File[] files = drnDir.listFiles();
		for (File dcmDir : files) {
			if (dcmDir.isDirectory()) {
				
				DCM dcmDto = new DCM();
				
				File[] dataFiles = dcmDir.listFiles();
				for (File datafile : dataFiles) {
					if (datafile.getName().endsWith(".pdf")
							|| datafile.getName().endsWith(".jpg")) {

//						String fileID = datafile.getName().replaceFirst(".pdf", "");
//						fileID = datafile.getName().replaceFirst(".jpg", "");
						
						if(datafile.getName().endsWith(".pdf"))
							dcmDto.pdfId = dcmDir.getName();
						if(datafile.getName().endsWith(".jpg"))
							dcmDto.imageId = dcmDir.getName();
						
						dcmDto.id=dcmDir.getName();;
						dcmDto.barCode=drnDir.getName();
						result.add(dcmDto);

					}
				}
			}
		}
		
		return result.toArray(new DCM[result.size()]);

	}

	/**
	 * 
	 * Получение бинарного контента
	 * 
	 * @param barCode
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public byte[] getDCMContent(String barCode, String id) throws WsException {
	
	File drnDir = new File(testDrnDataDir + File.separator + barCode);

	File[] files = drnDir.listFiles();
	for (File studyDir : files) {
		if (studyDir.isDirectory() && studyDir.getName().equals(id)) {
			
			File[] dataFiles = studyDir.listFiles();
			for (File datafile : dataFiles) {
				if (datafile.getName().endsWith(".pdf")
						|| datafile.getName().endsWith(".jpg")) {

					String fileID = datafile.getName().replaceFirst(".pdf", "");
					fileID = datafile.getName().replaceFirst(".jpg", "");
					
//					fileID = "data.jpg";
					
//					System.out.println("!!!! id="+id);
//					System.out.println("!!!! fileID="+fileID);
//					System.out.println("!!!! datafile="+datafile);
					
//					try {
//						return ImageIO.read(datafile);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						throw new WsException(e.getCause());
//					}
					
					try {
						FileInputStream fis = new FileInputStream(datafile);
						byte[] data = new byte[(int) datafile.length()];
						fis.read(data);
						fis.close();
						return data;
					} catch (IOException e) {
						e.printStackTrace();
						throw new WsException(e.getCause());
					}
					
					

				}
			}
		}
	}
	return null;

}

	/**
	 * @param barCode
	 * @return
	 */
	public StudyResult getCompliteStudyResult(String barCode) {

		StudyResult result = new StudyResult();

		File drnDir = new File(testDrnDataDir + File.separator + barCode);
		ArrayList<String> pdfs = new ArrayList<String>();
		ArrayList<String> jpgs = new ArrayList<String>();

		File[] files = drnDir.listFiles();
		for (File dcmDir : files) {
			if (dcmDir.isDirectory()) {
				File[] dataFiles = dcmDir.listFiles();
				for (File datafile : dataFiles) {
					if (datafile.getName().endsWith(".pdf")) {
						pdfs.add(dcmDir.getName());
					} else if (datafile.getName().endsWith(".jpg")) {
						jpgs.add(dcmDir.getName());
					}
				}
			}
		}

		result.imageUrls = jpgs.toArray(new String[jpgs.size()]);
		result.pdfUrls = pdfs.toArray(new String[pdfs.size()]);

		// result.result = "Отклонений не обнаружено";
		//
		// result.imageUrls = new String[] {
		// "http://localhost:8080/images/" + barCode + "/1.jpg",
		// "http://localhost:8080/images/" + barCode + "/2.jpg",
		// "http://localhost:8080/images/" + barCode + "/3.jpg" };
		//
		// result.pdfUrls = new String[] {
		// "http://localhost:8080/pdf/" + barCode + "/1.pdf",
		// "http://localhost:8080/pdf/" + barCode + "/2.pdf",
		// "http://localhost:8080/pdf/" + barCode + "/3.pdf" };

		return result;
	}

	/**
	 * Создание PDF-ки в исследовании
	 * 
	 * @param barCode
	 * @param studyUID
	 * @param content
	 * @return
	 */
	public String sendPdf(String barCode, byte[] content) {

		long id = new Date().getTime();
		File theDir = new File(testDrnDataDir + File.separator + barCode
				+ File.separator + id);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();

			if (result) {
				// System.out.println("DIR created");
			}
		}
		try {
			loadTestData(barCode);

			String filename = "data.pdf";
			// drnProp.put("pdf." + id, filename);
			FileOutputStream fos = new FileOutputStream(testDrnDataDir
					+ File.separator + barCode + File.separator + id
					+ File.separator + filename);
			fos.write(content);
			fos.flush();
			fos.close();
			saveTestData(barCode);
			return "http://localhost:8080/pdf/" + barCode + File.separator
					+ filename;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Нужно узнать, будет ли несколько исследований по одному
		// напралвению
		return null;

	}

	/**
	 * Передача окончательного результата
	 * 
	 * @param barCode
	 * @param resultStr
	 */
	public String sendFinalResult(String barCode, String resultStr) {

		try {
			loadTestData(barCode);
			drnProp.put("finalResult", resultStr);
			saveTestData(barCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Что лучше возвращать?
		return barCode + "=" + resultStr;
	}

	/**
	 * Передача ФИО врача узкого специалиста
	 * 
	 * @param barCode
	 * @param resultStr
	 */
	public String sendPhysician(String barCode, String fio) {

		try {
			loadTestData(barCode);
			drnProp.put("physician", fio);
			saveTestData(barCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Что лучше возвращать?
		return barCode + "=" + fio;
	}

	public RISCode[] getRISCodes() {

		ArrayList<RISCode> result = new ArrayList<RISCode>();

		RISCode code = new RISCode();
		code.modality = "MG";
		code.risCode = "MG00.1";
		code.description = "Исследование молочной железы 1";

		result.add(code);

		code = new RISCode();
		code.modality = "DF";
		code.risCode = "DF00.1";
		code.description = "Рентген 1";

		result.add(code);

		return result.toArray(new RISCode[result.size()]);

	}
}
