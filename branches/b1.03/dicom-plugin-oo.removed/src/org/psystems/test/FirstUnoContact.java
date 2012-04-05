package org.psystems.test;

import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.XComponentContext;

public class FirstUnoContact {
	public static void main(String[] args) {
		// всего одна функция
		// по завершении которой прекращаем работу
		try {
			// создаем компонентный контекст
			XComponentContext xContext = Bootstrap.bootstrap();
			// выводим сообщение
			System.out
					.println("Соединяемся с работающим экземпляром OOffice... ");
			// создаем основную фабрику объектов
			XMultiComponentFactory xMCF = xContext.getServiceManager();
			// проверяем, получилось ли создать?
			String avaiable = (xMCF != null ? "доступен" : "не доступен");
			// и сообщаем о результате
			System.out.println("удаленный ServiceManager " + avaiable);
		} catch (Exception e) {
			// при исключении – выводим дамп стека
			e.printStackTrace();
		} finally {
			// выходим без ошибки
			System.exit(0);
		}
	}
}