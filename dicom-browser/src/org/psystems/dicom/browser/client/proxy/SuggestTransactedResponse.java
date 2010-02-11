package org.psystems.dicom.browser.client.proxy;

import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Респонс с поддержкой транзакций
 * 
 * @author dima_d
 * 
 */
public class SuggestTransactedResponse extends SuggestOracle.Response {

	// идентификатор транзакции
	private long transactionId;

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getTransactionId() {
		return transactionId;
	}

}
