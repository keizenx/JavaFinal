package com.chezoli;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class HomeApp {
    private MainApp mainApp;
    private Scene scene;

    public HomeApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Scene createScene() {
        VBox root = new VBox();
        root.getStyleClass().add("root");
        root.setSpacing(0);
        root.setPrefWidth(1200);
        root.setPrefHeight(800);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("scroll-pane");
        
        VBox contentContainer = new VBox(0);
        contentContainer.getChildren().addAll(
            createTopBar(),
            createHeader(),
            createHeroSection(),
            createMenuCategories(),
            createQualitySection(),
            createFooter()
        );
        
        scrollPane.setContent(contentContainer);
        root.getChildren().add(scrollPane);
        
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        return scene;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-bar");
        
        HBox leftSide = new HBox();
        leftSide.getStyleClass().add("top-bar-left");
        
        HBox phoneBox = new HBox(5);
        phoneBox.setAlignment(Pos.CENTER);
        Text phoneIcon = new Text("");
        phoneIcon.getStyleClass().add("top-bar-icon");
        Label phoneNumber = new Label("(414) 857 - 0107");
        phoneNumber.getStyleClass().add("top-bar-text");
        phoneBox.getChildren().addAll(phoneIcon, phoneNumber);
        
        HBox emailBox = new HBox(5);
        emailBox.setAlignment(Pos.CENTER);
        Text emailIcon = new Text("");
        emailIcon.getStyleClass().add("top-bar-icon");
        Label emailAddress = new Label("yummy@bistrobliss");
        emailAddress.getStyleClass().add("top-bar-text");
        emailBox.getChildren().addAll(emailIcon, emailAddress);
        
        leftSide.getChildren().addAll(phoneBox, emailBox);
        
        HBox rightSide = new HBox();
        rightSide.getStyleClass().addAll("top-bar-right", "social-icons");
        
        String[] socialIcons = {"", "", "", ""};
        for (String icon : socialIcons) {
            Text socialIcon = new Text(icon);
            socialIcon.getStyleClass().addAll("social-icon");
            rightSide.getChildren().add(socialIcon);
        }
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(leftSide, spacer, rightSide);
        return topBar;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER);
        
        HBox logoContainer = new HBox(10);
        logoContainer.getStyleClass().add("logo-container");
        
        Text logoIcon = new Text("🍽");
        logoIcon.setStyle("-fx-font-size: 24px;");
        
        Label logoText = new Label("CHEZ OLI");
        logoText.getStyleClass().add("logo-text");
        
        logoContainer.getChildren().addAll(logoIcon, logoText);
        
        HBox navMenu = new HBox(40);
        navMenu.getStyleClass().add("nav-menu");
        navMenu.setAlignment(Pos.CENTER);
        
        String[] menuItems = {"Home", "About", "Menu", "Contact"};
        for (String item : menuItems) {
            Label menuItem = new Label(item);
            menuItem.getStyleClass().add("menu-item");
            if (item.equals("Home")) {
                menuItem.getStyleClass().add("menu-item-active");
            }
            menuItem.setOnMouseClicked(event -> {
                switch (item) {
                    case "Contact":
                        mainApp.showContact();
                        break;
                    case "About":
                        mainApp.showAbout();
                        break;
                    case "Menu":
                        mainApp.showMenu();
                        break;
                }
            });
            navMenu.getChildren().add(menuItem);
        }
        
        // Ajouter un bouton Admin si l'utilisateur est administrateur
        if (mainApp.isAdmin()) {
            Label adminLabel = new Label("Admin");
            adminLabel.getStyleClass().add("menu-item");
            adminLabel.getStyleClass().add("admin-item");
            adminLabel.setOnMouseClicked(event -> mainApp.showAdmin());
            navMenu.getChildren().add(adminLabel);
        }
        
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        
        Button loginButton = new Button("Connexion");
        loginButton.getStyleClass().add("login-button");
        loginButton.setVisible(!mainApp.isLoggedIn());
        loginButton.setOnAction(e -> mainApp.showLogin());
        
        Button logoutButton = new Button("Déconnexion");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setVisible(mainApp.isLoggedIn());
        logoutButton.setOnAction(e -> {
            mainApp.setLoggedIn(false);
            mainApp.setCurrentUser(null);
            mainApp.showLogin();
        });
        
        header.getChildren().addAll(logoContainer, leftSpacer, navMenu, rightSpacer, loginButton, logoutButton);
        return header;
    }

    private VBox createHeroSection() {
        VBox heroSection = new VBox();
        heroSection.getStyleClass().add("hero-section");
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(new Insets(100, 0, 100, 0));
        
        Label welcomeText = new Label("Bienvenue sur la plateforme");
        welcomeText.getStyleClass().add("hero-welcome");
        
        Label nameText = new Label("Les Délices Olivia");
        nameText.getStyleClass().add("hero-name");
        
        Button commandButton = new Button("Commander maintenant");
        commandButton.getStyleClass().add("hero-button");
        commandButton.setOnAction(e -> mainApp.showMenu());
        
        heroSection.getChildren().addAll(welcomeText, nameText, commandButton);
        return heroSection;
    }

    private GridPane createMenuCategories() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(50, 100, 50, 100));
        
        String[][] categories = {
            {"Petit Déjeuner", "Notre sélection de petit déjeuner"},
            {"Plan du Midi", "Découvrez nos menus du midi"},
            {"Boissons", "Large choix de boissons fraîches"},
            {"Desserts", "Délicieux desserts maison"}
        };
        
        int col = 0;
        int row = 0;
        
        for (String[] category : categories) {
            VBox card = new VBox(15);
            card.getStyleClass().add("menu-category-card");
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(20));
            
            Label title = new Label(category[0]);
            title.getStyleClass().add("category-title");
            
            Label description = new Label(category[1]);
            description.getStyleClass().add("category-description");
            description.setWrapText(true);
            
            Button viewButton = new Button("Voir le menu complet");
            viewButton.getStyleClass().add("category-button");
            viewButton.setOnAction(e -> mainApp.showMenu());
            
            card.getChildren().addAll(title, description, viewButton);
            
            grid.add(card, col, row);
            
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }
        
        return grid;
    }

    private HBox createQualitySection() {
        HBox section = new HBox(50);
        section.getStyleClass().add("quality-section");
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(50, 100, 50, 100));
        
        ImageView qualityImage = new ImageView(new Image(getClass().getResourceAsStream("/images/HomePage/imageBas.png")));
        qualityImage.setFitWidth(500);
        qualityImage.setFitHeight(400);
        qualityImage.getStyleClass().add("quality-image");
        
        VBox textContent = new VBox(20);
        textContent.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("DES PLATS DE QUALITÉS POUR DES CLIENTS DE QUALITÉ");
        title.getStyleClass().add("quality-title");
        title.setWrapText(true);
        
        Label description = new Label("Alors ça ne vous donne pas envie qu'est ce que vous attendez pour venir DABA avec OLI !!");
        description.getStyleClass().add("quality-description");
        description.setWrapText(true);
        
        textContent.getChildren().addAll(title, description);
        
        section.getChildren().addAll(qualityImage, textContent);
        return section;
    }

    private VBox createFooter() {
        VBox footer = new VBox(20);
        footer.getStyleClass().add("footer");
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(50, 0, 50, 0));
        
        Label copyright = new Label("© 2024 CHEZ OLI. Tous droits réservés.");
        copyright.getStyleClass().add("footer-text");
        
        footer.getChildren().add(copyright);
        return footer;
    }
} 