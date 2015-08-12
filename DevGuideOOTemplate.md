# Форматирование шаблона #

  * Поля именуются в соответствии с имененем dicom тега и имеют тип "Текст", например: тег 00100010 - Это ФИО (PatientName)
> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/make_field.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/make_field.png)
  * Области данных в которых предусмотрен ввод текста должны располагаться в специальном разделе (Меню: Вставка -> Раздел) с заданием свойства "Разрешить правку в защищенном от записи документе"
> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/make_razdel.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/make_razdel.png)
  * Размещение полей выбора (выпадающих списков) производится через (Меню: Вставка -> Поля -> Дополнительно -> Функции : Список)
> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/make_field_choice.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/make_field_choice.png)


# Перечень обязательных компонентов шаблона #

  * ID -  Внутренний номер исследования (тип Text) (Меню: Вставка -> Поля -> Дополнительно):
  * TextLogging Текстовое поле ввода (раздел "Формы") в которое будет писаться отладочная информация
> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/logging_props.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/logging_props.png)
  * Кнопка отправки данных с вызовом функции dicomExport макроса web-dicom JavaPlugin ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/export_btn.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/export_btn.png)
  * Назначить на событие "Активизация документа" вызов функции dicomImport макроса web-dicom JavaPlugin (Меню: Сервис -> Настройка -> События : Активизация документа)
> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/import_doc.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/import_doc.png)

# Экспорт шаблона #
> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/export_macros.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuideOOTemplate/export_macros.png)