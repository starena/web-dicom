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
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCResponse;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.browser.client.service.BrowserServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Карточка Dicom исследования
 * 
 * @author dima_d
 * 
 */
public class StudyCardPanel extends Composite {

	private String datePattern = "dd.MM.yyyy";
	private BrowserServiceAsync service;
	StudyProxy proxy = null;

	public StudyCardPanel(BrowserServiceAsync service, final StudyProxy proxy) {
		super();
		this.service = service;
		this.proxy = proxy;
		
		
		HorizontalPanel dcmItem = new HorizontalPanel();
		//TODO Перенести в css
		DOM.setStyleAttribute(dcmItem.getElement(), "background", "white");

		//
		HorizontalPanel dcmImage = new HorizontalPanel();
		dcmImage.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);

		final FlexTable table = new FlexTable();
		table.setStyleName("SearchItem");
//		 table.setBorderWidth(1);

		String sex = proxy.getPatientSex();
		if ("M".equalsIgnoreCase(sex)) {
			sex = "М";
		} else if ("F".equalsIgnoreCase(sex)) {
			sex = "Ж";
		}

		Label l = new Label(proxy.getPatientName() + " (" + sex + ") "
				+ proxy.getPatientBirthDateAsString(datePattern));
		l.setStyleName("DicomItem");

		table.setWidget(0, 0, l);
		table.getFlexCellFormatter().setColSpan(0, 0, 6);
		table.getFlexCellFormatter().setAlignment(0, 0,
				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_MIDDLE);
		
		
		

		table.setWidget(0, 1, dcmImage);
		table.getFlexCellFormatter().setAlignment(0, 0,
				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_TOP);
		table.getFlexCellFormatter().setRowSpan(0, 1, 5);

		createItemName(table, 1, 0, "дата:");
		createItemValue(table, 1, 1, proxy.getStudyDateAsString(datePattern));

		createItemName(table, 1, 2, "исследование:");
		createItemValue(table, 1, 3, proxy.getStudyDateAsString(datePattern));

		createItemName(table, 1, 4, "код пациента:");
		createItemValue(table, 1, 5, proxy.getPatientId());

		createItemName(table, 2, 0, "аппарат:");
		createItemValue(table, 2, 1,  proxy.getManufacturerModelName());

		createItemName(table, 2, 2, "врач:");
		createItemValue(table, 2, 3, proxy.getStudyDoctor());

		createItemName(table, 2, 4, "лаборант:");
		createItemValue(table, 2, 5, proxy.getStudyOperator());

		createItemName(table, 3, 0, "Тип исследования:");
		createItemValue(table, 3, 1, proxy.getStudyType());
//		table.getFlexCellFormatter().setColSpan(3, 1, 5);
		
		createItemName(table, 3, 2, "Описание:");
		createItemValue(table, 3, 3, proxy.getStudyDescription());
		table.getFlexCellFormatter().setColSpan(3, 3, 3);
		
		createItemName(table, 4, 0, "результат:");
		String result = "норма";
		if(proxy.getStudyResult()!=null && proxy.getStudyResult().length()>0) {
			result=proxy.getStudyResult();
		}
		createItemValue(table, 4, 1, proxy.getStudyDateAsString("dd.MM.yyyy")+" , " + result);
		table.getFlexCellFormatter().setColSpan(4, 1, 5);
		
		createItemName(table, 5, 0, "Протокол осмотра:");
		createItemValue(table, 5, 1, proxy.getStudyViewprotocol());
		table.getFlexCellFormatter().setColSpan(5, 1, 5);

		HTML linkDcm = createfileItems(proxy);

		linkDcm.setStyleName("DicomItemName");

		table.setWidget(6, 0, linkDcm);
		table.getFlexCellFormatter().setColSpan(6, 0, 6);
		table.getFlexCellFormatter().setAlignment(6, 0,
				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_MIDDLE);

		final Button changeStudy = new Button("изменить...");
		changeStudy.setStyleName("DicomItem");
		table.setWidget(7, 0, changeStudy);
		table.getFlexCellFormatter().setColSpan(7, 0, 6);
		table.getFlexCellFormatter().setAlignment(7, 0,
				HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);
		
		changeStudy.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				table.remove(changeStudy);
				StudyManagePanel panel = new StudyManagePanel(Dicom_browser.manageStudyService, proxy);
				table.setWidget(8, 0, panel);
				table.getFlexCellFormatter().setColSpan(8, 0, 6);
				table.getFlexCellFormatter().setAlignment(8, 0,
						HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);
			}
		});
		
		
	
		dcmItem.add(table);
		
		
		
		
		
		////////////////////////////

		for (Iterator<DcmFileProxy> it = proxy.getFiles().iterator(); it
				.hasNext();) {
			final DcmFileProxy fileProxy = it.next();

			
			if(!fileProxy.haveImage()) continue;
			
			Image image = new Image("images/" + fileProxy.getId()+".100x100");
			image.addStyleName("Image");
			image.setTitle("Щелкните здесь чтобы увеличить изображение");

			Integer w = fileProxy.getImageWidth();
			Integer h = fileProxy.getImageHeight();

			float k = w / h;
			int hNew = 100;
			int wNew = (int) (hNew / k);

			image.setHeight(hNew + "px");
			image.setWidth(wNew + "px");
			dcmImage.add(image);

			final Image imageFull = new Image("images/" + fileProxy.getId()+".fullsize");
			imageFull.addStyleName("Image");
			imageFull.setTitle("Щелкните здесь чтобы закрыть изображение");

			hNew = 600;
			wNew = (int) (hNew / k);

			imageFull.setHeight(hNew + "px");
			imageFull.setWidth(wNew + "px");

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

		}
		
		

		initWidget(dcmItem);

	}

	private HTML createfileItems(final StudyProxy studyProxy) {
		HTML linkDcm = new HTML();
		String html = "файлов:" + studyProxy.getFiles().size() + " ";
		for(Iterator<DcmFileProxy> iter = studyProxy.getFiles().iterator(); iter.hasNext();) {
			DcmFileProxy fileProxy = iter.next();
			html += "<a href='" + "dcm/" + fileProxy.getId()
			+ ".dcm' target='new' title='"+fileProxy.getFileName()+"'> [оригинал </a>"
			+ "- <a href='" + "dcmtags/" + fileProxy.getId()
			+ ".dcm' target='new' title='"+fileProxy.getFileName()+"'> тэги] </a>";
		}
		
		linkDcm.setHTML(html);
		return linkDcm;
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
	private void createItemValue(FlexTable t, int row, int col, String text) {
		Label l = new Label(text);
		l.setStyleName("DicomItemValue");
		t.setWidget(row, col, l);
		t.getFlexCellFormatter().setAlignment(row, col,
				HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);
	}
	
	/**
	 * @param t
	 * @param row
	 * @param col
	 * @param html
	 */
	private void createItemHTML(FlexTable t, int row, int col, String html) {
		HTML h = new HTML(html);
//		h.setStyleName("DicomItemValue");
		t.setWidget(row, col, h);
		t.getFlexCellFormatter().setAlignment(row, col,
				HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);
	}

	/**
	 * @param vp
	 */
	private void showTagsFromDb(final VerticalPanel vp) {

		DcmTagsRPCRequest req = new DcmTagsRPCRequest();
		req.setIdDcm(proxy.getId());

		vp.clear();
		vp.add(new Label("Загрузка..."));

		service.getDcmTags(req, new AsyncCallback<DcmTagsRPCResponse>() {

			@Override
			public void onFailure(Throwable caught) {
				vp.clear();
				vp.add(new Label("Ошибка полчения данных! " + caught));
			}

			public void onSuccess(DcmTagsRPCResponse result) {
				ArrayList<DcmTagProxy> a = result.getTagList();
				vp.clear();
				for (Iterator<DcmTagProxy> it = a.iterator(); it.hasNext();) {
					DcmTagProxy proxy = it.next();
					vp.add(new Label("" + proxy));
				}
			}

		});

	}

	protected void showTagsFromFile(final VerticalPanel vp) {

		DcmTagsRPCRequest req = new DcmTagsRPCRequest();
		req.setIdDcm(proxy.getId());

		vp.clear();
		vp.add(new Label("Загрузка..."));

		service.getDcmTagsFromFile(0, Dicom_browser.version, proxy.getId(),
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
