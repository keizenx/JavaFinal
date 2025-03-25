@echo off
REM Set path to your JavaFX SDK lib folder
set PATH_TO_FX="C:\Users\franck\Desktop\javafx-sdk-24\lib"

REM Verify JavaFX SDK exists
if not exist %PATH_TO_FX% (
    echo ERROR: JavaFX SDK not found at %PATH_TO_FX%
    echo Please download JavaFX SDK and extract it to the correct location
    pause
    exit /b 1
)

REM Create output directories if they don't exist
if not exist "classes" mkdir classes

REM Ensure source directories exist
if not exist "src\main\java\com\chezoli" (
    mkdir "src\main\java\com\chezoli"
)
if not exist "src\main\resources" (
    mkdir "src\main\resources"
)

echo Compiling...
javac -d classes --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base ^
    src/main/java/com/chezoli/*.java

if errorlevel 1 (
    echo Compilation failed
    pause
    exit /b 1
)

echo Copying resources...
xcopy /y /s /i "src\main\resources" "classes"

echo Running...
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp classes com.chezoli.MainApp

pause 