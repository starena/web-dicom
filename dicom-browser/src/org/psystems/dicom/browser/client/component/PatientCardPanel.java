package org.psystems.dicom.browser.client.component;

import org.psystems.dicom.browser.client.Dicom_browser;
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

	public PatientCardPanel(final PatientProxy patientProxy) {
		this.patientProxy = patientProxy;
		mainPanel = new VerticalPanel();
		String sex = "М";
		if("F".equals(patientProxy.getPatientSex())) {
			sex = "Ж";
		}
		Label l = new Label(patientProxy.getPatientName() + " ("
				+ sex  + ") "
				+ patientProxy.getPatientBirthDate());
		l.setStyleName("DicomItem");

		mainPanel.add(l);
		
		Button changeStudy = new Button("изменить...");
		changeStudy.setStyleName("DicomItem");
		mainPanel.add(changeStudy);
		
		changeStudy.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				StudyProxy proxy = new StudyProxy();
				proxy.setPatientId(""+patientProxy.getId());
				proxy.setPatientName(patientProxy.getPatientName());
				proxy.setPatientSex(patientProxy.getPatientSex());
				proxy.setPatientBirthDate(patientProxy.getPatientBirthDate());
				
				StudyManagePanel panel = new StudyManagePanel(Dicom_browser.manageStudyService, Dicom_browser.browserService, null, proxy);
				mainPanel.add(panel);
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
		DOM.setStyleAttribute(label.getElement(), "background", "yellow");
		return label;
	}

}
