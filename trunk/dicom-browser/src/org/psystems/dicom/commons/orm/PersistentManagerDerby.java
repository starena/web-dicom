package org.psystems.dicom.commons.orm;

import java.io.Serializable;

public class PersistentManagerDerby implements IPersistentManager {

	@Override
	public void makePesistent(Class<Serializable> clazz) {
		// TODO Auto-generated method stub

	}
	
	void Test () {
		makePesistent(Direction.class);
	}

}
