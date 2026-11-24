@echo off
setlocal
if not defined JAVA_HOME (
  echo JAVA_HOME must point to a JDK 17 or newer. 1>&2
  exit /b 1
)
"%JAVA_HOME%\bin\java.exe" -Dmaven.multiModuleProjectDirectory=. -cp "%~dp0.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
