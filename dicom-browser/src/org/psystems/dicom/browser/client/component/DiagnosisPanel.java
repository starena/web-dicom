/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;

import org.psystems.dicom.browser.client.Dicom_browser;
import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.proxy.DiagnosisProxy;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * Панель управления диагнозами
 * 
 * @author dima_d
 * 
 */
public class DiagnosisPanel extends VerticalPanel {

    private VerticalPanel showDiagnosisPanel;
    private DiagnosisProxy[] diagnosis;
    private DiagnosisProxy diagnosis4Add;

    /**
     * @param application
     */
    public DiagnosisPanel() {

	// панель для вывода списка диагнозов
	showDiagnosisPanel = new VerticalPanel();
	this.add(showDiagnosisPanel);

	// панель добавления диагноза
	HorizontalPanel addDiagnosisPanel = new HorizontalPanel();
	this.add(addDiagnosisPanel);

	
	final ListBox lbDiaType = new ListBox();
	lbDiaType.addItem(DiagnosisProxy.TYPE_MAIN);
	lbDiaType.addItem(DiagnosisProxy.TYPE_ACCOMPANYING);
	lbDiaType.addItem(DiagnosisProxy.TYPE_INVOLVEMENT);
	addDiagnosisPanel.add(lbDiaType);

	final ListBox lbDiaSubType = new ListBox();
	// Предварительный, Уточненный, Выписки, Направления, Приемного
	// отделения,
	// Клинический, Смерти, Паталогоанатомический.
	lbDiaSubType.addItem("Предварительный");
	lbDiaSubType.addItem("Уточненный");
	lbDiaSubType.addItem("Выписки");
	lbDiaSubType.addItem("Направления");
	lbDiaSubType.addItem("Приемного отделения");
	lbDiaSubType.addItem("Клинический");
	lbDiaSubType.addItem("Смерти");
	lbDiaSubType.addItem("Паталогоанатомический");
	addDiagnosisPanel.add(lbDiaSubType);
	

	DicSuggestBox diaBox = new DicSuggestBox("diagnosis");
	diaBox.getBox().addSelectionHandler(new SelectionHandler<Suggestion>() {
	    
	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		diagnosis4Add =  (DiagnosisProxy) item.getEvent();
	    }
	});
	
	addDiagnosisPanel.add(diaBox);
//	itemsPanel.add(new DicSuggestBox("services"));
	
	Button addBtn = new Button("Добавить");
	addDiagnosisPanel.add(addBtn);
	addBtn.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {

		if(diagnosis4Add==null) return;
		
		ArrayList<DiagnosisProxy> dias = new ArrayList<DiagnosisProxy>();
		for (int i = 0; i < diagnosis.length; i++) {
		    dias.add(diagnosis[i]);
		}

		DiagnosisProxy proxy = new DiagnosisProxy();
		proxy.setDiagnosisCode(diagnosis4Add.getDiagnosisCode());
		proxy.setDiagnosisDescription(diagnosis4Add.getDiagnosisDescription());
		proxy.setDiagnosisType(lbDiaType.getItemText(lbDiaType.getSelectedIndex()));
		proxy.setDiagnosisSubType(lbDiaSubType.getItemText(lbDiaSubType.getSelectedIndex()));

		dias.add(proxy);
		setDiagnosis(dias.toArray(new DiagnosisProxy[dias.size()]));

	    }
	});

    }

    public DiagnosisProxy[] getDiagnosis() {
	return diagnosis;
    }

    public void setDiagnosis(DiagnosisProxy[] diagnosis) {
	this.diagnosis = diagnosis;
	showDiagnosisPanel.clear();

	for (DiagnosisProxy dia : diagnosis) {
	    StringBuffer diaText = new StringBuffer();
	    diaText.append(dia.getDiagnosisCode() + " (" + dia.getDiagnosisType() + ";" + dia.getDiagnosisSubType()
		    + ") " + dia.getDiagnosisDescription());
	    showDiagnosisPanel.add(new Label(diaText.toString()));
	}
    }

}
