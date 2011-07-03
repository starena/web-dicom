package org.psystems.dicom.browser.client.component;

import org.psystems.dicom.browser.client.Dicom_browser;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Направление
 * 
 * @author dima_d
 * 
 */
public class DirectionCard extends Composite {

    private DirectionProxy drnProxy;
    private VerticalPanel mainPanel;
    protected StudyManagePanel studyManagePanel;
    private HorizontalPanel allStuduesPanel;

    // private StudyManagePanel studyManagePanel;

    public DirectionCard(DirectionProxy drnProxy) {
	this.drnProxy = drnProxy;
	mainPanel = new VerticalPanel();
	String sex = "М";
	if ("F".equals(drnProxy.getPatient().getPatientSex())) {
	    sex = "Ж";
	}

	String date = Utils.dateFormatUser.format(Utils.dateFormatSql
		.parse(drnProxy.getPatient().getPatientBirthDate()));
	Label labelPatient = new Label(drnProxy.getPatient().getPatientName() + " (" + sex + ") " + date + " - "
		+ drnProxy.getDoctorDirect().getEmployeeName() + " [" + drnProxy.getId() + "]");
	labelPatient.setStyleName("DicomItem");

	mainPanel.add(labelPatient);

	labelPatient.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {

		showForm();

	    }

	});

	initWidget(mainPanel);
    }

    protected void showForm() {

	boolean exiting = false;

	if (allStuduesPanel != null && allStuduesPanel.isAttached()) {
	    allStuduesPanel.removeFromParent();
	    allStuduesPanel = null;
	    exiting = true;
	}

	if (studyManagePanel != null && studyManagePanel.isAttached()) {
	    studyManagePanel.removeFromParent();
	    studyManagePanel = null;
	    exiting = true;
	}

	// TODO Передалать!!! замороченно получилось. нужно перегруппировать
	// компоненты просто чтобы label с ФИО было отдельно, а остально можно
	// было седллать .clear();
	if (exiting)
	    return;

	final Label loading = new Label("Получение данных...");
	mainPanel.add(loading);

	Dicom_browser.browserService.getStudiesByDirectionID(drnProxy.getId(), new AsyncCallback<StudyProxy[]>() {

	    @Override
	    public void onSuccess(StudyProxy[] result) {
		// TODO Auto-generated method stub
		if (result.length == 0)
		    showStudy(null);
		else
		    showStudies(result);

		loading.removeFromParent();
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Dicom_browser.showErrorDlg(caught);
		loading.removeFromParent();
	    }
	});

    }

    /**
     * Вывод набора ислседований
     * 
     * @param studies
     */
    private void showStudies(StudyProxy[] studies) {
	// TODO Auto-generated method stub
	allStuduesPanel = new HorizontalPanel();
	for (StudyProxy studyProxy : studies) {
	    allStuduesPanel.add(new StudiesPanel(studyProxy));
	}
	mainPanel.add(allStuduesPanel);
    }

    /**
     * Вывод конкретного исследования
     */
    private void showStudy(StudyProxy proxy) {
	if (studyManagePanel == null || !studyManagePanel.isAttached()) {

	    if (proxy == null) {
		proxy = new StudyProxy();
		PatientProxy pat = drnProxy.getPatient();
		proxy.setPatientId("" + pat.getPatientId());
		proxy.setPatientName(pat.getPatientName());
		proxy.setPatientSex(pat.getPatientSex());
		// proxy.setPatientBirthDate(ORMUtil.userDateStringToSQLDateString(patientProxy.getPatientBirthDate()));
		proxy.setPatientBirthDate(pat.getPatientBirthDate());
		proxy.setStudyId(drnProxy.getDirectionId());
		proxy.setDirection(drnProxy);

	    } else {

	    }
	    studyManagePanel = new StudyManagePanel(null, proxy);
	    mainPanel.add(studyManagePanel);
	}
	

    }

    /**
     * @param title
     * @return
     */
    Widget makeItemLabel(String title) {
	Label label = new Label(title);
	// TODO Убрать в css
	DOM.setStyleAttribute(label.getElement(), "font", "1.5em/ 150% normal Verdana, Tahoma, sans-serif");
	return label;
    }

    /**
     * @author dima_d
     * 
     *         Панель с информацией об исследовании
     */
    public class StudiesPanel extends HorizontalPanel {
	private StudyProxy studyProxy = null;

	public StudiesPanel(final StudyProxy studyProxy) {
	    super();
	    this.studyProxy = studyProxy;
	    final Button b = new Button("[" + studyProxy.getId() + "] " + studyProxy.getStudyDate());
	    add(b);
	    b.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    showStudy(studyProxy);
		    b.removeFromParent();
		    StudiesPanel.this.getParent().removeFromParent();
		}
	    });
	}

    }

}
