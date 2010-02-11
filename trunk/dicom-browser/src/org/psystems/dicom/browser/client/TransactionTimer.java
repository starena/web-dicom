package org.psystems.dicom.browser.client;

import com.google.gwt.user.client.Timer;

public abstract class TransactionTimer extends Timer {

	long transactionId;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

}
