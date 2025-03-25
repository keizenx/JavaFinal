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

public class MenuApp {
    private MainApp mainApp;

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
                }
            });
            navMenu.getChildren().add(menuItem);
        }
        
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        
        header.getChildren().addAll(logoContainer, leftSpacer, navMenu, rightSpacer);
        return header;
    }

    private VBox createMenuContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(50, 100, 50, 100));
        
        // Title section
        VBox titleSection = new VBox(10);
        titleSection.setAlignment(Pos.CENTER);
        
        Label title = new Label("Notre Menu");
        title.getStyleClass().add("menu-title");
        
        Label subtitle = new Label("BIENVENUE SUR LA PAGE DE MENU MERCI NOUS ESPÉRONS FAIRE\nVOTRE BONHEUR");
        subtitle.getStyleClass().add("menu-subtitle");
        
        titleSection.getChildren().addAll(title, subtitle);
        
        // Menu categories
        HBox categories = new HBox(20);
        categories.setAlignment(Pos.CENTER);
        categories.getStyleClass().add("menu-categories");
        
        String[] categoryNames = {"Tout"};
        for (String category : categoryNames) {
            Button categoryBtn = new Button(category);
            categoryBtn.getStyleClass().add("category-button");
            categoryBtn.getStyleClass().add("category-button-active");
            categories.getChildren().add(categoryBtn);
        }
        
        // Menu grid
        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(30);
        menuGrid.setVgap(30);
        menuGrid.setAlignment(Pos.CENTER);
        
        // Menu items data
        String[][] menuItems = {
            {"image_113.png", "ATTIEKE POULET", "1000 franc"},
            {"image_114.png", "ATTIEKE POISSON", "1000 franc"},
            {"image_115.png", "FRITE POULET", "1000 franc"},
            {"image_116.png", "CODY'S", "5000 franc"},
            {"image_117.png", "ORANGINA", "500 franc"},
            {"image_118.png", "EAU", "100 franc"},
            {"image_119.png", "COCA-COLA", "500 franc"},
            {"image_120.png", "Galette avec sucre", "300 franc"},
            {"image_121.png", "CREPE SUCRE", "500 franc"},
            {"image_122.png", "Gateau au four", "500 franc"},
            {"image_123.png", "Yaourt", "500 franc"},
            {"telechargement_1.png", "GARBA", "1000 franc"}
        };
        
        int col = 0;
        int row = 0;
        
        for (String[] item : menuItems) {
            VBox menuItem = new VBox(15);
            menuItem.getStyleClass().add("menu-item-card");
            menuItem.setAlignment(Pos.CENTER);
            
            try {
                System.out.println("Trying to load image from: /images/Notre_Menu/" + item[0]);
                Image image = new Image(getClass().getResourceAsStream("/images/Notre_Menu/" + item[0]));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(250);
                imageView.setFitHeight(250);
                imageView.getStyleClass().add("menu-item-image");
                
                Label name = new Label(item[1]);
                name.getStyleClass().add("menu-item-name");
                
                Label price = new Label(item[2]);
                price.getStyleClass().add("menu-item-price");
                
                menuItem.getChildren().addAll(imageView, name, price);
                
                menuGrid.add(menuItem, col, row);
                
                col++;
                if (col > 3) {
                    col = 0;
                    row++;
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + item[0]);
                e.printStackTrace();
            }
        }
        
        content.getChildren().addAll(titleSection, categories, menuGrid);
        return content;
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