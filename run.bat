@echo off
setlocal enabledelayedexpansion

:: Vérifier si le chemin JavaFX est déjà configuré
if exist "javafx_path.txt" (
    set /p JAVAFX_PATH=<javafx_path.txt
) else (
    call config.bat
    if errorlevel 1 exit /b 1
    set /p JAVAFX_PATH=<javafx_path.txt
)

echo.
echo Utilisation de JavaFX depuis: !JAVAFX_PATH!
echo.

REM Create output directories if they don't exist
if not exist "classes" mkdir classes

REM Ensure source directories exist
if not exist "src\main\java\com\chezoli" (
    mkdir "src\main\java\com\chezoli"
)
if not exist "src\main\resources" (
    mkdir "src\main\resources"
)

echo Compilation en cours...
javac -d classes --module-path "!JAVAFX_PATH!" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "lib/*" src/main/java/com/chezoli/*.java src/main/java/com/chezoli/dao/*.java

if errorlevel 1 (
    echo.
    echo [ERREUR] Échec de la compilation.
    pause
    exit /b 1
)

echo.
echo Copie des ressources...
xcopy /y /s /i "src\main\resources" "classes"

echo.
echo Démarrage de l'application...
java --module-path "!JAVAFX_PATH!" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base -cp "classes;lib/*" com.chezoli.MainApp

echo.
echo Application terminée.
pause 