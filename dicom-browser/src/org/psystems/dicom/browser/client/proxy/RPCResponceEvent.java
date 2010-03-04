package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

public class RPCResponceEvent implements Serializable {

	private static final long serialVersionUID = 4300229127183423102L;

	private long transactionId;// идентификатор траназкции
	private String version;// версия клиента
	private RPCResponce data;

	/**
	 * Инициализация класса
	 * 
	 * @param transactionId
	 * @param data
	 */
	public void init(long transactionId, String version, RPCResponce data) {
		this.transactionId = transactionId;
		this.version = version;
		this.data = data;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public String getVersion() {
		return version;
	}

	public RPCResponce getData() {
		return data;
	}
}
