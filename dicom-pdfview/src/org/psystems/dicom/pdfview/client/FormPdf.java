package org.psystems.dicom.pdfview.client;

import java.util.ArrayList;

import org.psystems.dicom.pdfview.client.ui.FormCheckBox;
import org.psystems.dicom.pdfview.client.ui.FormListBox;
import org.psystems.dicom.pdfview.client.ui.FormRadioButton;
import org.psystems.dicom.pdfview.client.ui.FormTextArea;
import org.psystems.dicom.pdfview.client.ui.FormTextBox;
import org.psystems.dicom.pdfview.dto.FormFieldCheckboxDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;
import org.psystems.dicom.pdfview.dto.FormFieldListDto;
import org.psystems.dicom.pdfview.dto.FormFieldRadioBtnDto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FormPdf extends Composite {

	// private VerticalPanel mainPanel;
	// private AbsolutePanel mainPanel = new AbsolutePanel();
	private Panel mPanel;
	// private Grid mainPanel = new Grid();

	// private int panelHeight = 1000;
	private int singlePanelMaxHeight = 20;// Ширина однострочной панели

	// Максимальный разброс по ккординете Y для выравнивания полей вовда в
	// строку
	private int flexLayoutMaxInterval = 10;

	protected ArrayList<FormFieldDto> fields;// Поля формы

	private String tmplName;

	public FormPdf(String tmplName) {

		this.tmplName = tmplName;

		// mPanel = new VerticalPanel();
		// initWidget(mPanel);
		// initAsVerticalPanel(tmplName, (VerticalPanel) mPanel);
		//
		// mPanel = new FlexTable();
		// initWidget(mPanel);
		// initAsFlexTable(tmplName, (FlexTable) mPanel);

		mPanel = new VerticalPanel();
		initWidget(mPanel);
		initAsVerticalandHorizontalLyout(tmplName, (VerticalPanel) mPanel);

	}

	/**
	 * Сохранение данных
	 */
	protected void save() {
		for (FormFieldDto field : fields) {
			System.out.println("!!! field=" + field.getFieldNameEncoded() + "("
					+ ")=[" + field.getValue() + "]");
		}

		Dicom_pdfview.service.makePdf(tmplName, fields,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						RootPanel.get().add(
								new Label("Make PDF fault! " + caught));
						System.err.println("Make PDF fault! " + caught);
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
						System.out.println("!!!!! success !!!!");
					}
				});
	}

	/**
	 * 
	 * Самый оптимальный лайоут (соседние виджеты собираем в одну строчку)
	 * 
	 * @param tmplName
	 * @param panel
	 */
	private void initAsVerticalandHorizontalLyout(String tmplName,
			final VerticalPanel panel) {

		Dicom_pdfview.service.getFormFields(tmplName,
				new AsyncCallback<ArrayList<FormFieldDto>>() {

					@Override
					public void onSuccess(ArrayList<FormFieldDto> result) {

						Button btn = new Button("Сохранить");
						mPanel.add(btn);
						btn.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {

								save();

							}
						});

						fields = result;

						// предыдущее координаты виджета
						float prevUpperY = Float.MAX_VALUE;

						HorizontalPanel hp = null;

						for (FormFieldDto formFieldDto : result) {

							VerticalPanel vp = new VerticalPanel();
							vp.add(new Label(formFieldDto.getFieldTitle()));

							if (Math.abs(prevUpperY
									- formFieldDto.getUpperRightY()) < flexLayoutMaxInterval) {
							} else {
								prevUpperY = formFieldDto.getUpperRightY();
								hp = new HorizontalPanel();
								hp.setSpacing(10);
								panel.add(hp);
							}

							hp.add(vp);

							// Если комбо или лист
							if (formFieldDto instanceof FormFieldListDto) {
								FormListBox listBox = new FormListBox();

								for (String val : ((FormFieldListDto) formFieldDto)
										.getValues()) {
									listBox.addItem(val);
								}
								listBox.setFormField(formFieldDto);
								vp.add(listBox);

							}
							// Если радиокнопка
							else if (formFieldDto instanceof FormFieldRadioBtnDto) {
								for (String val : ((FormFieldRadioBtnDto) formFieldDto)
										.getValues()) {

									FormRadioButton radioButton = new FormRadioButton(
											formFieldDto.getFieldName(), val);
									radioButton.setFormField(formFieldDto);
									vp.add(radioButton);
								}

							}
							// Если чекбокс
							else if (formFieldDto instanceof FormFieldCheckboxDto) {
								FormCheckBox checkBox = new FormCheckBox(
										formFieldDto.getFieldNameEncoded());
								checkBox.setFormField(formFieldDto);
								vp.add(checkBox);
							}
							// Если текстовое поле
							else {

								// ширина поля ввода
								float height = formFieldDto.getUpperRightY()
										- formFieldDto.getLowerLeftY();

								// широкое текстовое поле ввода
								if (height > singlePanelMaxHeight) {
									FormTextArea normalText = new FormTextArea();
									normalText
											.setVisibleLines((int) (height / singlePanelMaxHeight));
									normalText.setFormField(formFieldDto);
									vp.add(normalText);
								}
								// обычное текстовое поле ввода
								else {
									FormTextBox normalText = new FormTextBox();
									normalText.setFormField(formFieldDto);
									vp.add(normalText);
								}

							}

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

	/**
	 * @param tmplName
	 * @param panel
	 */
	private void initAsFlexTable(String tmplName, final FlexTable panel) {

		Dicom_pdfview.service.getFormFields(tmplName,
				new AsyncCallback<ArrayList<FormFieldDto>>() {

					@Override
					public void onSuccess(ArrayList<FormFieldDto> result) {
						// TODO Auto-generated method stub

						fields = result;

						int row = -1;
						int col = 0;
						// предыдущее координаты виджета
						float prevUpperY = Float.MAX_VALUE;

						for (FormFieldDto formFieldDto : result) {

							row++;

							VerticalPanel vp = new VerticalPanel();
							vp.add(new Label(formFieldDto.getFieldTitle()));

							if (Math.abs(prevUpperY
									- formFieldDto.getUpperRightY()) < flexLayoutMaxInterval) {
								col++;
								if (row > 0)
									row--;
							} else {
								prevUpperY = formFieldDto.getUpperRightY();
								col = 0;
							}

							panel.setWidget(row, col, vp);

							// System.out.println("!!!! "
							// + formFieldDto.getFieldTitle() + "[" + row
							// + ";" + col + "]");

							// Если комбо или лист
							if (formFieldDto instanceof FormFieldListDto) {
								FormListBox listBox = new FormListBox();

								for (String val : ((FormFieldListDto) formFieldDto)
										.getValues()) {
									listBox.addItem(val);
								}
								listBox.setFormField(formFieldDto);
								vp.add(listBox);

							}
							// Если радиокнопка
							else if (formFieldDto instanceof FormFieldRadioBtnDto) {
								for (String val : ((FormFieldRadioBtnDto) formFieldDto)
										.getValues()) {

									FormRadioButton radioButton = new FormRadioButton(
											formFieldDto.getFieldName(), val);
									radioButton.setFormField(formFieldDto);
									vp.add(radioButton);
								}

							}
							// Если чекбокс
							else if (formFieldDto instanceof FormFieldCheckboxDto) {
								FormCheckBox checkBox = new FormCheckBox(
										formFieldDto.getFieldNameEncoded());
								checkBox.setFormField(formFieldDto);
								vp.add(checkBox);
							}
							// Если текстовое поле
							else {

								// ширина поля ввода
								float height = formFieldDto.getUpperRightY()
										- formFieldDto.getLowerLeftY();

								// широкое текстовое поле ввода
								if (height > singlePanelMaxHeight) {
									FormTextArea normalText = new FormTextArea();
									normalText
											.setVisibleLines((int) (height / singlePanelMaxHeight));
									normalText.setFormField(formFieldDto);
									vp.add(normalText);
								}
								// обычное текстовое поле ввода
								else {
									FormTextBox normalText = new FormTextBox();
									normalText.setFormField(formFieldDto);
									vp.add(normalText);
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

	/**
	 * @param tmplName
	 * @param panel
	 */
	private void initAsVerticalPanel(String tmplName, final VerticalPanel panel) {

		Button btn = new Button("Сохранить");
		mPanel.add(btn);
		btn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				save();

			}
		});

		Dicom_pdfview.service.getFormFields(tmplName,
				new AsyncCallback<ArrayList<FormFieldDto>>() {

					@Override
					public void onSuccess(ArrayList<FormFieldDto> result) {
						// TODO Auto-generated method stub

						fields = result;

						for (FormFieldDto formFieldDto : result) {

							panel.add(new Label(formFieldDto.getFieldTitle()));

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
								FormListBox listBox = new FormListBox();

								for (String val : ((FormFieldListDto) formFieldDto)
										.getValues()) {
									listBox.addItem(val);
								}
								listBox.setFormField(formFieldDto);
								panel.add(listBox);

							}
							// Если радиокнопка
							else if (formFieldDto instanceof FormFieldRadioBtnDto) {
								for (String val : ((FormFieldRadioBtnDto) formFieldDto)
										.getValues()) {

									FormRadioButton radioButton = new FormRadioButton(
											formFieldDto.getFieldName(), val);
									radioButton.setFormField(formFieldDto);
									panel.add(radioButton);
								}

							}
							// Если чекбокс
							else if (formFieldDto instanceof FormFieldCheckboxDto) {
								FormCheckBox checkBox = new FormCheckBox(
										formFieldDto.getFieldNameEncoded());
								checkBox.setFormField(formFieldDto);
								panel.add(checkBox);
							}
							// Если текстовое поле
							else {

								// ширина поля ввода
								float height = formFieldDto.getUpperRightY()
										- formFieldDto.getLowerLeftY();

								// широкое текстовое поле ввода
								if (height > singlePanelMaxHeight) {
									FormTextArea normalText = new FormTextArea();
									normalText
											.setVisibleLines((int) (height / singlePanelMaxHeight));
									normalText.setFormField(formFieldDto);
									panel.add(normalText);
								}
								// обычное текстовое поле ввода
								else {
									FormTextBox normalText = new FormTextBox();
									normalText.setFormField(formFieldDto);
									panel.add(normalText);
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
