PUSHD "%~dp0..\.."

set JAVA_EXE="C:\pleiades\java\8\jre\bin\java.exe"
set JAVA_ARGS=-Xmx1024M
set JAR_FILES=bin
rem set JAR_FILES=bin;libs\*.jar
set MAIN_CLASS=cecilia.StartHttAlcott
set MAIN_ARGS=

%JAVA_EXE% %JAVA_ARGS% -cp %JAR_FILES% %MAIN_CLASS% %MAIN_ARGS%

POPD
