package org.psystems.webdicom2.ws;

import java.util.ArrayList;
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

	/**
	 * Создание исследования
	 * 
	 * @param drn
	 * @return
	 */
	public Direction sendDirection(Direction drn) {
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
