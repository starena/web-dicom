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

/**
 * Виджет отображающий результат поиска исследования
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
		// t.setBorderWidth(2);

		String sex = proxy.getPatientSex();
		if ("M".equalsIgnoreCase(sex)) {
			sex = "Муж.";
		} else if ("F".equalsIgnoreCase(sex)) {
			sex = "Жен.";
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

		l = new Label("Дата исследования:");
		l.setStyleName("DicomItemName");
		t.setWidget(1, 0, l);
		l = new Label("" + proxy.getStudyDateAsString(datePattern));
		l.setStyleName("DicomItemValue");
		t.setWidget(1, 1, l);

		l = new Label("ID исследования:");
		l.setStyleName("DicomItemName");
		t.setWidget(1, 2, l);
		l = new Label(proxy.getStudyId());
		l.setStyleName("DicomItemValue");
		t.setWidget(1, 3, l);

		l = new Label("ID пациента:");
		l.setStyleName("DicomItemName");
		t.setWidget(1, 4, l);
		l = new Label(proxy.getPatientId());
		l.setStyleName("DicomItemValue");
		t.setWidget(1, 5, l);

		l = new Label("Врач:");
		l.setStyleName("DicomItemName");
		t.setWidget(2, 0, l);
		t.getFlexCellFormatter().setAlignment(2, 0,
				HorizontalPanel.ALIGN_RIGHT, HorizontalPanel.ALIGN_MIDDLE);
		l = new Label(proxy.getStudyDoctor());
		l.setStyleName("DicomItemValue");
		t.setWidget(2, 1, l);

		l = new Label("Оператор:");
		l.setStyleName("DicomItemName");
		t.setWidget(2, 2, l);
		t.getFlexCellFormatter().setAlignment(2, 2,
				HorizontalPanel.ALIGN_RIGHT, HorizontalPanel.ALIGN_MIDDLE);
		l = new Label(proxy.getStudyOperator());
		l.setStyleName("DicomItemValue");
		t.setWidget(2, 3, l);

		l = new Label("Аппарат:");
		l.setStyleName("DicomItemName");
		t.setWidget(2, 4, l);
		t.getFlexCellFormatter().setAlignment(2, 4,
				HorizontalPanel.ALIGN_RIGHT, HorizontalPanel.ALIGN_MIDDLE);
		l = new Label("неизвестен");
		l.setStyleName("DicomItemValue");
		t.setWidget(2, 5, l);

		l = new Label("Результаты:");
		l.setStyleName("DicomItemName");
		t.setWidget(3, 0, l);
		t.getFlexCellFormatter().setAlignment(3, 0,
				HorizontalPanel.ALIGN_RIGHT, HorizontalPanel.ALIGN_MIDDLE);

		l = new Label("нет данных");
		l.setStyleName("DicomItemValue");
		t.setWidget(3, 1, l);
		t.getFlexCellFormatter().setColSpan(3, 1, 5);

		HTML linkDcm = new HTML();
		linkDcm.setHTML("<a href='" + "dcm/" + proxy.getId()
				+ "' target='new'> получить оригнальный DCM-файл </a>");
		linkDcm.setStyleName("DicomItemName");

		t.setWidget(4, 0, linkDcm);
		t.getFlexCellFormatter().setColSpan(4, 0, 6);

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

}
