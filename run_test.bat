@echo off
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_401
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d %~dp0backend
mvn clean test -Dtest=UserServiceTest#testSaveUser
pause
