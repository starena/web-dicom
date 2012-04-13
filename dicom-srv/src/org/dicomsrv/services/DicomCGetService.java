package org.dicomsrv.services;

import java.io.IOException;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.DicomServiceException;
import org.dcm4che2.net.service.CGetSCP;
import org.dcm4che2.net.service.DicomService;

public class DicomCGetService extends DicomService implements CGetSCP {

	protected DicomCGetService(String sopClass) {
		super(sopClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cget(Association as, int pcid, DicomObject rq,
			DicomObject rsp) throws DicomServiceException, IOException {
		// TODO Auto-generated method stub
		
	}

}
