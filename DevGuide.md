# Библиотеки #
  * Java JDK (1.6) http://java.sun.com
  * JAI https://jai-imageio.dev.java.net/binary-builds.html
  * AXIS2 (1.4) http://axis.apache.org/axis2/java/core/download.cgi
  * Derby (10.5.3) http://db.apache.org/derby/derby_downloads.html
  * DCM2CHE (2.0.21) http://sourceforge.net/projects/dcm4che/files/dcm4che2/
  * OpenOffice 3.2 http://www.openoffice.org/ или http://i-rs.ru/download
  * OpenOffice SDK
  * OpenOffice eclipse plugin
  1. http://wiki.services.openoffice.org/wiki/JavaEclipseTuto
  1. http://cedric.bosdonnat.free.fr/wordpress/?page_id=11&lp_lang_view=en
  * Apache Tomcat (6.0.24) http://tomcat.apache.org/download-60.cgi


## Настройка eclipse 3.5 ##

  * Необходимо прописать в среде JDK1.6.x под именем jdk1.6 (тип Standard VM)

> [eclipse](eclipse.md) -> Window -> Prefference -> Java-> Installed JREs

> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/eclipse_java.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/eclipse_java.png)

  * Необходимо зарегистрировать Runtime сервер Tomcat v6.0 с именем "Apache Tomcat v6.0"
> [eclipse](eclipse.md) -> Window -> Prefference -> Server -> Runtime Enviroment

> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/eclipse_tomcat.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/eclipse_tomcat.png)


## AXIS ##

  * Необходимо прописать путь к Axis2

> [eclipse](eclipse.md) -> Window -> Prefference -> Web Services -> Axis2 preference

> Вот примерный файл полученной конфигурации:
> C:\WORK\workspace.new\.metadata\.plugins\org.apache.axis2.facet\server.properties

> <pre>
path=C:\\java\\axis2-1.4<br>
</pre>

> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/eclipse_axis.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/eclipse_axis.png)

> Настройка AXIS в фасете проекта:

> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/project_axis.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/project_axis.png)


## OpenOffice SDK ##

  * Необходимо установить OpenOffice 3.2
  * Необходимо установить OpenOffice 3.2 SKD Можно взять отсюда:
> http://code.google.com/p/web-dicom/downloads/detail?name=openoffice-plugin-update.zip

  * Необходимо прописать путь к SDK и OpenOffice

> [eclipse](eclipse.md) -> Window -> Prefference -> OpenOffice.org.plugin -> SDK Configuration

> Вот примерный файл полученной конфигурации:
> .metadata\.plugins\org.openoffice.ide.eclipse.core\.ooo\_config

> <pre>
#<br>
#Mon Nov 22 10:09:46 YEKT 2010<br>
sdkpath0=C\:\\java\\openoffice\\SDK\\Basis\\sdk<br>
oooname0=OpenOffice.org 3.2<br>
ooopath0=C\:\\Program Files\\OpenOffice.org 3<br>
</pre>

> ![http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/eclipse_openoffice.png](http://web-dicom.googlecode.com/svn/wiki/images/DevGuide/eclipse_openoffice.png)


## Настройка Окружения ##