package org.psystems.dicom.pdfview.client;

import java.util.ArrayList;

import org.psystems.dicom.pdfview.dto.FormFieldCheckboxDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;
import org.psystems.dicom.pdfview.dto.FormFieldListDto;
import org.psystems.dicom.pdfview.dto.FormFieldRadioBtnDto;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FormPdf extends Composite {

	// private VerticalPanel mainPanel;
	// private AbsolutePanel mainPanel = new AbsolutePanel();
	private VerticalPanel mainPanel = new VerticalPanel();

	private int panelHeight = 1000;
	private int singlePanelMaxHeight = 20;// Ширина однострочной панели

	public FormPdf(String tmplName) {

		// mainPanel = new VerticalPanel();
		// mainPanel.add(new Button("!!"));

		// mainPanel.setSize("1000px", panelHeight+"px");
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

							mainPanel.add(new Label(formFieldDto
									.getFieldNameEncoded()));

							// Button btn = new Button(formFieldDto
							// .getFieldNameEncoded());
							//
							// btn.setTitle("[" + formFieldDto.getLowerLeftX()
							// + "," + formFieldDto.getLowerLeftY() + ","
							// + formFieldDto.getUpperRightX() + ","
							// + formFieldDto.getUpperRightY() + "]");
							//
							// mainPanel.add(btn);

							// Если комбо или лист
							if (formFieldDto instanceof FormFieldListDto) {
								ListBox listBox = new ListBox(false);

								for (String val : ((FormFieldListDto) formFieldDto)
										.getValues()) {
									listBox.addItem(val);
								}
								mainPanel.add(listBox);

							}
							// Если радиокнопка
							else if (formFieldDto instanceof FormFieldRadioBtnDto) {
								for (String val : ((FormFieldRadioBtnDto) formFieldDto)
										.getValues()) {

									RadioButton radioButton = new RadioButton(
											formFieldDto.getFieldName(), val);
									mainPanel.add(radioButton);
								}

							}
							// Если чекбокс
							else if (formFieldDto instanceof FormFieldCheckboxDto) {
								CheckBox checkBox = new CheckBox(formFieldDto
										.getFieldNameEncoded());
								mainPanel.add(checkBox);
							}
							// Если текстовое поле
							else {

								// ширина поля ввода
								float height = formFieldDto.getUpperRightY()
										- formFieldDto.getLowerLeftY();

								// широкое текстовое поле ввода
								if(height > singlePanelMaxHeight) {
									TextArea normalText = new TextArea();
									normalText.setVisibleLines((int)(height / singlePanelMaxHeight));
									mainPanel.add(normalText);
								}
								// обычное текстовое поле ввода
								else {
									TextBox normalText = new TextBox();
									mainPanel.add(normalText);
								}
								
							}

							// mainPanel.add(btn, (int) formFieldDto
							// .getLowerLeftX(), panelHeight - (int)
							// formFieldDto
							// .getLowerLeftY());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Выдать сообщение в случае ошибки !!!
						RootPanel.get().add(
								new Label("Load templates fault! " + caught));
						System.err.println("Load templates fault! " + caught);
						caught.printStackTrace();
					}
				});

	}

}
