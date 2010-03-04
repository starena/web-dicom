package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

/**
 * @author dima_d Запрос на получение тегов
 * 
 */
public class DcmTagsRPCRequest implements RPCRequest, Serializable {

	private static final long serialVersionUID = -702488773020935036L;

	private int idDcm;// DI Dicom-файла в БД

	public int getIdDcm() {
		return idDcm;
	}

	public void setIdDcm(int idDcm) {
		this.idDcm = idDcm;
	}

}
