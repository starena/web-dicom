package org.psystems.dicom.webservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.commons.orm.DataException;
import org.psystems.dicom.commons.orm.Diagnosis;
import org.psystems.dicom.commons.orm.Direction;
import org.psystems.dicom.commons.orm.Employee;
import org.psystems.dicom.commons.orm.ManufacturerDevice;
import org.psystems.dicom.commons.orm.ORMUtil;
import org.psystems.dicom.commons.orm.Patient;
import org.psystems.dicom.commons.orm.PersistentManagerDerby;
import org.psystems.dicom.commons.orm.Service;

public class WebDicom {

	private static Logger logger = Logger.getLogger(WebDicom.class);

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
	 */
	public long makeDirection(String directionId, Employee doctorDirect,
			Diagnosis[] diagnosisDirect, Service[] servicesDirect,
			String dateDirection, ManufacturerDevice device,
			String datePlanned, String directionCode, String directionLocation,
			Patient patient) throws DicomWebServiceException {

		System.out.println("!!!! patient=" + patient + " doctorDirect="
				+ doctorDirect + " servicesDirect=" + servicesDirect);
		for (Service service : servicesDirect) {
			System.out.println("!! service=" + service);
		}
		for (Diagnosis dia : diagnosisDirect) {
			System.out.println("!! dia=" + dia);
		}

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
			drn.setDatePlanned(datePlanned);
			drn.setDirectionCode(directionCode);
			drn.setDirectionLocation(directionLocation);
			drn.setPatient(patient);

			return pm.makePesistent(drn);
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

	private void throwPortalException(String msg, Throwable e)
			throws DicomWebServiceException {
		String marker = Thread.currentThread().getId() + "_"
				+ new Date().getTime();
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
}
