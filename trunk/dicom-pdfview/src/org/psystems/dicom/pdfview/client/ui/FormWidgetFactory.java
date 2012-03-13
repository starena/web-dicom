package org.psystems.dicom.pdfview.client.ui;

import org.psystems.dicom.pdfview.dto.FormFieldCheckboxDto;
import org.psystems.dicom.pdfview.dto.FormFieldDateDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;
import org.psystems.dicom.pdfview.dto.FormFieldListDto;
import org.psystems.dicom.pdfview.dto.FormFieldRadioBtnDto;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * 
 * Фабрика виджетов для PDF-форм
 * 
 * @author dima_d
 * 
 */
public class FormWidgetFactory {

	public static DateTimeFormat dateFormatUser = DateTimeFormat
			.getFormat("dd.MM.yyyy");
	public static DateTimeFormat dateFormatDicom = DateTimeFormat
			.getFormat("yyyyMMdd");
	public static DateTimeFormat dateFormatSql = DateTimeFormat
			.getFormat("yyyy-MM-dd");
	public static DateTimeFormat dateTimeFormatSql = DateTimeFormat
			.getFormat("yyyy-MM-dd HH:mm:ss");
	public static DateTimeFormat dateTimeFormatUser = DateTimeFormat
			.getFormat("dd.MM.yyyy HH:mm:ss");
	public static DateTimeFormat dateFormatYEARONLY = DateTimeFormat
			.getFormat("yyyy");

	/**
	 * 
	 * Получение экземпляра виджета для формы
	 * 
	 * @param dto
	 * @return
	 */
	public static IFormInput getWidgetInstance(FormFieldDto formFieldDto) {

		// Если комбо или лист
		if (formFieldDto instanceof FormFieldListDto) {
			FormListBox listBox = new FormListBox();

			for (String val : ((FormFieldListDto) formFieldDto).getValues()) {
				listBox.addItem(val);
			}
			listBox.setFormField(formFieldDto);
			return listBox;

		}
		// Если радиокнопка
		else if (formFieldDto instanceof FormFieldRadioBtnDto) {
			for (String val : ((FormFieldRadioBtnDto) formFieldDto).getValues()) {

				FormRadioButton radioButton = new FormRadioButton(
						formFieldDto.getFieldName(), val);
				radioButton.setFormField(formFieldDto);
				return radioButton;
			}

		}
		// Если чекбокс
		else if (formFieldDto instanceof FormFieldCheckboxDto) {
			FormCheckBox checkBox = new FormCheckBox(
					formFieldDto.getFieldNameEncoded());
			checkBox.setFormField(formFieldDto);
			return checkBox;
		}
		// Если Календарик
		else if (formFieldDto instanceof FormFieldDateDto) {

			FormDateBox dateBox = new FormDateBox();
			return dateBox;
		}
		// Если текстовое поле
		else {

			// ширина поля ввода
			float height = formFieldDto.getUpperRightY()
					- formFieldDto.getLowerLeftY();

			// широкое текстовое поле ввода
			if (height > FormTextArea.singlePanelMaxHeight) {
				FormTextArea normalText = new FormTextArea();
				normalText
						.setVisibleLines((int) (height / FormTextArea.singlePanelMaxHeight));
				normalText.setFormField(formFieldDto);
				return normalText;
			}
			// обычное текстовое поле ввода
			else {
				FormTextBox normalText = new FormTextBox();
				normalText.setFormField(formFieldDto);
				return normalText;
			}

		}
		return null;

	}
}
