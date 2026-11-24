@echo off
setlocal
if not defined JAVA_HOME (
  echo JAVA_HOME must point to a compatible JDK. 1>&2
  exit /b 1
)
"%JAVA_HOME%\bin\java.exe" -Xmx64m -Xms64m -cp "%~dp0gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
