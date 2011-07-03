package org.psystems.dicom.browser.client.component;

import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
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

    // private StudyManagePanel studyManagePanel;

    public DirectionCard(final DirectionProxy drnProxy) {
	this.drnProxy = drnProxy;
	mainPanel = new VerticalPanel();
	String sex = "М";
	if ("F".equals(drnProxy.getPatient().getPatientSex())) {
	    sex = "Ж";
	}

	String date = Utils.dateFormatUser.format(Utils.dateFormatSql
		.parse(drnProxy.getPatient().getPatientBirthDate()));
	Label labelPatient = new Label(drnProxy.getPatient().getPatientName() + " (" + sex + ") " + date + " - "
		+ drnProxy.getDoctorDirect().getEmployeeName());
	labelPatient.setStyleName("DicomItem");

	mainPanel.add(labelPatient);

	labelPatient.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		// showForm();

		Label loading = new Label("Получение данных...");
		mainPanel.add(loading);
		if (studyManagePanel == null || !studyManagePanel.isAttached()) {

		    StudyProxy proxy = new StudyProxy();
		    PatientProxy pat = drnProxy.getPatient();

		    proxy.setPatientId("" + pat.getPatientId());
		    proxy.setPatientName(pat.getPatientName());
		    proxy.setPatientSex(pat.getPatientSex());
		    // proxy.setPatientBirthDate(ORMUtil.userDateStringToSQLDateString(patientProxy.getPatientBirthDate()));
		    proxy.setPatientBirthDate(pat.getPatientBirthDate());

		    proxy.setStudyId(drnProxy.getDirectionId());

		    proxy.setDirection(drnProxy);

		    studyManagePanel = new StudyManagePanel(null, proxy);
		    mainPanel.add(studyManagePanel);

		} else {
		    studyManagePanel.removeFromParent();
		    studyManagePanel = null;
		}
		loading.removeFromParent();
	    }
	});

	initWidget(mainPanel);
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

}
