package org.dicomsrv.services;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.DicomServiceException;
import org.dcm4che2.net.DimseRSP;
import org.dcm4che2.net.NetworkApplicationEntity;
import org.dcm4che2.net.service.CFindService;
import org.dicomsrv.database.DatabaseFind;
import org.dicomsrv.ui.MainWindow;

public class DicomCFindGetService extends CFindService {

    private NetworkApplicationEntity ae = DicomServicesCore.getNetworkApplicationEntity();
    
    private static Object eventListener;
    
    public static void setEventListener(Object eventListener) {
		DicomCFindGetService.eventListener = eventListener;
	}

	public DicomCFindGetService() {
		super(new String [] {
				/*UID.StudyRootQueryRetrieveInformationModelFIND,
				UID.PatientRootQueryRetrieveInformationModelFIND,
				UID.PrivateStudyRootQueryRetrieveInformationModelFIND,*/
				UID.ModalityWorklistInformationModelFIND,
			}, DicomServicesCore.getExecutor());
		ae.register(this);
	}

	@Override
	protected DimseRSP doCFind(Association as, int pcid, DicomObject cmd,
			DicomObject data, DicomObject rsp) throws DicomServiceException {
		
		// download operation
		if (data.getString(Tag.AccessoryCode) != null && data.getString(Tag.AccessoryCode).equals("5000-download")) {
			DatabaseFind databaseFind = new DatabaseFind();
			databaseFind.generateQuery(data);
			databaseFind.executeFindQuery();
			if (databaseFind.hasMoreResults()) {
				// send file
				String[] files = new String[] { databaseFind
						.getResultPathName() };
				DicomSend.sendFiles(as.getRemoteAET(), as.getConnector()
						.getHostname(), 11119, files);
			}
			databaseFind.closeConnection();
			MainWindow window = (MainWindow) eventListener;
			window.addMessageToConsole("Received GET request!\n");
			return null;
		}
		// c-find operation
		else {
			MainWindow window = (MainWindow) eventListener;
			window.addMessageToConsole("Received QUERY request!\n");
			return new DicomMultiFindRSP(data, rsp);
		}
	}

	
}
