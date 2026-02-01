@echo off
echo Running Java Inventory Management System...

REM Check if bin directory exists and has MainApp.class
if not exist "bin\MainApp.class" (
    echo Application not compiled. Please run build.bat first.
    pause
    exit /b 1
)

REM Run the application
java -cp bin MainApp

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Error running application. Please check:
    echo 1. Application is compiled (run build.bat)
    echo 2. Java is properly installed
    echo 3. All required files are present
)

pause
