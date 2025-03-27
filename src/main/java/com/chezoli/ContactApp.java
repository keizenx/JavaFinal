package com.chezoli;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.io.File;
import java.net.URL;

public class ContactApp {
    private MainApp mainApp;
    private Scene scene;

    public ContactApp(MainApp mainApp) {
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
            createContactContent(),
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
        
        // Left side - contact info
        HBox leftSide = new HBox();
        leftSide.getStyleClass().add("top-bar-left");
        
        // Phone
        HBox phoneBox = new HBox(5);
        phoneBox.setAlignment(Pos.CENTER);
        Text phoneIcon = new Text("");
        phoneIcon.getStyleClass().add("top-bar-icon");
        Label phoneNumber = new Label("(414) 857 - 0107");
        phoneNumber.getStyleClass().add("top-bar-text");
        phoneBox.getChildren().addAll(phoneIcon, phoneNumber);
        
        // Email
        HBox emailBox = new HBox(5);
        emailBox.setAlignment(Pos.CENTER);
        Text emailIcon = new Text("");
        emailIcon.getStyleClass().add("top-bar-icon");
        Label emailAddress = new Label("yummy@bistrobliss");
        emailAddress.getStyleClass().add("top-bar-text");
        emailBox.getChildren().addAll(emailIcon, emailAddress);
        
        leftSide.getChildren().addAll(phoneBox, emailBox);
        
        // Right side - social icons
        HBox rightSide = new HBox();
        rightSide.getStyleClass().addAll("top-bar-right", "social-icons");
        
        String[] socialIcons = {"", "", "", ""};  // Twitter, Facebook, Instagram, Map marker
        for (String icon : socialIcons) {
            Text socialIcon = new Text(icon);
            socialIcon.getStyleClass().addAll("social-icon");
            rightSide.getChildren().add(socialIcon);
        }
        
        // Use Region to push items to sides
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(leftSide, spacer, rightSide);
        return topBar;
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER);
        
        // Logo container
        HBox logoContainer = new HBox(10);
        logoContainer.getStyleClass().add("logo-container");
        
        Text logoIcon = new Text("🍽");
        logoIcon.setStyle("-fx-font-size: 24px;");
        
        Label logoText = new Label("CHEZ OLI");
        logoText.getStyleClass().add("logo-text");
        
        logoContainer.getChildren().addAll(logoIcon, logoText);
        
        // Navigation menu
        HBox navMenu = new HBox(40);
        navMenu.getStyleClass().add("nav-menu");
        navMenu.setAlignment(Pos.CENTER);
        
        String[] menuItems = {"Home", "About", "Menu", "Contact"};
        for (String item : menuItems) {
            Label menuItem = new Label(item);
            menuItem.getStyleClass().add("menu-item");
            if (item.equals("Contact")) {
                menuItem.getStyleClass().add("menu-item-active");
            }
            menuItem.setOnMouseClicked(event -> {
                switch (item) {
                    case "Home":
                        mainApp.showHome();
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
        
        // Boutons de connexion/déconnexion
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
    
    private VBox createContactContent() {
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(50, 0, 50, 0));
        
        // Title section
        VBox titleSection = new VBox(10);
        titleSection.setAlignment(Pos.CENTER);
        
        Label title = new Label("Contactez nous");
        title.getStyleClass().add("contact-title");
        
        Label subtitle = new Label("Apportez nous vos recommandations n'hésitez pas de nous contacter");
        subtitle.getStyleClass().add("contact-subtitle");
        
        titleSection.getChildren().addAll(title, subtitle);
        
        // Form section
        VBox formSection = new VBox(15);
        formSection.getStyleClass().add("form-container");
        formSection.setMaxWidth(800);
        
        // Name field
        VBox nameBox = new VBox(5);
        Label nameLabel = new Label("Name");
        nameLabel.getStyleClass().add("form-label");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.getStyleClass().add("text-field");
        nameBox.getChildren().addAll(nameLabel, nameField);
        
        // Email field
        VBox emailBox = new VBox(5);
        Label emailLabel = new Label("Email");
        emailLabel.getStyleClass().add("form-label");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email address");
        emailField.getStyleClass().add("text-field");
        emailBox.getChildren().addAll(emailLabel, emailField);
        
        // Subject field
        VBox subjectBox = new VBox(5);
        Label subjectLabel = new Label("Subject");
        subjectLabel.getStyleClass().add("form-label");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Write a subject");
        subjectField.getStyleClass().add("text-field");
        subjectBox.getChildren().addAll(subjectLabel, subjectField);
        
        // Message field
        VBox messageBox = new VBox(5);
        Label messageLabel = new Label("Message");
        messageLabel.getStyleClass().add("form-label");
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write your message");
        messageArea.getStyleClass().add("text-area");
        messageArea.setPrefRowCount(5);
        messageBox.getChildren().addAll(messageLabel, messageArea);
        
        // Send button
        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("send-button");
        sendButton.setMaxWidth(Double.MAX_VALUE);
        
        formSection.getChildren().addAll(
            nameBox,
            emailBox,
            subjectBox,
            messageBox,
            sendButton
        );
        
        // Contact info section
        HBox contactInfoSection = new HBox(30);
        contactInfoSection.setAlignment(Pos.CENTER);
        contactInfoSection.setPadding(new Insets(30, 0, 0, 0));
        
        // Phone info
        VBox phoneInfo = new VBox(5);
        phoneInfo.setAlignment(Pos.CENTER);
        Label phoneTitle = new Label("Numéro téléphone:");
        phoneTitle.getStyleClass().add("contact-info-title");
        Label phoneNumber = new Label("+22501234234");
        phoneNumber.getStyleClass().addAll("contact-info-value", "phone-number");
        phoneInfo.getChildren().addAll(phoneTitle, phoneNumber);
        
        // Hours info
        VBox hoursInfo = new VBox(5);
        hoursInfo.setAlignment(Pos.CENTER);
        Label hoursTitle = new Label("Heures:");
        hoursTitle.getStyleClass().add("contact-info-title");
        VBox hoursValue = new VBox(2);
        hoursValue.setAlignment(Pos.CENTER);
        Label weekHours = new Label("Mon-Fri: 06h - 20h");
        Label weekendHours = new Label("Sat-Sun: 06h - 18h");
        weekHours.getStyleClass().add("contact-info-value");
        weekendHours.getStyleClass().add("contact-info-value");
        hoursValue.getChildren().addAll(weekHours, weekendHours);
        hoursInfo.getChildren().addAll(hoursTitle, hoursValue);
        
        // Location info
        VBox locationInfo = new VBox(5);
        locationInfo.setAlignment(Pos.CENTER);
        Label locationTitle = new Label("Notre Localisation:");
        locationTitle.getStyleClass().add("contact-info-title");
        Label locationValue = new Label("Carrefour Seka Seka");
        locationValue.getStyleClass().add("contact-info-value");
        locationInfo.getChildren().addAll(locationTitle, locationValue);
        
        contactInfoSection.getChildren().addAll(phoneInfo, hoursInfo, locationInfo);
        
        content.getChildren().addAll(titleSection, formSection, contactInfoSection);
        return content;
    }
    
    private HBox createContactInfo() {
        HBox infoContainer = new HBox(50);
        infoContainer.getStyleClass().add("contact-info-container");
        infoContainer.setAlignment(Pos.CENTER);
        
        // Phone number
        VBox phoneBox = new VBox(5);
        Label phoneTitle = new Label("Numéro téléphone:");
        phoneTitle.getStyleClass().add("contact-info-title");
        Label phoneNumber = new Label("+22501234234");
        phoneNumber.getStyleClass().addAll("contact-info-value", "phone-number");
        phoneBox.getChildren().addAll(phoneTitle, phoneNumber);
        
        // Hours
        VBox hoursBox = new VBox(5);
        Label hoursTitle = new Label("Heures:");
        hoursTitle.getStyleClass().add("contact-info-title");
        VBox hoursList = new VBox(2);
        Label hours1 = new Label("Mon-Fri: 08h - 20h");
        hours1.getStyleClass().add("contact-info-value");
        Label hours2 = new Label("Sat-Sun: 08h - 18h");
        hours2.getStyleClass().add("contact-info-value");
        hoursList.getChildren().addAll(hours1, hours2);
        hoursBox.getChildren().addAll(hoursTitle, hoursList);
        
        // Location
        VBox locationBox = new VBox(5);
        Label locationTitle = new Label("Notre Localisation:");
        locationTitle.getStyleClass().add("contact-info-title");
        Label location = new Label("Carrefour Seka Seka");
        location.getStyleClass().add("contact-info-value");
        locationBox.getChildren().addAll(locationTitle, location);
        
        infoContainer.getChildren().addAll(phoneBox, hoursBox, locationBox);
        return infoContainer;
    }
    
    private Node createFooter() {
        VBox footer = new VBox(40);  // Add spacing between content and copyright
        footer.getStyleClass().add("footer");
        footer.setMinHeight(400);  // Ensure minimum height
        footer.setPrefWidth(Region.USE_COMPUTED_SIZE);  // Use full width
        footer.setAlignment(Pos.CENTER);  // Center content vertically

        // Footer content container
        HBox footerContent = new HBox();
        footerContent.getStyleClass().add("footer-content");
        footerContent.setAlignment(Pos.CENTER);
        footerContent.setPadding(new Insets(40, 100, 40, 100));  // Add padding
        footerContent.setSpacing(100);  // Space between sections

        // Left section (Logo, text, social)
        VBox footerLeft = new VBox(20);  // Space between elements
        footerLeft.getStyleClass().add("footer-left");
        footerLeft.setMaxWidth(300);  // Limit width for text wrapping
        footerLeft.setAlignment(Pos.TOP_LEFT);  // Align content to top-left

        // Logo with icon
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        
        Text restaurantIcon = new Text("🍽");
        restaurantIcon.setStyle("-fx-font-size: 24px; -fx-fill: white;");
        
        Label footerLogo = new Label("CHEZ OLI");
        footerLogo.getStyleClass().add("footer-logo");
        
        logoBox.getChildren().addAll(restaurantIcon, footerLogo);

        // Description text
        Label footerText = new Label("In the new era of technology we look a in the future with certainty and pride to for our company end.");
        footerText.getStyleClass().add("footer-text");
        footerText.setWrapText(true);

        // Social icons
        HBox socialIcons = new HBox(15);
        socialIcons.getStyleClass().add("footer-social");
        socialIcons.setAlignment(Pos.CENTER_LEFT);

        String[] socialSymbols = {"", "", "", ""};  // Twitter, Facebook, Instagram, GitHub
        for (String symbol : socialSymbols) {
            StackPane circle = new StackPane();
            circle.getStyleClass().add("social-circle");
            circle.setPrefSize(36, 36);  // Fixed size for circles
            circle.setMinSize(36, 36);   // Ensure minimum size
            
            Text icon = new Text(symbol);
            icon.getStyleClass().add("footer-social-icon");
            
            circle.getChildren().add(icon);
            socialIcons.getChildren().add(circle);
        }

        footerLeft.getChildren().addAll(logoBox, footerText, socialIcons);

        // Footer columns container
        HBox footerColumns = new HBox(80);  // Space between columns
        footerColumns.getStyleClass().add("footer-columns");
        footerColumns.setAlignment(Pos.TOP_LEFT);  // Align columns to top

        // Pages column
        VBox pagesColumn = createFooterColumn("Pages", 
            new String[]{"Home", "About", "Menu", "Pricing", "Blog", "Contact", "Delivery"});

        // Utility Pages column
        VBox utilityColumn = createFooterColumn("Utility Pages", 
            new String[]{"Start Here", "Styleguide", "Password Protected", "404 Not Found", "Licenses", "Changelog", "View More"});

        footerColumns.getChildren().addAll(pagesColumn, utilityColumn);

        footerContent.getChildren().addAll(footerLeft, footerColumns);

        // Copyright section
        VBox copyrightSection = new VBox();
        copyrightSection.setAlignment(Pos.CENTER);
        copyrightSection.setPadding(new Insets(20, 0, 0, 0));
        copyrightSection.getStyleClass().add("copyright-section");

        // Copyright text
        Label copyright = new Label("Copyright © 2024 Identity Creative. All Rights Reserved.");
        copyright.getStyleClass().add("copyright");
        copyright.setAlignment(Pos.CENTER);
        copyright.setMaxWidth(Double.MAX_VALUE);

        copyrightSection.getChildren().add(copyright);

        footer.getChildren().addAll(footerContent, copyrightSection);
        return footer;
    }

    private VBox createFooterColumn(String title, String[] links) {
        VBox column = new VBox(15);  // Space between title and links
        column.getStyleClass().add("footer-column");

        Label columnTitle = new Label(title);
        columnTitle.getStyleClass().add("footer-column-title");

        VBox linksList = new VBox(10);  // Space between links
        linksList.getStyleClass().add("footer-links");

        for (String link : links) {
            Label linkLabel = new Label(link);
            linkLabel.getStyleClass().add("footer-link");
            linksList.getChildren().add(linkLabel);
        }

        column.getChildren().addAll(columnTitle, linksList);
        return column;
    }
    
    // Helper class for social icons
    private static class Circle extends StackPane {
        public Circle(double radius, Color color) {
            javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(radius);
            circle.setFill(color);
            getChildren().add(circle);
        }
    }
} 