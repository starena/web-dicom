package org.psystems.dicom.webservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
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
 */
public class WebDicom {

	private static Logger logger = Logger.getLogger(WebDicom.class);

	private void throwPortalException(String msg, Throwable e)
			throws DicomWebServiceException {
		String marker = Thread.currentThread().getId() + "_"
				+ Math.random() + " - " + new Date() ;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stack = sw.toString();
		logger.warn("WEBSERVICE ERROR [" + marker + "] " + msg + " " + e
				+ " stack trace:\n" + stack);
		throw new DicomWebServiceException("WEBSERVICE ERROR [" + marker + "] "
				+ msg + " " + e, e);
		// System.err.println("Portal Error ["+marker+"] "+e.getMessage()+" stack:\n"+stack);
		// return new DefaultGWTRPCException(marker,msg,e,stack);
	}

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
	public long makeDirection(String directionId, Employee doctorDirect,
			Diagnosis[] diagnosisDirect, Service[] servicesDirect,
			String dateDirection, ManufacturerDevice device,
			String dateTimePlanned, String directionCode, String directionLocation,
			Patient patient) throws DicomWebServiceException {

//		System.out.println("!!!! patient=" + patient + " doctorDirect="
//				+ doctorDirect + " servicesDirect=" + servicesDirect);
//		for (Service service : servicesDirect) {
//			System.out.println("!! service=" + service);
//		}
//		for (Diagnosis dia : diagnosisDirect) {
//			System.out.println("!! dia=" + dia);
//		}
		
		//Проверки
		doctorDirect.chechEntity();
		patient.chechEntity();

		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);

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
			// return (Direction) pm.getObjectbyInternalID(internalID);

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
	public Direction getDirectionBydirectionId(String directionId)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);

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
	public Direction getDirectionById(long id) throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);

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

	public Direction[] queryDirection(QueryDirection query)
			throws DicomWebServiceException {

		
		query.chechEntity();
		
		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);

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
	 * Поиск Исследований
	 * 
	 * @param query
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Study[] queryStudy(QueryStudy query) throws DicomWebServiceException {

		query.chechEntity();
		
		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);

		Connection connection;
		try {

			connection = ORMUtil.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			ArrayList<Study> studies = pm.queryStudies(query);

//			Study[] studies = Study.getStudues(connection, query.getId(), query.getStudyId(),  query
//					.getStudyModality(), null, query.getPatientId(), query.getPatientName(), query
//					.getPatientShortName(), query.getPatientBirthDate(), query
//					.getPatientSex(), query.getBeginStudyDate(), query
//					.getEndStudyDate(), null, null);

			String url = servletContext
					.getInitParameter("webdicom.ws.viewstudy.url");

			// "http://127.0.0.1:8888/study/"
			ArrayList<Study> tmpData = new ArrayList<Study>();

			for (Study study : tmpData) {
				study.setStudyUrl(url + "/" + study.getId());

				// //Фильтруем результаты в которых есть отклонения (только
				// для флюшек)
				// System.out.println("!!! studyModality="+studyModality);
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

	

}
