package org.psystems.dicom.browser.client;

import java.util.ArrayList;
import java.util.Iterator;

import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.DcmImageProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCRequest;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCResponce;
import org.psystems.dicom.browser.client.proxy.RPCDcmFileProxyEvent;
import org.psystems.dicom.browser.client.proxy.RPCRequestEvent;
import org.psystems.dicom.browser.client.proxy.RPCResponceEvent;
import org.psystems.dicom.browser.client.service.BrowserServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Виджет отображающий результат поиска исследования
 * 
 * @author dima_d
 * 
 */
public class SearchedItem extends Composite {

	private String datePattern = "dd.MM.yyyy";
	private BrowserServiceAsync service;
	DcmFileProxy proxy = null;

	public SearchedItem(BrowserServiceAsync service, final DcmFileProxy proxy) {
		super();
		this.service = service;
		this.proxy = proxy;

		HorizontalPanel dcmItem = new HorizontalPanel();

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
		createItemValue(table, 2, 1, "неизвестен");

		createItemName(table, 2, 2, "врач:");
		createItemValue(table, 2, 3, proxy.getStudyDoctor());

		createItemName(table, 2, 4, "оператор:");
		createItemValue(table, 2, 5, proxy.getStudyOperator());

		createItemName(table, 3, 0, "результат:");
		createItemValue(table, 3, 1, proxy.getStudyDescription());
		table.getFlexCellFormatter().setColSpan(3, 1, 5);

		HTML linkDcm = new HTML();
		linkDcm.setHTML("<a href='" + "dcm/" + proxy.getId()
				+ ".dcm' target='new'> получить оригнальный DICOM-файл </a>"
				+ "<a href='" + "dcmtags/" + proxy.getId()
				+ ".dcm' target='new'> показать тэги </a>");

		linkDcm.setStyleName("DicomItemName");

		table.setWidget(4, 0, linkDcm);
		table.getFlexCellFormatter().setColSpan(4, 0, 6);
		table.getFlexCellFormatter().setAlignment(4, 0,
				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_MIDDLE);

		// t.setText(2, 2, "bottom-right corner");
		// t.setWidget(1, 0, new Button("Wide Button"));
		// t.getFlexCellFormatter().setColSpan(1, 0, 3);
		dcmItem.add(table);

		for (Iterator<DcmImageProxy> it = proxy.getImagesIds().iterator(); it
				.hasNext();) {
			final DcmImageProxy imageProxy = it.next();

			Image image = new Image("images/" + imageProxy.getId());
			image.addStyleName("Image");
			image.setTitle("Щелкните здесь чтобы увеличить изображение");

			Integer w = imageProxy.getWidth();
			Integer h = imageProxy.getHeight();

			float k = w / h;
			int hNew = 100;
			int wNew = (int) (hNew / k);

			image.setHeight(hNew + "px");
			image.setWidth(wNew + "px");
			dcmImage.add(image);

			final Image imageFull = new Image("images/" + imageProxy.getId());
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
							+ imageProxy.getId()
							+ "' target='new'> Открыть в новом окне </a>");
					link.setStyleName("DicomItemName");
					vp.add(link);

					pGlass.show();
					pGlass.center();
				}

			};

			image.addClickHandler(clickOpenHandler);

		}
		
		
		HorizontalPanel tagsPanel = new HorizontalPanel();
		table.setWidget(5, 0, tagsPanel);
		table.getFlexCellFormatter().setColSpan(5, 0, 6);
		table.getFlexCellFormatter().setColSpan(4, 0, 6);
		table.getFlexCellFormatter().setAlignment(4, 0,
				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_TOP);

		final VerticalPanel vp = new VerticalPanel();
		tagsPanel.add(vp);
		

		Label showTagsLabelfromDB = new Label("Показать теги из БД");
		showTagsLabelfromDB.setStyleName("DicomItemValue");
		DOM.setStyleAttribute(showTagsLabelfromDB.getElement(), "cursor",
				"pointer");
		vp.add(showTagsLabelfromDB);

		final VerticalPanel vp1 = new VerticalPanel();
		tagsPanel.add(vp1);
		

		showTagsLabelfromDB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showTagsFromDb(vp);
			}

		});

		Label showTagsLabelFromFile = new Label("Показать теги из файла");
		showTagsLabelFromFile.setStyleName("DicomItemValue");
		DOM.setStyleAttribute(showTagsLabelFromFile.getElement(), "cursor",
				"pointer");
		vp1.add(showTagsLabelFromFile);

		showTagsLabelFromFile.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				showTagsFromFile(vp1);
			}

		});

		// All composites must call initWidget() in their constructors.
		initWidget(dcmItem);

	}

	/**
	 * @param t
	 * @param row
	 * @param col
	 * @param title
	 */
	private void createItemName(FlexTable t, int row, int col, String title) {
		Label l = new Label(title);
		l.setStyleName("DicomItemName");
		t.setWidget(row, col, l);
		t.getFlexCellFormatter().setAlignment(row, col,
				HorizontalPanel.ALIGN_RIGHT, HorizontalPanel.ALIGN_MIDDLE);
	}

	/**
	 * @param t
	 * @param row
	 * @param col
	 * @param title
	 */
	private void createItemValue(FlexTable t, int row, int col, String title) {
		Label l = new Label(title);
		l.setStyleName("DicomItemValue");
		t.setWidget(row, col, l);
		t.getFlexCellFormatter().setAlignment(row, col,
				HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);
	}

	/**
	 * @param vp
	 */
	private void showTagsFromDb(final VerticalPanel vp) {

		RPCRequestEvent requestEvent = new RPCRequestEvent();
		DcmTagsRPCRequest req = new DcmTagsRPCRequest();
		req.setIdDcm(proxy.getId());

		requestEvent.init(0, Dicom_browser.version, req);

		vp.clear();
		vp.add(new Label("Загрузка..."));

		service.getDcmTags(requestEvent, new AsyncCallback<RPCResponceEvent>() {

			@Override
			public void onFailure(Throwable caught) {
				vp.clear();
				vp.add(new Label("Ошибка полчения данных! " + caught));
			}

			public void onSuccess(RPCResponceEvent result) {
				// TODO Auto-generated method stub
				DcmTagsRPCResponce data = (DcmTagsRPCResponce) result.getData();
				ArrayList<DcmTagProxy> a = data.getTagList();
				vp.clear();
				for (Iterator<DcmTagProxy> it = a.iterator(); it.hasNext();) {
					DcmTagProxy proxy = it.next();
					// System.out.println(proxy);
					vp.add(new Label("" + proxy));
				}
			}

		});

	}

	protected void showTagsFromFile(final VerticalPanel vp) {

		RPCRequestEvent requestEvent = new RPCRequestEvent();
		DcmTagsRPCRequest req = new DcmTagsRPCRequest();
		req.setIdDcm(proxy.getId());

		requestEvent.init(0, Dicom_browser.version, req);

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
