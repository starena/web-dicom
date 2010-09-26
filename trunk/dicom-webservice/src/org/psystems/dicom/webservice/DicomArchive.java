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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.psystems.dicom.commons.Util;
import org.psystems.dicom.commons.orm.DataException;
import org.psystems.dicom.commons.orm.Study;

public class DicomArchive {

	private static Logger logger = Logger.getLogger(DicomArchive.class);

	/**
	 * @param i
	 * @return
	 * @throws DicomWebServiceException
	 */
	public Study getStudy(long i) throws DicomWebServiceException {

		Study study = Study.getInstance(i);
		// study.setId(i);
		study.setStudyDate(new Date());
		study
				.setManufacturerModelUID("1.2.826.0.1.3680043.2.634.0.64717.2010225.13460.1");
		study.setStudyDoctor("Врач №1");
		study.setStudyId("studyID=" + i);
		study.setPatientName("Пациент №1");
		study.setPatientId("patientID=ХХХ");
		study.setStudyResult("Результат 'норма'");
		study.setStudyType("флюорография");
		study.setStudyUrl("http://localhost/" + i + ".dcm");

		return study;
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
			Connection connection = Util.getConnection(servletContext);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Study> data = new ArrayList<Study>();
		for (long i = 0; i < 10; i++) {
			Study study = Study.getInstance(i);
			study.setId(i);
			study.setStudyDate(new Date());
			study
					.setManufacturerModelUID("1.2.826.0.1.3680043.2.634.0.64717.2010225.13460.1");
			study.setStudyDoctor("Врач №1");
			study.setStudyId("studyID=" + i);
			study.setPatientName("Пациент №1");
			study.setPatientId("patientID=ХХХ");
			study.setStudyResult("Результат 'норма'");
			study.setStudyType("флюорография");
			study.setStudyUrl("http://localhost/" + i + ".dcm");
			data.add(study);
		}

		Study[] result = new Study[data.size()];
		return data.toArray(result);
	}


	/**
	 * Поиск исследований по типу
	 * 
	 * @param studyModality - (CR - флюорография)
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
	public Study[] findStudiesByType(String studyModality, String patientName, String patientShortName,
			String patientBirthDate, String patientSex, String beginStudyDate,
			String endStudyDate) throws DicomWebServiceException {

		ServletContext servletContext = (ServletContext) MessageContext
				.getCurrentMessageContext().getProperty(
						HTTPConstants.MC_HTTP_SERVLETCONTEXT);

		// System.out.println("!! servlet=" + servletContext);

		Connection connection;
		try {
			connection = Util.getConnection(servletContext);
			try {
				Study[] studies = Study.getStudues(connection, studyModality, null, patientName, patientShortName,
						patientBirthDate, patientSex, beginStudyDate,
						endStudyDate,null,null);
				
				String url = servletContext
				.getInitParameter("webdicom.ws.viewstudy.url");
				
				
				//"http://127.0.0.1:8888/study/"
				ArrayList<Study> tmpData = new ArrayList<Study>();
				
				
				for(int i=0; i<studies.length; i++) {
					studies[i].setStudyUrl(url+"/"+studies[i].getId());

					////Фильтруем результаты в которых есть отклонения (только для флюшек)
//					System.out.println("!!! studyModality="+studyModality);
					if(studies[i].getStudyModality()!=null && studies[i].getStudyModality().equals("CR")) {
						if(studies[i].getStudyResult()==null || studies[i].getStudyResult().length()==0) {
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
	 * Создание нового исследования
	 * 
	 * @param PatientId
	 * @param PatientName
	 * @param patientDateBirthday
	 * @param patientSex
	 * @param studyType
	 * @return
	 * @throws DicomWebServiceException
	 */
	public int newStudy(String PatientId, String PatientName,
			Date patientDateBirthday, String patientSex, String studyType,
			Date studyPlanningDate) throws DicomWebServiceException {
		if (PatientName == null)
			throw new DicomWebServiceException("PatientName is empty!");
		return 1;
	}

}