package org.psystems.dicom.commons.orm;

import java.io.Serializable;

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
	 * Получение экземпляра сущности по id
	 * 
	 * @param id
	 * @return
	 */
	public Serializable getObjectbyID(Long id);

	/**
	 * Получение экземпляра сущности по uid
	 * 
	 * @param uid
	 * @return
	 */
	public Serializable getObjectbyUID(String uid);
}
