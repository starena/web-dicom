@echo off
rem -------------------------------------------------------------------------
rem dcm4che2/dcmrcv  Launcher
rem -------------------------------------------------------------------------

rem $Id: dcmrcv.bat 11988 2009-07-24 13:13:57Z gunterze $

if not "%ECHO%" == ""  echo %ECHO%
if "%OS%" == "Windows_NT"  setlocal

set MAIN_CLASS=org.psystems.dicom.daemon.Archive
set MAIN_JAR=dcmarchive.jar

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%

rem Read all command line arguments

set ARGS=
:loop
if [%1] == [] goto end
        set ARGS=%ARGS% %1
        shift
        goto loop
:end

if not "%DCM4CHE_HOME%" == "" goto HAVE_DCM4CHE_HOME

set DCM4CHE_HOME=%DIRNAME%..

:HAVE_DCM4CHE_HOME

if not "%JAVA_HOME%" == "" goto HAVE_JAVA_HOME

set JAVA=java

goto SKIP_SET_JAVA_HOME

:HAVE_JAVA_HOME

set JAVA=%JAVA_HOME%\bin\java

:SKIP_SET_JAVA_HOME

set CP=%DCM4CHE_HOME%\etc\
set CP=%CP%;%DCM4CHE_HOME%\lib\%MAIN_JAR%
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-core-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-filecache-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-net-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-log4j12-1.5.0.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-api-1.5.0.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\commons-cli-1.2.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\derby.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\derbyclient.jar 
set CP=%CP%;%DCM4CHE_HOME%\lib\derbynet.jar 
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-core-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-imageio-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-image-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-filecache-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-net-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\commons-cli-1.2.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\dcm4che-imageio-rle-2.0.21.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-api-1.5.0.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-log4j12-1.5.0.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\org.apache.log4j_1.2.13.v200806030600.jar
set CP=%CP%;%DCM4CHE_HOME%\lib\jai_imageio.jar 
set CP=%CP%;%DCM4CHE_HOME%\lib\slf4j-log4j12-1.5.0.jar


"%JAVA%" %JAVA_OPTS% -cp "%CP%" %MAIN_CLASS% %ARGS%

