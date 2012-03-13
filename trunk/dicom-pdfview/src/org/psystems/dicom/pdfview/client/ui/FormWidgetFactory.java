package org.psystems.dicom.pdfview.client.ui;

import org.psystems.dicom.pdfview.dto.FormFieldCheckboxDto;
import org.psystems.dicom.pdfview.dto.FormFieldDto;
import org.psystems.dicom.pdfview.dto.FormFieldListDto;
import org.psystems.dicom.pdfview.dto.FormFieldRadioBtnDto;

/**
 * 
 * Фабрика виджетов для PDF-форм
 * 
 * @author dima_d
 *
 */
public class FormWidgetFactory {

	/**
	 * 
	 * Получение экземпляра виджета для формы
	 * 
	 * @param dto
	 * @return
	 */
	public static IFormInput getInstance(FormFieldDto formFieldDto) {

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
