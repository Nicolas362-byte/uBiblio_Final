@echo off
if not exist target\uBiblio-1.0.jar (
  echo Build dulu: mvn clean package
  pause
  exit /b 1
)
java -jar target\uBiblio-1.0.jar
