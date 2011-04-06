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
    
    Russian translation <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
     
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
 */
package org.psystems.dicom.commons.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.psystems.dicom.browser.client.component.WorkListPanel;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;

/**
 * @author dima_d
 * 
 */
public class StudyImplDerby extends Study {

	

	public static List<Study> getStudues(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Создание экземпляра из БД
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private static Study getInstance(ResultSet rs) throws SQLException {
		
		Study study = new StudyImplDerby();
		study.setId(rs.getLong("ID"));
		study.setStudyInstanceUID(rs.getString("STUDY_UID"));
		study.setStudyModality(rs.getString("STUDY_MODALITY"));
		study.setStudyType(rs.getString("STUDY_TYPE"));
		study.setStudyDescription(rs.getString("STUDY_DESCRIPTION"));
		study.setStudyDate(rs.getDate("STUDY_DATE"));
		study.setManufacturerModelUID(rs
				.getString("STUDY_MANUFACTURER_UID"));
		study.setManufacturerModelName(rs
				.getString("STUDY_MANUFACTURER_MODEL_NAME"));
		study.setStudyDoctor(rs.getString("STUDY_DOCTOR"));
		study.setStudyOperator(rs.getString("STUDY_OPERATOR"));
		study.setStudyViewprotocol(rs.getString("STUDY_VIEW_PROTOCOL"));
		study.setStudyViewprotocolDate(rs.getDate("STUDY_VIEW_PROTOCOL_DATE"));
		study.setStudyId(rs.getString("STUDY_ID"));
		study.setPatientName(rs.getString("PATIENT_NAME"));
		study.setPatientShortName(rs.getString("PATIENT_SHORTNAME"));
		study.setPatientSex(rs.getString("PATIENT_SEX"));
		study.setPatientBirthDate(rs.getDate("PATIENT_BIRTH_DATE"));
		study.setPatientId(rs.getString("PATIENT_ID"));
		study.setStudyResult(rs.getString("STUDY_RESULT"));
		study.setStudyUrl("");// TODO сделать!!
		study.setDcmFiles(new Long[] { 1l, 2l, 3l });// TODO сделать!!
		study.setStudyDateModify(rs.getTimestamp("DATE_MODIFIED"));
		
		study.setStudyDateRemoved(null);
		if(rs.getTimestamp("REMOVED")!=null)
		study.setStudyDateRemoved(rs.getTimestamp("REMOVED"));
		
		return study;
		
	}
	
	/**
	 * Получение прокси-класса для файла исследования
	 * @param connection
	 * @param idFile
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<DcmFileProxy> getDcmFileProxies(Connection connection, long idStudy)
			throws DataException {
		
		PreparedStatement psFiles = null;
		
		try {
		psFiles = connection.prepareStatement("SELECT * FROM WEBDICOM.DCMFILE WHERE FID_STUDY = ? ");
		psFiles.setLong(1, idStudy);
		ResultSet rs = psFiles.executeQuery();

			ArrayList<DcmFileProxy> files = new ArrayList<DcmFileProxy>();
			while (rs.next()) {
				DcmFileProxy dcmfileProxy = new DcmFileProxy();
				
				dcmfileProxy.setId(rs.getLong("ID"));
				dcmfileProxy.setIdStudy(rs.getLong("FID_STUDY"));
				dcmfileProxy.setType(rs.getString("TYPE"));
				dcmfileProxy.setMimeType(rs.getString("MIME_TYPE"));
				dcmfileProxy.setEncapsulatedDocSize(rs.getLong("DOCUMENT_SIZE"));
				dcmfileProxy.setFileName(rs.getString("DCM_FILE_NAME"));
				dcmfileProxy.setFileSize(rs.getLong("DCM_FILE_SIZE"));
				dcmfileProxy.setImageSize(rs.getLong("IMAGE_FILE_SIZE"));
				dcmfileProxy.setImageWidth(rs.getInt("IMAGE_WIDTH"));
				dcmfileProxy.setImageHeight(rs.getInt("IMAGE_HEIGHT"));
				
				dcmfileProxy.setDateRemoved(null);
				if(rs.getTimestamp("REMOVED")!=null)
					dcmfileProxy.setDateRemoved(rs.getTimestamp("REMOVED"));
				
				files.add(dcmfileProxy);
				
			}
			return files;
		}
		catch(SQLException e) {
			throw new DataException(e);
		}
		finally {

			try {
				if (psFiles != null)
					psFiles.close();

			} catch (SQLException e) {
				throw new DataException(e);
			}
		}
		
	}
	
	public static Study[] getStudues(Connection connection,
			String studyModality, String manufacturerModelName, String patientName, String patientShortName,
			String patientBirthDate, String patientSex, String beginStudyDate,
			String endStudyDate, String studyResult, String sortOrder) throws DataException {

		PreparedStatement psSelect = null;

		// TODO Сделать ограничение на количество возвращаемых строк

		// "SELECT * FROM WEBDICOM.STUDY"
		// + " WHERE UPPER(PATIENT_NAME) like UPPER( ? || '%')"
		// + " order by PATIENT_NAME, STUDY_DATE "

		String sqlAddon = "";

		if (studyModality != null && studyModality.length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " STUDY_MODALITY = ? ";
		}
		
		if (manufacturerModelName != null && manufacturerModelName.length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " STUDY_MANUFACTURER_MODEL_NAME = ? ";
		}

		if (patientName != null && patientName.length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " UPPER(PATIENT_NAME) like UPPER( ? || '%') ";
		}
		
		
		if (patientShortName != null && patientShortName.length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " PATIENT_SHORTNAME = UPPER(?) ";
		}

		if (patientBirthDate != null && patientBirthDate.length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " PATIENT_BIRTH_DATE = ? ";
		}

		if (patientSex != null && patientSex.length() > 0) {
			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " PATIENT_SEX = ? ";
		}

		if (beginStudyDate != null && beginStudyDate.length() > 0
				&& endStudyDate != null && endStudyDate.length() > 0) {

			if (java.sql.Date.valueOf(beginStudyDate).getTime() > java.sql.Date
					.valueOf(endStudyDate).getTime()) {
				throw new DataException(new IllegalArgumentException(
						"beginStudyDate > endStudyDate"));
			}

			if (sqlAddon.length() != 0)
				sqlAddon += " AND ";
			sqlAddon += " STUDY_DATE BETWEEN ? AND ? ";
		} else {

			if (beginStudyDate != null && beginStudyDate.length() > 0
					&& (endStudyDate == null || endStudyDate.length() > 0)) {
				if (sqlAddon.length() != 0)
					sqlAddon += " AND ";
				sqlAddon += " STUDY_DATE > ? ";
			}

			if (endStudyDate != null && endStudyDate.length() > 0
					&& (beginStudyDate == null || beginStudyDate.length() > 0)) {
				if (sqlAddon.length() != 0)
					sqlAddon += " AND ";
				sqlAddon += " STUDY_DATE < ?  ";
			}
			
			
		}
		
		
		if (studyResult != null && studyResult.length() > 0) {
			if (sqlAddon.length() != 0)
				
			if(studyResult.equals("new")) {
				sqlAddon += " AND ";
				sqlAddon += " ( STUDY_VIEW_PROTOCOL is NULL OR STUDY_VIEW_PROTOCOL = '' ) ";
			}
			if(studyResult.equals("old")) {
				sqlAddon += " AND ";
				sqlAddon += " ( STUDY_VIEW_PROTOCOL IS NOT NULL AND STUDY_VIEW_PROTOCOL != '' )";
			}
			
		}
		String order = "PATIENT_NAME, STUDY_DATE";
		if(sortOrder!=null) {
			order = sortOrder;
		}

		String sql = "SELECT * FROM WEBDICOM.STUDY" + " WHERE " + sqlAddon
				+ " order by "+order;


	
		
		IllegalArgumentException ex = new IllegalArgumentException(
				"All query arguments empty! Set any argument's");
		if (sqlAddon.length() == 0)
			throw new DataException(ex);

		try {

			psSelect = connection.prepareStatement(sql);
			int index = 1;

			if (studyModality != null && studyModality.length() > 0) {
				psSelect.setString(index++, studyModality);
			}
			
			if (manufacturerModelName != null && manufacturerModelName.length() > 0) {
				psSelect.setString(index++, manufacturerModelName);
			}

			if (patientName != null && patientName.length() > 0) {
				psSelect.setString(index++, patientName);
			}

			if (patientShortName != null && patientShortName.length() > 0) {
				psSelect.setString(index++, patientShortName);
			}

			if (patientBirthDate != null && patientBirthDate.length() > 0) {
				psSelect.setDate(index++, java.sql.Date
						.valueOf(patientBirthDate));
			}

			if (patientSex != null && patientSex.length() > 0) {
				psSelect.setString(index++, patientSex);
			}

			if (beginStudyDate != null && beginStudyDate.length() > 0
					&& endStudyDate != null && endStudyDate.length() > 0) {
				psSelect
						.setDate(index++, java.sql.Date.valueOf(beginStudyDate));
				psSelect.setDate(index++, java.sql.Date.valueOf(endStudyDate));
			}else {
				
				if (beginStudyDate != null && beginStudyDate.length() > 0
						&& (endStudyDate == null || endStudyDate.length() > 0)) {
					psSelect
					.setDate(index++, java.sql.Date.valueOf(beginStudyDate));
				}

				if (endStudyDate != null && endStudyDate.length() > 0
						&& (beginStudyDate == null || beginStudyDate.length() > 0)) {
					psSelect
					.setDate(index++, java.sql.Date.valueOf(endStudyDate));
				}
			}
			
			
		

			ResultSet rs = psSelect.executeQuery();

			ArrayList<Study> data = new ArrayList<Study>();

			
			int counter = 1;
			while (rs.next()) {
				//TODO убрать в конфиг
				if(counter++> WorkListPanel.maxResultCount) break;
				data.add(getInstance(rs));
			}
			rs.close();

//			System.out.println("!!!! data="+data.size());
			Study[] result = new Study[data.size()];
			return data.toArray(result);

		} catch (SQLException e) {
			throw new DataException(e);
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();

			} catch (SQLException e) {
				throw new DataException(e);
			}
		}

	}

	
	
	/**
	 * @param connection
	 * @param findId
	 * @return
	 */
	public static Study getStudyByID (Connection connection, Long findId)  throws DataException {
		
		PreparedStatement psSelect = null;
		String sql = "SELECT * FROM WEBDICOM.STUDY WHERE ID = ?";

		try {

			psSelect = connection.prepareStatement(sql);
			psSelect.setLong(1, findId);
			ResultSet rs = psSelect.executeQuery();

			while (rs.next()) {
				return getInstance(rs);
			}
			rs.close();


		} catch (SQLException e) {
			throw new DataException(e);
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();

			} catch (SQLException e) {
				throw new DataException(e);
			}
		}
		return null;

	}
	
	/**
	 * @param connection
	 * @param idStudy
	 * @param removed
	 * @throws DefaultGWTRPCException
	 */
	public static void studyRemoveRestore(Connection connection, long idStudy,
			boolean removed) throws DataException {
		
		PreparedStatement psSelect = null;
		String sql = null;
		if (removed)
			sql = "UPDATE WEBDICOM.STUDY SET REMOVED = CURRENT_TIMESTAMP WHERE ID = ?";
		else
			sql = "UPDATE WEBDICOM.STUDY SET REMOVED = NULL WHERE ID = ?";

		try {

			psSelect = connection.prepareStatement(sql);
			psSelect.setLong(1, idStudy);
			connection.setAutoCommit(false);
			int count = psSelect.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			throw new DataException(e);
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();

			} catch (SQLException e) {
				throw new DataException(e);
			}
		}
	}
	
	
	/**
	 * 
	 */
	public static void dcmFileRemoveRestore(Connection  connection, long idDcmFile, boolean removed) throws DataException {
		
		PreparedStatement psSelect = null;
		String sql = null;
		if (removed)
			sql = "UPDATE WEBDICOM.DCMFILE SET REMOVED = CURRENT_TIMESTAMP WHERE ID = ?";
		else
			sql = "UPDATE WEBDICOM.DCMFILE SET REMOVED = NULL WHERE ID = ?";

		try {

			psSelect = connection.prepareStatement(sql);
			psSelect.setLong(1, idDcmFile);
			connection.setAutoCommit(false);
			int count = psSelect.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			throw new DataException(e);
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();

			} catch (SQLException e) {
				throw new DataException(e);
			}
		}
	}
	
	

}
