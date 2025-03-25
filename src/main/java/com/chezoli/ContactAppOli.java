package com.chezoli;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.net.URL;

public class ContactAppOli {
    private MainApp mainApp;

    public ContactAppOli(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Scene createScene() {
        // Main container
        VBox root = new VBox();
        root.getStyleClass().add("root");
        
        // Top bar with contact info and social icons
        HBox topBar = createTopBar();
        
        // Navigation
        HBox navigation = createNavigation();
        
        // Main content container
        VBox mainContent = new VBox(20);
        mainContent.setMaxWidth(600);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(50, 20, 50, 20));
        
        // Title section
        VBox titleSection = createTitleSection();
        
        // Contact form
        VBox contactForm = createContactForm();
        
        // Contact information
        HBox contactInfo = createContactInfo();
        
        mainContent.getChildren().addAll(titleSection, contactForm, contactInfo);
        
        // Footer
        VBox footer = createFooter();
        
        // Combine all sections
        root.getChildren().addAll(topBar, navigation, mainContent, footer);
        
        // Create scene
        Scene scene = new Scene(root, 1200, 800);
        
        // Load CSS
        try {
            URL cssUrl = getClass().getResource("/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
        }
        
        return scene;
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: white; -fx-padding: 10px 20px;");
        topBar.setAlignment(Pos.CENTER);
        
        // Left side - Contact info
        HBox contactInfo = new HBox(20);
        Text phoneText = new Text("(414) 857 - 0107");
        Text emailText = new Text("yummy@bistrobliss");
        contactInfo.getChildren().addAll(phoneText, emailText);
        
        // Right side - Social icons
        HBox socialIcons = new HBox(10);
        socialIcons.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(socialIcons, Priority.ALWAYS);
        String[] iconNames = {"twitter", "facebook", "instagram", "map"};
        for (String iconName : iconNames) {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/" + iconName + ".png")));
            icon.setFitHeight(15);
            icon.setPreserveRatio(true);
            socialIcons.getChildren().add(icon);
        }
        
        topBar.getChildren().addAll(contactInfo, socialIcons);
        return topBar;
    }
    
    private HBox createNavigation() {
        HBox navigation = new HBox(10);
        navigation.setStyle("-fx-background-color: white; -fx-padding: 20px;");
        navigation.setAlignment(Pos.CENTER);
        
        // Logo
        HBox logoContainer = new HBox(10);
        logoContainer.setAlignment(Pos.CENTER_LEFT);
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);
        Text logoText = new Text("CHEZ OLI");
        logoText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        logoContainer.getChildren().addAll(logo, logoText);
        
        // Navigation menu
        HBox menu = new HBox(20);
        menu.setAlignment(Pos.CENTER);
        String[] menuItems = {"Home", "About", "Menu"};
        for (String item : menuItems) {
            Button menuItem = new Button(item);
            menuItem.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            menu.getChildren().add(menuItem);
        }
        
        Button contactButton = new Button("Contact");
        contactButton.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: black; -fx-padding: 8 15;");
        
        HBox.setHgrow(menu, Priority.ALWAYS);
        navigation.getChildren().addAll(logoContainer, menu, contactButton);
        return navigation;
    }
    
    private VBox createTitleSection() {
        VBox titleSection = new VBox(10);
        titleSection.setAlignment(Pos.CENTER);
        
        Text title = new Text("Contactez nous");
        title.setFont(Font.font("Playfair Display", FontWeight.NORMAL, 48));
        
        Text subtitle = new Text("Apportez nous vos recommandations n'oubliez pas de nous noter");
        subtitle.setStyle("-fx-font-size: 14px; -fx-fill: #666666;");
        
        titleSection.getChildren().addAll(title, subtitle);
        return titleSection;
    }
    
    private VBox createContactForm() {
        VBox formContainer = new VBox(15);
        formContainer.setMaxWidth(400);
        
        // Name and Email in one row
        HBox nameEmailRow = new HBox(15);
        nameEmailRow.setAlignment(Pos.CENTER);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Nom");
        nameField.getStyleClass().add("form-field");
        HBox.setHgrow(nameField, Priority.ALWAYS);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("form-field");
        HBox.setHgrow(emailField, Priority.ALWAYS);
        
        nameEmailRow.getChildren().addAll(nameField, emailField);
        
        // Subject field
        TextField subjectField = new TextField();
        subjectField.setPromptText("Sujet");
        subjectField.getStyleClass().add("form-field");
        
        // Message area
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Message");
        messageArea.getStyleClass().add("form-field");
        messageArea.setPrefRowCount(4);
        
        // Send button
        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("send-button");
        sendButton.setMaxWidth(Double.MAX_VALUE);
        
        formContainer.getChildren().addAll(nameEmailRow, subjectField, messageArea, sendButton);
        return formContainer;
    }
    
    private HBox createContactInfo() {
        HBox contactInfo = new HBox(50);
        contactInfo.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");
        contactInfo.setAlignment(Pos.CENTER);
        
        // Numbers secours
        VBox numbersSection = new VBox(5);
        numbersSection.setAlignment(Pos.CENTER);
        Text numbersTitle = new Text("Numero secours:");
        Text numbersText = new Text("+22501438292494");
        numbersText.setStyle("-fx-fill: red;");
        numbersSection.getChildren().addAll(numbersTitle, numbersText);
        
        // Hours
        VBox hoursSection = new VBox(5);
        hoursSection.setAlignment(Pos.CENTER);
        Text hoursTitle = new Text("Hours:");
        Text weekHours = new Text("Mon-Fri: 06H-20H");
        Text weekendHours = new Text("Sat, Sun: 06H-18H");
        hoursSection.getChildren().addAll(hoursTitle, weekHours, weekendHours);
        
        // Location
        VBox locationSection = new VBox(5);
        locationSection.setAlignment(Pos.CENTER);
        Text locationTitle = new Text("Notre Localisation:");
        Text locationText = new Text("Carrefour Seka Seka");
        locationSection.getChildren().addAll(locationTitle, locationText);
        
        contactInfo.getChildren().addAll(numbersSection, hoursSection, locationSection);
        return contactInfo;
    }
    
    private VBox createFooter() {
        VBox footer = new VBox();
        footer.setStyle("-fx-background-color: #333333;");
        footer.setPadding(new Insets(50, 0, 50, 0));
        footer.setAlignment(Pos.CENTER);
        
        // Logo and description
        VBox logoSection = new VBox(10);
        logoSection.setAlignment(Pos.CENTER);
        
        ImageView footerLogo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo-white.png")));
        footerLogo.setFitHeight(40);
        footerLogo.setPreserveRatio(true);
        
        Text description = new Text("In the new era of technology we look a\nin the future with certainty and pride to\nfor our company and.");
        description.setStyle("-fx-fill: white; -fx-text-alignment: center;");
        
        // Social icons
        HBox socialIcons = new HBox(15);
        socialIcons.setAlignment(Pos.CENTER);
        String[] iconNames = {"twitter", "facebook", "instagram", "map"};
        for (String iconName : iconNames) {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/" + iconName + ".png")));
            icon.setFitHeight(20);
            icon.setPreserveRatio(true);
            socialIcons.getChildren().add(icon);
        }
        
        // Pages and Utility Pages
        HBox linksContainer = new HBox(100);
        linksContainer.setAlignment(Pos.CENTER);
        
        VBox pagesSection = createFooterLinkSection("Pages", 
            "Home", "About", "Menu", "Pricing", "Blog", "Contact", "Delivery");
        
        VBox utilitySection = createFooterLinkSection("Utility Pages", 
            "Start Here", "Styleguide", "Password Protected", "404 Not Found", 
            "Licenses", "Changelog", "View More");
        
        linksContainer.getChildren().addAll(pagesSection, utilitySection);
        
        // Copyright
        Text copyright = new Text("Copyright © 2023 Hashtag Developer. All Rights Reserved");
        copyright.setStyle("-fx-fill: white;");
        
        logoSection.getChildren().addAll(footerLogo, description, socialIcons);
        footer.getChildren().addAll(logoSection, linksContainer, copyright);
        
        return footer;
    }
    
    private VBox createFooterLinkSection(String title, String... links) {
        VBox section = new VBox(10);
        Text sectionTitle = new Text(title);
        sectionTitle.setStyle("-fx-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        
        VBox linkContainer = new VBox(5);
        for (String link : links) {
            Text linkText = new Text(link);
            linkText.setStyle("-fx-fill: white;");
            linkContainer.getChildren().add(linkText);
        }
        
        section.getChildren().addAll(sectionTitle, linkContainer);
        return section;
    }
}