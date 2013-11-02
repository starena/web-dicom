package org.psystems.webdicom2.ws;

import java.io.IOException;

import org.psystems.webdicom2.ws.dto.DCM;
import org.psystems.webdicom2.ws.dto.DCMTag;
import org.psystems.webdicom2.ws.dto.Direction;
import org.psystems.webdicom2.ws.dto.RISCode;
import org.psystems.webdicom2.ws.dto.StudyResult;

public interface IGate {

	/**
	 * Создание исследования
	 * 
	 * @param drn
	 * @return
	 */
	public Direction sendDirection(Direction drn) throws WsException;

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
	public int removeDirection(String misId) throws WsException;

	/**
	 * Получение списка выполненных сейрий
	 * 
	 * @param misId
	 * @return
	 */
	public DCM[] getDCM(String misId) throws WsException;

	/**
	 * 
	 * DCM-ки по дате
	 * 
	 * @param date
	 *            - формат YYYYMMDD
	 * @return
	 * @throws WsException
	 */
	public DCM[] getDCMbyDate(String date) throws WsException;

	/**
	 * Список тэгов
	 * 
	 * @param dcmId
	 * @return
	 * @throws WsException
	 */
	public DCMTag[] getDCMTags(String dcmId) throws WsException;

	/**
	 * 
	 * Получение бинарного контента
	 * 
	 * @param misId
	 * @param contentId
	 * @return
	 * @throws IOException
	 */
	public byte[] getDCMContent(String misId, String contentId)
			throws WsException;

	/**
	 * @param misId
	 * @return
	 */
	public StudyResult getCompliteStudyResult(String misId) throws WsException;

	/**
	 * Создание PDF-ки в исследовании
	 * 
	 * @param misId
	 * @param studyUID
	 * @param content
	 * @return
	 */
	public String sendPdf(String misId, byte[] content) throws WsException;

	/**
	 * 
	 * Передача Картинки
	 * 
	 * @param misId
	 * @param content
	 * @return
	 * @throws WsException
	 */
	public String sendImage(String misId, byte[] content) throws WsException;

	/**
	 * Передача окончательного результата
	 * 
	 * @param misId
	 * @param resultStr
	 */
	public String sendFinalResult(String misId, String resultStr)
			throws WsException;

	/**
	 * Передача ФИО врача узкого специалиста
	 * 
	 * @param misId
	 * @param resultStr
	 */
	public String sendPhysician(String misId, String fio) throws WsException;

	/**
	 * TODO Не используется
	 * 
	 * @return
	 */
	public RISCode[] getRISCodes() throws WsException;
}
