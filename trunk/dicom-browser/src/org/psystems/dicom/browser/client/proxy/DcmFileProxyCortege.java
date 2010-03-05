package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author dima_d
 * 
 */
public class DcmFileProxyCortege implements Serializable {

	private static final long serialVersionUID = -7977305129674187420L;
	private String studyId; // ID исследования
	private ArrayList<DcmFileProxy> dcmProxies = new ArrayList<DcmFileProxy>();

	/**
	 * Инициализация класса
	 * 
	 * @param studyId
	 */
	public void init(String studyId) {
		this.studyId = studyId;
	}

	public ArrayList<DcmFileProxy> getDcmProxies() {
		return dcmProxies;
	}

	public void setDcmProxies(ArrayList<DcmFileProxy> dcmProxies) {
		this.dcmProxies = dcmProxies;
	}

	public String getStudyId() {
		return studyId;
	}
	

}
