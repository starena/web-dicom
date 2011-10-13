package org.psystems.dicom.webservice;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
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
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
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
			@WebParam(name = "modality") String modality,
			@WebParam(name = "doctorDirect") Employee doctorDirect,
			@WebParam(name = "diagnosisDirect") Diagnosis[] diagnosisDirect,
			@WebParam(name = "servicesDirect") Service[] servicesDirect,
			@WebParam(name = "dateDirection") String dateDirection,
			@WebParam(name = "device") ManufacturerDevice device,
			@WebParam(name = "dateTimePlanned") String dateTimePlanned,
			@WebParam(name = "directionCode") String directionCode,
			@WebParam(name = "directionLocation") String directionLocation,
			@WebParam(name = "patient") Patient patient,
			@WebParam(name = "senderLpu") String senderLpu)
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
			drn.setModality(modality);
			drn.setDoctorDirect(doctorDirect);
			drn.setDiagnosisDirect(diagnosisDirect);
			drn.setServicesDirect(servicesDirect);
			drn.setDateDirection(dateDirection);
			drn.setDevice(device);
			drn.setDateTimePlanned(dateTimePlanned);
			drn.setDirectionCode(directionCode);
			drn.setDirectionLocation(directionLocation);
			drn.setPatient(patient);
			drn.setSenderLpu(senderLpu);
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
	@WebMethod(operationName = "queryStudies")
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "queryStudyReq", targetNamespace = "http://webservice.dicom.psystems.org")
	@ResponseWrapper(localName = "queryStudyResp", targetNamespace = "http://webservice.dicom.psystems.org")
	public Study[] queryStudies(@WebParam(name = "query") QueryStudy query)
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

			// System.out.println("!!!!! url=" + url);

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
	@WebMethod(operationName = "queryDirections")
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "queryDirectionReq", targetNamespace = "http://webservice.dicom.psystems.org")
	@ResponseWrapper(localName = "queryDirectionResp", targetNamespace = "http://webservice.dicom.psystems.org")
	public Direction[] queryDirections(
			@WebParam(name = "query") QueryDirection query)
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
	 * @param id
	 * @return
	 */
	public ArrayList<Image> getStudyImages(@WebParam(name = "id") long id)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

		ArrayList<Image> images = new ArrayList<Image>();
		Connection connection = null;
		PreparedStatement psSelect = null;

		System.out.println("getStudyImages id=" + id);
		try {
			connection = ORMUtil.getConnection(servletContext);

			psSelect = connection
					.prepareStatement("SELECT * FROM WEBDICOM.DCMFILE WHERE FID_STUDY = ? ");
			psSelect.setLong(1, id);
			ResultSet rs = psSelect.executeQuery();
			// TODO убрать!!! ../dicom-browser/
			// FIXME убрать! Перевести на Config.getIncomingFolder()
			String imagesRootDir = servletContext
					.getInitParameter("webdicom.dir.dst");
			String incoming = imagesRootDir;
			ArrayList<String> fileNames = new ArrayList<String>();

			while (rs.next()) {
				if (rs.getString("MIME_TYPE") != null
						&& rs.getString("MIME_TYPE").equals("image/jpg")) {
					String filename = incoming + "/"
							+ rs.getString("DCM_FILE_NAME")
							+ ".images/fullsize.jpg";
					fileNames.add(filename);
					System.out.println("!!!!!!!!!! filename=" + filename);
				}
			}
			rs.close();

			for (String filename : fileNames) {
				try {

					File image = new File(filename);
					BufferedImage img = ImageIO.read(image);
					images.add(img);

				} catch (IOException e) {
					throwPortalException("getImage error:", e);
				}
			}

		} catch (SQLException e) {
			throwPortalException("getImage error:", e);
		} finally {
			try {
				if (psSelect != null)
					psSelect.close();
				// connection.close();
			} catch (SQLException e) {
				throwPortalException("getImage error:", e);
			}
		}
		return images;

	}

	/**
	 * @param id
	 * @return
	 */
	public ArrayList<byte[]> getStudyProtocols(@WebParam(name = "id") long id)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) context
				.getMessageContext().get(MessageContext.SERVLET_CONTEXT);

		ArrayList<byte[]> pdfs = new ArrayList<byte[]>();
		Connection connection = null;
		PreparedStatement psSelect = null;

		System.out.println("getStudyImages id=" + id);
		try {
			connection = ORMUtil.getConnection(servletContext);

			psSelect = connection
					.prepareStatement("SELECT * FROM WEBDICOM.DCMFILE WHERE FID_STUDY = ? ");
			psSelect.setLong(1, id);
			ResultSet rs = psSelect.executeQuery();
			// TODO убрать!!! ../dicom-browser/
			// FIXME убрать! Перевести на Config.getIncomingFolder()
			String imagesRootDir = servletContext
					.getInitParameter("webdicom.dir.dst");
			String incoming = imagesRootDir;
			ArrayList<String> fileNames = new ArrayList<String>();

			while (rs.next()) {
				if (rs.getString("MIME_TYPE") != null
						&& rs.getString("MIME_TYPE").equals("application/pdf")) {
					String filename = incoming + "/"
							+ rs.getString("DCM_FILE_NAME");
					fileNames.add(filename);
					System.out.println("!!!!!!!!!! filename=" + filename);
				}
			}
			rs.close();

			for (String filename : fileNames) {
				DicomObject dcmObj;
				DicomInputStream din = null;
				// SpecificCharacterSet cs = new Win1251CharacterSet();
				// SpecificCharacterSet cs = new
				// SpecificCharacterSet("ISO-8859-5");

				try {

					File f = new File(filename);
					long fileSize = f.length();
					din = new DicomInputStream(f);
					dcmObj = din.readDicomObject();

					// читаем кодировку из dcm-файла
					// if (dcmObj.get(Tag.SpecificCharacterSet) != null
					// && dcmObj.get(Tag.SpecificCharacterSet).length() > 0) {
					// cs = SpecificCharacterSet.valueOf(dcmObj.get(
					// Tag.SpecificCharacterSet).getStrings(null, false));
					// }

					DicomElement elementMimeType = dcmObj
							.get(Tag.MIMETypeOfEncapsulatedDocument);
					// String mimeType = elementMimeType.getValueAsString(cs,
					// elementMimeType.length());
					// resp.setContentType("application/pdf");
					// resp.setContentType(mimeType);

					DicomElement elementData = dcmObj
							.get(Tag.EncapsulatedDocument);
					byte[] data = elementData.getBytes();

					pdfs.add(data);
					// BufferedOutputStream out = new BufferedOutputStream(resp
					// .getOutputStream());
					//
					// out.write(data);
					//
					// out.flush();
					// out.close();

				} catch (org.dcm4che2.data.ConfigurationError e) {
					if (e.getCause() instanceof UnsupportedEncodingException) {
						logger.fatal("Unsupported character set" + " " + e);
					}
					logger.fatal("" + e);
				} catch (IOException e) {
					e.printStackTrace();
					logger.fatal("" + e);
				} finally {
					try {
						if (din != null)
							din.close();
					} catch (IOException ignore) {

					}
				}
			}

		} catch (SQLException e) {
			throwPortalException("getImage error:", e);
		} finally {
			try {
				if (psSelect != null)
					psSelect.close();
				// connection.close();
			} catch (SQLException e) {
				throwPortalException("getImage error:", e);
			}
		}
		return pdfs;

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
