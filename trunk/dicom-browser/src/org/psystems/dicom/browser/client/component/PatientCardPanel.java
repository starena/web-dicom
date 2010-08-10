package org.psystems.dicom.browser.client.component;

import org.psystems.dicom.browser.client.proxy.PatientProxy;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
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

	public PatientCardPanel(PatientProxy patientProxy) {
		this.patientProxy = patientProxy;
		VerticalPanel mainPanel = new VerticalPanel();
		Label l = new Label(patientProxy.getPatientName() + " ("
				+ patientProxy.getPatientSex() + ") "
				+ patientProxy.getPatientBirthDate());
		l.setStyleName("DicomItem");

		mainPanel.add(l);

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
