# Introduction #

Данный раздел документации описывает работу с WEB-сервисами проекта


# Details #

Необходимо:

  * Скачать java-машину http://java.sun.com/javase/downloads/index.jsp
  * Скачать Apache Tomcat http://tomcat.apache.org/ (вот версия 5.5 под windows http://www.sai.msu.su/apache/tomcat/tomcat-5/v5.5.28/bin/apache-tomcat-5.5.28.exe)
  * Скачать архив с тестовыми сермисами http://code.google.com/p/web-dicom/downloads/list
  * Загрузить архив в контейнер
  * Проверить работу сервисов по ссылке http://localhost:8080/dicom-webservice (если контейнер обслуживает порт 8080) см. рисунки №1,№2
  * WSDL описание сервиса можно получить по ссылке http://localhost:8080/dicom-webservice/services/DicomArchive?wsdl (если контейнер обслуживает порт 8080) см. рисунок №3

Для понимания структур передаваемых объектов вот ссылки на исходный код:
  * http://web-dicom.googlecode.com/svn/trunk/dicom-webservice/src/org/psystems/dicom/webservice/DicomArchive.java

  * http://web-dicom.googlecode.com/svn/trunk/dicom-webservice/src/org/psystems/dicom/webservice/Study.java

  * Протестировать работу сервисов можно с помощью утилиты wstest.jar. Для запуска используем команду **java -jar wstest.jar** (Для работы утилиты как и для работы контейнера необходимо поставить виртуальную java машину)

# Поясняющие рисунки #

## рис. 1 ##
http://web-dicom.googlecode.com/svn/wiki/images/manuals/ws1.PNG

## рис. 2 ##
http://web-dicom.googlecode.com/svn/wiki/images/manuals/ws2.PNG

## рис. 3 ##
http://web-dicom.googlecode.com/svn/wiki/images/manuals/ws3.PNG