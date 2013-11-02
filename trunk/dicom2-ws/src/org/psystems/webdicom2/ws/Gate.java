package org.psystems.webdicom2.ws;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.MTOM;

import org.psystems.webdicom2.ws.dto.DCM;
import org.psystems.webdicom2.ws.dto.DCMTag;
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
@MTOM
@WebService
@SOAPBinding(style = Style.DOCUMENT)
public class Gate {

	public static Logger logger = Logger.getLogger(Gate.class.getName());

	@Resource
	private WebServiceContext context;

	// Драйвер
	IGate delegate = null;

	public Gate() {
		delegate = new GateDrvDev(this);
	}

	/**
	 * @return
	 */
	MessageContext getMessageContext() {
		return context.getMessageContext();
	}

	/**
	 * Создание исследования
	 * 
	 * @param drn
	 * @return
	 */
	public Direction sendDirection(@WebParam(name = "direction") Direction drn)
			throws WsException {
		return delegate.sendDirection(drn);
	}

	/**
	 * Удалить направление
	 * 
	 * @param misId
	 *            - ID из МИС
	 * 
	 * @return - количество связанных исследований
	 * @throws WsException
	 * 
	 */
	public int removeDirection(@WebParam(name = "misId") String misId)
			throws WsException {
		return delegate.removeDirection(misId);
	}

	/**
	 * Получение списка выполненных сейрий
	 * 
	 * @param misId
	 * @return
	 * @throws WsException
	 */
	public DCM[] getDCM(@WebParam(name = "misId") String misId)
			throws WsException {

		return delegate.getDCM(misId);
	}

	/**
	 * 
	 * DCM-ки по дате
	 * 
	 * @param date
	 *            - формат YYYYMMDD
	 * @return
	 * @throws WsException
	 */
	public DCM[] getDCMbyDate(@WebParam(name = "date") String date)
			throws WsException {

		return delegate.getDCMbyDate(date);
	}

	/**
	 * Список тэгов
	 * 
	 * @param dcmId
	 * @return
	 * @throws WsException
	 */
	public DCMTag[] getDCMTags(@WebParam(name = "dcmId") String dcmId)
			throws WsException {

		return delegate.getDCMTags(dcmId);
	}

	/**
	 * 
	 * Получение бинарного контента
	 * 
	 * @param misId
	 * @param contentId
	 * @return
	 * @throws IOException
	 */
	public byte[] getDCMContent(@WebParam(name = "misId") String misId,
			@WebParam(name = "contentId") String contentId) throws WsException {

		return delegate.getDCMContent(misId, contentId);
	}

	/**
	 * @param misId
	 * @return
	 * @throws WsException
	 */
	public StudyResult getCompliteStudyResult(
			@WebParam(name = "misId") String misId) throws WsException {

		return delegate.getCompliteStudyResult(misId);
	}

	/**
	 * Создание PDF-ки в исследовании
	 * 
	 * @param misId
	 * @param studyUID
	 * @param content
	 * @return
	 */
	public String sendPdf(@WebParam(name = "misId") String misId,
			@WebParam(name = "content") byte[] content) throws WsException {

		return delegate.sendPdf(misId, content);

	}

	public String sendImage(@WebParam(name = "misId") String misId,
			@WebParam(name = "content") byte[] content) throws WsException {

		return delegate.sendImage(misId, content);
	}

	/**
	 * Передача окончательного результата
	 * 
	 * @param misId
	 * @param resultStr
	 * @throws WsException
	 */
	public String sendFinalResult(@WebParam(name = "misId") String misId,
			@WebParam(name = "resultStr") String resultStr) throws WsException {

		return delegate.sendFinalResult(misId, resultStr);
	}

	/**
	 * Передача ФИО врача узкого специалиста
	 * 
	 * @param misId
	 * @param resultStr
	 * @throws WsException
	 */
	public String sendPhysician(@WebParam(name = "misId") String misId,
			@WebParam(name = "fio") String fio) throws WsException {

		return delegate.sendPhysician(misId, fio);
	}

	/**
	 * TODO Не используется
	 * 
	 * @return
	 * @throws WsException
	 */
	public RISCode[] getRISCodes() throws WsException {

		return delegate.getRISCodes();
	}
}
