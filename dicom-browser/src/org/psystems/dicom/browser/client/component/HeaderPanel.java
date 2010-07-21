/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;

/**
 * Панель заголовка проекта
 * 
 * @author dima_d
 * 
 */
public class HeaderPanel extends HorizontalPanel {

	/**
	 * @param application
	 */
	public HeaderPanel() {
		
		HorizontalPanel itemsPanel = new HorizontalPanel();
		this.add(itemsPanel);
		itemsPanel.setSpacing(5);
		
		_applayStyles();
		Hyperlink mainPage = new Hyperlink("На главную", "");
		itemsPanel.add(mainPage);
		Hyperlink newStudy = new Hyperlink("Создать исследование", "newstudy");
		itemsPanel.add(newStudy);
		Hyperlink showIntro = new Hyperlink("Приглашение", "showintro");
		itemsPanel.add(showIntro);
	}
	
	/**
	 * Применение стилей
	 */
	private void _applayStyles() {
//		DOM.setStyleAttribute(this.getElement(), "marginLeft", "50");
		DOM.setStyleAttribute(this.getElement(), "width", "100%");
		DOM.setStyleAttribute(this.getElement(), "borderBottom", "1px solid #44639C");
		
		this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);		
	}

}
