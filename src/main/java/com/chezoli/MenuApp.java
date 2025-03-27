package com.chezoli;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class MenuApp {
    private MainApp mainApp;

    private final String[][] entrees = {
        {"image_113.png", "ATTIEKE POULET", "1000 franc"},
        {"image_114.png", "ATTIEKE POISSON", "1000 franc"},
        {"telechargement_1.png", "GARBA", "1000 franc"}
    };

    private final String[][] plats = {
        {"image_115.png", "FRITE POULET", "1000 franc"},
        {"image_116.png", "CODY'S", "5000 franc"}
    };

    private final String[][] desserts = {
        {"image_120.png", "Galette avec sucre", "300 franc"},
        {"image_121.png", "CREPE SUCRE", "500 franc"},
        {"image_122.png", "Gateau au four", "500 franc"},
        {"image_123.png", "Yaourt", "500 franc"}
    };

    private final String[][] boissons = {
        {"image_117.png", "ORANGINA", "500 franc"},
        {"image_118.png", "EAU", "100 franc"},
        {"image_119.png", "COCA-COLA", "500 franc"}
    };

    public MenuApp(MainApp mainApp) {
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
            createMenuContent(),
            createFooter()
        );
        
        scrollPane.setContent(contentContainer);
        root.getChildren().add(scrollPane);
        
        Scene scene = new Scene(root);
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
            if (item.equals("Menu")) {
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
                    case "Home":
                        mainApp.showHome();
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

    private VBox createMenuContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(50, 0, 50, 0));
        
        // Title section
        VBox titleSection = new VBox(10);
        titleSection.setAlignment(Pos.CENTER);
        
        Label title = new Label("Notre Menu");
        title.getStyleClass().add("menu-title");
        
        Label subtitle = new Label("Découvrez nos délicieux plats");
        subtitle.getStyleClass().add("menu-subtitle");
        
        titleSection.getChildren().addAll(title, subtitle);

        // Bouton Commander
        Button commanderButton = new Button("Accéder à la page de commande");
        commanderButton.getStyleClass().add("order-button");
        commanderButton.setOnAction(e -> {
            // Récupérer les articles du menu depuis la base de données
            List<MenuItem> menuItems = new com.chezoli.dao.MenuItemDAO().getAllMenuItems();
            mainApp.showOrder("Tout", menuItems);
        });
        
        // Menu sections
        VBox menuSections = new VBox(50);
        menuSections.setAlignment(Pos.CENTER);
        
        // Utiliser MenuItemDAO pour récupérer les articles depuis la base de données
        com.chezoli.dao.MenuItemDAO menuItemDAO = new com.chezoli.dao.MenuItemDAO();
        List<MenuItem> allMenuItems = menuItemDAO.getAllMenuItems();
        
        // Filtrer les articles par catégorie
        List<MenuItem> entreesItems = allMenuItems.stream()
            .filter(item -> "Entrées".equals(item.getCategory()))
            .collect(java.util.stream.Collectors.toList());
        
        List<MenuItem> platsItems = allMenuItems.stream()
            .filter(item -> "Plats Principaux".equals(item.getCategory()))
            .collect(java.util.stream.Collectors.toList());
        
        List<MenuItem> dessertsItems = allMenuItems.stream()
            .filter(item -> "Desserts".equals(item.getCategory()))
            .collect(java.util.stream.Collectors.toList());
        
        List<MenuItem> boissonsItems = allMenuItems.stream()
            .filter(item -> "Boissons".equals(item.getCategory()))
            .collect(java.util.stream.Collectors.toList());
        
        // Créer les sections de menu
        menuSections.getChildren().addAll(
            createCategorySection("Entrées", entreesItems),
            createCategorySection("Plats Principaux", platsItems),
            createCategorySection("Desserts", dessertsItems),
            createCategorySection("Boissons", boissonsItems)
        );
        
        content.getChildren().addAll(titleSection, commanderButton, menuSections);
        return content;
    }

    private VBox createCategorySection(String categoryTitle, List<MenuItem> items) {
        VBox section = new VBox(20);
        section.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(categoryTitle);
        titleLabel.getStyleClass().add("category-title");
        
        FlowPane itemsGrid = new FlowPane();
        itemsGrid.setHgap(30);
        itemsGrid.setVgap(30);
        itemsGrid.setAlignment(Pos.CENTER);
        
        for (MenuItem item : items) {
            VBox itemCard = new VBox(10);
            itemCard.getStyleClass().add("menu-item-card");
            itemCard.setAlignment(Pos.CENTER);
            
            // Image
            ImageView imageView = new ImageView();
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            
            String imagePath = item.getImagePath();
            System.out.println("Tentative de chargement de l'image pour: " + item.getName() + " avec URL: " + item.getImageUrl());
            System.out.println("Chemin d'image résolu: " + imagePath);
            
            try {
                if (imagePath != null) {
                    // Vérifier si c'est une URL externe (pour le cas de l'image de remplacement)
                    if (imagePath.startsWith("http")) {
                        imageView.setImage(new Image(imagePath));
                    } else {
                        imageView.setImage(new Image(imagePath));
                    }
                } else {
                    // Fallback si l'image est null
                    String defaultPath = getClass().getResource("/images/logo.png").toExternalForm();
                    imageView.setImage(new Image(defaultPath));
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
                try {
                    // Dernière tentative avec un placeholder en ligne
                    imageView.setImage(new Image("https://dummyimage.com/200x200/cccccc/ffffff.png&text=Image+Non+Disponible"));
                } catch (Exception ex) {
                    // Si même le placeholder échoue, ne rien faire
                }
            }
            
            Label nameLabel = new Label(item.getName());
            nameLabel.getStyleClass().add("menu-item-name");
            
            Label descriptionLabel = new Label(item.getDescription());
            descriptionLabel.getStyleClass().add("menu-item-description");
            descriptionLabel.setWrapText(true);
            
            Label priceLabel = new Label(item.getFormattedPrice());
            priceLabel.getStyleClass().add("menu-item-price");
            
            itemCard.getChildren().addAll(imageView, nameLabel, descriptionLabel, priceLabel);
            itemsGrid.getChildren().add(itemCard);
        }
        
        section.getChildren().addAll(titleLabel, itemsGrid);
        return section;
    }

    private Node createFooter() {
        VBox footer = new VBox(40);
        footer.getStyleClass().add("footer");
        footer.setMinHeight(400);
        footer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        footer.setAlignment(Pos.CENTER);

        HBox footerContent = new HBox();
        footerContent.getStyleClass().add("footer-content");
        footerContent.setAlignment(Pos.CENTER);
        footerContent.setPadding(new Insets(40, 100, 40, 100));
        footerContent.setSpacing(100);

        VBox footerLeft = new VBox(20);
        footerLeft.getStyleClass().add("footer-left");
        footerLeft.setMaxWidth(300);
        footerLeft.setAlignment(Pos.TOP_LEFT);

        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        
        Text restaurantIcon = new Text("🍽");
        restaurantIcon.setStyle("-fx-font-size: 24px; -fx-fill: white;");
        
        Label footerLogo = new Label("CHEZ OLI");
        footerLogo.getStyleClass().add("footer-logo");
        
        logoBox.getChildren().addAll(restaurantIcon, footerLogo);

        Label footerText = new Label("In the new era of technology we look a in the future with certainty and pride to for our company end.");
        footerText.getStyleClass().add("footer-text");
        footerText.setWrapText(true);

        HBox socialIcons = new HBox(15);
        socialIcons.getStyleClass().add("footer-social");
        socialIcons.setAlignment(Pos.CENTER_LEFT);

        String[] socialSymbols = {"", "", "", ""};
        for (String symbol : socialSymbols) {
            StackPane circle = new StackPane();
            circle.getStyleClass().add("social-circle");
            circle.setPrefSize(36, 36);
            circle.setMinSize(36, 36);
            
            Text icon = new Text(symbol);
            icon.getStyleClass().add("footer-social-icon");
            
            circle.getChildren().add(icon);
            socialIcons.getChildren().add(circle);
        }

        footerLeft.getChildren().addAll(logoBox, footerText, socialIcons);

        HBox footerColumns = new HBox(80);
        footerColumns.getStyleClass().add("footer-columns");
        footerColumns.setAlignment(Pos.TOP_LEFT);

        VBox pagesColumn = createFooterColumn("Pages", 
            new String[]{"Home", "About", "Menu", "Pricing", "Blog", "Contact", "Delivery"});

        VBox utilityColumn = createFooterColumn("Utility Pages", 
            new String[]{"Start Here", "Styleguide", "Password Protected", "404 Not Found", "Licenses", "Changelog", "View More"});

        footerColumns.getChildren().addAll(pagesColumn, utilityColumn);

        footerContent.getChildren().addAll(footerLeft, footerColumns);

        VBox copyrightSection = new VBox();
        copyrightSection.setAlignment(Pos.CENTER);
        copyrightSection.setPadding(new Insets(20, 0, 0, 0));
        copyrightSection.getStyleClass().add("copyright-section");

        Label copyright = new Label("Copyright © 2024 Identity Creative. All Rights Reserved.");
        copyright.getStyleClass().add("copyright");
        copyright.setAlignment(Pos.CENTER);
        copyright.setMaxWidth(Double.MAX_VALUE);

        copyrightSection.getChildren().add(copyright);

        footer.getChildren().addAll(footerContent, copyrightSection);
        return footer;
    }

    private VBox createFooterColumn(String title, String[] links) {
        VBox column = new VBox(15);
        column.getStyleClass().add("footer-column");

        Label columnTitle = new Label(title);
        columnTitle.getStyleClass().add("footer-column-title");

        VBox linksList = new VBox(10);
        linksList.getStyleClass().add("footer-links");

        for (String link : links) {
            Label linkLabel = new Label(link);
            linkLabel.getStyleClass().add("footer-link");
            linksList.getChildren().add(linkLabel);
        }

        column.getChildren().addAll(columnTitle, linksList);
        return column;
    }
} 