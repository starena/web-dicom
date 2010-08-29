/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Iterator;

import org.psystems.dicom.browser.client.Dicom_browser;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCRequest;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ImageBundle.Resource;

/**
 * Карточка Dicom исследования
 * 
 * @author dima_d
 * 
 */
public class StudyCard extends Composite {
	
	
	interface Resources extends ClientBundle {

		  @Source("logoTXT.png")
		  /*@ImageOptions(flipRtl = true)*/
		  ImageResource logoTXT();
		  
		  @Source("logoPDF.png")
		  /*@ImageOptions(flipRtl = true)*/
		  ImageResource logoPDF();
		}
	
	Resources resources = GWT.create(Resources.class);


	private String datePattern = "dd.MM.yyyy";
	StudyProxy proxy = null;
	private Label labelPatientName;
	private Label labelStudyDate;
	private Label labelStudyViewProtocolDate;
	private Label labelPatientId;
	private Label labelManufacturerModelName;
	private Label labelStudyDoctor;
	private Label labelStudyOperator;
	private Label labelStudyType;
	private Label labelStudyDescription;
	private Label labelResult;
	private Label labelStudyViewprotocol;
	private HorizontalPanel FilesPanel;
	private boolean fullMode;
	private VerticalPanel mainPanel;
	private FlexTable commonTable;

	/**
	 * Карточка исследования
	 * @param fullMode
	 */
	public StudyCard(boolean fullMode) {
		super();
		//this.fullMode = fullMode;
		this.fullMode = false;//TODO Убрать!
		
		mainPanel = new VerticalPanel();

		labelPatientName = new Label("");
		labelPatientName.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isFullMode()) setFullMode(false); else setFullMode(true);
			}
		});
		labelPatientName.setStyleName("DicomItem");
		//TODO Убрать в css
		labelPatientName.setWidth("100%");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		mainPanel.add(hp);
		
		hp.add(labelPatientName);
		
		
		final Button changeStudyBtn = new Button("изменить...");
		hp.add(changeStudyBtn);
		
		changeStudyBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setFullMode(true);
				StudyManagePanel panel = new StudyManagePanel(Dicom_browser.manageStudyService, Dicom_browser.browserService, StudyCard.this, proxy);
				mainPanel.add(panel);
				changeStudyBtn.removeFromParent();
			}
		});
		

		initWidget(mainPanel);
		addStyleName("StudyCard");
	}




	/**
	 * Создание панели с общими обисанием исследования
	 */
	private void makeCommonPanel() {
		
		commonTable = new FlexTable();
		commonTable.setStyleName("SearchItem");
		mainPanel.add(commonTable);
		
//		table.setWidget(0, 1, DCMFilesPanel);
//		table.getFlexCellFormatter().setAlignment(0, 0,
//				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_TOP);
//		table.getFlexCellFormatter().setRowSpan(0, 1, 5);

		createItemName(commonTable, 1, 0, "дата исследования:");
		labelStudyDate = createItemValue(commonTable, 1, 1, "");

		createItemName(commonTable, 1, 2, "дата описания:");
		labelStudyViewProtocolDate = createItemValue(commonTable, 1, 3, "");

		createItemName(commonTable, 1, 4, "код пациента:");
		labelPatientId = createItemValue(commonTable, 1, 5, "");

		createItemName(commonTable, 2, 0, "аппарат:");
		labelManufacturerModelName = createItemValue(commonTable, 2, 1,  "");

		createItemName(commonTable, 2, 2, "врач:");
		labelStudyDoctor = createItemValue(commonTable, 2, 3, "");

		createItemName(commonTable, 2, 4, "лаборант:");
		labelStudyOperator = createItemValue(commonTable, 2, 5, "");

		createItemName(commonTable, 3, 0, "Тип исследования:");
		labelStudyType = createItemValue(commonTable, 3, 1, "");
		
		createItemName(commonTable, 3, 2, "Описание:");
		labelStudyDescription = createItemValue(commonTable, 3, 3, "");
		commonTable.getFlexCellFormatter().setColSpan(3, 3, 3);
		
		createItemName(commonTable, 4, 0, "результат:");
		labelResult =  createItemValue(commonTable, 4, 1, "");
		commonTable.getFlexCellFormatter().setColSpan(4, 1, 5);
		
		createItemName(commonTable, 5, 0, "Протокол осмотра:");
		labelStudyViewprotocol = createItemValue(commonTable, 5, 1, "");
		commonTable.getFlexCellFormatter().setColSpan(5, 1, 5);

	
		
		FilesPanel = new HorizontalPanel();
		mainPanel.add(FilesPanel);
//		mainPanel.add(changeStudy);
		
		setProxy(proxy);
	}
	
	

	
	public boolean isFullMode() {
		return fullMode;
	}


	public void setFullMode(boolean fullMode) {
		
		if(this.fullMode==fullMode) return;
		
		this.fullMode = fullMode;
		if (fullMode)
			makeCommonPanel();
		else {
			commonTable.removeFromParent();
			FilesPanel.removeFromParent();
		}
	}

	/**
	 * создание панели с картинками и проч.
	 * 
	 * @param dcmImage
	 */
	private void makeFilesPanel() {
		
		FilesPanel.clear();
		
		for (Iterator<DcmFileProxy> it = proxy.getFiles().iterator(); it
				.hasNext();) {
			final DcmFileProxy fileProxy = it.next();

			
//			if(!fileProxy.haveImage()) {
				
				
//				String html = "файлов:" + studyProxy.getFiles().size() + " ";
				String html =  "<a href='" + "dcm/" + fileProxy.getId()
					+ ".dcm' target='new' title='"+fileProxy.getFileName()+"'> оригинал </a>"
					
					
					+ " :: <a href='" + "dcmtags/" + fileProxy.getId()
					+ ".dcm' target='new' title='"+fileProxy.getFileName()+"'> тэги </a>";
				
//				DCMFilesPanel.add(new HTML(html));
//				FilesPanel.add(new Button(fileProxy.getType()));
//				continue;
//			}
			
			
			
			VerticalPanel contentPanel = new VerticalPanel();
			contentPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			
			//TODO переделат на fileProxy.getType
			if(fileProxy.haveImage()) {
				Image imagePreview = makeItemImage(fileProxy);
				contentPanel.add(imagePreview);
			}else {
				
				ImageResource imgRes = resources.logoPDF();
				Image imageLogoTXT = new Image(imgRes);
				imageLogoTXT.addStyleName("Image");
				contentPanel.add(imageLogoTXT);
				
				String htmlAttach =  "<a href='" + "dcmattach/" + fileProxy.getId()
				+ ".dcm' target='new' title='"+fileProxy.getFileName()+"'> PDF </a>";
				contentPanel.add(new HTML(htmlAttach));
			}
			
			contentPanel.add(new HTML(html));
			
			
			
			FilesPanel.add(contentPanel);

		}
	}

	
	/**
	 * Информация о снимке исследования
	 * 
	 * @param fileProxy
	 * @return
	 */
	private Image makeItemImage(final DcmFileProxy fileProxy) {
		
		Image image = new Image("images/" + fileProxy.getId()+".100x100");
		image.addStyleName("Image");
		image.setTitle("Щелкните здесь чтобы увеличить изображение");

		Integer w = fileProxy.getImageWidth();
		Integer h = fileProxy.getImageHeight();

		final float k = w / h;
		int hNew = 100;
		int wNew = (int) (hNew / k);

		image.setHeight(hNew + "px");
		image.setWidth(wNew + "px");
		

		
		ClickHandler clickOpenHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final PopupPanel pGlass = new PopupPanel();
				// pGlass.setModal(true);
				pGlass.setGlassEnabled(true);
				pGlass.setAutoHideEnabled(true);

				VerticalPanel vp = new VerticalPanel();
				pGlass.add(vp);
				vp.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

				Label lTitle = new Label(proxy.getPatientName() + " ["
						+ proxy.getPatientBirthDateAsString(datePattern)
						+ "]" + " исследование от "
						+ proxy.getStudyDateAsString(datePattern));

				lTitle.setStyleName("DicomItemValue");

				vp.add(lTitle);
				
				final Image imageFull = new Image("images/" + fileProxy.getId()+".fullsize");
				imageFull.addStyleName("Image");
				imageFull.setTitle("Щелкните здесь чтобы закрыть изображение");

				int fullhNew = 600;
				int fullwNew = (int) (fullhNew / k);

				imageFull.setHeight(fullhNew + "px");
				imageFull.setWidth(fullwNew + "px");

				
				vp.add(imageFull);

				imageFull.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						pGlass.hide();
					}

				});

				HTML link = new HTML();
				link.setHTML("&nbsp;&nbsp;<a href='" + "images/"
						+ fileProxy.getId()+".fullsize"
						+ "' target='new'> Открыть в новом окне </a>");
				link.setStyleName("DicomItemName");
				vp.add(link);

				pGlass.show();
				pGlass.center();
			}

		};

		image.addClickHandler(clickOpenHandler);
		return image;
		
	}




	/**
	 * Задание данных для данной формы
	 * @param proxy
	 */
	 void setProxy(StudyProxy proxy) {

		this.proxy = proxy;
		
		String sex = proxy.getPatientSex();
		if ("M".equalsIgnoreCase(sex)) {
			sex = "М";
		} else if ("F".equalsIgnoreCase(sex)) {
			sex = "Ж";
		}
		String result = "норма";
		if(proxy.getStudyResult()!=null) {
			result=proxy.getStudyResult();
		}

		labelPatientName.setText(proxy.getPatientName() + " (" + sex + ") "
				+ proxy.getPatientBirthDateAsString(datePattern) + " - "+result+ " ("+proxy.getStudyDateAsString(datePattern)+")");
		
		//Установка оповещения неописанного исследования
		if(result==null || result.length()==0 || proxy.getStudyDescription() == null ||
				proxy.getStudyDescription().length() ==0 || proxy.getStudyViewprotocol() == null ||
				proxy.getStudyViewprotocol().length() == 0) {
			setWarningOfNotResult();
		}
		
		if(!fullMode) return;
		
		labelStudyDate.setText(proxy.getStudyDateAsString(datePattern));
		labelStudyViewProtocolDate.setText(proxy.getStudyViewprotocolDateAsString(datePattern));
		labelPatientId.setText(proxy.getPatientId());
		labelManufacturerModelName.setText(proxy.getManufacturerModelName());
		labelStudyDoctor.setText(proxy.getStudyDoctor());
		labelStudyOperator.setText(proxy.getStudyOperator());
		labelStudyType.setText(proxy.getStudyType());
		labelStudyDescription.setText(proxy.getStudyDescription());
		
		
		String resultStr = "";
		if(proxy.getStudyViewprotocolDate()!=null) {
			resultStr = proxy.getStudyViewprotocolDateAsString("dd.MM.yyyy");  
		}
		resultStr += " , "+ result;
		labelResult.setText(resultStr);
		
		labelStudyViewprotocol.setText(proxy.getStudyViewprotocol());
		
		
		
		//Панель и картинками и вложениями
		makeFilesPanel();
		
	}

	/**
	 * Установка режима показывающего что иследование не описано
	 */
	private void setWarningOfNotResult() {
		labelPatientName.addStyleName("DicomItemWarn");
	}



	/**
	 * @param t
	 * @param row
	 * @param col
	 * @param text
	 */
	private void createItemName(FlexTable t, int row, int col, String text) {
		Label l = new Label(text);
		l.setStyleName("DicomItemName");
		t.setWidget(row, col, l);
		t.getFlexCellFormatter().setAlignment(row, col,
				HorizontalPanel.ALIGN_RIGHT, HorizontalPanel.ALIGN_MIDDLE);
	}

	/**
	 * @param t
	 * @param row
	 * @param col
	 * @param text
	 */
	private Label createItemValue(FlexTable t, int row, int col, String text) {
		Label l = new Label(text);
		l.setStyleName("DicomItemValue");
		t.setWidget(row, col, l);
		t.getFlexCellFormatter().setAlignment(row, col,
				HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);
		return l;
	}
	
	protected void showTagsFromFile(final VerticalPanel vp) {

		DcmTagsRPCRequest req = new DcmTagsRPCRequest();
		req.setIdDcm(proxy.getId());

		vp.clear();
		vp.add(new Label("Загрузка..."));

		Dicom_browser.browserService.getDcmTagsFromFile(0, Dicom_browser.version, proxy.getId(),
				new AsyncCallback<ArrayList<DcmTagProxy>>() {

					@Override
					public void onFailure(Throwable caught) {
						vp.clear();
						vp.add(new Label("Ошибка полчения данных! " + caught));
					}

					@Override
					public void onSuccess(ArrayList<DcmTagProxy> result) {
						vp.clear();
						for (Iterator<DcmTagProxy> it = result.iterator(); it
								.hasNext();) {
							DcmTagProxy proxy = it.next();
							vp.add(new Label("" + proxy));
						}
					}
				});

	}
	




}
