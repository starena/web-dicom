package org.psystems.dicom.browser.client.component;

import java.util.Date;

import org.psystems.dicom.browser.client.Dicom_browser;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;
import org.psystems.dicom.browser.client.service.DicSuggestBoxService;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * @author dima_d
 * 
 *         Компонент выбора значения из словаря
 */
public class DicSuggestBox extends Composite {

	private SuggestBox box;
	private long searchTransactionID;
	private String dicName = null;// название словаря

	public DicSuggestBox(String dicName) {
		this.dicName = dicName;
		ItemSuggestOracle oracle = new ItemSuggestOracle();
		box = new SuggestBox(oracle);
		initWidget(box);
	}

	public class ItemSuggestOracle extends SuggestOracle {
		public boolean isDisplayStringHTML() {
			return true;
		}

		public void requestSuggestions(SuggestOracle.Request req,
				SuggestOracle.Callback callback) {
			try {
				searchTransactionID = new Date().getTime();
				DicSuggestBoxService.Util.getInstance().getSuggestions(
						searchTransactionID, Dicom_browser.version, dicName,
						req, new ItemSuggestCallback(req, callback));
			} catch (DefaultGWTRPCException e) {
				Dicom_browser.showErrorDlg(e);
				e.printStackTrace();
			}
		}

		class ItemSuggestCallback implements AsyncCallback {
			private SuggestOracle.Request req;
			private SuggestOracle.Callback callback;

			public ItemSuggestCallback(SuggestOracle.Request _req,
					SuggestOracle.Callback _callback) {
				req = _req;
				callback = _callback;
			}

			public void onFailure(Throwable error) {
				Dicom_browser.showErrorDlg(error);
				callback.onSuggestionsReady(req, new SuggestOracle.Response());
				Dicom_browser.showErrorDlg(error);
			}

			public void onSuccess(Object retValue) {

				// TODO попробовать сделать нормлаьный interrupt (дабы
				// не качать все данные)
				// Если сменился идентификатор транзакции, то ничего не
				// принимаем
				if (searchTransactionID != ((SuggestTransactedResponse) retValue)
						.getTransactionId()) {
					return;
				}

				callback.onSuggestionsReady(req,
						(SuggestOracle.Response) retValue);
			}
		}

	}

	/**
	 * Получить внутренний SuggestBox
	 * @return
	 */
	public SuggestBox getSuggestBox() {
	    return box;
	}
	
	

}
