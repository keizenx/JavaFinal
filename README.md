# CHEZ OLI - Application de Restaurant

## Description
CHEZ OLI est une application JavaFX moderne pour un restaurant, offrant une interface utilisateur élégante et intuitive. L'application comprend trois sections principales :

- **Menu** : Présentation des plats et boissons avec photos et prix
- **À propos** : Histoire du restaurant et valeurs
- **Contact** : Formulaire de contact et informations

## Prérequis
- Java JDK 17 ou supérieur
- JavaFX SDK 24
- Un système d'exploitation Windows, Linux ou macOS

## Installation

1. Clonez le dépôt :
```bash
git clone https://github.com/ZieTech-dev/CantineG5
```

2. Téléchargez JavaFX SDK 24 depuis :
   https://gluonhq.com/products/javafx/

3. Extrayez JavaFX SDK dans :
   `C:\Users\[votre-nom]\Desktop\javafx-sdk-24`

4. Assurez-vous que la structure du projet est la suivante :
```
javaFinal/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── chezoli/
│   │   │           ├── MainApp.java
│   │   │           ├── MenuApp.java
│   │   │           ├── AboutApp.java
│   │   │           └── ContactApp.java
│   │   └── resources/
│   │       ├── images/
│   │       │   ├── Notre_Menu/
│   │       │   │   └── [images du menu]
│   │       │   └── [autres images]
│   │       └── styles.css
├── classes/
├── run.bat
└── README.md
```

## Exécution

1. Double-cliquez sur `run.bat`
   - Le script compile automatiquement le code
   - Copie les ressources nécessaires
   - Lance l'application

2. Si vous rencontrez des erreurs :
   - Vérifiez que JavaFX SDK est bien installé
   - Vérifiez le chemin dans `run.bat`
   - Assurez-vous que toutes les images sont présentes

## Fonctionnalités

### Page Menu
- Affichage des plats avec photos
- Prix en francs CFA
- Navigation fluide

### Page À propos
- Histoire du restaurant
- Valeurs de l'entreprise
- Design moderne avec animations

### Page Contact
- Formulaire de contact
- Informations de contact
- Carte de localisation

## Style et Design
- Police principale : Playfair Display
- Police secondaire : Inter
- Couleurs :
  - Rouge accent : #B22F3D
  - Texte principal : #1F1F1F
  - Texte secondaire : #666666

## Licence
© 2024 CHEZ OLI. Tous droits réservés. 