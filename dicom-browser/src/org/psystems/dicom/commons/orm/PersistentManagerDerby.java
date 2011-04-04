package org.psystems.dicom.commons.orm;

import java.io.Serializable;

public class PersistentManagerDerby implements IPersistentManager {

	void Test() {
		makePesistent(Direction.class);
	}

	@Override
	public void makePesistent(Serializable obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Serializable getObjectbyID(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getObjectbyUID(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
