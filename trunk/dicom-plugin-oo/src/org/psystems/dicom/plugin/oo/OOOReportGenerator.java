package org.psystems.dicom.plugin.oo;

import java.util.HashMap;

import javax.swing.JOptionPane;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XDispatchHelper;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XRefreshable;
import com.sun.star.util.XURLTransformer;
import com.sun.star.view.XPrintable;

public class OOOReportGenerator {

	private static XComponentContext xRemoteContext = null;
	private static XMultiComponentFactory xRemoteServiceManager = null;
	private static XURLTransformer xTransformer = null;
	private static XComponentLoader xComponentLoader = null;
	private static XDesktop xDesktop = null;

	public OOOReportGenerator() {
	}

	/*
	 * соедиенение с экземпляром OpenOffice.org и инициализация нужных
	 * переменных
	 */
	public static void connect() throws Exception {
		// получим контекст удаленного компонента офиса
		xRemoteContext = com.sun.star.comp.helper.Bootstrap.bootstrap();
		xRemoteServiceManager = xRemoteContext.getServiceManager();

		// создадим сервис, который понадобится при печати
		Object transformer = xRemoteServiceManager.createInstanceWithContext(
				"com.sun.star.util.URLTransformer", xRemoteContext);
		xTransformer = (XURLTransformer) UnoRuntime.queryInterface(
				XURLTransformer.class, transformer);

		// получим сервис Desktop
		Object desktop = (XInterface) xRemoteServiceManager
				.createInstanceWithContext("com.sun.star.frame.Desktop",
						xRemoteContext);
		xDesktop = (XDesktop) UnoRuntime
				.queryInterface(XDesktop.class, desktop);

		// данный интерфейс позволяет загружать и сохранять документы
		xComponentLoader = (XComponentLoader) UnoRuntime.queryInterface(
				XComponentLoader.class, desktop);
	}

	/*
	 * Открыть документ, на который указывает sURL для простоты предполагаем,
	 * что sURL - путь к файлу
	 */
	public static XComponent openDocument(String sURL) throws Exception {
		// преобразуем путь к файлу в URL
		java.io.File sourceFile = new java.io.File(sURL);
		StringBuffer sTmp = new StringBuffer("file:///");
		sTmp.append(sourceFile.getCanonicalPath().replace('\\', '/'));
		sURL = sTmp.toString();

		PropertyValue[] loadProps = new PropertyValue[0];
		return xComponentLoader.loadComponentFromURL(sURL, "_blank", 0,
				loadProps);
	}

	/*
	 * Закрыть документ, если askIfVetoed=true, то в случае попытки закрытия
	 * документа с несохраненными изменениями будет выдан соответствующий запрос
	 */
	public static boolean closeDocument(XComponent comp, boolean askIfVetoed) {
		XCloseable c = (XCloseable) UnoRuntime.queryInterface(XCloseable.class,
				comp);
		boolean dispose = true;
		try {
			c.close(false);
		} catch (com.sun.star.util.CloseVetoException e) {
			if (askIfVetoed) {
				int action = JOptionPane.showConfirmDialog(null,
						"Есть несохраненные изменения!"
								+ "Действительно хотите закрыть?", "Внимание",
						JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
				if (JOptionPane.NO_OPTION == action) {
					dispose = false;
				}
			}
			if (dispose) {
				comp.dispose();
			}
		}
		return dispose;
	}

	/*
	 * Распечатать документ на принтере по умолчанию
	 */
	public static void printDocument(XComponent comp)
			throws com.sun.star.lang.IllegalArgumentException {
		XPrintable xPrintable = (XPrintable) UnoRuntime.queryInterface(
				XPrintable.class, comp);
		PropertyValue[] printOpts = new PropertyValue[0];
		xPrintable.print(printOpts);
	}

	/*
	 * Сохранить документ под текущим именем
	 */
	public static void saveDocument(XComponent comp, PropertyValue[] props) {
		XStorable store = (XStorable) UnoRuntime.queryInterface(
				XStorable.class, comp);
		saveAsDocument(comp, store.getLocation(), props);
	}

	/*
	 * Сохранить документ под именем, на которое указывает aURL
	 */
	public static void saveAsDocument(XComponent comp, String aURL,
			PropertyValue[] props) {
		XStorable store = (XStorable) UnoRuntime.queryInterface(
				XStorable.class, comp);
		try {
			store.storeToURL(aURL, props);
		} catch (Exception e) {
			System.out.println("Не могу сохранить файл!" + e);
		}
	}

	/*
	 * Выполнить команды с помощью механизма обработчика команд
	 */
	public static void executeCommands(String[] commands)
			throws com.sun.star.uno.Exception {
		// Получаем текущий фрейм
		XFrame xFrame = xDesktop.getCurrentFrame();
		// Находим соответствующий DispatchProvider.
		XDispatchProvider xDispatchProvider = (XDispatchProvider) UnoRuntime
				.queryInterface(XDispatchProvider.class, xFrame);
		for (int n = 0; n < commands.length; n++) {
			// Подготовим URL
			com.sun.star.util.URL[] aURL = new com.sun.star.util.URL[1];
			aURL[0] = new com.sun.star.util.URL();
			com.sun.star.frame.XDispatch xDispatch = null;

			aURL[0].Complete = ".uno:" + commands[n];
			xTransformer.parseSmart(aURL, ".uno:");

			// Попытаемся получить обработчик для полученного URL
			xDispatch = xDispatchProvider.queryDispatch(aURL[0], "", 0);
			if (xDispatch != null) {
				com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[0];
				Object obj = xRemoteServiceManager.createInstanceWithContext(
						"com.sun.star.frame.DispatchHelper", xRemoteContext);
				XDispatchHelper dh = (XDispatchHelper) UnoRuntime
						.queryInterface(XDispatchHelper.class, obj);
				dh.executeDispatch(xDispatchProvider, aURL[0].Complete, "", 0,
						lParams);
			} else {
				System.out.println("Не найден обработчик для "
						+ aURL[0].Complete);
			}
		}
	}

	public static void main(String[] args) {
		HashMap<String, String> variableMap = new HashMap<String, String>();

//		if(args.length==0) {
//			System.err.println("set template file name: sample/sample1.odt");
//			System.exit(-1);
//		}
		
//		variableMap.put("CONTRACT_NUMBER", "1234567");
//		variableMap.put("CONTRACT_DATE", "31 декабря 2007 г.");
//		variableMap.put("EXECUTOR", "ООО \"Рога и Копыта\"");
//		variableMap.put("EXECUTOR_PERSON", "Потыкайло Петра Сергеевича");
//		variableMap.put("CUSTOMER", "Пупкин Василий Иванович");
		

		variableMap.put("PatientName", "DDV");
		variableMap.put("StudyUID", "12345");
		
		try {
			connect();

			
			XComponent currentDocument = openDocument("sample/sample1.odt");
//			XComponent currentDocument = openDocument(args[0]);

			XTextFieldsSupplier xTextFieldsSupplier = (XTextFieldsSupplier) UnoRuntime
					.queryInterface(XTextFieldsSupplier.class, currentDocument);

			// Создадим перечисление всех полей документа
			XEnumerationAccess xEnumerationAccess = xTextFieldsSupplier
					.getTextFields();
			XEnumeration xTextFieldsEnumeration = xEnumerationAccess
					.createEnumeration();
			XRefreshable xRefreshable = (XRefreshable) UnoRuntime
					.queryInterface(XRefreshable.class, xEnumerationAccess);

			while (xTextFieldsEnumeration.hasMoreElements()) {
				Object service = xTextFieldsEnumeration.nextElement();
				XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime
						.queryInterface(XServiceInfo.class, service);

				if (xServiceInfo
						.supportsService("com.sun.star.text.TextField.SetExpression")) {
					XPropertySet xPropertySet = (XPropertySet) UnoRuntime
							.queryInterface(XPropertySet.class, service);
					String name = (String) xPropertySet
							.getPropertyValue("VariableName");
					Object content = variableMap.get(name);
					xPropertySet.setPropertyValue("SubType", new Short(
							com.sun.star.text.SetVariableType.STRING));
					xPropertySet.setPropertyValue("Content",
							content == null ? " " : content.toString());
					xPropertySet.setPropertyValue("IsVisible", true);
				}
			}
			xRefreshable.refresh();

			String[] cmds = { "Print" };
			executeCommands(cmds);
			/*
			 * можно еще printDocument(currentDocument) - распечатать на
			 * принтере по умолчанию saveDocument (currentDocument, props) -
			 * сохранить текущий документ saveAsDocument (currentDocument, sURL,
			 * props) - сохранить под новым именем
			 */

			closeDocument(currentDocument, false);
			XCloseable c = (XCloseable) UnoRuntime.queryInterface(
					XCloseable.class, currentDocument);
			while (true) {
				try {
					c.close(false);
					break;
				} catch (com.sun.star.util.CloseVetoException e) {
				}
				Thread.sleep(200);
			}
		} catch (Exception e) {
			System.out.println("Ошибка печати документа ");
			e.printStackTrace();
		}
	}
}
