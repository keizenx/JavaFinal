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
import com.chezoli.dao.ContactMessageDAO;

public class ContactApp {
    private MainApp mainApp;
    private Scene scene;
    private ContactMessageDAO contactMessageDAO;

    public ContactApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.contactMessageDAO = new ContactMessageDAO();
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
        Text phoneIcon = new Text("📞");
        phoneIcon.getStyleClass().add("top-bar-icon");
        Label phoneNumber = new Label("(414) 857 - 0107");
        phoneNumber.getStyleClass().add("top-bar-text");
        phoneBox.getChildren().addAll(phoneIcon, phoneNumber);
        
        // Email
        HBox emailBox = new HBox(5);
        emailBox.setAlignment(Pos.CENTER);
        Text emailIcon = new Text("✉");
        emailIcon.getStyleClass().add("top-bar-icon");
        Label emailAddress = new Label("yummy@bistrobliss");
        emailAddress.getStyleClass().add("top-bar-text");
        emailBox.getChildren().addAll(emailIcon, emailAddress);
        
        leftSide.getChildren().addAll(phoneBox, emailBox);
        
        // Right side - social icons
        HBox rightSide = new HBox(20);
        rightSide.getStyleClass().addAll("top-bar-right", "social-icons");
        
        String[] socialIcons = {"🐦", "📘", "📸", "📍"};  // Twitter, Facebook, Instagram, Map marker
        for (String icon : socialIcons) {
            Text socialIcon = new Text(icon);
            socialIcon.getStyleClass().addAll("social-icon");
            socialIcon.setOnMouseClicked(e -> {
                // Handle social media clicks
                switch(icon) {
                    case "🐦": openWebPage("https://twitter.com/bistrobliss"); break;
                    case "📘": openWebPage("https://facebook.com/bistrobliss"); break;
                    case "📸": openWebPage("https://instagram.com/bistrobliss"); break;
                    case "📍": openWebPage("https://maps.google.com/?q=bistrobliss"); break;
                }
            });
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
    
    private VBox createContactContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(50, 0, 50, 0));
        
        // Title section
        VBox titleSection = new VBox(10);
        titleSection.setAlignment(Pos.CENTER);
        
        Label title = new Label("Contactez-nous");
        title.getStyleClass().add("contact-title");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        
        Label subtitle = new Label("N'hésitez pas à nous contacter pour toute question ou suggestion");
        subtitle.getStyleClass().add("contact-subtitle");
        subtitle.setFont(Font.font("Arial", 16));
        
        titleSection.getChildren().addAll(title, subtitle);
        
        // Contact info and form container
        HBox contactContainer = new HBox(40);
        contactContainer.setAlignment(Pos.CENTER);
        contactContainer.setMaxWidth(1000);
        
        // Contact info section
        VBox contactInfo = new VBox(20);
        contactInfo.getStyleClass().add("contact-info");
        contactInfo.setPadding(new Insets(30));
        contactInfo.setPrefWidth(400);
        
        Label infoTitle = new Label("Informations de Contact");
        infoTitle.getStyleClass().add("info-title");
        infoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        VBox infoItems = new VBox(15);
        infoItems.getChildren().addAll(
            createInfoItem("📍", "123 Rue de l'Innovation, 75000 Paris"),
            createInfoItem("📞", "+33 1 23 45 67 89"),
            createInfoItem("✉", "contact@bistrobliss.com"),
            createInfoItem("⏰", "Lundi - Vendredi: 9h00 - 18h00")
        );
        
        contactInfo.getChildren().addAll(infoTitle, infoItems);
        
        // Form section
        VBox formSection = new VBox(20);
        formSection.getStyleClass().add("form-container");
        formSection.setPrefWidth(500);
        formSection.setPadding(new Insets(30));
        
        Label formTitle = new Label("Envoyez-nous un message");
        formTitle.getStyleClass().add("form-title");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Votre nom");
        nameField.getStyleClass().add("text-field");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Votre email");
        emailField.getStyleClass().add("text-field");
        
        TextField subjectField = new TextField();
        subjectField.setPromptText("Sujet");
        subjectField.getStyleClass().add("text-field");
        
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Votre message");
        messageArea.getStyleClass().add("text-area");
        messageArea.setPrefRowCount(5);
        
        Button sendButton = new Button("Envoyer le message");
        sendButton.getStyleClass().add("send-button");
        sendButton.setMaxWidth(Double.MAX_VALUE);
        sendButton.setOnAction(e -> {
            // Handle form submission
            if (validateForm(nameField, emailField, subjectField, messageArea)) {
                showSuccessMessage();
            }
        });
        
        formSection.getChildren().addAll(
            formTitle,
            nameField,
            emailField,
            subjectField,
            messageArea,
            sendButton
        );
        
        contactContainer.getChildren().addAll(contactInfo, formSection);
        
        content.getChildren().addAll(titleSection, contactContainer);
        return content;
    }
    
    private HBox createInfoItem(String icon, String text) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Text iconText = new Text(icon);
        iconText.getStyleClass().add("info-icon");
        
        Label textLabel = new Label(text);
        textLabel.getStyleClass().add("info-text");
        
        item.getChildren().addAll(iconText, textLabel);
        return item;
    }
    
    private boolean validateForm(TextField name, TextField email, TextField subject, TextArea message) {
        boolean isValid = true;
        
        if (name.getText().trim().isEmpty()) {
            showError(name, "Le nom est requis");
            isValid = false;
        }
        
        if (email.getText().trim().isEmpty()) {
            showError(email, "L'email est requis");
            isValid = false;
        } else if (!email.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError(email, "Format d'email invalide");
            isValid = false;
        }
        
        if (subject.getText().trim().isEmpty()) {
            showError(subject, "Le sujet est requis");
            isValid = false;
        }
        
        if (message.getText().trim().isEmpty()) {
            showError(message, "Le message est requis");
            isValid = false;
        }

        if (isValid) {
            // Sauvegarder le message dans la base de données
            contactMessageDAO.createContactMessage(
                name.getText().trim(),
                email.getText().trim(),
                subject.getText().trim(),
                message.getText().trim()
            );
        }
        
        return isValid;
    }
    
    private void showError(Control field, String message) {
        field.getStyleClass().add("error");
        Tooltip tooltip = new Tooltip(message);
        field.setTooltip(tooltip);
    }
    
    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Votre message a été envoyé avec succès!");
        alert.showAndWait();
    }
    
    private void openWebPage(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Node createFooter() {
        VBox footer = new VBox(20);
        footer.getStyleClass().add("footer");
        footer.setPadding(new Insets(40, 0, 20, 0));
        
        HBox footerContent = new HBox(40);
        footerContent.setAlignment(Pos.CENTER);
        
        footerContent.getChildren().addAll(
            createFooterColumn("À propos", new String[]{"Notre histoire", "Notre équipe", "Nos valeurs", "Carrières"}),
            createFooterColumn("Services", new String[]{"Menu", "Réservations", "Événements", "Catering"}),
            createFooterColumn("Contact", new String[]{"Adresse", "Téléphone", "Email", "Horaires"})
        );
        
        footer.getChildren().addAll(footerContent);
        return footer;
    }
    
    private VBox createFooterColumn(String title, String[] links) {
        VBox column = new VBox(10);
        column.setAlignment(Pos.TOP_CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("footer-title");
        
        VBox linksBox = new VBox(5);
        for (String link : links) {
            Label linkLabel = new Label(link);
            linkLabel.getStyleClass().add("footer-link");
            linkLabel.setOnMouseClicked(e -> {
                // Handle footer link clicks
            });
            linksBox.getChildren().add(linkLabel);
        }
        
        column.getChildren().addAll(titleLabel, linksBox);
        return column;
    }
} 