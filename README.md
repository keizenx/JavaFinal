# CHEZ OLI - Application de Restaurant

## Description
CHEZ OLI est une application JavaFX moderne pour un restaurant, offrant une interface utilisateur élégante et intuitive. L'application comprend plusieurs fonctionnalités complètes :

- **Accueil** : Présentation du restaurant
- **Menu** : Présentation des plats et boissons avec photos et prix
- **Commander** : Interface de commande avec panier
- **À propos** : Histoire du restaurant et valeurs
- **Contact** : Formulaire de contact et informations
- **Connexion/Inscription** : Gestion des comptes utilisateurs
- **Administration** : Gestion des plats, commandes et utilisateurs

## Prérequis
- Java JDK 17 ou supérieur
- JavaFX SDK 24
- MySQL Server 8.0 ou supérieur
- Un système d'exploitation Windows, Linux ou macOS

## Installation

### 1. Installation de MySQL
1. Téléchargez et installez MySQL Server depuis [le site officiel](https://dev.mysql.com/downloads/mysql/)
2. Lors de l'installation, définissez les identifiants suivants :
   - Utilisateur : `root`
   - Mot de passe : `franckX`
   - Port : `3306` (port par défaut)

> **Note** : Si vous souhaitez utiliser des identifiants différents, vous devrez modifier le fichier `DatabaseConnection.java` en conséquence.

### 2. Installation de JavaFX
1. Téléchargez JavaFX SDK 24 depuis [le site officiel](https://gluonhq.com/products/javafx/)
2. Extrayez l'archive dans un dossier de votre choix, par exemple : `C:\Users\votre_nom\Desktop\javafx-sdk-24`

### 3. Obtention du code
1. Clonez le dépôt :
```bash
git clone https://github.com/ZieTech-dev/CantineG5
```

2. Assurez-vous que la structure du projet est la suivante :
```
javaFinal/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── chezoli/
│   │   │           ├── MainApp.java
│   │   │           ├── MenuApp.java
│   │   │           ├── OrderApp.java
│   │   │           ├── AdminApp.java
│   │   │           ├── AboutApp.java
│   │   │           ├── ContactApp.java
│   │   │           ├── LoginApp.java
│   │   │           ├── SignUpApp.java
│   │   │           ├── dao/
│   │   │           │   ├── DatabaseConnection.java
│   │   │           │   ├── MenuItemDAO.java
│   │   │           │   ├── OrderDAO.java
│   │   │           │   ├── UserDAO.java
│   │   │           │   └── CategoryDAO.java
│   │   │           └── [autres fichiers]
│   │   └── resources/
│   │       ├── db/
│   │       │   └── init.sql
│   │       ├── images/
│   │       │   ├── Notre_Menu/
│   │       │   │   └── [images du menu]
│   │       │   └── [autres images]
│   │       └── styles.css
├── classes/
├── run.bat
└── README.md
```

### 4. Configurer le script de lancement

Créez ou modifiez le fichier `run.bat` à la racine du projet avec le contenu suivant :

```batch
@echo off
setlocal enabledelayedexpansion

:: Chemin vers JavaFX (à adapter selon votre installation)
set JAVAFX_PATH=C:\Users\votre_nom\Desktop\javafx-sdk-24\lib

echo Utilisation de JavaFX depuis: %JAVAFX_PATH%
echo.

:: Compiler les sources
echo Compilation en cours...
if not exist classes mkdir classes

javac --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -d classes -sourcepath src/main/java src/main/java/com/chezoli/MainApp.java

if %errorlevel% neq 0 (
    echo [ERREUR] Échec de la compilation.
    pause
    exit /b 1
)

:: Copier les ressources
echo Copie des ressources...
xcopy /y /s /q src\main\resources\* classes\

:: Exécuter l'application
echo Démarrage de l'application...
java --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -cp classes;lib\* com.chezoli.MainApp

pause
```

> **Important** : Modifiez le chemin `JAVAFX_PATH` pour qu'il corresponde à l'emplacement où vous avez installé JavaFX.

## Démarrage de l'application

1. Assurez-vous que MySQL Server est en cours d'exécution
2. Double-cliquez sur le fichier `run.bat` pour lancer l'application
3. Lors du premier lancement, la base de données sera automatiquement créée et initialisée

## Comptes et identifiants

### Compte administrateur par défaut
- Nom d'utilisateur : `admin`
- Mot de passe : `admin123`

### Créer un compte client
1. Cliquez sur "Inscription" dans la barre de navigation
2. Remplissez le formulaire avec vos informations
3. Connectez-vous avec vos identifiants

## Fonctionnalités

### Page Accueil
- Présentation du restaurant
- Navigation vers les autres sections

### Page Menu
- Affichage des plats avec photos
- Prix en francs CFA
- Filtrage par catégorie

### Page Commander
- Interface de commande avec panier
- Ajout et suppression d'articles
- Modification des quantités
- Calcul automatique du total
- Choix du mode de paiement
- Historique des commandes

### Page À propos
- Histoire du restaurant
- Valeurs de l'entreprise
- Design moderne avec animations

### Page Contact
- Formulaire de contact
- Informations de contact
- Carte de localisation

### Panneau d'administration
- Gestion des plats (ajout, modification, suppression)
- Gestion des commandes (visualisation, changement de statut)
- Gestion des utilisateurs

## Persistance des données
Toutes les modifications effectuées dans l'application (ajout de plats, création d'utilisateurs, commandes) sont sauvegardées dans la base de données MySQL et seront disponibles lors des prochains lancements de l'application.

## Résolution des problèmes courants

### Erreur de connexion à la base de données
- Vérifiez que MySQL Server est bien démarré
- Vérifiez les identifiants dans `DatabaseConnection.java`
- Assurez-vous que le port 3306 n'est pas bloqué par un pare-feu

### Erreur de chargement de JavaFX
- Vérifiez que le chemin vers JavaFX dans le fichier `run.bat` est correct
- Assurez-vous d'avoir téléchargé JavaFX SDK 24 compatible avec votre système d'exploitation

### Problèmes d'affichage des images
- Vérifiez que le dossier `src/main/resources/images` a bien été copié dans le dossier `classes`

## Style et Design
- Police principale : Playfair Display
- Police secondaire : Inter
- Couleurs :
  - Rouge accent : #B22F3D
  - Texte principal : #1F1F1F
  - Texte secondaire : #666666

## Licence
© 2024 CHEZ OLI. Tous droits réservés. 