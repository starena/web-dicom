# Настройка доступа к WEB-консоли (http://hostname:4848) #

http://docs.oracle.com/cd/E18930_01/html/821-2435/gkofl.html

Ключи и сертификаты находятся здесь (в папке с именем домена domain1/config):

  * Both private keys are stored in the domain-wide DAS keystore file, keystore.jks.

  * Both public certificates are stored in the domain-wide DAS truststore file, cacerts.jks.


Для включения опции доступа к WEB-админке удаленно нужно настроить админский пароль, иначе получаем сообщение:
<pre>
#asadmin enable-secure-admin<br>
remote failure: At least one admin user has an empty password, which secure admin does not permit. Use the change-admin-password command or the admin console to create non-empty passwords for admin accounts.<br>
Command enable-secure-admin failed.<br>
</pre>

Через WEB-консоль задаем пароль для админа и можно включать режим:
<pre>
#asadmin enable-secure-admin<br>
Enter admin user name>  admin<br>
Enter admin password for user "admin"><br>
You must restart all running servers for the change in secure admin to take effect.<br>
Command enable-secure-admin executed successfully.<br>
</pre>

После этого будет доступ к WEB-консоли удаленно