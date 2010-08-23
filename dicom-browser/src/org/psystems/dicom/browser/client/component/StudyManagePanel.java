/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.psystems.dicom.browser.client.Dicom_browser;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.PatientsRPCRequest;
import org.psystems.dicom.browser.client.proxy.PatientsRPCResponse;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.browser.client.service.BrowserServiceAsync;
import org.psystems.dicom.browser.client.service.ManageStydyServiceAsync;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author dima_d
 * 
 */
public class StudyManagePanel extends Composite implements
		ValueChangeHandler<String> {

	private ManageStydyServiceAsync manageStudyService;
	private HTML submitResult;
	private Button submitBtn;
	private TextBox patientName;
	private DateBox birstdayDox;
	private DateBox studyDateBox;
	private FileUpload fileUpload;

	private FlexTable formTable;
	private VerticalPanel formDataPanel;
	private Hidden patientBirthDateHidden;
	private Hidden studyDateHidden;
	private TextBox medicalAlerts;
	private TextBox studyDescription;
	private TextArea studyComments;
	private TextBox studyDoctror;
	private TextBox studyOperator;
	private DateBox studyViewProtocolDateBox;
	private Hidden studyViewProtocolDateHidden;
	private ListBox patientNameCheck;
	private BrowserServiceAsync browserService;
	protected HashMap<String, PatientProxy> itemProxies = new HashMap<String, PatientProxy>();
	private ListBox lbSex;
	public final static String medicalAlertsTitle = "норма";

	public StudyManagePanel(final ManageStydyServiceAsync manageStudyService,
			BrowserServiceAsync browserService, StudyProxy proxy) {

		this.browserService = browserService;
		this.manageStudyService = manageStudyService;

		// История
		History.addValueChangeHandler(this);
		// History.fireCurrentHistoryState();

		DecoratorPanel mainPanel = new DecoratorPanel();

		final FormPanel formPanel = new FormPanel();
		formPanel.setAction("newstudy/upload");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		mainPanel.setWidget(formPanel);

		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub
				// System.out.println("!!! onSubmitComplete [" +
				// event.getResults()+"]");

				if (!event.getResults().matches(".+___success___.+")) {
					submitResult.setHTML("" + event.toDebugString() + "<HR>"
							+ event.getResults());
					submitError();
				} else {
					submitSuccess();
				}

			}

		});

		formPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				// TODO Auto-generated method stub
				// event.cancel();
				dataVerifyed(false);
			}
		});

		formTable = new FlexTable();
		// TODO Убрать в css
		DOM.setStyleAttribute(formPanel.getElement(), "background", "#E9EDF5");

		formDataPanel = new VerticalPanel();
		formPanel.add(formDataPanel);
		formDataPanel.add(formTable);

		//
		Hidden studyInstanceUID = new Hidden();
		studyInstanceUID.setName("0020000D");
		studyInstanceUID.setValue(proxy.getStudyUID());
		addFormHidden(studyInstanceUID);

		//
		Hidden studySeriesUID = new Hidden();
		studySeriesUID.setName("0020000E");
		studySeriesUID.setValue(proxy.getStudyUID() + "."
				+ new Date().getTime());
		addFormHidden(studySeriesUID);

		//
		Hidden studyId = new Hidden();
		studyId.setName("00200010");
		studyId.setValue(proxy.getStudyId());
		addFormHidden(studyId);

		//
		Hidden patientId = new Hidden();
		patientId.setName("00100021");
		patientId.setValue(proxy.getPatientId());
		addFormHidden(patientId);

		//
		Hidden manufacturerModelName = new Hidden();
		manufacturerModelName.setName("00081090");
		manufacturerModelName.setValue(proxy.getManufacturerModelName());
		addFormHidden(manufacturerModelName);

		int rowCounter = 0;

		// Тип исследования Modality 00080060
		// TODO Добавить словарь типов чтобы в интерфейсе показывать не CR,OT
		// итп..
		ListBox studyModality = new ListBox();
		studyModality.setName("00080060");
		studyModality.addItem("Прочее", "OT");
		studyModality.addItem("Флюорография", "CR");
		if ("CR".equals(proxy.getStudyModality())) {
			studyModality.setSelectedIndex(1);
		} else {
			studyModality.setSelectedIndex(0);
		}
		addFormRow(rowCounter++, "Модальность", studyModality);

		//
		patientName = new TextBox();
		patientName.setName("00100010");
		patientName.setWidth("400px");
		patientName.setText(proxy.getPatientName());
		patientName.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				patientVerify();
			}
		});

		addFormRow(rowCounter++, "ФИО", patientName);

		//
		patientNameCheck = new ListBox(true);
		patientNameCheck.setSize("400px", "20em");

		patientNameCheck.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				String id = patientNameCheck.getValue(patientNameCheck
						.getSelectedIndex());
				
				PatientProxy proxyFinded = itemProxies.get(id);
//				System.out.println("!!!! id="+id+";"+proxyFinded);
				applyVerifyedData(proxyFinded);
				
			}
		});

		formTable.setWidget(rowCounter - 2, 2,
				makeItemLabel("Сверка имени (Click - выбор)"));
		formTable.getCellFormatter().setHorizontalAlignment(rowCounter - 2, 2,
				HasHorizontalAlignment.ALIGN_CENTER);

		formTable.setWidget(rowCounter - 1, 2, patientNameCheck);
		formTable.getCellFormatter().setVerticalAlignment(rowCounter - 1, 2,
				HasVerticalAlignment.ALIGN_TOP);
		formTable.getFlexCellFormatter().setRowSpan(rowCounter - 1, 2, 20);

		//
		lbSex = new ListBox();
		lbSex.setName("00100040");
		lbSex.addItem("Муж", "M");
		lbSex.addItem("Жен", "F");
		if ("F".equals(proxy.getPatientSex())) {
			lbSex.setSelectedIndex(1);
		} else {
			lbSex.setSelectedIndex(0);
		}
		addFormRow(rowCounter++, "Пол", lbSex);

		//
		birstdayDox = new DateBox();
		birstdayDox.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
		birstdayDox.setValue(proxy.getPatientBirthDate());
		birstdayDox.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				String d = Utils.dateFormatDicom.format(event.getValue());
				patientBirthDateHidden.setValue(d);
			}
		});

		patientBirthDateHidden = new Hidden();
		patientBirthDateHidden.setName("00100030");
		addFormHidden(patientBirthDateHidden);
		patientBirthDateHidden.setValue(Utils.dateFormatDicom.format(proxy
				.getPatientBirthDate()));

		addFormRow(rowCounter++, "Дата рождения", birstdayDox);

		//
		studyDateBox = new DateBox();
		studyDateBox.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
		studyDateBox.setValue(proxy.getStudyDate());
		studyDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				studyDateHidden.setValue(Utils.dateFormatDicom.format(event
						.getValue()));
			}
		});

		studyDateHidden = new Hidden();
		studyDateHidden.setName("00080020");
		addFormHidden(studyDateHidden);
		studyDateHidden.setValue(Utils.dateFormatDicom.format(studyDateBox
				.getValue()));

		addFormRow(rowCounter++, "Дата исследования", studyDateBox);

		//
		studyViewProtocolDateBox = new DateBox();
		studyViewProtocolDateBox.setFormat(new DateBox.DefaultFormat(
				Utils.dateFormatUser));
		studyViewProtocolDateBox.setValue(new Date());
		studyViewProtocolDateBox
				.addValueChangeHandler(new ValueChangeHandler<Date>() {

					@Override
					public void onValueChange(ValueChangeEvent<Date> event) {
						studyViewProtocolDateHidden
								.setValue(Utils.dateFormatDicom.format(event
										.getValue()));
					}
				});

		studyViewProtocolDateHidden = new Hidden();
		studyViewProtocolDateHidden.setName("00321050");
		addFormHidden(studyViewProtocolDateHidden);
		studyViewProtocolDateHidden.setValue(Utils.dateFormatDicom
				.format(studyViewProtocolDateBox.getValue()));

		addFormRow(rowCounter++, "Дата описания", studyViewProtocolDateBox);

		//
		studyDoctror = new TextBox();
		studyDoctror.setName("00080090");
		studyDoctror.setWidth("400px");
		studyDoctror.setText(proxy.getStudyDoctor());
		addFormRow(rowCounter++, "Врач", studyDoctror);

		//
		studyOperator = new TextBox();
		studyOperator.setName("00081070");
		studyOperator.setWidth("400px");
		studyOperator.setText(proxy.getStudyOperator());
		addFormRow(rowCounter++, "Лаборант", studyOperator);

		//
		// TODO Взять из конфигурации
		final ListBox lbDescriptionTemplates = new ListBox();
		// lbDescriptionTemplates.setName("00100040");
		lbDescriptionTemplates.addItem("Выберите шаблон...", "");
		lbDescriptionTemplates.addItem("Флюорография, Прямая передняя",
				"Флюорография, Прямая передняя");

		lbDescriptionTemplates.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				// System.out.println("!!! "+event)!!!;
				int i = lbDescriptionTemplates.getSelectedIndex();
				studyDescription.setText(lbDescriptionTemplates.getValue(i));
			}
		});

		addFormRow(rowCounter++, "Варианты описания", lbDescriptionTemplates);
		//
		studyDescription = new TextBox();
		studyDescription.setName("00081030");
		studyDescription.setWidth("400px");
		studyDescription.setText(proxy.getStudyDescription());
		addFormRow(rowCounter++, "Описание исследования", studyDescription);

		//
		medicalAlerts = new TextBox();
		medicalAlerts.setName("00102000");
		medicalAlerts.setWidth("400px");

		medicalAlerts.addStyleName("DicomSuggestionEmpty");
		medicalAlerts.setTitle(medicalAlertsTitle);
		medicalAlerts.setText(medicalAlertsTitle);

		medicalAlerts.setText(proxy.getStudyResult());
		if (proxy.getStudyResult() == null
				|| proxy.getStudyResult().length() == 0) {
			medicalAlerts.setText(medicalAlertsTitle);
		}

		if (proxy.getStudyResult() != null
				&& proxy.getStudyResult().length() > 0) {
			medicalAlerts.removeStyleName("DicomSuggestionEmpty");
			medicalAlerts.addStyleName("DicomSuggestion");
		}
		addFormRow(rowCounter++, "Результат", medicalAlerts);

		medicalAlerts.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {

				medicalAlerts.removeStyleName("DicomSuggestionEmpty");
				medicalAlerts.addStyleName("DicomSuggestion");

				if (medicalAlerts.getText().equals(medicalAlerts.getTitle())) {
					medicalAlerts.setValue("");
				} else {
					medicalAlerts.setValue(medicalAlerts.getValue());
				}
			}

		});

		medicalAlerts.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {

				if (medicalAlerts.getText().equals("")) {
					medicalAlerts.setValue(medicalAlerts.getTitle());
					medicalAlerts.removeStyleName("DicomSuggestion");
					medicalAlerts.addStyleName("DicomSuggestionEmpty");
				} else {
					medicalAlerts.setValue(medicalAlerts.getValue());
				}

			}

		});

		//
		final ListBox lbCommentsTemplates = new ListBox();
		// lbCommentsTemplates.setName("00100040");
		lbCommentsTemplates.addItem("Выберите шаблон...", "");
		lbCommentsTemplates.addItem(
				"Органы грудной клетки без видимой патологии",
				"Органы грудной клетки без видимой патологии");

		lbCommentsTemplates.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				// System.out.println("!!! "+event)!!!;
				int i = lbCommentsTemplates.getSelectedIndex();
				studyComments.setText(lbCommentsTemplates.getValue(i));
			}
		});

		addFormRow(rowCounter++, "варианты протокола", lbCommentsTemplates);

		//
		studyComments = new TextArea();
		studyComments.setName("00324000");// Tag.StudyComments
		studyComments.setSize("400px", "200px");
		studyComments.setText(proxy.getStudyViewprotocol());
		addFormRow(rowCounter++, "Протокол", studyComments);

		//		
		//
		fileUpload = new FileUpload();
		fileUpload.setName("upload");

		addFormRow(rowCounter++, "Снимок", fileUpload);

		//
		submitBtn = new Button("Сохранить изменения...");
		dataVerifyed(false);
		submitBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				formPanel.submit();
			}
		});
		addFormRow(rowCounter++, submitBtn);

		//
		submitResult = new HTML("UID:" + proxy.getStudyUID());
		addFormRow(rowCounter++, submitResult);

		initWidget(mainPanel);

		patientVerify();
	}

	protected void dataVerifyed(boolean b) {

		if (b) {
			submitBtn.setText("Сохранить изменения...");
		} else {
			submitBtn.setText("Сохранить НЕПРОВЕРЕННЫЕ изменения...");
		}
	}

	private void patientVerify() {

		patientNameCheck.addItem("Ищем: " + patientName.getValue());
		dataVerifyed(false);

		PatientsRPCRequest req = new PatientsRPCRequest();
		req.setTransactionId(1);
		req.setQueryStr(patientName.getText() + "%");
		req.setLimit(20);

		browserService.getPatients(req,
				new AsyncCallback<PatientsRPCResponse>() {

					private Object patientProxy;

					public void onFailure(Throwable caught) {

						// transactionFinished();
						Dicom_browser.showErrorDlg(caught);

					}

					public void onSuccess(PatientsRPCResponse result) {

						// TODO попробовать сделать нормлаьный interrupt (дабы
						// не качать все данные)

						// Если сменился идентификатор транзакции, то ничего не
						// принимаем
						// if (searchTransactionID != result.getTransactionId())
						// {
						// return;
						// }

						Dicom_browser.hideWorkStatusMsg();

						ArrayList<PatientProxy> patients = result.getPatients();
						patientNameCheck.clear();
						itemProxies = new HashMap<String, PatientProxy>();
						itemProxies.clear();
						
						PatientProxy lastPatientProxy = null;
						for (Iterator<PatientProxy> it = patients.iterator(); it
								.hasNext();) {

							PatientProxy patientProxy = it.next();
							lastPatientProxy = patientProxy;
							String sex;
							if( "M".equals(patientProxy.getPatientSex())) {
								sex = "М";
							} else {
								sex = "Ж";
							}
							// String d =
							// Utils.dateFormatDicom.format(event.getValue());
							patientNameCheck.addItem(patientProxy
									.getPatientName()
									+ " ("
									+ sex 
									+ ") "
									+ Utils.dateFormatUser.format(patientProxy
											.getPatientBirthDate()),""+patientProxy
											.getId());
							
							itemProxies.put(""+patientProxy.getId(), patientProxy);
							patientNameCheck.setSelectedIndex(0);
						}

						if (patients.size() == 0) {
							patientNameCheck.addItem("Совпадений не найдено!");
							dataVerifyed(false);

						}

						if (patients.size() == 1) {
							
							applyVerifyedData(lastPatientProxy);
						}

						// transactionFinished();

					}

				});

	}

	/**
	 * Применение сверенных данных
	 * @param lastPatientProxy
	 */
	protected void applyVerifyedData(PatientProxy lastPatientProxy) {
		if(lastPatientProxy==null) return;
		patientName.setValue(lastPatientProxy.getPatientName());
		birstdayDox.setValue(lastPatientProxy.getPatientBirthDate());
		if("M".equals(lastPatientProxy.getPatientSex()))
			lbSex.setSelectedIndex(0);
		else
			lbSex.setSelectedIndex(1);
		dataVerifyed(true);
	}

	/**
	 * Добавление строчки на форму
	 * 
	 * @param row
	 * @param title
	 * @param input
	 */
	private void addFormRow(int row, String title, Widget input) {

		formTable.setWidget(row, 0, makeItemLabel(title));
		formTable.getCellFormatter().setHorizontalAlignment(row, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		formTable.getCellFormatter().setVerticalAlignment(row, 0,
				HasVerticalAlignment.ALIGN_TOP);
		formTable.setWidget(row, 1, input);

	}

	/**
	 * Добавление строчки на форму
	 * 
	 * @param row
	 * @param input
	 */
	private void addFormRow(int row, Widget input) {

		formTable.setWidget(row, 0, input);
		formTable.getFlexCellFormatter().setColSpan(row, 0, 2);
		formTable.getFlexCellFormatter().setHorizontalAlignment(row, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

	}

	/**
	 * Добавление hidden-поля на форму
	 * 
	 * @param input
	 */
	private void addFormHidden(Hidden input) {
		formDataPanel.add(input);
	}

	private void clearForm() {
		dataVerifyed(true);
		resetForm();
		studyDateBox.setValue(new Date());
	}

	private native void resetForm() /*-{
		$doc.forms[0].reset();
	}-*/;

	/**
	 * НЕуспешное завершение сохранения исследования
	 */
	protected void submitError() {
		dataVerifyed(true);
	}

	/**
	 * Успешное завершение сохранения исследования
	 */
	protected void submitSuccess() {
		// clearForm();
		dataVerifyed(true);
	}

	/**
	 * @param title
	 * @return
	 */
	Widget makeItemLabel(String title) {
		Label label = new Label(title);
		// TODO Убрать в css
		DOM.setStyleAttribute(label.getElement(), "font",
				"1.5em/ 150% normal Verdana, Tahoma, sans-serif");
		return label;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// TODO Auto-generated method stub

	}

}
