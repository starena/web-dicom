/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.dicom.webservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.psystems.dicom.commons.UtilCommon;
import org.psystems.dicom.commons.orm.DataException;
import org.psystems.dicom.commons.orm.Diagnosis;
import org.psystems.dicom.commons.orm.Employee;
import org.psystems.dicom.commons.orm.ManufacturerDevice;
import org.psystems.dicom.commons.orm.Patient;
import org.psystems.dicom.commons.orm.PersistentManagerDerby;
import org.psystems.dicom.commons.orm.QueryDirection;
import org.psystems.dicom.commons.orm.Service;
import org.psystems.dicom.commons.orm.Study;
import org.psystems.dicom.commons.orm.Direction;
import org.psystems.dicom.webservice.DicomWebServiceException;

public class DicomArchive {

	private static Logger logger = Logger.getLogger(DicomArchive.class);

	/**
	 * @param i
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Study getStudy(long i) throws DicomWebServiceException {

		// Study study = Study.getInstance(i);
		// // study.setId(i);
		// study.setStudyDate(new Date());
		// study
		// .setManufacturerModelUID("1.2.826.0.1.3680043.2.634.0.64717.2010225.13460.1");
		// study.setStudyDoctor("Врач №1");
		// study.setStudyId("studyID=" + i);
		// study.setPatientName("Пациент №1");
		// study.setPatientId("patientID=ХХХ");
		// study.setStudyResult("Результат 'норма'");
		// study.setStudyType("флюорография");
		// study.setStudyUrl("http://localhost/" + i + ".dcm");
		//
		// return study;
		return null;
	}

	/**
	 * @param s
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Study[] findStudies(String s) throws DicomWebServiceException {

		// MessageContext msgContext =
		// MessageContext.getCurrentMessageContext();
		// System.out.println("!! msgContext="+msgContext);
		//		
		// System.out.println("!! MC_HTTP_SERVLET="+HTTPConstants.MC_HTTP_SERVLET);
		// System.out.println("!! MC_HTTP_SERVLETCONTEXT="+HTTPConstants.MC_HTTP_SERVLETCONTEXT);
		//		
		// Map props = msgContext.getProperties();
		// for( Iterator iter = props.keySet().iterator(); iter.hasNext();) {
		// Object key = iter.next();
		// Object val = props.get(key);
		// System.out.println("!! props ["+key + "]="+val);
		// }
		//		
		// HttpServlet prop =
		// ((HttpServlet)msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLET));
		// System.out.println("!! prop="+prop);

		// ServletContext context
		// =((HttpServlet)msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLET)).
		// getServletContext();
		// System.out.println("!! context="+context);

		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);
		System.out.println("!! servlet=" + servletContext);

		try {
			Connection connection = UtilCommon.getConnection(servletContext);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Study> data = new ArrayList<Study>();
		// for (long i = 0; i < 10; i++) {
		// Study study = Study.getInstance(i);
		// study.setId(i);
		// study.setStudyDate(new Date());
		// study
		// .setManufacturerModelUID("1.2.826.0.1.3680043.2.634.0.64717.2010225.13460.1");
		// study.setStudyDoctor("Врач №1");
		// study.setStudyId("studyID=" + i);
		// study.setPatientName("Пациент №1");
		// study.setPatientId("patientID=ХХХ");
		// study.setStudyResult("Результат 'норма'");
		// study.setStudyType("флюорография");
		// study.setStudyUrl("http://localhost/" + i + ".dcm");
		// data.add(study);
		// }

		Study[] result = new Study[data.size()];
		return data.toArray(result);
	}

	/**
	 * Поиск исследований по типу
	 * 
	 * @param studyModality
	 *            - (CR - флюорография)
	 * 
	 * @param patientName
	 * @param patientShortName
	 * @param patientBirthDate
	 * @param patientSex
	 * @param beginStudyDate
	 * @param endStudyDate
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Study[] findStudiesByType(String studyModality, String patientName,
			String patientShortName, String patientBirthDate,
			String patientSex, String beginStudyDate, String endStudyDate)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);

		// System.out.println("!! servlet=" + servletContext);

		Connection connection;
		try {
			connection = UtilCommon.getConnection(servletContext);
			try {
				Study[] studies = Study.getStudues(connection, studyModality,
						null, patientName, patientShortName, patientBirthDate,
						patientSex, beginStudyDate, endStudyDate, null, null);

				String url = servletContext
						.getInitParameter("webdicom.ws.viewstudy.url");

				// "http://127.0.0.1:8888/study/"
				ArrayList<Study> tmpData = new ArrayList<Study>();

				for (int i = 0; i < studies.length; i++) {
					studies[i].setStudyUrl(url + "/" + studies[i].getId());

					// //Фильтруем результаты в которых есть отклонения (только
					// для флюшек)
					// System.out.println("!!! studyModality="+studyModality);
					if (studies[i].getStudyModality() != null
							&& studies[i].getStudyModality().equals("CR")) {
						if (studies[i].getStudyResult() == null
								|| studies[i].getStudyResult().length() == 0) {
							studies[i].setStudyResult("норма");
							tmpData.add(studies[i]);
						}
					} else {
						tmpData.add(studies[i]);
					}
				}

				studies = new Study[tmpData.size()];
				tmpData.toArray(studies);

				return studies;
			} catch (DataException e) {
				logger.warn("Error get studies: " + e);
				throw new DicomWebServiceException(e);
			}
		} catch (SQLException e) {
			logger.warn("Error get studies: " + e);
			throw new DicomWebServiceException(e);
		}

	}

	/**
	 * Получение списка доступных аппаратов
	 * 
	 * @return
	 */
	public ManufacturerDevice[] getManufacturers() {
		ArrayList<ManufacturerDevice> devices = ManufacturerDevice
				.getAllManufacturerDevices();
		ManufacturerDevice[] result = new ManufacturerDevice[devices.size()];
		return devices.toArray(result);
	}

	/**
	 * Создание нового исследования (без вложения) Универсальный вариант
	 * 
	 * TODO Это хардкдрный вариант! т.к. моджно выставлять любые теги !!!
	 * 
	 * @param props
	 *            - теги
	 * @throws DicomWebServiceException
	 */
	public void newStudyUniversal(DicomTag[] tags)
			throws DicomWebServiceException {

		try {
			ServletContext servletContext = (ServletContext) MessageContext
					.getCurrentMessageContext().getProperty(
							HTTPConstants.MC_HTTP_SERVLETCONTEXT);

			/*
			 * !!! FormFiled: 00100010=ДЕренок !!! FormFiled: 00100040=M !!!
			 * FormFiled: 00080090= !!! FormFiled: 00081070= !!! FormFiled:
			 * 00081030= !!! FormFiled: 00102000=123 !!! FormFiled: 00324000=
			 * !!! FormFiled: content_type=image/jpg !!! UploadFile:
			 * upload;alg_shema.jpg;image/jpeg;true;65491 !!! FormFiled:
			 * 0020000D=1.2.40.0.13.1.40452786674097928919318313426061085423 !!!
			 * FormFiled:
			 * 0020000E=1.2.40.0.13.1.40452786674097928919318313426061085423.1288615271530
			 * !!! FormFiled: 00200010= !!! FormFiled: 00100021= !!! FormFiled:
			 * 00081090=ENDOSCP !!! FormFiled: 00080060=ES !!! FormFiled:
			 * 00100030=19740301 !!! FormFiled: 00080020=20100917 !!! FormFiled:
			 * 00321050=20101101
			 */
			Properties props = new Properties();
			for (int i = 0; i < tags.length; i++) {
				props.put(tags[i].getTagName(), tags[i].getTagValue());
			}

			UtilCommon.makeSendDicomFile(servletContext, props);
		} catch (Exception e) {
			throw new DicomWebServiceException(e);
		}
	}

	/**
	 * Создание нового исследования Узкоспециализированный вариант
	 * 
	 * @param transactionId
	 *            - Идентификатор транзакции (StudyID)
	 * @param patientId
	 *            - Идентификатор пациента (PatientID)
	 * @param studyModality
	 *            - Тип исследования (CR,ES,...)
	 * @param ManufacturerModelName
	 *            - Аппарат (может быть NULL)
	 * @param patientName
	 *            ФИО
	 * @param patientDateBirthday
	 *            формат: YYYY-MM-DD
	 * @param patientSex
	 *            формат: M|F
	 * @param studyPlanningDate
	 *            формат: YYYY-MM-DD
	 * @return
	 * @throws DicomWebServiceException
	 */
	public void newStudy(String transactionId, String patientId,
			String studyModality, String ManufacturerModelName,
			String patientName, String patientDateBirthday, String patientSex,
			String studyPlanningDate) throws DicomWebServiceException {

		if (transactionId == null || transactionId.length() == 0)
			throw new DicomWebServiceException("Illegal argument transactionId");

		if (patientId == null || patientId.length() == 0)
			throw new DicomWebServiceException("Illegal argument patientId");

		if (studyModality == null || studyModality.length() == 0)
			throw new DicomWebServiceException("Illegal argument studyModality");

		if (patientName == null || patientName.length() == 0)
			throw new DicomWebServiceException("Illegal argument patientName");

		if (patientDateBirthday == null || patientDateBirthday.length() == 0)
			throw new DicomWebServiceException(
					"Illegal argument patientDateBirthday");

		if (patientSex == null || patientSex.length() == 0)
			throw new DicomWebServiceException("Illegal argument patientSex");

		if (studyPlanningDate == null || studyPlanningDate.length() == 0)
			throw new DicomWebServiceException(
					"Illegal argument studyPlanningDate");

		try {
			ServletContext servletContext = (ServletContext) MessageContext
					.getCurrentMessageContext().getProperty(
							HTTPConstants.MC_HTTP_SERVLETCONTEXT);

			/*
			 * !!! FormFiled: 00100010=ДЕренок !!! FormFiled: 00100040=M !!!
			 * FormFiled: 00080090= !!! FormFiled: 00081070= !!! FormFiled:
			 * 00081030= !!! FormFiled: 00102000=123 !!! FormFiled: 00324000=
			 * !!! FormFiled: content_type=image/jpg !!! UploadFile:
			 * upload;alg_shema.jpg;image/jpeg;true;65491 !!! FormFiled:
			 * 0020000D=1.2.40.0.13.1.40452786674097928919318313426061085423 !!!
			 * FormFiled:
			 * 0020000E=1.2.40.0.13.1.40452786674097928919318313426061085423.1288615271530
			 * !!! FormFiled: 00200010= !!! FormFiled: 00100021= !!! FormFiled:
			 * 00081090=ENDOSCP !!! FormFiled: 00080060=ES !!! FormFiled:
			 * 00100030=19740301 !!! FormFiled: 00080020=20100917 !!! FormFiled:
			 * 00321050=20101101
			 */

			/*
			 * PatientID = 0x00100020; !!! FormFiled: 00081090=DUODiagnost !!!
			 * FormFiled: 00080060=DF !!! FormFiled: 00100010=FIO1 !!!
			 * FormFiled: 00100040=M !!! FormFiled: 00080090=Петрова Н.Н. врач
			 * !!! FormFiled: 00081070=Тебенев Е.Н. лаборант !!! FormFiled:
			 * 00081030=Описание !!! FormFiled: 00102000=Результат !!!
			 * FormFiled: 00324000=Протокол !!! FormFiled:
			 * content_type=application/pdf !!! UploadFile:
			 * upload;;application/octet-stream;true;0 !!! FormFiled: 0020000D=
			 * !!! FormFiled: 0020000E=null.1288778002514 !!! FormFiled:
			 * 00200010= Study ID !!! FormFiled: 00100021=1 !!! FormFiled:
			 * 00100030=19740301 !!! FormFiled: 00080020=20101102 дата
			 * исследования !!! FormFiled: 00321050=20101103 дата описания
			 */

			// org.dcm4che2.data.Tag

			Properties props = new Properties();
			props.put("00200010", transactionId.toUpperCase());// StudyID
			props.put("00100020", patientId.toUpperCase());// PatientID
			props.put("00080060", studyModality.toUpperCase());//
			if (ManufacturerModelName != null) {
				props.put("00081090", ManufacturerModelName.toUpperCase());// ManufacturerModelName
			}
			props.put("00100010", patientName.toUpperCase());// patientName
			props.put("00100030", Utils.SqlDate2DicomDate(patientDateBirthday
					.toUpperCase()));// patientDateBirthday
			props.put("00100040", patientSex.toUpperCase());// patientSex
			props.put("00080020", Utils.SqlDate2DicomDate(studyPlanningDate
					.toUpperCase()));// дата исследования

			// FIXME Сделать подстановку номального UID issue#50

			String StudyInstanceUID = "1.2.40.0.13.1" + "."
					+ new Date().getTime();
			String SeriesInstanceUID = StudyInstanceUID + "."
					+ new Date().getTime();

			props.put("0020000D", StudyInstanceUID);// StudyInstanceUID
			props.put("0020000E", SeriesInstanceUID);// Series Instance UID
			// taglist.add(maketag("0020000D","1.2.40.0.13.1.40452786674097928919318313426061085423"));
			// taglist.add(maketag("0020000E","1.2.40.0.13.1.40452786674097928919318313426061085423.1288615271532"));

			// FIXME Небольшой хак. приходится передавать content_type
			props.put("content_type", "application/pdf");// дата исследования

			UtilCommon.makeSendDicomFile(servletContext, props);
		} catch (Exception e) {
			throw new DicomWebServiceException(e);
		}
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
	 */
	public long makeDirection(String directionId, Employee doctorDirect,
			Diagnosis[] diagnosisDirect,
			Service[] servicesDirect, String dateDirection,
			ManufacturerDevice device, String datePlanned,
			String directionCode, String directionLocation, Patient patient)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) MessageContext
		.getCurrentMessageContext().getProperty(
				HTTPConstants.MC_HTTP_SERVLETCONTEXT);

		Connection connection;
		try {
			connection = UtilCommon.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			Direction drn = new Direction();
			drn.setDirectionId(directionId);
			drn.setDoctorDirect(doctorDirect);
			drn.setDiagnosisDirect(diagnosisDirect);
			drn.setServicesDirect(servicesDirect);
			drn.setDateDirection(Utils.formatSQL.parse(dateDirection));
			drn.setDevice(device);
			drn.setDatePlanned(Utils.formatSQL.parse(datePlanned));
			drn.setDirectionCode(directionCode);
			drn.setDirectionLocation(directionLocation);
			drn.setPatient(patient);

			return pm.makePesistent(drn);
			// return (Direction) pm.getObjectbyInternalID(internalID);

		} catch (SQLException e) {
			logger.warn("Error make direction: " + e);
			throw new DicomWebServiceException(e);
		} catch (DataException e) {
			logger.warn("Error make direction: " + e);
			throw new DicomWebServiceException(e);
		} catch (ParseException e) {
			logger.warn("Error make direction: " + e);
			throw new DicomWebServiceException(e);
		}
	}

	
	/**
	 * @param directionId
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Direction getDirectionByInternalId(String internalID)
			throws DicomWebServiceException {
		
		
		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);


		Connection connection;
		try {
			connection = UtilCommon.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			return (Direction) pm.getObjectbyInternalID(internalID);

		} catch (SQLException e) {
			logger.warn("Error get direction: " + e);
			throw new DicomWebServiceException(e);
		} catch (DataException e) {
			logger.warn("Error get direction: " + e);
			throw new DicomWebServiceException(e);
		}
	}
	
	public Direction[] queryDirection(QueryDirection query)
			throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);

		Connection connection;
		try {
			connection = UtilCommon.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			ArrayList<Direction> directions = pm.queryDirections(query);
			return directions.toArray(new Direction[directions.size()]);

		} catch (SQLException e) {
			logger.warn("Error get direction: " + e);
			throw new DicomWebServiceException(e);
		} catch (DataException e) {
			logger.warn("Error get direction: " + e);
			throw new DicomWebServiceException(e);
		}
	}
		
	/**
	 * @param directionId
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Direction getDirectionById(long id)
			throws DicomWebServiceException {
		
		
		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);


		Connection connection;
		try {
			connection = UtilCommon.getConnection(servletContext);
			PersistentManagerDerby pm = new PersistentManagerDerby(connection);
			return (Direction) pm.getObjectbyID(id);

		} catch (SQLException e) {
			logger.warn("Error get direction: " + e);
			throw new DicomWebServiceException(e);
		} catch (DataException e) {
			logger.warn("Error get direction: " + e);
			throw new DicomWebServiceException(e);
		}

//		Direction drn = new Direction();
//		drn.setDateDirection(new Date());
//
//		ManufacturerDevice device = new ManufacturerDevice();
//		device.setManufacturerModelName("TestModel");
//		device.setManufacturerModelDescription("Тестовый аппарат");
//		device.setManufacturerModelType("ES");
//		device.setManufacturerModelTypeDescription("Эндоскоп");
//		drn.setDevice(device);
//
//
//		Diagnosis dia = new Diagnosis();
//		dia.setDiagnosisCode("M01.1");
//		dia.setDiagnosisType(Diagnosis.TYPE_MAIN);// основной
//		dia.setDiagnosisDescription("Заболевание такое-то...");
//		dia.setDiagnosisSubType("Предварительный");
//		
//		Diagnosis dia1 = new Diagnosis();
//		dia1.setDiagnosisCode("K01.1");
//		dia1.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);// сопутствующий
//		dia1.setDiagnosisDescription("Еще одно заболевание такое-то...");
//		dia1.setDiagnosisSubType("Предварительный");
//		
//		drn.setDiagnosisDirect(new Diagnosis[] {dia,dia1});
//
//		drn.setDirectionCode("Test code");
//		drn.setDirectionId("123456");
//		drn.setDirectionLocation("605");
//		
//		Employee doctorDirect = new Employee();
//		doctorDirect.setEmployeeCode("123");
//		doctorDirect.setEmployeeName("Врач Петров И.И.");
//		doctorDirect.setEmployeeType(Employee.TYPE_DOCTOR);
//		drn.setDoctorDirect(doctorDirect);
//		
//		Patient patient = new Patient();
//		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//		
//		
//		try {
//			patient.setPatientBirthDate(format.parse("01-03-1974"));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		patient.setPatientId("123123");
//		patient.setPatientName("Иванов Иван Иванович");
//		patient.setPatientSex("M");
//		patient.setPatientShortName("ИВАИВ74");
//		drn.setPatient(patient);
//		
//		Service srv = new Service();
//		srv.setServiceCode("A.03.16.001.01");
//		srv.setServiceAlias("ЭГДС");
//		srv.setServiceDescription("Эзофагогастродуоденоскопия диагностическая");
//		
//		Service srv1 = new Service();
//		srv1.setServiceCode("A.02.12.002.02");
//		srv1.setServiceAlias("СМАД");
//		srv1.setServiceDescription("Суточное мониторирование артериального давления");
//		
//		drn.setServicesDirect(new Service[] { srv,srv1});
//		
//		
//		return drn;

	}

}