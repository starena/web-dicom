package org.dicomsrv.services;

import java.io.IOException;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;


public class ServiceManager {

	private final DicomStorageService storageService = new DicomStorageService();
		
	@SuppressWarnings("unused")
	private final DicomCFindGetService cFindService = new DicomCFindGetService();
	
	public static int STORAGE_SERVICE_EVENT = 1;
	public static int FIND_SERVICE_EVENT = 2;
	public static int SEND_SERVICE_EVENT = 3;
	
	
	public void startServices(String aeTitle, int port, String hostname, String destination, Object eventListener) {
		DicomServicesCore.prepare();
		DicomServicesCore.setPort(port);
		DicomServicesCore.setHostname(hostname);
		DicomServicesCore.setAEtitle(aeTitle);
		DicomServicesCore.setPackPDV(true);
		DicomServicesCore.setTcpNoDelay(true);
		DicomServicesCore.initTransferCapability();
		storageService.setDestination(destination);
        storageService.setEventListener(eventListener);
        DicomCFindGetService.setEventListener(eventListener);
        try {
			DicomServicesCore.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopServices() {
		DicomServicesCore.stop();
	}
	
	private String getStringFromObject(int tag, DicomObject dicomObject) {
		String returnValue = dicomObject.getString(tag); 
		return returnValue == null ? "[ Unknown ]" : returnValue;
	}
	
	public String getRetrievedObjectInformation() {
		DicomObject dicomObject; 
		String returnValue = "";

		// TODO: Przerobic na wartosci przekazywane do bazy danych
		while ((dicomObject = storageService.getFirstRetrievedDicomObject()) != null) {
			returnValue += "Retrieved Dicom Object:" + "\n"
			+"\tName: " + getStringFromObject(Tag.PatientName, dicomObject) + "\n"
			+"\tSex: " + getStringFromObject(Tag.PatientSex, dicomObject) + "\n"
			+"\tBirth Date: " + getStringFromObject(Tag.PatientBirthDate, dicomObject) + "\n"
			+"\tStudy Date: " + getStringFromObject(Tag.StudyDate, dicomObject) + "\n";
		}
		
		return returnValue;
	}
	
	public int getMessageType() {
		return FIND_SERVICE_EVENT;
	}
}
