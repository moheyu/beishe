$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_401"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
cd backend
mvn clean test -Dtest=UserServiceTest#testSaveUser
