package org.psystems.test;

import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XComponent;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.PropertyValue;
import com.sun.star.sheet.CellFlags;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetView;
import com.sun.star.sheet.XCellRangesQuery;
import com.sun.star.sheet.XSheetCellRanges;
import com.sun.star.sheet.XCellAddressable;
import com.sun.star.table.CellVertJustify;
import com.sun.star.table.XCell;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XController;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;

public class FirstLoadComponent {
	public FirstLoadComponent() {
		// пустой конструктор
	}

	// глобальные переменные — компонентный контекст
	private XComponentContext xRemouteContext = null;
	// и менеджер сервисов
	private XMultiComponentFactory xRemouteServiceManager = null;

	public static void main(String[] args) {
		// сначала создаем экземпляр этого приложения
		FirstLoadComponent firstLoadComponent1 = new FirstLoadComponent();
		try {
			// пытаемся установить соединение
			firstLoadComponent1.useConnection();
		} catch (Exception e) {
			// при неудаче – вывод стека вызовов
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// в любом случае – выход без ошибки
			System.exit(0);
		}
	}

	private void useConnection() throws Exception {
		// основная процедура приложения, условно разделенноая на две части
		//
		// первая часть – соединение с офисом,
		// вторая же выполняет всю работу
		//
		// почему авторы примера объединили
		// две совершенно разные процедуры в одну – непонятно,
		// но оставим как есть
		//
		// первая часть
		// вот так пытаемся соединиться с экземпляром офиса
		try {
			// создаем экземпляр удаленного компонентного контекста
			xRemouteContext = com.sun.star.comp.helper.Bootstrap.bootstrap();
			System.out.println("соединяемся с работающим Office...");
			// и получаем от него экземпляр менеджера сервисов
			xRemouteServiceManager = xRemouteContext.getServiceManager();
		} catch (Exception e) {
			// при неудаче – как обычно
			e.printStackTrace();
			System.exit(1);
		}
		// вторая часть
		try {
			// создаем экземпляр объекта рабочего стола --
			// основного объекта офисного приложения
			Object desktop = xRemouteServiceManager.createInstanceWithContext(
					"com.sun.star.frame.Desktop", xRemouteContext);
			// приведением типов получаем объект загрузчика компонентов
			XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
					.queryInterface(XComponentLoader.class, desktop);
			// объект-набор свойств (одно свойство в наборе)
			PropertyValue[] loadProps = new PropertyValue[0];
			// компонент-лист
			XComponent xSpreadsheetComponent = xComponentLoader
					.loadComponentFromURL("private:factory/scalc", "_blank", 0,
							loadProps);
			// а это – документ-лист (образуется запросом интерфейса и
			// приведением типа)
			XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
					.queryInterface(XSpreadsheetDocument.class,
							xSpreadsheetComponent);
			// мз документа получаем набор листов – пока пустой
			XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
			// вставка нового листа с заданным именем
			xSpreadsheets.insertNewByName("Новый лист", (short) 0);
			// получаем тип элемента набора листов
			// (т.к. UNO заранее об этом ничего не знает)
			com.sun.star.uno.Type elemType = xSpreadsheets.getElementType();
			// debug-сообщение – вывод имени типа
			System.out.println(elemType.getTypeName());
			// наконец получаем объект-лист типа Object
			Object sheet = xSpreadsheets.getByName("Новый лист");
			// и приводим его к нужному типу

			XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime
					.queryInterface(XSpreadsheet.class, sheet);
			// получаем ячейку листа с координатами 0,0
			XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
			// пишем в нее значение 21
			xCell.setValue(21);
			// то же для следующей ячейки
			xCell = xSpreadsheet.getCellByPosition(0, 1);
			xCell.setValue(21);
			// а в следующую – пишем формулу
			xCell = xSpreadsheet.getCellByPosition(0, 2);
			xCell.setFormula("=sum(A1:A2");
			// набор свойств ячейки (обратите внимание, что работаем
			// с последней активной ячейкой – той, в которой формула
			XPropertySet xCellProps = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, xCell);
			// устанавливаем свойство стиля ячейки
			xCellProps.setPropertyValue("CellStyle", "Result");
			// объект-модель листа
			// получаем приведением типа запрошенного от компонента-листа
			// интерфейса
			XModel xSpreadsheetModel = (XModel) UnoRuntime.queryInterface(
					XModel.class, xSpreadsheetComponent);
			// объект-контроллер листа
			// получаем get-методом от объекта-модели
			XController xSpreadsheetController = xSpreadsheetModel
					.getCurrentController();
			// объект-вид листа
			// приводим тип интерфейса объекта-контроллера
			XSpreadsheetView xSpreadsheetView = (XSpreadsheetView) UnoRuntime
					.queryInterface(XSpreadsheetView.class,
							xSpreadsheetController);
			// созданный новый листа делаем активным при помощи
			// объекта-вида
			xSpreadsheetView.setActiveSheet(xSpreadsheet);
			// установка свойства вертикального выравнивания ячейки
			// (той, где формула)
			xCellProps.setPropertyValue("VertJustify", CellVertJustify.TOP);
			// пересоздаем набор свойств (два свойства в наборе)
			loadProps = new PropertyValue[1];
			// объект-свойство
			PropertyValue asTemplate = new PropertyValue();
			// инициализируем его
			asTemplate.Name = "AsTemplate";
			asTemplate.Value = new Boolean(true);
			// и заносим в набор свойств
			loadProps[0] = asTemplate;
			// получаем от листа объект-диапазон ячеек
			XCellRangesQuery xCellQuery = (XCellRangesQuery) UnoRuntime
					.queryInterface(XCellRangesQuery.class, sheet);
			// и получаем набор ячеек с формулами
			XSheetCellRanges xFormulaCells = xCellQuery
					.queryContentCells((short) CellFlags.FORMULA);
			// ячейки этого набора приводим к виду набора с типом доступа –
			// перечисление
			XEnumerationAccess xFormulas = xFormulaCells.getCells();
			// создаем из полученного набора перечисление

			XEnumeration xFormulaEnum = xFormulas.createEnumeration();
			// в цикле, пока не закончатся элементы в перечислении
			while (xFormulaEnum.hasMoreElements()) {
				// получаем очередной элемент в виде Object
				Object formulaCell = xFormulaEnum.nextElement();
				// приводим его к типу ячейки
				xCell = (XCell) UnoRuntime.queryInterface(XCell.class,
						formulaCell);
				// и к типу адресуемой ячейки – чтобы получить ее координаты
				XCellAddressable xCellAddress = (XCellAddressable) UnoRuntime
						.queryInterface(XCellAddressable.class, xCell);
				// выводим координаты ячейки с формулой и ее содержимое
				System.out.println("Ячейке в строке "
						+ xCellAddress.getCellAddress().Column + ", ряду "
						+ xCellAddress.getCellAddress().Row
						+ " содержит формулу " + xCell.getFormula());
			}
		} catch (DisposedException e) {
			// при неудчае – обнуляем удаленный компонентный контекст
			xRemouteContext = null;
			// и передаем исключение «родителям»
			throw e;
		}
	}
}