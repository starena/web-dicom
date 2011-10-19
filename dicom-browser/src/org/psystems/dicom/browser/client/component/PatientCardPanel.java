package org.psystems.dicom.browser.client.component;

import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Карточка пациента
 * 
 * @author dima_d
 * 
 */
public class PatientCardPanel extends Composite {

    private PatientProxy patientProxy;
    private VerticalPanel mainPanel;
    private StudyManagePanel studyManagePanel;
    private boolean btnVisible = false;
    private HorizontalPanel btnPanel;
    protected DirectionCardExternal drnPanel;

    public PatientCardPanel(final PatientProxy patientProxy) {
	this.patientProxy = patientProxy;
	mainPanel = new VerticalPanel();
	String sex = "М";
	if ("F".equals(patientProxy.getPatientSex())) {
	    sex = "Ж";
	}

	Label labelPatient = new Label(patientProxy.getPatientName() + " (" + sex + ") "
		+ patientProxy.getPatientBirthDate());
	labelPatient.setStyleName("DicomItem");

	mainPanel.add(labelPatient);

	labelPatient.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		makeButtons(patientProxy);

	    }
	});

	initWidget(mainPanel);

    }

    private void makeButtons(final PatientProxy patientProxy) {

	if (btnVisible) {
	    btnPanel.removeFromParent();
	    btnVisible = false;
	} else {
	    btnPanel = new HorizontalPanel();
	    mainPanel.add(btnPanel);
	    Button btnNewStudy = new Button("создать новое исследование");
	    btnPanel.add(btnNewStudy);

	    btnNewStudy.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    if (studyManagePanel == null || !studyManagePanel.isAttached()) {

			StudyProxy proxy = new StudyProxy();
			proxy.setPatientId("" + patientProxy.getId());
			proxy.setPatientName(patientProxy.getPatientName());
			proxy.setPatientSex(patientProxy.getPatientSex());
			// proxy.setPatientBirthDate(ORMUtil.userDateStringToSQLDateString(patientProxy.getPatientBirthDate()));
			proxy.setPatientBirthDate(patientProxy.getPatientBirthDate());

			studyManagePanel = new StudyManagePanel(null, proxy);
			mainPanel.add(studyManagePanel);

		    } else {
			studyManagePanel.removeFromParent();
			studyManagePanel = null;
		    }
		}
	    });

	    Button btnNewDrn = new Button("создать новое направление");
	    btnPanel.add(btnNewDrn);
	    btnNewDrn.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {

		    if (drnPanel == null || !drnPanel.isAttached()) {

//			StudyProxy proxy = new StudyProxy();
//			proxy.setPatientId("" + patientProxy.getId());
//			proxy.setPatientName(patientProxy.getPatientName());
//			proxy.setPatientSex(patientProxy.getPatientSex());
//			// proxy.setPatientBirthDate(ORMUtil.userDateStringToSQLDateString(patientProxy.getPatientBirthDate()));
//			proxy.setPatientBirthDate(patientProxy.getPatientBirthDate());

			drnPanel = new DirectionCardExternal(patientProxy);
			mainPanel.add(drnPanel);
		    } else {
			drnPanel.removeFromParent();
			drnPanel = null;
		    }

		}
	    });

	    btnVisible = true;
	}
    }

    /**
     * @param title
     * @return
     */
    Widget makeItemLabel(String title) {
	Label label = new Label(title);
	DOM.setStyleAttribute(label.getElement(), "background", "yellow");
	return label;
    }

}
