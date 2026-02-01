@echo off
echo Building Java Inventory Management System...

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Clean previous builds
del /Q bin\*.class 2>nul

REM Compile all Java files to bin directory
javac -d bin src\*.java

if %ERRORLEVEL% EQU 0 (
    echo Build successful!
    echo Compiled classes are in bin\ directory
    echo.
    echo To run the application:
    echo java -cp bin MainApp
) else (
    echo Build failed!
    echo Please check the error messages above.
)

pause
