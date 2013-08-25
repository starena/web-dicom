package org.psystems.webdicom2.ws;

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
import javax.jws.soap.SOAPBinding.Style;

import org.psystems.webdicom2.ws.dto.Direction;
import org.psystems.webdicom2.ws.dto.RISCode;
import org.psystems.webdicom2.ws.dto.StudyResult;

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
@javax.xml.ws.soap.MTOM
@WebService
@SOAPBinding(style = Style.DOCUMENT)
public class Gate {

	public static Logger logger = Logger.getLogger(Gate.class.getName());

	@Resource
	private WebServiceContext context;

	String testDrnDatafile = "/tmp/webdicom.data.xml";

	private static Properties drnProp;

	private void loadTestData() throws IOException {

		try {
			FileInputStream fis = new FileInputStream(testDrnDatafile);
			drnProp = new Properties();
			drnProp.loadFromXML(fis);
			fis.close();
		} catch (FileNotFoundException ex) {
			drnProp = new Properties();
		}
	}

	private void saveTestData() throws IOException {

		Properties tmp = new Properties() {

			@Override
			public Set<Object> keySet() {
				return Collections.unmodifiableSet(new TreeSet<Object>(super
						.keySet()));
			}

		};

		tmp.putAll(drnProp);
		FileOutputStream fos = new FileOutputStream(testDrnDatafile);
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

			loadTestData();

			drnProp.put("drn." + drn.barCode + ".barCode", drn.barCode);
			drnProp.put("drn." + drn.barCode + ".dateBirsday", drn.dateBirsday);
			drnProp.put("drn." + drn.barCode + ".dateStudy", drn.dateStudy);
			drnProp.put("drn." + drn.barCode + ".modality", drn.modality);
			drnProp.put("drn." + drn.barCode + ".patientId", drn.patientId);
			drnProp.put("drn." + drn.barCode + ".patientName", drn.patientName);
			drnProp.put("drn." + drn.barCode + ".serviceName", drn.serviceName);
			drnProp.put("drn." + drn.barCode + ".sex", drn.sex);

			saveTestData();

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
		return 10;
	}

	/**
	 * @param barCode
	 * @return
	 */
	public StudyResult getStudyResult(String barCode) {


//		try {
//
//				loadTestData();
//
//			drnProp.put("drn." + drn.barCode + ".barCode", drn.barCode);
//			drnProp.put("drn." + drn.barCode + ".dateBirsday", drn.dateBirsday);
//			drnProp.put("drn." + drn.barCode + ".dateStudy", drn.dateStudy);
//			drnProp.put("drn." + drn.barCode + ".modality", drn.modality);
//			drnProp.put("drn." + drn.barCode + ".patientId", drn.patientId);
//			drnProp.put("drn." + drn.barCode + ".patientName", drn.patientName);
//			drnProp.put("drn." + drn.barCode + ".serviceName", drn.serviceName);
//			drnProp.put("drn." + drn.barCode + ".sex", drn.sex);
//
//			saveTestData();
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		StudyResult result = new StudyResult();
		result.result = "Отклонений не обнаружено";
		result.imageUrls = new String[] {
				"http://localhost:8080/images/" + barCode + "/1.jpg",
				"http://localhost:8080/images/" + barCode + "/2.jpg",
				"http://localhost:8080/images/" + barCode + "/3.jpg" };

		result.pdfUrls = new String[] {
				"http://localhost:8080/pdf/" + barCode + "/1.pdf",
				"http://localhost:8080/pdf/" + barCode + "/2.pdf",
				"http://localhost:8080/pdf/" + barCode + "/3.pdf" };

		return result;
	}

	/**
	 * Создание PDF-ки в исследовании
	 * 
	 * @param barCode
	 * @param content
	 * @return
	 */
	public String sendPdf(String barCode, byte[] content) {
		
		try {
			loadTestData();
			long  id = new Date().getTime();
			String filename = barCode + "." + id + ".pdf"; 
			drnProp.put("drn." + barCode + ".pdf."+id, filename);
			FileOutputStream fos = new FileOutputStream("/tmp/"+filename);
			fos.write(content);
			fos.flush();
			fos.close();
			saveTestData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Нужно узнать, будет ли несколько исследований по одному
		// напралвению
		return "http://localhost:8080/pdf/" + barCode + "/1.pdf";

	}

	/**
	 * Передача окончательного результата
	 * 
	 * @param barCode
	 * @param resultStr
	 */
	public String sendFinalResult(String barCode, String resultStr) {

		try {
			loadTestData();
			drnProp.put("drn." + barCode + ".finalResult", resultStr);
			saveTestData();
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
			loadTestData();
			drnProp.put("drn." + barCode + ".physician", fio);
			saveTestData();
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
