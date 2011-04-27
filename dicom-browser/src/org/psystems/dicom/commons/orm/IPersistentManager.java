package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.ArrayList;

import org.psystems.dicom.commons.orm.entity.DataException;
import org.psystems.dicom.commons.orm.entity.Direction;
import org.psystems.dicom.commons.orm.entity.QueryDirection;
import org.psystems.dicom.commons.orm.entity.QueryStudy;
import org.psystems.dicom.commons.orm.entity.Study;

public interface IPersistentManager {

	/**
	 * Сохранение направления
	 * 
	 * @param drn
	 * @return
	 * @throws DataException
	 */
	public long pesistentDirection(Direction drn) throws DataException;

	/**
	 * Получение экземпляра направления по его внутренему Id
	 * (PtudyId,PatientId,...)
	 * 
	 * @param id
	 * @return
	 */
	public Direction getDirectionByDirectionId(String internalID)
			throws DataException;

	/**
	 * Получение экземпляра направления по id
	 * 
	 * @param id
	 * @return
	 */
	public Direction getDirectionByID(Long id) throws DataException;
	
	/**
	 * Получение экземпляра исследования по id
	 * 
	 * @param id
	 * @return
	 */
	public Study getStudyByID(Long id) throws DataException;

	/**
	 * Получение экземпляра исследования по uid
	 * 
	 * @param uid
	 * @return
	 */
	public Study getStudyByUID(String uid) throws DataException;


	/**
	 * Получение коллекции направлений
	 * 
	 * @param request
	 * @return
	 * @throws DataException
	 */
	public ArrayList<Direction> queryDirections(QueryDirection request)
			throws DataException;
	
	/**
	 * Получение коллекции исследований
	 * 
	 * @param request
	 * @return
	 * @throws DataException
	 */
	public ArrayList<Study> queryStudies(QueryStudy request)
			throws DataException;
}
