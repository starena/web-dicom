package org.psystems.dicom.webservice.test;

import java.util.List;

import org.psystems.dicom.webservice.client.DicomService;
import org.psystems.dicom.webservice.client.DicomServiceService;
import org.psystems.dicom.webservice.client.Direction;
import org.psystems.dicom.webservice.client.QueryStudy;
import org.psystems.dicom.webservice.client.QueryStudyReq;
import org.psystems.dicom.webservice.client.Study;

public class Test {

	private static DicomServiceService service;
	private static DicomService port;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		service = new DicomServiceService();
		port = service.getDicomServicePort();

		queryStudy();
		qetDirrection();
	}

	private static void queryStudy() {

		System.out.println("queryStudy...");

		QueryStudyReq request = new QueryStudyReq();
		QueryStudy query = new QueryStudy();
		query.setId(18l);
		request.setArg0(query);

		List<Study> studies = port.queryStudy(request).getReturn();
		for (Study study : studies) {
			System.out.println("study: " + study);
		}
	}
	
	private static void qetDirrection() {

		System.out.println("qetDirrection...");

		

		Direction drn = port.getDirectionById(130l);
		System.out.println("Direction: "+drn.getDevice().getManufacturerModelTypeDescription());
		
	}

}
