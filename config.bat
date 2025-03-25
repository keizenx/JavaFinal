@echo off
setlocal enabledelayedexpansion

:JAVAFX_PATH
cls
echo ===========================================
echo Configuration de JavaFX
echo ===========================================
echo.
echo Cette configuration sera sauvegardée pour les prochaines utilisations.
echo.
set /p JAVAFX_PATH="Entrez le chemin vers votre JavaFX SDK lib (ex: C:\Path\To\javafx-sdk-24\lib): "

if not exist "!JAVAFX_PATH!" (
    echo.
    echo [ERREUR] Le chemin spécifié n'existe pas: !JAVAFX_PATH!
    echo Appuyez sur une touche pour réessayer...
    pause >nul
    goto JAVAFX_PATH
)

echo !JAVAFX_PATH!> javafx_path.txt
echo.
echo Configuration sauvegardée avec succès !
echo Le chemin JavaFX est maintenant configuré vers: !JAVAFX_PATH!
echo.
pause 