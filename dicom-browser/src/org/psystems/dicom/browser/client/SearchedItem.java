package org.psystems.dicom.browser.client;

import java.util.Iterator;

import org.psystems.dicom.browser.client.proxy.DcmFileProxy;
import org.psystems.dicom.browser.client.proxy.DcmImageProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Виджет отображающий результат поиска исследования
 * 
 * @author dima_d
 * 
 */
public class SearchedItem extends Composite {

	private String datePattern = "dd.MM.yyyy";
	DcmFileProxy proxy = null;

	public SearchedItem(final DcmFileProxy proxy) {
		super();
		this.proxy = proxy;
		HorizontalPanel dcmItem = new HorizontalPanel();

		//
		HorizontalPanel dcmImage = new HorizontalPanel();

		FlexTable t = new FlexTable();
		t.setStyleName("SearchItem");
//		t.setBorderWidth(1);

		String sex = proxy.getPatientSex();
		if ("M".equalsIgnoreCase(sex)) {
			sex = "М";
		} else if ("F".equalsIgnoreCase(sex)) {
			sex = "Ж";
		}

		Label l = new Label(proxy.getPatientName() + " (" + sex + ") "
				+ proxy.getPatientBirthDateAsString(datePattern));
		l.setStyleName("DicomItem");

		t.setWidget(0, 0, l);
		t.getFlexCellFormatter().setColSpan(0, 0, 6);
		t.getFlexCellFormatter().setAlignment(0, 0,
				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_MIDDLE);

		t.setWidget(0, 1, dcmImage);
		t.getFlexCellFormatter().setAlignment(0, 0,
				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_MIDDLE);
		t.getFlexCellFormatter().setRowSpan(0, 1, 5);

		
		createItemName(t, 1, 0, "дата:");
		createItemValue(t, 1, 1, proxy.getStudyDateAsString(datePattern));

		createItemName(t, 1, 2, "исследование:");
		createItemValue(t, 1, 3, proxy.getStudyDateAsString(datePattern));

		createItemName(t, 1, 4, "код пациента:");
		createItemValue(t, 1, 5, proxy.getPatientId());
		
		createItemName(t, 2, 0, "аппарат:");
		createItemValue(t, 2, 1, "неизвестен");
		
		createItemName(t, 2, 2, "врач:");
		createItemValue(t, 2, 3, proxy.getStudyDoctor());

		createItemName(t, 2, 4, "оператор:");
		createItemValue(t, 2, 5, proxy.getStudyOperator());
		
	
		
		createItemName(t, 3, 0, "результат:");
		createItemValue(t, 3, 1, "неизвестен");
		t.getFlexCellFormatter().setColSpan(3, 1, 5);

	
		HTML linkDcm = new HTML();
		linkDcm.setHTML("<a href='" + "dcm/" + proxy.getId()
				+ "' target='new'> получить оригнальный DICOM-файл </a>");
		linkDcm.setStyleName("DicomItemName");

		t.setWidget(4, 0, linkDcm);
		t.getFlexCellFormatter().setColSpan(4, 0, 6);
		t.getFlexCellFormatter().setAlignment(4, 0,
				HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_MIDDLE);

		// t.setText(2, 2, "bottom-right corner");
		// t.setWidget(1, 0, new Button("Wide Button"));
		// t.getFlexCellFormatter().setColSpan(1, 0, 3);
		dcmItem.add(t);

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

			// PopupPanel pGlass1 = new PopupPanel();
			// pGlass1.setStyleName("ImageGlassPanel");
			// pGlass1.setModal(true);
			// pGlass1.setPopupPosition(100 , 100);
			// pGlass1.show();

			// image.setSize("150px", "150px");
			// System.out.println("!!! image SIZE: "
			// + image.getWidth() + ";"
			// + image.getHeight());

			// VerticalPanel vp = new VerticalPanel();
			dcmImage.add(image);

			// vp.add(image);

			final Image imageFull = new Image("images/" + imageProxy.getId());
			imageFull.addStyleName("Image");
			imageFull.setTitle("Щелкните здесь чтобы закрыть изображение");

			hNew = 600;
			wNew = (int) (hNew / k);

			imageFull.setHeight(hNew + "px");
			imageFull.setWidth(wNew + "px");

			// HorizontalPanel hp = new HorizontalPanel();
			// vp.add(hp);

			ClickHandler clickOpenHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					final PopupPanel pGlass = new PopupPanel();
					pGlass.setStyleName("GlassPanel");
					pGlass.setModal(true);
					pGlass.show();

					final DialogBox db = new DialogBox();
					db.setModal(true);
					db.setAutoHideEnabled(true);
					db.setTitle("Увеличенное изображение");

					db.setText(proxy.getPatientName() + " ["
							+ proxy.getPatientBirthDateAsString(datePattern)
							+ "]" + " исследование от "
							+ proxy.getStudyDateAsString(datePattern));

					db.addCloseHandler(new CloseHandler<PopupPanel>() {

						@Override
						public void onClose(CloseEvent<PopupPanel> event) {
							pGlass.hide();
						}

					});

					VerticalPanel vp = new VerticalPanel();
					db.add(vp);
					vp.add(imageFull);

					imageFull.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							pGlass.hide();
							db.hide();
						}

					});

					HTML link = new HTML();
					link.setHTML("&nbsp;&nbsp;<a href='" + "images/"
							+ imageProxy.getId()
							+ "' target='new'> Открыть в новом окне </a>");
					link.setStyleName("DicomItemName");
					vp.add(link);

					db.show();
					db.center();
				}

			};

			image.addClickHandler(clickOpenHandler);

		}

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

}
