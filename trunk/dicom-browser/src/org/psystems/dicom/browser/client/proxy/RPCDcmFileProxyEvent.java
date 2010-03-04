package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

/**
 * TODO Передалеть на интерфейс RPCResponce
 * @author dima_d
 * 
 */
public class RPCDcmFileProxyEvent implements Serializable {

	private static final long serialVersionUID = 4300229127183423102L;
	//идентификатор траназкции
	private long transactionId;
	//данные (Object не поддерживается...)
	private DcmFileProxy[] data;//TODO Сделать УНИВЕРСАЛЬНО !!!

	/**
	 * Инициализация класса
	 * 
	 * @param transactionId
	 * @param data
	 */
	public void init(long transactionId, DcmFileProxy[] data) {
		this.transactionId = transactionId;
		this.data = data;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public DcmFileProxy[] getData() {
		return data;
	}

}
