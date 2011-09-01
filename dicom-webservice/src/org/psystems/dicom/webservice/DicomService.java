package org.psystems.dicom.webservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
import org.psystems.dicom.commons.orm.ORMUtil;
import org.psystems.dicom.commons.orm.PersistentManagerDerby;
import org.psystems.dicom.commons.orm.entity.DataException;
import org.psystems.dicom.commons.orm.entity.Diagnosis;
import org.psystems.dicom.commons.orm.entity.Direction;
import org.psystems.dicom.commons.orm.entity.Employee;
import org.psystems.dicom.commons.orm.entity.ManufacturerDevice;
import org.psystems.dicom.commons.orm.entity.Patient;
import org.psystems.dicom.commons.orm.entity.QueryDirection;
import org.psystems.dicom.commons.orm.entity.QueryStudy;
import org.psystems.dicom.commons.orm.entity.Service;
import org.psystems.dicom.commons.orm.entity.Study;

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
@WebService
public class DicomService {

	private static Logger logger = Logger.getLogger(DicomService.class);

	@Resource
	private WebServiceContext context;

	/**
	 * @param directionId
	 *            штрих код
	 * @param doctorDirect
	 *            Направивший врач
	 * @param diagnosisDirect
	 *            Диагнозы при направлении
	 * @param servicesDirect
	 *            Услуги при направлении
	 * @param dateDirection
	 *            Дата направления (YYYY-MM-DD)
	 * @param device
	 *            Аппарат
	 * @param datePlanned
	 *            Планируемая дата выполнения исследования (YYYY-MM-DD)
	 * @param directionCode
	 *            Идентификатор случая заболевания
	 * @param directionLocation
	 *            Кабинет
	 * @param patient
	 *            Пациент
	 * @throws DicomWebServiceException
	 * 
	 */
	public long makeDirection(
			@WebParam(name = "directionId") String directionId,
			@WebParam(name = "doctorDirect") Employee doctorDirect,
			@WebParam(name = "diagnosisDirect") Diagnosis[] diagnosisDirect,
			@WebParam(name = "servicesDirect") Service[] servicesDirect,
			@WebParam(name = "dateDirection") String dateDirection,
			@WebParam(name = "device") ManufacturerDevice device,
			@WebParam(name = "dateTimePlanned") String dateTimePlanned,
			@WebParam(name = "directionCode") String directionCode,
			@WebParam(name = "directionLocation") String directionLocation,
			@WebParam(name = "patient") Patient patient)
			throws DicomWebServiceException {

		// Проверки
		doctorDirect.chechEntity();
		patient.chechEntity();

		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

		Connection connection;

		try {

			connection = ORMUtil.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			Direction drn = new Direction();
			drn.setDirectionId(directionId);
			drn.setDoctorDirect(doctorDirect);
			drn.setDiagnosisDirect(diagnosisDirect);
			drn.setServicesDirect(servicesDirect);
			drn.setDateDirection(dateDirection);
			drn.setDevice(device);
			drn.setDateTimePlanned(dateTimePlanned);
			drn.setDirectionCode(directionCode);
			drn.setDirectionLocation(directionLocation);
			drn.setPatient(patient);
			return pm.makePesistentDirection(drn);

		} catch (SQLException e) {
			throwPortalException("make direction error:", e);
		} catch (DataException e) {
			throwPortalException("make direction error:", e);
		} catch (RuntimeException e) {
			throwPortalException("make direction error:", e);
		}
		return -1;

	}

	/**
	 * @param directionId
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Direction getDirectionBydirectionId(
			@WebParam(name = "directionId") String directionId)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

		Connection connection;
		try {
			connection = ORMUtil.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			return (Direction) pm.getDirectionByDirectionId(directionId);

		} catch (SQLException e) {
			throwPortalException("get direction error:", e);
		} catch (DataException e) {
			throwPortalException("get direction error:", e);
		}
		return null;
	}

	/**
	 * @param id
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Direction getDirectionById(@WebParam(name = "id") long id)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

		Connection connection;
		try {
			connection = ORMUtil.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			return (Direction) pm.getDirectionByID(id);

		} catch (SQLException e) {
			throwPortalException("get direction error:", e);
		} catch (DataException e) {
			throwPortalException("get direction error:", e);
		}
		return null;

	}

	/**
	 * Поиск Исследований
	 * 
	 * @param query
	 * @return
	 * @throws DicomWebServiceException
	 */
//	@WebMethod(operationName = "querySTUDY")
	// @WebResult(targetNamespace = "")
	// @RequestWrapper(localName = "queryStudyReq", targetNamespace =
	// "http://webservice.dicom.psystems.org")
	// @ResponseWrapper(localName = "queryStudyResp", targetNamespace =
	// "http://webservice.dicom.psystems.org")
	public Study[] queryStudies(@WebParam QueryStudy query)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

		// Заменяем строки с "" на null
		if (query.getBeginStudyDate() != null
				&& query.getBeginStudyDate().length() == 0)
			query.setBeginStudyDate(null);

		if (query.getEndStudyDate() != null
				&& query.getEndStudyDate().length() == 0)
			query.setEndStudyDate(null);

		if (query.getBeginStudyDateTimeModify() != null
				&& query.getBeginStudyDateTimeModify().length() == 0)
			query.setBeginStudyDateTimeModify(null);

		if (query.getEndStudyDateTimeModify() != null
				&& query.getEndStudyDateTimeModify().length() == 0)
			query.setEndStudyDateTimeModify(null);

		if (query.getPatientBirthDate() != null
				&& query.getPatientBirthDate().length() == 0)
			query.setPatientBirthDate(null);

		if (query.getPatientSex() != null
				&& query.getPatientSex().length() == 0)
			query.setPatientSex(null);

		if (query.getStudyModality() != null
				&& query.getStudyModality().length() == 0)
			query.setStudyModality(null);

		query.chechEntity();

		Connection connection;
		try {

			connection = ORMUtil.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			ArrayList<Study> studies = pm.queryStudies(query);

			// FIXME Сделать путь к ..
			// "http://127.0.0.1:8888/browser/study/"
			String url = servletContext.getContextPath();

			System.out.println("!!!!! url=" + url);

			ArrayList<Study> tmpData = new ArrayList<Study>();

			for (Study study : tmpData) {
				study.setStudyUrl(url + "/" + study.getId());

				// Фильтруем результаты в которых есть отклонения
				// (только для флюшек)
				if (study.getStudyModality() != null
						&& study.getStudyModality().equals("CR")) {
					if (study.getStudyResult() == null
							|| study.getStudyResult().length() == 0) {
						study.setStudyResult("норма");
						tmpData.add(study);
					}
				} else {
					tmpData.add(study);
				}
			}

			return studies.toArray(new Study[studies.size()]);

		} catch (SQLException e) {
			throwPortalException("query study error:", e);
		} catch (DataException e) {
			throwPortalException("query study error:", e);
		}
		return null;
	}

	/**
	 * @param query
	 * @return
	 * @throws DicomWebServiceException
	 */
//	@WebMethod(operationName = "queryDIRECTION")
	// @WebResult(targetNamespace = "")
	// @RequestWrapper(localName = "queryDirectionReq", targetNamespace =
	// "http://webservice.dicom.psystems.org",
	// className="org.psystems.dicom.commons.orm.entity.QueryDirection")
	// @ResponseWrapper(localName = "queryDirectionResp", targetNamespace =
	// "http://webservice.dicom.psystems.org")
	public Direction[] queryDirections(QueryDirection query)
			throws DicomWebServiceException {

		if (query.getDateDirection() != null
				&& query.getDateDirection().length() == 0)
			query.setDateDirection(null);

		if (query.getPatientBirthDate() != null
				&& query.getPatientBirthDate().length() == 0)
			query.setPatientBirthDate(null);

		if (query.getDateTimePlannedBegin() != null
				&& query.getDateTimePlannedBegin().length() == 0)
			query.setDateTimePlannedBegin(null);

		if (query.getDateTimePlannedEnd() != null
				&& query.getDateTimePlannedEnd().length() == 0)
			query.setDateTimePlannedEnd(null);

		query.chechEntity();

		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

		Connection connection;
		try {
			connection = ORMUtil.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			ArrayList<Direction> directions = pm.queryDirections(query);
			return directions.toArray(new Direction[directions.size()]);

		} catch (SQLException e) {
			throwPortalException("query direction error:", e);
		} catch (DataException e) {
			throwPortalException("query direction error:", e);
		}
		return null;
	}

	/**
	 * Печать сообщения об ошибке
	 * 
	 * @param msg
	 * @param e
	 * @throws DicomWebServiceException
	 */
	private void throwPortalException(String msg, Throwable e)
			throws DicomWebServiceException {
		String marker = Thread.currentThread().getId() + "_" + Math.random()
				+ " - " + new Date();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stack = sw.toString();
		logger.warn("WEBSERVICE ERROR [" + marker + "] " + msg + " " + e
				+ " stack trace:\n" + stack);
		throw new DicomWebServiceException("WEBSERVICE ERROR [" + marker + "] "
				+ msg + " " + e, e);
	}

}
