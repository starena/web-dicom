# Ссылки на документы по этой теме #
  * Основной Wiki http://wiki.apache.org/hadoop/
  * Eclipse plugin
  1. http://wiki.apache.org/hadoop/EclipsePlugIn
  1. http://wiki.apache.org/hadoop/EclipseEnvironment
  * Руководство но настройке под Windows Hadoop + Eclipse http://v-lad.org/Tutorials/Hadoop/00%20-%20Intro.html


# Установка на Windows машинках #

Задать HADOOP\_HOME
```
$ export HADOOP_HOME=/home/dima_d/hadoop-0.21.0
```

Создать папку {HADOOP\_HOME}/logs (#chmod 777)

Установка-настройка cygwin (номер ревизии брать последний) http://hbase.apache.org/docs/r0.89.20100924/cygwin.html

Установку проводим по руководству http://v-lad.org/Tutorials/Hadoop/00%20-%20Intro.html

**!!! Внимание  !!!** в версии 0.21 появились изменения!

  1. Переименовали конфигурационный файл hadoop-site.xml в hdfs-site.xml
  1. 
  1. Копируем файл hdfs-default.xml изи папки ${HADOOP\_ROOT}hdfs\src\java\
> и добавляем в него:
    1. hbase.rootdir must read e.g. [file:///C:/cygwin/root/tmp/hbase/data](file:///C:/cygwin/root/tmp/hbase/data)
    1. hbase.tmp.dir must read C:/cygwin/root/tmp/hbase/tmp
    1. hbase.zookeeper.quorum must read 127.0.0.1 because for some reason localhost doesn't seem to resolve properly on Cygwin.

  1. В hadoop-config.sh добавить трансляцию символа разделтеля Java CLASSPATH ':' -> ';' (строка 190 примерно) по топику http://lucene.472066.n3.nabble.com/Hadoop-installation-on-Windows-tt1883975.html#a1891882
```
# cygwin path translation
if $cygwin; then
  HADOOP_COMMON_HOME=`cygpath -w "$HADOOP_COMMON_HOME"`
  HADOOP_LOG_DIR=`cygpath -w "$HADOOP_LOG_DIR"`

  # *** Fix CLASSPATH BEGIN  ***
  CLASSPATH=`cygpath -p -w "$CLASSPATH"` 
  # *** Fix CLASSPATH END ***

fi
```

  1. Создать папку для хранения данных (она будет форматирована)
```
 #mkdir /tmp/hadoop-dima_d/dfs/name
```

  1. Добавить в конфиг hdfs-site.xml
```
	<property>
		<name>hadoop.tmp.dir</name>
		<value>/tmp/hadoop-${user.name}</value>
	</property>
	
	<property>
		<name>mapred.child.java.opts</name>
		<value>-Xmx512m</value>
	</property>
```