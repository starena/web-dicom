package org.psystems.test;

//import sun.security.action.GetBooleanAction;
import com.sun.star.lang.XComponent; //import com.sun.star.uno.Exception;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.UnoRuntime; //import com.sun.star.uno.AnyConverter;
//import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.beans.XPropertySet; //import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.beans.PropertyValue; //import com.sun.star.beans.UnknownPropertyException;
//import com.sun.star.beans.PropertyVetoException;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor; //import com.sun.star.text.XWordCursor;
import com.sun.star.text.XTextContent; //import com.sun.star.text.XTextTable;
//import com.sun.star.text.XTextTableCursor;
//import com.sun.star.table.XTableRows;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XCell; //import com.sun.star.table.XCellCursor;
import com.sun.star.table.TableBorder;
import com.sun.star.table.BorderLine;
import com.sun.star.drawing.XShape;
import com.sun.star.awt.FontSlant;
import com.sun.star.awt.FontWeight;
import com.sun.star.awt.Size;
import com.sun.star.awt.Point;
import com.sun.star.sheet.XSpreadsheetDocument; //import com.sun.star.sheet.XSpreadsheet;
//import com.sun.star.sheet.XSheetCellCursor;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.drawing.XDrawPagesSupplier;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.text.XTextTablesSupplier;
import com.sun.star.container.XNamed;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.text.XTextRange;

public class HelloTextTableShape {
	// основные объекты:
	// удаленный контекст
	private XComponentContext xRemoteContext = null;
	// удаленный менеджер сервисов
	private XMultiComponentFactory xRemoteServiceManager = null;

	public HelloTextTableShape() {
		// конструктор пуст, ничего особенного не делаем
	}

	public static void main(String[] srgs) {
		// объект текущей программы-примера
		HelloTextTableShape helloTextTableShape1 = new HelloTextTableShape();
		try {
			// пытаемся выполнить основную функцию
			helloTextTableShape1.useDocuments();
		} catch (java.lang.Exception e) {
			// при ошибке – сообщение об ошибке
			System.err.println(e.getMessage());
			// и стек вызовов для отслеживания ошибки
			e.printStackTrace();
		} finally {
			// в любом случае – выход без ошибки
			System.exit(0);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	protected void useDocuments() throws java.lang.Exception {
		// основная функция программы-примера
		// последовательная работа с документами
		//
		// Writer
		useWriter();
		// Calc
		useCalc();
		// Draw
		useDraw();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	protected void useWriter() throws java.lang.Exception {
		// работа с документом Writer
		try {
			// создание объекта-экземпляра Writer
			XComponent xWriterComponent = newDocComponent("swriter");
			// объект-текстовый документ объекта Writer путем приведения типов
			XTextDocument xTextDocument = (XTextDocument) UnoRuntime
					.queryInterface(XTextDocument.class, xWriterComponent);
			// доступ к содержащемуся в нем тексту
			XText xText = xTextDocument.getText();
			// функция манипуляции текстом
			manipulateText(xText);
			// объект-фабрика объектов на основе объекта Writer
			XMultiServiceFactory xWriterFactory = (XMultiServiceFactory) UnoRuntime
					.queryInterface(XMultiServiceFactory.class,
							xWriterComponent);
			// с помощью объекта-фабрики создаем объект-текстовую таблицу
			Object table = xWriterFactory
					.createInstance("com.sun.star.text.TextTable");
			// и в ней – объект-текстовый контекст таблицы
			XTextContent xTextContentTable = (XTextContent) UnoRuntime
					.queryInterface(XTextContent.class, table);
			// вставляем его в конец текста
			xText.insertTextContent(xText.getEnd(), xTextContentTable, false);
			// в таблице – объект-регион ячеек
			XCellRange xCellRange = (XCellRange) UnoRuntime.queryInterface(
					XCellRange.class, table);
			// в нем – ячейка с указанной позицией
			XCell xCell = xCellRange.getCellByPosition(0, 1);
			// в ней – объект-текст как содержимое ячейки
			XText xCellText = (XText) UnoRuntime.queryInterface(XText.class,
					xCell);
			// манипуляции с этим текстом
			manipulateText(xCellText);
			// манипуляции с таблицей
			manipulateTable(xCellRange);
			// создаем объект-фигуру Writer при помощи фабрики объектов
			Object writerShape = xWriterFactory
					.createInstance("com.sun.star.drawing.RectangleShape");
			// приводим этот объект к нужному интерфейсному типу
			XShape xWriterShape = (XShape) UnoRuntime.queryInterface(
					XShape.class, writerShape);
			// установка размера фигуры
			xWriterShape.setSize(new Size(10000, 10000));
			// объект-текстовый контекст фигуры
			XTextContent xTextContentShape = (XTextContent) UnoRuntime
					.queryInterface(XTextContent.class, writerShape);
			// вставка его в конец текста документа
			xText.insertTextContent(xText.getEnd(), xTextContentShape, false);
			// набор свойств фигуры
			XPropertySet xShapeProps = (XPropertySet) UnoRuntime
					.queryInterface(XPropertySet.class, writerShape);
			// установка свойства TextContourFrame в true
			xShapeProps.setPropertyValue("TextContourFrame", new Boolean(true));
			// объект – текст фигуры

			XText xShapeText = (XText) UnoRuntime.queryInterface(XText.class,
					writerShape);
			// манипуляции с текстом
			manipulateText(xShapeText);
			// манипуляции с фигурой
			manipulateShape(xWriterShape);
			// создание объекта-закладки при помощи фабрики объектов
			Object bookmark = xWriterFactory
					.createInstance("com.sun.star.text.Bookmark");
			// приведение к типу именованной закладки
			XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class,
					bookmark);
			// установка имени
			xNamed.setName("МояУникальнаяЗакладка");
			// текстовый контекст закладки
			XTextContent xTextContent = (XTextContent) UnoRuntime
					.queryInterface(XTextContent.class, bookmark);
			// вставляем в конец текста документа
			xText.insertTextContent(xText.getEnd(), xTextContent, false);
			// создаем объект-поставщик закладок
			XBookmarksSupplier xBookmarksSupplier = (XBookmarksSupplier) UnoRuntime
					.queryInterface(XBookmarksSupplier.class, xWriterComponent);
			// обеспечиваем именованный доступ
			XNameAccess xNamedBookmarks = xBookmarksSupplier.getBookmarks();
			// получаем созданную ранее закладку от набора именованных закладок
			Object foundBookmark = xNamedBookmarks
					.getByName("МояУникальнаяЗакладка");
			// ее текстовый контекст
			XTextContent xFoundBookmark = (XTextContent) UnoRuntime
					.queryInterface(XTextContent.class, foundBookmark);
			// в ней объект-якорь
			XTextRange xFound = xFoundBookmark.getAnchor();
			// и вставляем строку
			xFound.setString("Раз, два, три, четыре, пять,"
					+ "вышел зайчик погулять...");
			// создаем объект-поставщик текстовых таблиц
			XTextTablesSupplier xTableSupplier = (XTextTablesSupplier) UnoRuntime
					.queryInterface(XTextTablesSupplier.class, xWriterComponent);
			// именованный доступ
			XNameAccess xNamedTables = xTableSupplier.getTextTables();
			// и индексированный доступ
			XIndexAccess xIndexedTables = (XIndexAccess) UnoRuntime
					.queryInterface(XIndexAccess.class, xNamedTables);
			// обнуляем набор свойств
			XPropertySet xTableProps = null;
			// в цикле для каждой таблицы
			for (int i = 0; i < xIndexedTables.getCount(); i++) {
				// получаем очередную таблицу из набора
				table = xIndexedTables.getByIndex(i);
				// для нее создаем объект-набор свойств
				xTableProps = (XPropertySet) UnoRuntime.queryInterface(
						XPropertySet.class, table);
				// и устанавливаем цвет фона BackColor
				xTableProps
						.setPropertyValue("BackColor", new Integer(0xc8ffb9));
			}
		} catch (DisposedException e)

		{
			// при исключении – уничтожаем объект-удаленный контекст
			xRemoteContext = null;
			// передаем исключение на дальнейшую обработку
			throw e;
		}
	}

	protected void useCalc() throws java.lang.Exception {
		// работа с Calc
		try {
			// создаем экземпляр приложения Calc
			XComponent xCalcComponent = newDocComponent("scalc");
			// объект-документ_таблица
			XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument) UnoRuntime
					.queryInterface(XSpreadsheetDocument.class, xCalcComponent);
			// из него – набор листов
			Object sheets = xSpreadsheetDocument.getSheets();
			// индексированный доступ к листам
			XIndexAccess xIndexedSheets = (XIndexAccess) UnoRuntime
					.queryInterface(XIndexAccess.class, sheets);
			// первый в наборе лист
			Object sheet = xIndexedSheets.getByIndex(0);
			// набор ячеек таблицы
			XCellRange xSpreadsheetCells = (XCellRange) UnoRuntime
					.queryInterface(XCellRange.class, sheet);
			// левая верхняя ячейка
			XCell xCell = xSpreadsheetCells.getCellByPosition(0, 1);
			// для нее – объект-набор свойств
			XPropertySet xCellProps = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, xCell);
			// устанавливаем свойство IsTextWrapped
			xCellProps.setPropertyValue("IsTextWrapped", new Boolean(true));
			// текст ячейки
			XText xCellText = (XText) UnoRuntime.queryInterface(XText.class,
					xCell);
			// манипуляция с текстом
			manipulateText(xCellText);
			// манипуляция с таблицей
			manipulateTable(xSpreadsheetCells);
			// объект-фабрика объектов Calc
			XMultiServiceFactory xCalcFactory = (XMultiServiceFactory) UnoRuntime
					.queryInterface(XMultiServiceFactory.class, xCalcComponent);
			// объект-поставщик фигур Calc
			XDrawPageSupplier xDrawPageSupplier = (XDrawPageSupplier) UnoRuntime
					.queryInterface(XDrawPageSupplier.class, sheet);
			// объект-"страница" для рисования фигуры
			XDrawPage xDrawPage = xDrawPageSupplier.getDrawPage();
			// объект-фигура
			Object calcShape = xCalcFactory
					.createInstance("com.sun.star.drawing.RectangleShape");
			// приводим ее к типу фигуры Calc
			XShape xCalcShape = (XShape) UnoRuntime.queryInterface(
					XShape.class, calcShape);
			// устанавливаем размер
			xCalcShape.setSize(new Size(10000, 10000));
			// и позицию
			xCalcShape.setPosition(new Point(7000, 7000));
			// добавляем фигуру на "страницу"
			xDrawPage.add(xCalcShape);
			// объект-набор свойств фигуры Calc
			XPropertySet xShapeProps = (XPropertySet) UnoRuntime
					.queryInterface(XPropertySet.class, calcShape);
			// установка свойства TextContourFrame для объекта-фигуры
			xShapeProps.setPropertyValue("TextContourFrame", new Boolean(true));
			// объект-текст в этой фигуре
			XText xShapeText = (XText) UnoRuntime.queryInterface(XText.class,
					calcShape);
			// манипуляция текстом
			manipulateText(xShapeText);
			// манипуляция фигурой
			manipulateShape(xCalcShape);
		} catch (DisposedException e) {
			// как выше
			xRemoteContext = null;
			throw e;
		}
	}

	protected void useDraw() throws java.lang.Exception {
		try {
			// создание объекта-экземпляра Draw
			XComponent xDrawComponent = newDocComponent("sdraw");
			// объект-поставщик "страниц" для рисования
			XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime
					.queryInterface(XDrawPagesSupplier.class, xDrawComponent);
			// из него – объект-набор страниц
			Object drawPages = xDrawPagesSupplier.getDrawPages();
			// обеспечиваем индексированный доступ к набору
			XIndexAccess xIndexedDrawPages = (XIndexAccess) UnoRuntime
					.queryInterface(XIndexAccess.class, drawPages);
			// первая страница
			Object drawPage = xIndexedDrawPages.getByIndex(0);
			// приводим к нужному типу
			XDrawPage xDrawPage = (XDrawPage) UnoRuntime.queryInterface(
					XDrawPage.class, drawPage);
			// объект-фабрика объектов Draw
			XMultiServiceFactory xDrawFactory = (XMultiServiceFactory) UnoRuntime
					.queryInterface(XMultiServiceFactory.class, xDrawComponent);
			// создаем объект-фигуру Draw (прямоугольник)
			Object drawShape = xDrawFactory
					.createInstance("com.sun.star.drawing.RectangleShape");
			// приводим к нужному типу
			XShape xDrawShape = (XShape) UnoRuntime.queryInterface(
					XShape.class, drawShape);
			// размер
			xDrawShape.setSize(new Size(10000, 10000));
			// позиция
			xDrawShape.setPosition(new Point(5000, 5000));
			// добавляем на страницу
			xDrawPage.add(xDrawShape);
			// объект-текст фигуры
			XText xShapeText = (XText) UnoRuntime.queryInterface(XText.class,
					drawShape);
			// набор свойств
			XPropertySet xShapeProps = (XPropertySet) UnoRuntime
					.queryInterface(XPropertySet.class, drawShape);
			// устанавливаем свойство
			xShapeProps.setPropertyValue("TextContourFrame", new Boolean(true));
			// манипуляция текстом
			manipulateText(xShapeText);
			// манипуляция фигурой
			manipulateShape(xDrawShape);
		} catch (DisposedException e) {
			// как выше
			xRemoteContext = null;
			throw e;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////

	protected void manipulateText(XText xText)
			throws com.sun.star.uno.Exception {
		// функция манипуляции текстом
		// получает объект, соответствующий тексту, содержащемуся в каком-то
		// объекте приложения OpenOffice.org
		//
		// пишем строку
		xText.setString("Тут охотник выбегает, "
				+ "Прямо в зайчика стреляет!...");
		// создаем объект-текстовый курсор
		XTextCursor xTextCursor = xText.createTextCursor();
		// набор свойств текстового курсора
		XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
				XPropertySet.class, xTextCursor);
		// специфические для объекта особенности-свойства устанавливаются
		// напрямую
		xTextCursor.gotoStart(false);
		xTextCursor.goRight((short) 8, true);
		// а менее специфические – как обычно
		xCursorProps.setPropertyValue("CharPosture", FontSlant.ITALIC);
		xCursorProps.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
		xTextCursor.gotoEnd(false);
		xText.insertString(xTextCursor, "\n Пиф-паф, ой-ой-ой...", false);
		xText.insertString(xTextCursor, "\n \n Помирает зайчик мой!", false);
		// в результате первые 8 символов текста устанавливаются в
		// "жирный курсив"
		// и добавляется дополнительный текст
	}

	protected void manipulateTable(XCellRange xCellRange)
			throws com.sun.star.uno.Exception {
		// функция манипуляции таблицей
		// получает набор ячеек таблицы
		//
		//
		String backColorPropertyName = "";
		XPropertySet xTableProps = null;
		// получаем первую ячейку полученного в аргументе набора
		XCell xCell = xCellRange.getCellByPosition(0, 0);
		// приводим к текстовому типу
		XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);
		// пишем строку
		xCellText.setString("Заголовок");
		// ячейка рядом (справа) от предыдущей
		xCell = xCellRange.getCellByPosition(1, 0);
		// аналогично – приводим к текстовому типу
		xCellText = (XText) UnoRuntime.queryInterface(XText.class, xCell);
		// пишем строку
		xCellText.setString("Еще один");
		// ячейка ниже
		xCell = xCellRange.getCellByPosition(1, 1);
		// пишем значение (число, поэтому к текстовому типу можно не приводить)
		xCell.setValue(1940);
		// выделяем регион
		XCellRange xSelectedCells = xCellRange.getCellRangeByName("A1:B1");
		// его набор свойств
		XPropertySet xCellProps = (XPropertySet) UnoRuntime.queryInterface(
				XPropertySet.class, xSelectedCells);
		// интерфейс, предоставляющий информацию о сервисе
		XServiceInfo xServiceInfo = (XServiceInfo) UnoRuntime.queryInterface(
				XServiceInfo.class, xCellRange);
		// проверяем, поддерживает ли родительский объект (регион ячеек) этот
		// интерфейс (в строке записан)

		// иначе говоря, если работаем с листом Calc
		if (xServiceInfo.supportsService("com.sun.star.sheet.Spreadsheet")) {
			// если да --
			//
			// строка – имя свойства
			backColorPropertyName = "CellBackColor";
			// выделяем регион ячеек
			xSelectedCells = xCellRange.getCellRangeByName("A1:B2");
			// набор свойств таблицы
			xTableProps = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, xSelectedCells);
		}
		// иначе, если работаем с текстовой таблицей
		else if (xServiceInfo.supportsService("com.sun.star.text.TextTable")) {
			// имя свойства другое
			backColorPropertyName = "BackColor";
			// набор свойств таблацы
			xTableProps = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, xCellRange);
		}
		// и теперь пишем значение свойства (цвет фона ячеек)
		xCellProps.setPropertyValue(backColorPropertyName,
				new Integer(0x99ccff));
		// объект-контурная линия
		BorderLine theLine = new BorderLine();
		// цвет
		theLine.Color = 0x000099;
		// толщина
		theLine.OuterLineWidth = 10;
		// объект-граница таблицы
		TableBorder bord = new TableBorder();
		// всем границам присваиваем объект-контурную линию
		bord.VerticalLine = bord.HorizontalLine = bord.LeftLine = bord.RightLine = bord.TopLine = bord.BottomLine = theLine;
		// разрешаем рисовать линии на всех границах таблицы
		bord.IsVerticalLineValid = bord.IsHorizontalLineValid = bord.IsLeftLineValid = bord.IsRightLineValid = bord.IsTopLineValid = bord.IsBottomLineValid = true;
		// устанавливаем свойство таблицы
		xTableProps.setPropertyValue("TableBorder", bord);
		// получаем значение свойства
		bord = (TableBorder) xTableProps.getPropertyValue("TableBorder");
		// изменяем имеющиеся объекты
		theLine = bord.TopLine;
		// цвет
		int col = theLine.Color;
		// выводи значение
		System.out.println(col);
	}

	protected void manipulateShape(XShape xShape)
			throws com.sun.star.uno.Exception {
		// манипулирование фигурой
		//
		// вначале получаем набор свойств текущей фигуры (полученной в
		// аргументе)
		XPropertySet xShapeProps = (XPropertySet) UnoRuntime.queryInterface(
				XPropertySet.class, xShape);
		// устанавливаем свойства
		xShapeProps.setPropertyValue("FillColor", new Integer(0x99ccff));
		xShapeProps.setPropertyValue("LineColor", new Integer(0x000099));
		xShapeProps.setPropertyValue("RotateAngle", new Integer(3000));
		xShapeProps.setPropertyValue("TextLeftDistance", new Integer(0));
		xShapeProps.setPropertyValue("TextRightDistance", new Integer(0));
		xShapeProps.setPropertyValue("TextUpperDistance", new Integer(0));
		xShapeProps.setPropertyValue("TextLowerDistance", new Integer(0));
		//
		// все :)
	}

	protected XComponent newDocComponent(String docType)
			throws java.lang.Exception {
		// получение документа заданного в аргументе типа
		//
		// формируем строку-URL
		String loadUrl = "private:factory/" + docType;
		// получаем удаленный менеджер сервисов из текущего приложения
		// (то есть программы, текст который вы читаете)
		xRemoteServiceManager = this.getRemoteServiceManager();
		// объект-рабочий стол, основной объект OOo
		Object desktop = xRemoteServiceManager.createInstanceWithContext(
				"com.sun.star.frame.Desktop", xRemoteContext);
		// объект-загрузчик компонентов
		XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
				.queryInterface(XComponentLoader.class, desktop);
		// массив значений свойств
		PropertyValue[] loadProps = new PropertyValue[0];
		// функция возвращает то, что загрузил объект-загрузчик компонентов
		// по указанному URL
		return xComponentLoader.loadComponentFromURL(loadUrl, "_blank", 0,
				loadProps);
	}

	protected XMultiComponentFactory getRemoteServiceManager()
			throws java.lang.Exception {
		// получение удаленного менеджера сервисов
		//
		// если он еще не получен и не получен удаленный контекст –
		if (xRemoteContext == null && xRemoteServiceManager == null) {
			try // пытаемся
			{
				// получаем удаленный контекст
				xRemoteContext = Bootstrap.bootstrap();
				// выводим сообщение
				System.out.println("Соединение с работающим офисом... ");
				// и получаем удаленный менеджер сервисов
				xRemoteServiceManager = xRemoteContext.getServiceManager();
			} catch (java.lang.Exception e) {
				// иначе – выводим стек вызовов
				e.printStackTrace();
				// выходим с ошибкой
				System.exit(1);
			}
		}
		// все нормально
		return xRemoteServiceManager;
	}
}