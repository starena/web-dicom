package org.psystems.dicom.commons.orm;

import java.io.Serializable;
import java.util.ArrayList;

public interface IPersistentManager {

	/**
	 * Сохранение сущности
	 * 
	 * @param obj
	 * @return
	 * @throws DataException
	 */
	public long makePesistent(Serializable obj) throws DataException;

	/**
	 * Получение экземпляра сущности по его внутренему Id
	 * (PtudyId,PatientId,...)
	 * 
	 * @param id
	 * @return
	 */
	public Serializable getObjectbyInternalID(String internalID)
			throws DataException;

	/**
	 * Получение экземпляра сущности по id
	 * 
	 * @param id
	 * @return
	 */
	public Serializable getObjectbyID(Long id) throws DataException;

	/**
	 * Получение экземпляра сущности по uid
	 * 
	 * @param uid
	 * @return
	 */
	public Serializable getObjectbyUID(String uid) throws DataException;


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
