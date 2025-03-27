package com.chezoli;

import com.chezoli.dao.UserDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.Node;

public class LoginApp {
    private MainApp mainApp;
    private UserDAO userDAO;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;

    public LoginApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.userDAO = new UserDAO();
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
            createLoginContent(),
            createFooter()
        );
        
        scrollPane.setContent(contentContainer);
        root.getChildren().add(scrollPane);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        return scene;
    }

    private VBox createLoginContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(50, 0, 50, 0));
        
        // Title section
        VBox titleSection = new VBox(10);
        titleSection.setAlignment(Pos.CENTER);
        
        Label title = new Label("Connexion");
        title.getStyleClass().add("login-title");
        
        Label subtitle = new Label("Connectez-vous pour commander");
        subtitle.getStyleClass().add("login-subtitle");
        
        titleSection.getChildren().addAll(title, subtitle);

        // Form section
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.CENTER);
        formSection.setMaxWidth(400);
        formSection.getStyleClass().add("login-form");

        usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        usernameField.getStyleClass().add("login-input");

        passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.getStyleClass().add("login-input");

        Button loginButton = new Button("Se connecter");
        loginButton.getStyleClass().add("login-button");
        
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        loginButton.setOnAction(e -> {
            handleLogin();
        });

        Hyperlink signUpLink = new Hyperlink("Pas encore inscrit ? Créez un compte");
        signUpLink.getStyleClass().add("signup-link");
        signUpLink.setOnAction(e -> mainApp.showSignUp());

        formSection.getChildren().addAll(
            usernameField,
            passwordField,
            errorLabel,
            loginButton,
            signUpLink
        );

        content.getChildren().addAll(titleSection, formSection);
        return content;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        try {
            // Vérifier si l'utilisateur existe dans la base de données via UserDAO
            if (mainApp.getUserDAO().authenticate(username, password)) {
                User user = mainApp.getUserDAO().getUser(username);
                mainApp.setCurrentUser(user);
                mainApp.setLoggedIn(true);
                
                // Vérifier si c'est un administrateur et rediriger en conséquence
                if ("ADMIN".equalsIgnoreCase(user.getRole()) || 
                    "Administrateur".equalsIgnoreCase(user.getRole())) {
                    System.out.println("Connexion administrateur réussie: " + user.getUsername() + " - Rôle: " + user.getRole());
                    mainApp.showAdmin();
                } else {
                    mainApp.showHome();
                }
            } else if ("admin".equals(username) && "admin123".equals(password)) {
                // Authentification spéciale pour l'admin par défaut
                User adminUser = new User("admin", "admin123", "ADMIN");
                mainApp.setCurrentUser(adminUser);
                mainApp.setLoggedIn(true);
                System.out.println("Connexion admin par défaut réussie");
                mainApp.showAdmin();
            } else {
                showError("Nom d'utilisateur ou mot de passe incorrect");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de la connexion: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
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
                    case "Contact":
                        mainApp.showContact();
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

        footerLeft.getChildren().addAll(logoBox, footerText);

        HBox footerColumns = new HBox(80);
        footerColumns.getStyleClass().add("footer-columns");
        footerColumns.setAlignment(Pos.TOP_LEFT);

        VBox pagesColumn = createFooterColumn("Pages", 
            new String[]{"Home", "About", "Menu", "Contact"});

        VBox utilityColumn = createFooterColumn("Utility Pages", 
            new String[]{"Start Here", "Styleguide", "Password Protected", "404 Not Found", "Licenses", "Changelog", "View More"});

        footerColumns.getChildren().addAll(pagesColumn, utilityColumn);

        footerContent.getChildren().addAll(footerLeft, footerColumns);

        VBox copyrightSection = new VBox();
        copyrightSection.setAlignment(Pos.CENTER);
        copyrightSection.setPadding(new Insets(20, 0, 0, 0));
        copyrightSection.getStyleClass().add("copyright-section");

        Label copyright = new Label("Copyright © 2024 CHEZ OLI. All Rights Reserved.");
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