/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.psystems.dicom.browser.client.Dicom_browser;
import org.psystems.dicom.browser.client.TransactionTimer;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.OOTemplateProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.PatientsRPCRequest;
import org.psystems.dicom.browser.client.proxy.PatientsRPCResponse;
import org.psystems.dicom.browser.client.proxy.Session;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.browser.client.service.BrowserServiceAsync;

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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author dima_d
 * 
 */
public class StudyManagePanel extends Composite implements
		ValueChangeHandler<String> {

	private StudyCard studyCardPanel;
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
//	private TextBox studyOperator;
//	private TextBox studyDoctror2;
	private DateBox studyViewProtocolDateBox;
	private Hidden studyViewProtocolDateHidden;
	private ListBox patientNameCheck;
	protected HashMap<String, PatientProxy> itemProxies = new HashMap<String, PatientProxy>();
	private ListBox lbSex;
	private StudyProxy proxy;
//	private HTML verifyHTML;
	private ListBox studyDoctror;
	private ListBox studyOperator;
	private int rowCounter;
	private Hidden studyInstanceUID;
	public final static String medicalAlertsTitle = "норма";
	
	private int timeClose = 5;
	private String msg;
	private TransactionTimer timer = null;
	private ListBox studyManufacturerModelName;
	private ListBox studyModality;
	
	private HTML ooTemplatePanel = new HTML();
	
	public StudyManagePanel(StudyCard studyCardPanel, StudyProxy proxy) {

		this.studyCardPanel = studyCardPanel;
		this.proxy = proxy;

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
				
				// System.out.println("!!! onSubmitComplete [" +
				// event.getResults()+"]");
				
				//TODO Сделать через анализ статуса ответа (200 - ОК)
				if (!event.getResults().matches(".+___success___.+")) {
					submitError(event);
				} else {
					submitSuccess();
				}

			}

		});

//		formPanel.addSubmitHandler(new SubmitHandler() {
//
//			@Override
//			public void onSubmit(SubmitEvent event) {
//				// TODO Auto-generated method stub
//				// event.cancel();
//				dataVerifyed(false);
////				startcheckindStudyModify();
//				verifyHTML.setHTML("Отправка данных...");
//			}
//		});

		formTable = new FlexTable();
		// TODO Убрать в css
		DOM.setStyleAttribute(formPanel.getElement(), "background", "#E9EDF5");

		HorizontalPanel hpMain = new HorizontalPanel();
		hpMain.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hpMain.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		
		formDataPanel = new VerticalPanel();
		
		formPanel.add(hpMain);
		hpMain.add(formDataPanel);
		formDataPanel.add(formTable);

		//
		studyInstanceUID = new Hidden();
		studyInstanceUID.setName("0020000D");
		studyInstanceUID.setValue(proxy.getStudyInstanceUID());
		addFormHidden(studyInstanceUID);

		//
		Hidden studySeriesUID = new Hidden();
		studySeriesUID.setName("0020000E");
		studySeriesUID.setValue(proxy.getStudyInstanceUID() + "."
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

		

		rowCounter = 0;
		
		//словари
		HashMap <String, String>dicModel = new HashMap<String, String>();
		dicModel.put("LORAD AFFINITY", "Маммограф (LORAD AFFINITY)" );
		dicModel.put("CLINOMAT", "Рентген (CLINOMAT)");
		dicModel.put("РДК 50/6", "Рентген (РДК 50/6)");
		dicModel.put("DUODiagnost", "Рентген (DUODiagnost)");
		dicModel.put("FUJINON EG WR5", "Эндоскоп (FUJINON EG WR5)");
		dicModel.put("SonoScape-1", "УЗИ-1 (SonoScape) ДП 523 каб.");
		dicModel.put("SonoScape-2", "УЗИ-2 (SonoScape)");
		dicModel.put("SonoScape-3", "УЗИ-3 (SonoScape)");
		dicModel.put("VIVID 3", "УЗИ (VIVID) ДП 527 каб.");
		dicModel.put("УЗИ (VOLUSON)", "VOLUSON 730 BT04 EXPERT");
		dicModel.put("Acuson Sequoia", "УЗИ (Acuson Sequoia) каб.408");

		//TODO Уточнить имена и кабинеты
		dicModel.put("SSI-1000","SSI-1000");
		dicModel.put("Эхоэнцефалоскоп-ЭЭС-25-ЭМА","Эхоэнцефалоскоп-ЭЭС-25-ЭМА");
		dicModel.put("Электроэнцефалограф-Alliance","Электроэнцефалограф-Alliance");
		dicModel.put("Companion III","Companion III");
		dicModel.put("Microvit","Microvit");
		dicModel.put("СМАД, Schiller AG","СМАД, Schiller AG");
		dicModel.put("ЭКГ-Schiller Medical S.A.","ЭКГ-Schiller Medical S.A.");
		dicModel.put("ЭКГ-Cardiovit AT-2 plus C","ЭКГ-Cardiovit AT-2 plus C");
		dicModel.put("Велоэргометрия-АТ- 104 Schiller","Велоэргометрия-АТ- 104 Schiller");
		dicModel.put("Voluson 730 Expert","Voluson 730 Expert");
		dicModel.put("SSD-3500","SSD-3500");
		dicModel.put("Спирометр-Spirovit SP-1","Спирометр-Spirovit SP-1");
		dicModel.put("Спиро-Спектр 2","Спиро-Спектр 2");
		
		dicModel.put("Aloka alfa","Aloka alfa 6 каб.527 ДП");
		
		
		
		
		
		HashMap <String, String>dicModality = new HashMap<String, String>();
		dicModality.put("OT", "Прочее");
		dicModality.put("CR", "Флюорография");
		dicModality.put("DF", "Рентген");
		dicModality.put("US", "Узи" );
		dicModality.put("ES", "Эндоскопия");
		dicModality.put("MG", "Маммография");
		
		if (proxy.getManufacturerModelName() != null) {
			
			String model = proxy.getManufacturerModelName();
			if(dicModel.get(proxy.getManufacturerModelName())!=null) {
				model = dicModel.get(proxy.getManufacturerModelName());
			}
			addFormRow(rowCounter++, "Аппарат", new Label(model));
			//
			Hidden manufacturerModelName = new Hidden();
			manufacturerModelName.setName("00081090");
			
			manufacturerModelName.setValue(proxy.getManufacturerModelName());
			addFormHidden(manufacturerModelName);
		} else {
			// Тип исследования Modality 00080060
			studyManufacturerModelName = new ListBox();
			studyManufacturerModelName.setName("00081090");
			studyManufacturerModelName.addItem("-- выберите аппарат --", "");
			
			for( Iterator<String> iter = dicModel.keySet().iterator(); iter.hasNext();) {
				String key = iter.next();
				String val = dicModel.get(key);
				studyManufacturerModelName.addItem(val, key);
			}

			addFormRow(rowCounter++, "Аппарат", studyManufacturerModelName);
		}
		
		
		if (proxy.getStudyModality() != null) {
			
			String modal = proxy.getStudyModality();
			if(dicModality.get(proxy.getStudyModality())!=null) {
				modal = dicModality.get(proxy.getStudyModality());
			}
			
			addFormRow(rowCounter++, "Тип исследования", new Label(modal));
			//
			Hidden modality = new Hidden();
			modality.setName("00080060");
			
			modality.setValue(proxy.getStudyModality());
			addFormHidden(modality);
			
		} else {
			// Тип исследования Modality 00080060
			studyModality = new ListBox();
			studyModality.setName("00080060");
			studyModality.addItem("-- выберите тип --", "");
			
			for( Iterator<String> iter = dicModality.keySet().iterator(); iter.hasNext();) {
				String key = iter.next();
				String val = dicModality.get(key);
				studyModality.addItem(val, key);
			}


		
			addFormRow(rowCounter++, "Тип исследования",
					studyModality);

		}
		
		
		
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
		patientNameCheck.setSize("450px", "20em");

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
		birstdayDox.setValue(Utils.dateFormatSql.parse(proxy.getPatientBirthDate()));
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
		if(proxy.getPatientBirthDate()!=null)
			patientBirthDateHidden.setValue(Utils.dateFormatDicom.format(Utils.dateFormatSql.parse(proxy.getPatientBirthDate())));

		addFormRow(rowCounter++, "Дата рождения", birstdayDox);

		//
		studyDateBox = new DateBox();
		studyDateBox.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
		
		if(proxy.getStudyDate()==null) {
			studyDateBox.setValue(new Date());
		} else { 
			studyDateBox.setValue(Utils.dateFormatSql.parse(proxy.getStudyDate()));
		}
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
		if(studyDateBox.getValue()!=null)
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
//		studyDoctror = new TextBox();
//		studyDoctror.setName("00080090");
//		studyDoctror.setWidth("400px");
//		studyDoctror.setText(proxy.getStudyDoctor());
//		addFormRow(rowCounter++, "Врач", studyDoctror);
//		
		
		studyDoctror = new ListBox();
		studyDoctror.setName("00080090");
		//TODO Вынести в конфиг!!!
		studyDoctror.addItem("Выберите врача...", "");
		studyDoctror.addItem("Петрова  Н.Н.", "Петрова  Н.Н.");
		studyDoctror.addItem("Девяткова И.А.", "Девяткова И.А.");
		studyDoctror.addItem("Солоница В.Д.", "Солоница В.Д.");
		studyDoctror.addItem("Корж С.С.", "Корж С.С.");
		studyDoctror.addItem("Кузнецова Е.А.", "Кузнецова Е.А.");
		studyDoctror.addItem("Лызлова И.Е", "Лызлова И.Е");
		studyDoctror.addItem("Шешеня Т.В.", "Шешеня Т.В.");
		studyDoctror.addItem("Тимошенко С.А.", "Тимошенко С.А.");
		
		studyDoctror.addItem("Батрак С.И.", "Батрак С.И.");
		studyDoctror.addItem("Леткина З.Ю.", "Леткина З.Ю.");
		studyDoctror.addItem("Перлова Е.В.", "Перлова Е.В.");
		
		studyDoctror.addItem("Сотиболдиев А.И.", "Сотиболдиев А.И.");
		studyDoctror.addItem("Сосновских Э.А.", "Сосновских Э.А.");
		
		
		
//		studyDoctror2.addItem("Ввести нового...", "manualinput");
		
		boolean find = false;
		for( int i=0; i< studyDoctror.getItemCount(); i++) {
			String item = studyDoctror.getItemText(i);
			if(item.equalsIgnoreCase(proxy.getStudyDoctor())) {
				studyDoctror.setSelectedIndex(i);
				find = true;
				break;
			}
		}
		
		if(!find && proxy.getStudyDoctor()!=null && proxy.getStudyDoctor().length()!=0) {
			studyDoctror.addItem(proxy.getStudyDoctor() + " (нет в словаре!)", proxy.getStudyDoctor());
			studyDoctror.setSelectedIndex(studyDoctror.getItemCount()-1);
		}
		
//		studyDoctror2.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				// TODO Auto-generated method stub
//				// System.out.println("!!! "+event)!!!;
////				int i = studyDoctror2.getSelectedIndex();
////				studyDoctror2.getValue(i);
//			}
//		});
		
		addFormRow(rowCounter++, "Врач", studyDoctror);

		//
//		studyOperator = new TextBox();
//		studyOperator.setName("00081070");
//		studyOperator.setWidth("400px");
//		studyOperator.setText(proxy.getStudyOperator());
//		addFormRow(rowCounter++, "Лаборант", studyOperator);
		
		//TODO Вынести в конфиг!!!
		studyOperator = new ListBox();
		studyOperator.setName("00081070");
		studyOperator.addItem("Выберите лаборанта...", "");
		studyOperator.addItem("Михеева И.А.", "Михеева И.А.");
//		studyOperator.addItem("Давыдов В.С.", "Давыдов В.С.");
		studyOperator.addItem("Тебенев Е.Н.", "Тебенев Е.Н.");
		studyOperator.addItem("Диденко В.А.", "Диденко В.А.");
		
		
//		studyOperator2.addItem("Ввести нового...", "manualinput");
		
		find = false;
		for( int i=0; i< studyOperator.getItemCount(); i++) {
			String item = studyOperator.getItemText(i);
			if(item.equalsIgnoreCase(proxy.getStudyOperator())) {
				studyOperator.setSelectedIndex(i);
				find = true;
				break;
			}
		}
		
		if(!find && proxy.getStudyOperator()!=null && proxy.getStudyOperator().length()!=0) {
			studyOperator.addItem(proxy.getStudyOperator() + " (нет в словаре!)", proxy.getStudyOperator());
			studyOperator.setSelectedIndex(studyOperator.getItemCount()-1);
		}
		
//		studyOperator2.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				// TODO Auto-generated method stub
//				// System.out.println("!!! "+event)!!!;
////				int i = studyOperator2.getSelectedIndex();
////				studyOperator2.getValue(i);
//			}
//		});
		
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

//		medicalAlerts.addStyleName("DicomSuggestionEmpty");
		medicalAlerts.setTitle(medicalAlertsTitle);
		medicalAlerts.setText(medicalAlertsTitle);

		medicalAlerts.setText(proxy.getStudyResult());
		if (proxy.getStudyResult() == null
				|| proxy.getStudyResult().length() == 0) {
			medicalAlerts.setText(medicalAlertsTitle);
		}

		if (proxy.getStudyResult() != null
				&& proxy.getStudyResult().length() > 0) {
//			medicalAlerts.removeStyleName("DicomSuggestionEmpty");
//			medicalAlerts.addStyleName("DicomSuggestion");
		}
		addFormRow(rowCounter++, "Результат", medicalAlerts);

		medicalAlerts.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {

//				medicalAlerts.removeStyleName("DicomSuggestionEmpty");
//				medicalAlerts.addStyleName("DicomSuggestion");

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
//					medicalAlerts.removeStyleName("DicomSuggestion");
//					medicalAlerts.addStyleName("DicomSuggestionEmpty");
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

		ListBox lbAttachnebtType = new ListBox();
		lbAttachnebtType.setName("content_type");
		lbAttachnebtType.addItem("Описание", "application/pdf");
		lbAttachnebtType.addItem("Снимок", "image/jpg");
		
		addFormRow(rowCounter++, lbAttachnebtType, fileUpload);

		//
		submitBtn = new Button("Сохранить изменения...");
		dataVerifyed(false);
		submitBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				//TODO Сделать подсветки виджета
				
				if(studyManufacturerModelName!=null && studyManufacturerModelName.getSelectedIndex()<=0) {
					Window.alert("Выберите аппарат!");
					studyManufacturerModelName.setFocus(true);
					return;
				}
				
				//TODO Сделать подсветки виджета
				if(studyModality!=null && studyModality.getSelectedIndex()<=0) {
					Window.alert("Выберите тип исследования!");
					studyModality.setFocus(true);
					return;
				}
				
				//TODO Сделать недоступной кнопку "Сохранить"
				submitResult.setHTML("сохранение...");
				submitBtn.setEnabled(false);
				formPanel.submit();

			}
		});
		addFormRow(rowCounter++, submitBtn);
		

		//
		submitResult = new HTML();
		
		addFormRow(rowCounter++, submitResult);
		
		//Добавление правой части панели
		
//		formTable.getFlexCellFormatter().setRowSpan(0, 2, 17);
		int row = 0;
		
		
		VerticalPanel vpRight = new VerticalPanel();
		vpRight.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		vpRight.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		formTable.setWidget(row, 2,vpRight);
		
		hpMain.add(vpRight);
		
//		vpRight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		formTable.getCellFormatter().setHorizontalAlignment(row, 2,
//				VerticalPanel.ALIGN_LEFT);
//		formTable.getCellFormatter().setVerticalAlignment(row, 2, VerticalPanel.ALIGN_TOP);
		
		vpRight.add(makeItemLabel("Сверка имени (Click - выбор)"));
		vpRight.add( patientNameCheck);
		
		vpRight.add(makeItemLabel("Шаблоны"));
		ooTemplatePanel.setHTML("загрузка шаблонов...");
		vpRight.add(ooTemplatePanel);
		
		
		
		
		if(proxy!=null && proxy.getId()>0) {
			initTemplates(proxy.getStudyModality());
		}else {
			ooTemplatePanel.setHTML("Прикрепление шаблона доступно только в существующем исследовании");
		}
		
		formTable.getFlexCellFormatter().setRowSpan(0, 2, rowCounter);
		
//		System.out.println("!!! rowCounter="+rowCounter);
//		formTable.getFlexCellFormatter().setRowSpan(rowCounter, 1 , 17);
		
		
		
		//
		updateFromSession();

		
		initWidget(mainPanel);

		patientVerify();
	}



	/**
	 * Обновление из сессии
	 */
	private void updateFromSession() {
		
		submitResult.setHTML("Загрузка конфигурации...");
		submitBtn.setEnabled(false);
		
		Dicom_browser.browserService.getSessionObject(new AsyncCallback<Session>() {

			@Override
			public void onFailure(Throwable caught) {
				Dicom_browser.showErrorDlg(caught);
				submitResult.setHTML("Ошибка загрузка конфигурации!");
				submitBtn.setEnabled(true);
			}

			@Override
			public void onSuccess(Session result) {
				
				submitResult.setHTML("");
//				System.out.println("!!!! Session result="+result);
				submitBtn.setEnabled(true);	
				if(result==null) return;
				
				String ManufacturerModelName = result.getStudyManagePanel_ManufacturerModelName();
				String Modality = result.getStudyManagePanel_Modality();
				
//				System.out.println("!!!! getStudyManagePanel_ManufacturerModelName="+result.getStudyManagePanel_ManufacturerModelName());
//				System.out.println("!!!! getStudyManagePanel_Modality="+result.getStudyManagePanel_Modality());
				
				for(int i = 0; i<studyManufacturerModelName.getItemCount(); i++) {
					if(studyManufacturerModelName.getValue(i).equals(ManufacturerModelName)) {
						studyManufacturerModelName.setSelectedIndex(i);
						break;
					}
				}
				
				for(int i = 0; i<studyModality.getItemCount(); i++) {
					if(studyModality.getValue(i).equals(Modality)) {
						studyModality.setSelectedIndex(i);
						break;
					}
				}
				
				
			}
		});
		
//		Dicom_browser.browserService.getDcmTagsFromFile(0, Dicom_browser.version, proxy.getId(),
//				new AsyncCallback<ArrayList<DcmTagProxy>>() {
//
//					@Override
//					public void onFailure(Throwable caught) {
//						vp.clear();
//						vp.add(new Label("Ошибка полчения данных! " + caught));
//					}
//
//					@Override
//					public void onSuccess(ArrayList<DcmTagProxy> result) {
//						vp.clear();
//						for (Iterator<DcmTagProxy> it = result.iterator(); it
//								.hasNext();) {
//							DcmTagProxy proxy = it.next();
//							vp.add(new Label("" + proxy));
//						}
//					}
//				});
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

		Dicom_browser.browserService.getPatients(req,
				new AsyncCallback<PatientsRPCResponse>() {

					private Object patientProxy;

					public void onFailure(Throwable caught) {

						// transactionFinished();
//						Dicom_browser.showErrorDlg(caught);
						patientNameCheck.clear();
						patientNameCheck.addItem("Ошибка поиска из внешней системы!");
						VerticalPanel panel = (VerticalPanel)patientNameCheck.getParent();
						
//						HTML msg = new HTML();
						TextArea msg = new TextArea();
						msg.setSize("450px", "20em");
						if(caught instanceof DefaultGWTRPCException) {
							DefaultGWTRPCException ex = (DefaultGWTRPCException)caught;
							msg.setText("Ошибка поиска из внешней системы !!!! \n" + ex.getMessage()+
									"\n[" + ex.getLogMarker()+ "]\n"+ ex.getStack()+"</pre>");
						} else {
							msg.setText(caught.getMessage());
						}
						
						
						
						panel.add(msg);

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
									+ Utils.dateFormatUser.format(Utils.dateFormatSql.parse(patientProxy.getPatientBirthDate())),
									""+patientProxy
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
		
		birstdayDox.setValue(Utils.dateFormatSql.parse(lastPatientProxy.getPatientBirthDate()));
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
	 * @param title
	 * @param input
	 */
	private void addFormRow(int row, Widget title, Widget input) {

		formTable.setWidget(row, 0, title);
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
	protected void submitError(SubmitCompleteEvent event) {
		submitResult.setHTML("" + event.toDebugString() + "<HR>"
				+ event.getResults());
		submitBtn.setEnabled(true);
//		dataVerifyed(true);
	}

	/**
	 * Успешное завершение сохранения исследования
	 */
	protected void submitSuccess() {
		// clearForm();
		
		Dicom_browser.browserService.getStudyByID(1, Dicom_browser.version, StudyManagePanel.this.proxy.getId(), new AsyncCallback<StudyProxy>() {

			@Override
			public void onFailure(Throwable caught) {
				Dicom_browser.showErrorDlg(caught);
			}

			@Override
			public void onSuccess(StudyProxy result) {
				
				
				if(studyCardPanel!=null) {
					if(result==null) {
						Dicom_browser.showErrorDlg(new RuntimeException("Данные не найдены! id исследования: " +StudyManagePanel.this.proxy.getId()));
						submitResult.setHTML("Данные не найдены! id исследования: "+StudyManagePanel.this.proxy.getId());
						return;
					}
					studyCardPanel.setProxy(result);
				}
				
				
				
				msg = "Исследование изменено. Данные успешно сохранены.";
				if(studyCardPanel==null) {
					msg = "Создано новое исследование.";
					timeClose = 10;
				}
				
//				submitBtn.setEnabled(true);
				submitBtn.removeFromParent();
				
				
				Button closeBtn = new Button("Закрыть форму ввода");
				closeBtn.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						StudyManagePanel.this.removeFromParent();
						timer.cancel();
					}
				});
				
				addFormRow(rowCounter++, closeBtn);
				
				timer = new TransactionTimer() {
					int counter = 0;
					@Override
					public void run() {
						
						submitResult.setHTML(msg + " форма автоматически закроется через <b>"+(timeClose - counter)+"</b> секунд(ы)");
						if(counter++ >= timeClose) {
							
							StudyManagePanel.this.removeFromParent();
						}
					}
					
				};
				
				
				timer.scheduleRepeating(1000);
				

				
				}
				
			
		});
		
		
	}
	
	/**
	 * Получение списка шаблонов
	 * @param modality
	 * @return
	 */
	void initTemplates(String modality) {
		
		System.out.println("!!! modality="+modality);
		Dicom_browser.browserService.getOOTemplates(modality, new AsyncCallback<ArrayList<OOTemplateProxy>>() {

			@Override
			public void onFailure(Throwable caught) {
				Dicom_browser.showErrorDlg(caught);
			}

			@Override
			public void onSuccess(ArrayList<OOTemplateProxy> result) {
				
				String tmpls = "";
				for(int i=0; i<result.size(); i++) {
					tmpls += "<a href='"+result.get(i).getUrl()+"?id="+proxy.getId()+"'>" + result.get(i).getTitle() +"</a><br>";
				}
				
//				for(int i=0; i<100; i++) {
//					tmpls += "<a href='"+"?id="+proxy.getId()+"'>" + i +"</a><br>";	
//				}
				
				ooTemplatePanel.setHTML(tmpls);
			}
		});
		
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
