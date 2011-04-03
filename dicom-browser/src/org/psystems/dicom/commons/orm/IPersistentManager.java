package org.psystems.dicom.commons.orm;

import java.io.Serializable;

public interface IPersistentManager {

	public void makePesistent(Class<Serializable> clazz);
}
