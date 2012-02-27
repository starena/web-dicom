package org.psystems.dicom.pdfview.client;

import java.util.ArrayList;

import org.psystems.dicom.pdfview.dto.FormFieldDto;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FormPdf extends Composite {

	private VerticalPanel mainPanel;

	public FormPdf(String tmplName) {
		mainPanel = new VerticalPanel();
		mainPanel.add(new Button("!!"));

		initWidget(mainPanel);
		initPanel(tmplName);
	}

	/**
	 * @param tmplName
	 */
	private void initPanel(String tmplName) {

		Dicom_pdfview.service.getFormFields(tmplName,
				new AsyncCallback<ArrayList<FormFieldDto>>() {

					@Override
					public void onSuccess(ArrayList<FormFieldDto> result) {
						// TODO Auto-generated method stub
						for (FormFieldDto formFieldDto : result) {
							mainPanel.add(new Button(formFieldDto
									.getFieldNameEncoded()
									+ " - " + formFieldDto.getUpperRightY()));
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Выдать сообщение в случае ошибки !!!
						RootPanel.get().add(new Label("Load templates fault! "+caught));
						System.err.println("Load templates fault! "+caught);
						caught.printStackTrace();
					}
				});

	}

}
