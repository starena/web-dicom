/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.Date;

import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.browser.client.service.ManageStydyServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
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

	DateTimeFormat dateFormatBox = DateTimeFormat.getFormat("dd.MM.yyyy");
	DateTimeFormat dateFormatHidden = DateTimeFormat.getFormat("yyyyMMdd");
	private FlexTable formTable;
	private VerticalPanel formDataPanel;
	private Hidden patientBirthDateHidden;
	private Hidden studyDateHidden;
	private TextBox medicalAlerts;
	private TextArea studyDescription;

	public StudyManagePanel(final ManageStydyServiceAsync manageStudyService,
			StudyProxy proxy) {

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
				submitBtn.setEnabled(false);
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

		int rowCounter = 0;

		// Тип исследования Modality 00080060
		// TODO Добавить словарь типов чтобы в интерфейсе показывать не CR,OT
		// итп..
		ListBox studyType = new ListBox();
		studyType.setName("00100040");
		studyType.addItem("Прочее", "OT");
		studyType.addItem("Флюорография", "CR");
		if ("CR".equals(proxy.getStudyType())) {
			studyType.setSelectedIndex(1);
		} else {
			studyType.setSelectedIndex(0);
		}
		addFormRow(rowCounter++, "Тип", studyType);

		//
		patientName = new TextBox();
		patientName.setName("00100010");
		patientName.setWidth("400px");
		patientName.setText(proxy.getPatientName());
		addFormRow(rowCounter++, "ФИО", patientName);

		//
		ListBox lbSex = new ListBox();
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
		birstdayDox.setFormat(new DateBox.DefaultFormat(dateFormatBox));
		birstdayDox.setValue(proxy.getPatientBirthDate());
		birstdayDox.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				String d = dateFormatHidden.format(event.getValue());
				patientBirthDateHidden.setValue(d);
			}
		});

		patientBirthDateHidden = new Hidden();
		patientBirthDateHidden.setName("00100030");
		addFormHidden(patientBirthDateHidden);
		patientBirthDateHidden.setValue(dateFormatHidden.format(proxy
				.getPatientBirthDate()));

		addFormRow(rowCounter++, "Дата рождения", birstdayDox);

		//
		studyDateBox = new DateBox();
		studyDateBox.setFormat(new DateBox.DefaultFormat(dateFormatBox));
		studyDateBox.setValue(new Date());
		studyDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				studyDateHidden.setValue(dateFormatHidden.format(event
						.getValue()));
			}
		});

		studyDateHidden = new Hidden();
		studyDateHidden.setName("00080020");
		addFormHidden(studyDateHidden);
		studyDateHidden.setValue(dateFormatHidden.format(studyDateBox
				.getValue()));

		addFormRow(rowCounter++, "Дата исследования", studyDateBox);

		//
		medicalAlerts = new TextBox();
		medicalAlerts.setName("00102000");
		medicalAlerts.setWidth("400px");
		medicalAlerts.setText(proxy.getStudyResult());
		addFormRow(rowCounter++, "Осложнения", medicalAlerts);

		//
		studyDescription = new TextArea();
		studyDescription.setName("00081030");
		studyDescription.setSize("400px", "200px");
		studyDescription.setText(proxy.getStudyDescription());

		// addFormRow(rowCounter++, makeItemLabel("Описание"));
		// addFormRow(rowCounter++, studyDescription);
		addFormRow(rowCounter++, "Описание", studyDescription);

		//
		fileUpload = new FileUpload();
		fileUpload.setName("upload");

		addFormRow(rowCounter++, "Снимок", fileUpload);

		//
		submitBtn = new Button("Создать");
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
		submitBtn.setEnabled(true);
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
		submitBtn.setEnabled(true);
	}

	/**
	 * Успешное завершение сохранения исследования
	 */
	protected void submitSuccess() {
		clearForm();
		submitBtn.setEnabled(true);

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
