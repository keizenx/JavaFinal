package com.chezoli;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AboutApp {
    private MainApp mainApp;

    public AboutApp(MainApp mainApp) {
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
            createAboutContent(),
            createFooter()
        );
        
        scrollPane.setContent(contentContainer);
        root.getChildren().add(scrollPane);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        
        return scene;
    }

    private VBox createAboutContent() {
        VBox content = new VBox(50);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(50, 0, 50, 0));
        
        // Title section
        VBox titleSection = new VBox(10);
        titleSection.setAlignment(Pos.CENTER);
        
        Label title = new Label("À propos de nous");
        title.getStyleClass().add("about-title");
        
        Label subtitle = new Label("Découvrez notre histoire et notre passion pour la cuisine authentique");
        subtitle.getStyleClass().add("about-subtitle");
        
        titleSection.getChildren().addAll(title, subtitle);
        
        // Story section
        VBox storySection = new VBox(30);
        storySection.setMaxWidth(800);
        storySection.setAlignment(Pos.CENTER);
        storySection.getStyleClass().add("about-story-section");
        
        Label storyTitle = new Label("Notre Histoire");
        storyTitle.getStyleClass().add("story-title");
        
        Label storyText = new Label(
            "Fondé en 2020, Chez Oli est né d'une passion pour la cuisine authentique et le service " +
            "d'excellence. Notre restaurant combine les saveurs traditionnelles avec une touche moderne, " +
            "créant une expérience culinaire unique pour nos clients.\n\n" +
            "Chaque plat est préparé avec des ingrédients soigneusement sélectionnés, en mettant l'accent " +
            "sur la qualité et la fraîcheur. Notre équipe dévouée travaille sans relâche pour offrir " +
            "une expérience gastronomique exceptionnelle."
        );
        storyText.setWrapText(true);
        storyText.getStyleClass().add("story-text");
        
        storySection.getChildren().addAll(storyTitle, storyText);
        
        // Values section
        HBox valuesSection = new HBox(40);
        valuesSection.setAlignment(Pos.CENTER);
        valuesSection.getStyleClass().add("values-section");
        
        // Create value cards
        String[][] values = {
            {"🌟", "Qualité", "Nous sélectionnons les meilleurs ingrédients pour créer des plats exceptionnels"},
            {"💝", "Passion", "Notre amour pour la cuisine se reflète dans chaque plat que nous servons"},
            {"🤝", "Service", "Un accueil chaleureux et un service attentionné pour une expérience mémorable"}
        };
        
        for (String[] value : values) {
            VBox card = new VBox(15);
            card.getStyleClass().add("value-card");
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(30));
            
            Text icon = new Text(value[0]);
            icon.getStyleClass().add("value-icon");
            
            Label cardTitle = new Label(value[1]);
            cardTitle.getStyleClass().add("value-title");
            
            Label description = new Label(value[2]);
            description.setWrapText(true);
            description.setTextAlignment(TextAlignment.CENTER);
            description.getStyleClass().add("value-description");
            
            card.getChildren().addAll(icon, cardTitle, description);
            valuesSection.getChildren().add(card);
        }
        
        content.getChildren().addAll(titleSection, storySection, valuesSection);
        return content;
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
            if (item.equals("About")) {
                menuItem.getStyleClass().add("menu-item-active");
            }
            menuItem.setOnMouseClicked(event -> {
                switch (item) {
                    case "Contact":
                        mainApp.showContact();
                        break;
                    case "Menu":
                        mainApp.showMenu();
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