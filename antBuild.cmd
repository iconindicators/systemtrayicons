cls

setlocal

rem ===== Setup environment for ant.
set ANT_HOME=C:/progra~1/apache-ant-1.7.0
set JAVA_HOME=C:/Progra~1/Java/jdk1.6.0_07
set NSIS_HOME=C:/Progra~1/NSIS
set PATH=%PATH%;%ANT_HOME%\bin;

ant %1

endlocal
