/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.psystems.dicom.browser.client.Browser;
import org.psystems.dicom.browser.client.TransactionTimer;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.PatientsRPCRequest;
import org.psystems.dicom.browser.client.proxy.PatientsRPCResponse;
import org.psystems.dicom.browser.client.proxy.QueryDirectionProxy;
import org.psystems.dicom.browser.client.proxy.QueryStudyProxy;
import org.psystems.dicom.browser.client.proxy.RPCDcmProxyEvent;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;
import org.psystems.dicom.browser.client.service.ItemSuggestService;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Панель поиска
 * 
 * @author dima_d
 * 
 */
public class SearchPanel extends Composite implements ValueChangeHandler<String> {

    private Browser Application;

    private String searchTitle = "...введите фамилию (% - любой символ)...";
    SearchResultPanel resultPanel;
    private Button sendButton;
    private Button clearButton;
    private SuggestBox nameField;
    private String type = "study";// Тип (исследования, пациенты)

    // Идентификатор транзакции (Время последнего запроса)
    private long searchTransactionID;

    protected boolean showRemovedStudies = false;// показ удаленных исследований
    protected ArrayList<StudyProxy> removedCards = new ArrayList<StudyProxy>();

    private VerticalPanel toolPanel;

    /**
     * @param application
     */
    public SearchPanel(Browser application) {

	this.Application = application;
	History.addValueChangeHandler(this);

	VerticalPanel mainPanel = new VerticalPanel();
	_construct(mainPanel);

	initWidget(mainPanel);

    }

    /**
     * Задание панели для отображения результатов поиска
     * 
     * @param panel
     */
    public void setResultPanel(SearchResultPanel panel) {
	resultPanel = panel;
    }

    /**
     * Размещение компонентов TODO Перенести в суперкласс и сделать абстрактным?
     * 
     * @param mainPanel
     */
    private void _construct(Panel mainPanel) {

	HorizontalPanel searchPanel = new HorizontalPanel();
	mainPanel.add(searchPanel);
	searchPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

	// resultPanel = new VerticalPanel();
	// mainPanel.add(resultPanel);

	sendButton = new Button("Поиск");
	sendButton.setStyleName("SearchToolButton");

	ItemSuggestOracle oracle = new ItemSuggestOracle();
	nameField = new SuggestBox(oracle);
	nameField.addStyleName("DicomSuggestionEmpty");

	nameField.setLimit(10);
	nameField.setWidth("600px");
	nameField.setTitle(searchTitle);
	nameField.setText(searchTitle);

	nameField.getTextBox().addFocusHandler(new FocusHandler() {

	    @Override
	    public void onFocus(FocusEvent event) {

		if (Application.showPageIntro) {
		    Application.showPageIntro = false;
		    resultPanel.clear();
		}

		nameField.removeStyleName("DicomSuggestionEmpty");
		nameField.addStyleName("DicomSuggestion");

		if (nameField.getText().equals(nameField.getTitle())) {
		    nameField.setValue("");
		} else {
		    nameField.setValue(nameField.getValue());
		}
	    }

	});

	nameField.getTextBox().addBlurHandler(new BlurHandler() {

	    @Override
	    public void onBlur(BlurEvent event) {

		if (nameField.getText().equals("")) {
		    nameField.setValue(nameField.getTitle());
		    nameField.removeStyleName("DicomSuggestion");
		    nameField.addStyleName("DicomSuggestionEmpty");
		} else {
		    nameField.setValue(nameField.getValue());
		}

	    }

	});

	// onblur="this.value=(this.value=='')?this.title:this.value;"
	// onfocus="this.value=(this.value==this.title)?'':this.value;"

	searchPanel.add(nameField);

	searchPanel.add(sendButton);

	clearButton = new Button("Сброс");
	clearButton.setStyleName("SearchToolButton");
	searchPanel.add(clearButton);

	clearButton.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		nameField.setText(searchTitle);
		nameField.removeStyleName("DicomSuggestion");
		nameField.addStyleName("DicomSuggestionEmpty");
		resultPanel.clear();
		transactionFinished();
	    }

	});
	
	

	nameField.addSelectionHandler(new SelectionHandler<Suggestion>() {

	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {

		if (type.equals("study"))
		    searchStudyes();
		else if (type.equals("patient"))
		    searchPatients();
		else if (type.equals("worklist"))
		    searchWorklist();
		else if (type.equals("direction"))
		    searchDirections();
	    }
	});

	sendButton.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if (type.equals("study"))
		    searchStudyes();
		else if (type.equals("patient"))
		    searchPatients();
		else if (type.equals("worklist"))
		    searchWorklist();
		else if (type.equals("direction"))
		    searchDirections();
	    }
	});

	toolPanel = new VerticalPanel();
	toolPanel.add(new Label("tool"));
	mainPanel.add(toolPanel);

    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
	// TODO Auto-generated method stub

    }

    /**
     * старт транзакции
     */
    private void transactionStarted() {
	resultPanel.clear();
	sendButton.setEnabled(false);
	clearButton.setEnabled(false);
    }

    /**
     * Завершение транзакции
     */
    private void transactionFinished() {

	Application.hideWorkStatusMsg();
	sendButton.setEnabled(true);
	clearButton.setEnabled(true);
	nameField.setFocus(true);
	searchTransactionID = new Date().getTime();
    }

    /**
     * Прерывание транзакции
     */
    private void transactionInterrupt() {
	transactionFinished();
    }

    /**
     * Поиск исследований
     */
    private void searchPatients() {

	Date d = new Date();
	searchTransactionID = d.getTime();

	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy. G 'at' HH:mm:ss vvvv");
	// showWorkStatusMsg("Послан <b> запрос данных </b> по пациенту ... "
	// + dateFormat.format(d));
	Application.showWorkStatusMsg("");

	TransactionTimer t = new TransactionTimer() {

	    private int counter = 0;

	    public void run() {

		// System.out.println("!!!!!!!!!!!! " + getTransactionId() + "="
		// + searchTransactionID);
		if (getTransactionId() != searchTransactionID) {
		    cancel();
		    return;
		}

		if (counter == 0) {

		    Button b = new Button("Остановить поиск");
		    b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			    transactionInterrupt();
			}
		    });

		    // TODO Вынести логику в Application
		    Application.addToWorkStatusWidget(b);
		    // TODO Вынести логику в Application
		    Application
			    .addToWorkStatusMsg(" Возможно имеется <i>проблема</i> со связью. Вы <b>всегда</b> можете остановить поиск...");
		}
		counter++;

		Application.addToWorkStatusMsg(" Поиск продолжается " + counter * 2 + " сек.");
		// HTML l = new HTML("<a href=''>[Остановить]</a>");
		// DOM.setStyleAttribute(l.getElement(), "cursor",
		// "pointer");

	    }
	};
	t.setTransactionId(searchTransactionID);
	// t.schedule(2000);
	t.scheduleRepeating(3000);

	String querystr = nameField.getText();
	transactionStarted();

	PatientsRPCRequest req = new PatientsRPCRequest();
	req.setTransactionId(searchTransactionID);
	req.setQueryStr(querystr);
	req.setLimit(20);

	Application.browserService.getPatients(req, new AsyncCallback<PatientsRPCResponse>() {

	    public void onFailure(Throwable caught) {

		transactionFinished();
		Application.showErrorDlg(caught);

	    }

	    public void onSuccess(PatientsRPCResponse result) {

		// TODO попробовать сделать нормлаьный interrupt (дабы
		// не качать все данные)

		// Если сменился идентификатор транзакции, то ничего не
		// принимаем
		if (searchTransactionID != result.getTransactionId()) {
		    return;
		}

		Application.hideWorkStatusMsg();

		ArrayList<PatientProxy> patients = result.getPatients();
		for (Iterator<PatientProxy> it = patients.iterator(); it.hasNext();) {

		    PatientProxy patientProxy = it.next();
		    VerticalPanel table = new VerticalPanel();
		    resultPanel.add(table);
		    PatientCardPanel patientCard = new PatientCardPanel(patientProxy);
		    table.add(patientCard);
		}

		if (patients.size() == 0) {
		    showNotFound();
		}

		transactionFinished();

	    }

	});

    }

    /**
     * Поиск исследований
     */
    private void searchStudyes() {

	Date d = new Date();
	searchTransactionID = d.getTime();

	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy. G 'at' HH:mm:ss vvvv");
	// showWorkStatusMsg("Послан <b> запрос данных </b> по пациенту ... "
	// + dateFormat.format(d));
	Application.showWorkStatusMsg("");

	TransactionTimer t = new TransactionTimer() {

	    private int counter = 0;

	    public void run() {

		// System.out.println("!!!!!!!!!!!! " + getTransactionId() + "="
		// + searchTransactionID);
		if (getTransactionId() != searchTransactionID) {
		    cancel();
		    return;
		}

		if (counter == 0) {

		    Button b = new Button("Остановить поиск");
		    b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			    transactionInterrupt();
			}
		    });

		    // TODO Вынести логику в Application
		    Application.addToWorkStatusWidget(b);
		    // TODO Вынести логику в Application
		    Application
			    .addToWorkStatusMsg(" Возможно имеется <i>проблема</i> со связью. Вы <b>всегда</b> можете остановить поиск...");
		}
		counter++;

		Application.addToWorkStatusMsg(" Поиск продолжается " + counter * 2 + " сек.");
		// HTML l = new HTML("<a href=''>[Остановить]</a>");
		// DOM.setStyleAttribute(l.getElement(), "cursor",
		// "pointer");

	    }
	};
	t.setTransactionId(searchTransactionID);
	// t.schedule(2000);
	t.scheduleRepeating(3000);

	String querystr = nameField.getText();
	transactionStarted();

	PatientsRPCRequest req = new PatientsRPCRequest();
	req.setTransactionId(searchTransactionID);
	req.setQueryStr(querystr);
	req.setLimit(20);

	Application.browserService.findStudy(searchTransactionID, Browser.version, querystr, null,
		new AsyncCallback<RPCDcmProxyEvent>() {

		    public void onFailure(Throwable caught) {

			transactionFinished();
			Application.showErrorDlg(caught);

		    }

		    public void onSuccess(RPCDcmProxyEvent result) {

			// TODO попробовать сделать нормлаьный interrupt (дабы
			// не качать все данные)
			// Если сменился идентификатор транзакции, то ничего не
			// принимаем
			if (searchTransactionID != result.getTransactionId()) {
			    return;
			}

			Application.hideWorkStatusMsg();

			ArrayList<StudyProxy> cortegeList = result.getData();
			removedCards.clear();

			int removed = 0;
			for (Iterator<StudyProxy> it = cortegeList.iterator(); it.hasNext();) {

			    StudyProxy studyProxy = it.next();

			    if (studyProxy.getStudyDateTimeRemoved() != null) {
				removed++;
				if (!showRemovedStudies) {
				    removedCards.add(studyProxy);
				    continue;
				}
			    }

			    StudyCard s = new StudyCard(true);
			    s.setProxy(studyProxy);
			    resultPanel.add(s);
			}

			if (removed > 0) {
			    final Label l = new Label("Показать удаленные исследования ( " + removed + " шт.)");
			    l.removeFromParent();
			    resultPanel.add(l);
			    l.addStyleName("LabelLink");
			    l.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

				    for (int i = 0; i < removedCards.size(); i++) {
					StudyCard s = new StudyCard(true);
					s.setProxy(removedCards.get(i));
					resultPanel.add(s);
				    }

				    l.removeFromParent();
				}
			    });
			}
			if (cortegeList.size() == 0) {
			    showNotFound();
			}

			transactionFinished();

		    }

		});
    }

    /**
     * Поиск направлений
     */
    void searchDirections() {

	Date d = new Date();
	searchTransactionID = d.getTime();

	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy. G 'at' HH:mm:ss vvvv");
	// showWorkStatusMsg("Послан <b> запрос данных </b> по пациенту ... "
	// + dateFormat.format(d));
	Application.showWorkStatusMsg("");

	TransactionTimer t = new TransactionTimer() {

	    private int counter = 0;

	    public void run() {

		// System.out.println("!!!!!!!!!!!! " + getTransactionId() + "="
		// + searchTransactionID);
		if (getTransactionId() != searchTransactionID) {
		    cancel();
		    return;
		}

		if (counter == 0) {

		    Button b = new Button("Остановить поиск");
		    b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			    transactionInterrupt();
			}
		    });

		    // TODO Вынести логику в Application
		    Application.addToWorkStatusWidget(b);
		    // TODO Вынести логику в Application
		    Application
			    .addToWorkStatusMsg(" Возможно имеется <i>проблема</i> со связью. Вы <b>всегда</b> можете остановить поиск...");
		}
		counter++;

		Application.addToWorkStatusMsg(" Поиск продолжается " + counter * 2 + " сек.");
		// HTML l = new HTML("<a href=''>[Остановить]</a>");
		// DOM.setStyleAttribute(l.getElement(), "cursor",
		// "pointer");

	    }
	};
	t.setTransactionId(searchTransactionID);
	// t.schedule(2000);
	t.scheduleRepeating(3000);

	String querystr = nameField.getText();
	transactionStarted();

	// querystr = "Деренок Дмитрий Владимирович";// TODO убрать!!!
	// resultPanel.add(new Label("!!!!!!!!!!!!!!" + querystr));

	QueryDirectionProxy query = new QueryDirectionProxy();
	// TODO Корявый костыль !!! ...введите фамилию (% - любой символ)...
	if (querystr != null && querystr.length() > 0 && !querystr.equals("...введите фамилию (% - любой символ)...")) {
	    if (querystr.matches("^\\D{5}\\d{2}$")) {//поиск по КБП
		query.setPatientShortName(querystr);
	    } else {
		query.setPatientName(querystr + "%");
	    }
	}
	query.setManufacturerDevice(DirectionsPanel.manufacturerModelName);
	query.setDateTimePlannedBegin(DirectionsPanel.dateBegin);
	query.setDateTimePlannedEnd(DirectionsPanel.dateEnd);

	Application.browserService.getDirections(query, new AsyncCallback<ArrayList<DirectionProxy>>() {

	    @Override
	    public void onFailure(Throwable caught) {
		transactionFinished();
		Application.showErrorDlg(caught);

	    }

	    @Override
	    public void onSuccess(ArrayList<DirectionProxy> result) {

		Application.hideWorkStatusMsg();

		Label l = new Label("Количество Направлений: " + result.size());
		l.addStyleName("DicomItem");
		resultPanel.add(l);

		for (DirectionProxy directionProxy : result) {

		    DirectionCard drn = new DirectionCard(directionProxy);
		    resultPanel.add(drn);

		}

		if (result.size() >= DirectionsPanel.maxResultCount) {
		    HTML emptyStr = new HTML();
		    emptyStr.setWidth("900px");
		    emptyStr.setStyleName("DicomItemValue");
		    emptyStr.setHTML("Показаны только первые " + DirectionsPanel.maxResultCount
			    + " строк! Чтобы посмотреть все - сужайте критерий поиска.");

		    resultPanel.add(emptyStr);
		}

		if (result.size() == 0) {
		    showNotFound();
		}

		transactionFinished();

	    }
	});

    }

    /**
     * Поиск исследований
     */
    void searchWorklist() {

	// System.out.println("!!!! searchWorklist");
	Date d = new Date();
	searchTransactionID = d.getTime();

	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy. G 'at' HH:mm:ss vvvv");
	// showWorkStatusMsg("Послан <b> запрос данных </b> по пациенту ... "
	// + dateFormat.format(d));
	Application.showWorkStatusMsg("");

	TransactionTimer t = new TransactionTimer() {

	    private int counter = 0;

	    public void run() {

		// System.out.println("!!!!!!!!!!!! " + getTransactionId() + "="
		// + searchTransactionID);
		if (getTransactionId() != searchTransactionID) {
		    cancel();
		    return;
		}

		if (counter == 0) {

		    Button b = new Button("Остановить поиск");
		    b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			    transactionInterrupt();
			}
		    });

		    // TODO Вынести логику в Application
		    Application.addToWorkStatusWidget(b);
		    // TODO Вынести логику в Application
		    Application
			    .addToWorkStatusMsg(" Возможно имеется <i>проблема</i> со связью. Вы <b>всегда</b> можете остановить поиск...");
		}
		counter++;

		Application.addToWorkStatusMsg(" Поиск продолжается " + counter * 2 + " сек.");
		// HTML l = new HTML("<a href=''>[Остановить]</a>");
		// DOM.setStyleAttribute(l.getElement(), "cursor",
		// "pointer");

	    }
	};
	t.setTransactionId(searchTransactionID);
	// t.schedule(2000);
	t.scheduleRepeating(3000);

	String querystr = nameField.getText();
	transactionStarted();

	// querystr = "Деренок Дмитрий Владимирович";// TODO убрать!!!
	// resultPanel.add(new Label("!!!!!!!!!!!!!!" + querystr));

	
	QueryStudyProxy query = new QueryStudyProxy();
	// TODO Корявый костыль !!! ...введите фамилию (% - любой символ)...
	if (querystr != null && querystr.length() > 0 && !querystr.equals("...введите фамилию (% - любой символ)...")) {
	    if (querystr.matches("^\\D{5}\\d{2}$")) {//поиск по КБП
		query.setPatientShortName(querystr);
	    } else {
		query.setPatientName(querystr + "%");
	    }
	}
	if (WorkListPanel.manufacturerModelName.length() > 0)
	    query.setManufacturerModelName(WorkListPanel.manufacturerModelName);
	
	query.setBeginStudyDate(WorkListPanel.dateBegin);
	query.setEndStudyDate(WorkListPanel.dateEnd);

	if (WorkListPanel.studyResult.equals("new"))
	    query.setStudyNotComplite(true);

	if (WorkListPanel.studyResult.equals("old"))
	    query.setStudyComplite(true);

	// query.setManufacturerDevice(DirectionsPanel.manufacturerModelName);
	// query.setDateTimePlannedBegin(DirectionsPanel.dateBegin);
	// query.setDateTimePlannedEnd(DirectionsPanel.dateEnd);

	Application.browserService.getStudies(query, new AsyncCallback<ArrayList<StudyProxy>>() {

	    @Override
	    public void onFailure(Throwable caught) {
		transactionFinished();
		Application.showErrorDlg(caught);

	    }

	    @Override
	    public void onSuccess(ArrayList<StudyProxy> result) {

		Application.hideWorkStatusMsg();

		Label l = new Label("Количество исследований: " + result.size());
		l.addStyleName("DicomItem");
		resultPanel.add(l);
		WorkListPanel.studies = result;

		
		for (StudyProxy studyProxy : result) {

		    StudyCard s = new StudyCard(false);
		    s.setProxy(studyProxy);
		    resultPanel.add(s);

		}

		if (result.size() >= DirectionsPanel.maxResultCount) {
		    HTML emptyStr = new HTML();
		    emptyStr.setWidth("900px");
		    emptyStr.setStyleName("DicomItemValue");
		    emptyStr.setHTML("Показаны только первые " + DirectionsPanel.maxResultCount
			    + " строк! Чтобы посмотреть все - сужайте критерий поиска.");

		    resultPanel.add(emptyStr);
		}

		if (result.size() == 0) {
		    showNotFound();
		}

		transactionFinished();

	    }
	});

    }

    protected void showNotFound() {
	HTML emptyStr = new HTML();
	emptyStr.setWidth("400px");
	emptyStr.setStyleName("DicomItemValue");
	emptyStr.setHTML("Ничего не найдено...");

	resultPanel.add(emptyStr);

	emptyStr = new HTML();
	emptyStr.setWidth("800px");
	emptyStr.setHTML(

	" <p> Попробуйте сузить поиск используя 'групповой символ' (англ. wildcard). "
		+ " <br><br>Групповой символ (заменяющий один или несколько символов:"
		+ " знак 'подчеркивание' (_) может представлять любой одиночный символ; "
		+ " процент (%) используется для представления любого символа или группы символов) "
		+ " Например набрав: пе%в получите результаты пациентов с фамилиями"
		+ " Петров, Переладов. А набрав  пе___в - получите результат: Петров" + " </p>");

	resultPanel.add(emptyStr);
    }

    public class ItemSuggestOracle extends SuggestOracle {
	public boolean isDisplayStringHTML() {
	    return true;
	}

	public void requestSuggestions(SuggestOracle.Request req, SuggestOracle.Callback callback) {
	    try {
		searchTransactionID = new Date().getTime();
		ItemSuggestService.Util.getInstance().getSuggestions(searchTransactionID, Browser.version, type, req,
			new ItemSuggestCallback(req, callback));
	    } catch (DefaultGWTRPCException e) {
		Application.showErrorDlg(e);
		e.printStackTrace();
	    }
	}

	class ItemSuggestCallback implements AsyncCallback {
	    private SuggestOracle.Request req;
	    private SuggestOracle.Callback callback;

	    public ItemSuggestCallback(SuggestOracle.Request _req, SuggestOracle.Callback _callback) {
		req = _req;
		callback = _callback;
	    }

	    public void onFailure(Throwable error) {
		Application.showErrorDlg(error);
		callback.onSuggestionsReady(req, new SuggestOracle.Response());
		Application.showErrorDlg(error);
	    }

	    public void onSuccess(Object retValue) {

		// TODO попробовать сделать нормлаьный interrupt (дабы
		// не качать все данные)
		// Если сменился идентификатор транзакции, то ничего не
		// принимаем
		if (searchTransactionID != ((SuggestTransactedResponse) retValue).getTransactionId()) {
		    return;
		}

		callback.onSuggestionsReady(req, (SuggestOracle.Response) retValue);
	    }
	}

    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
	toolPanel.clear();
	if (type.equals("direction")) {
	    DirectionsPanel drnToolPanel = new DirectionsPanel(Application, this);
	    toolPanel.add(drnToolPanel);
	} else if (type.equals("worklist")) {
	    WorkListPanel wrkToolPanel = new WorkListPanel(this);
	    toolPanel.add(wrkToolPanel);
	}

    }

    public void addToolPanel(DirectionsPanel drnToolPanel) {
	// TODO Auto-generated method stub
	// mainPanel.add(new Label("123"));
    }

}