package com.chezoli;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Optional;

public class OrderApp {
    private MainApp mainApp;
    private String category;
    private List<MenuItem> menuItems;
    private double totalAmount = 0.0;
    private Scene scene;
    private Label totalAmountLabel;

    public OrderApp(MainApp mainApp, String category, List<MenuItem> menuItems) {
        this.mainApp = mainApp;
        this.category = category;
        this.menuItems = menuItems;
    }

    public Scene createScene() {
        // Rafraîchir les articles depuis la base de données pour s'assurer d'avoir les dernières données
        try {
            com.chezoli.dao.MenuItemDAO menuItemDAO = new com.chezoli.dao.MenuItemDAO();
            this.menuItems = menuItemDAO.getAllMenuItems();
            System.out.println("Articles du menu rechargés dans OrderApp: " + menuItems.size());
            
            // Afficher les détails des articles pour diagnostic
            for (MenuItem item : menuItems) {
                System.out.println("Article chargé dans OrderApp - ID: " + item.getId() + 
                                 " | Nom: " + item.getName() + 
                                 " | Prix: " + item.getPrice() + 
                                 " | Catégorie: " + item.getCategory() + 
                                 " | Image: " + item.getImageUrl());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du rechargement des articles du menu: " + e.getMessage());
            e.printStackTrace();
        }
        
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
            createOrderContent(),
            createFooter()
        );
        
        scrollPane.setContent(contentContainer);
        root.getChildren().add(scrollPane);
        
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        return scene;
    }

    private VBox createOrderContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(50, 0, 50, 0));
        
        // Title section
        VBox titleSection = new VBox(10);
        titleSection.setAlignment(Pos.CENTER);
        
        Label title = new Label("Commander");
        title.getStyleClass().add("order-title");
        
        Label subtitle = new Label("Sélectionnez les articles que vous souhaitez commander");
        subtitle.getStyleClass().add("order-subtitle");
        
        titleSection.getChildren().addAll(title, subtitle);

        // Main content with menu items and cart
        HBox mainContent = new HBox(30);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(30, 50, 30, 50));

        // Menu items section (left side)
        VBox menuSection = new VBox(30);
        menuSection.setAlignment(Pos.TOP_CENTER);
        menuSection.setPrefWidth(800);

        // Categories buttons
        HBox categoriesBox = new HBox(15);
        categoriesBox.setAlignment(Pos.CENTER);
        
        Set<String> categories = menuItems.stream()
            .map(MenuItem::getCategory)
            .collect(Collectors.toSet());
        
        // Diagnostic: Afficher les catégories disponibles
        System.out.println("Catégories disponibles dans OrderApp: " + categories);
        
        for (String cat : categories) {
            Button categoryButton = new Button(cat);
            categoryButton.getStyleClass().add("category-button");
            if (cat.equals(category)) {
                categoryButton.getStyleClass().add("category-button-active");
            }
            categoryButton.setOnAction(e -> {
                mainApp.showOrder(cat, menuItems);
            });
            categoriesBox.getChildren().add(categoryButton);
        }
        
        // Menu items grid
        GridPane menuGrid = new GridPane();
        menuGrid.setAlignment(Pos.CENTER);
        menuGrid.setHgap(30);
        menuGrid.setVgap(30);
        
        int col = 0;
        int row = 0;
        
        List<MenuItem> filteredItems = category.equals("Tout") ? 
            menuItems : 
            menuItems.stream()
                .filter(item -> item.getCategory().equals(category))
                .collect(Collectors.toList());
        
        // Diagnostic: Afficher le nombre d'articles filtrés
        System.out.println("Nombre d'articles filtrés pour la catégorie '" + category + "': " + filteredItems.size());
        
        for (MenuItem item : filteredItems) {
            // Diagnostic: Afficher les détails de chaque article
            System.out.println("Article filtré: ID=" + item.getId() + ", Nom=" + item.getName() + ", Prix=" + item.getPrice() + ", Cat=" + item.getCategory() + ", Image=" + item.getImageUrl());
            
            VBox itemCard = createMenuItemCard(item);
            menuGrid.add(itemCard, col, row);
            
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        menuSection.getChildren().addAll(categoriesBox, menuGrid);
        
        // Cart section (right side)
        VBox cartSection = new VBox(20);
        cartSection.setAlignment(Pos.TOP_CENTER);
        cartSection.setPrefWidth(400);
        cartSection.getStyleClass().add("order-summary");
        
        Label cartTitle = new Label("Votre panier");
        cartTitle.getStyleClass().add("summary-title");
        
        ListView<MainApp.OrderItem> orderList = createOrderList();
        
        HBox totalBox = new HBox(10);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label totalLabel = new Label("Total:");
        totalLabel.getStyleClass().add("total-label");
        
        totalAmountLabel = new Label(String.format("%,.0f FCFA", mainApp.getCartTotal()));
        totalAmountLabel.getStyleClass().add("total-amount");
        
        totalBox.getChildren().addAll(totalLabel, totalAmountLabel);
        
        Button confirmOrderButton = new Button("Confirmer la commande");
        confirmOrderButton.getStyleClass().add("confirm-order-button");
        confirmOrderButton.setMaxWidth(Double.MAX_VALUE);
        confirmOrderButton.setOnAction(e -> {
            confirmOrder();
        });
        
        cartSection.getChildren().addAll(
            cartTitle,
            orderList,
            totalBox,
            confirmOrderButton
        );

        mainContent.getChildren().addAll(menuSection, cartSection);
        content.getChildren().addAll(titleSection, mainContent);
        return content;
    }

    private VBox createMenuItemCard(MenuItem menuItem) {
        VBox card = new VBox(15);
        card.getStyleClass().add("menu-item-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        
        try {
            // Logging pour le diagnostic
            System.out.println("Tentative de chargement de l'image pour: " + menuItem.getName() + " - Image URL: " + menuItem.getImageUrl());
            
            // Utiliser la nouvelle méthode pour obtenir le chemin de l'image
            String imagePath = menuItem.getImagePath();
            System.out.println("Chemin d'image trouvé: " + imagePath);
            
            Image image = null;
            try {
                if (imagePath != null) {
                    // Vérifier si c'est une URL externe (pour le cas de l'image de remplacement)
                    if (imagePath.startsWith("http")) {
                        image = new Image(imagePath);
                    } else {
                        // Pour les ressources internes
                        image = new Image(imagePath);
                    }
                } else {
                    // Fallback si l'image est null
                    try {
                        String defaultImagePath = getClass().getResource("/images/logo.png").toExternalForm();
                        image = new Image(defaultImagePath);
                    } catch (Exception e) {
                        System.out.println("Impossible de charger l'image par défaut");
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image avec le chemin: " + imagePath);
                e.printStackTrace();
            }
            
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.getStyleClass().add("menu-item-image");
            
            Label nameLabel = new Label(menuItem.getName());
            nameLabel.getStyleClass().add("menu-item-name");
            
            Label descriptionLabel = new Label(menuItem.getDescription());
            descriptionLabel.getStyleClass().add("menu-item-description");
            descriptionLabel.setWrapText(true);
            
            Label priceLabel = new Label(menuItem.getFormattedPrice());
            priceLabel.getStyleClass().add("menu-item-price");
            
            // Ajouter un label pour afficher l'ID (à des fins de débogage)
            Label idLabel = new Label("ID: " + menuItem.getId());
            idLabel.getStyleClass().add("menu-item-debug");
            idLabel.setVisible(false); // Cacher en production
            
            HBox quantityBox = new HBox(10);
            quantityBox.setAlignment(Pos.CENTER);
            
            Button minusButton = new Button("-");
            minusButton.getStyleClass().add("quantity-button");
            
            TextField quantityField = new TextField("1");
            quantityField.getStyleClass().add("quantity-field");
            quantityField.setPrefWidth(40);
            quantityField.setAlignment(Pos.CENTER);
            
            Button plusButton = new Button("+");
            plusButton.getStyleClass().add("quantity-button");
            
            quantityBox.getChildren().addAll(minusButton, quantityField, plusButton);
            
            Button addToOrderButton = new Button("Ajouter au panier");
            addToOrderButton.getStyleClass().add("add-to-order-button");
            addToOrderButton.setMaxWidth(Double.MAX_VALUE);
            
            minusButton.setOnAction(e -> {
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity > 1) {
                    quantityField.setText(String.valueOf(quantity - 1));
                }
            });
            
            plusButton.setOnAction(e -> {
                int quantity = Integer.parseInt(quantityField.getText());
                quantityField.setText(String.valueOf(quantity + 1));
            });
            
            addToOrderButton.setOnAction(e -> {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    mainApp.addToCart(menuItem, quantity);
                    quantityField.setText("1"); // Réinitialiser la quantité
                    updateTotalAmount();
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez entrer une quantité valide");
                    alert.showAndWait();
                }
            });
            
            card.getChildren().addAll(
                imageView,
                nameLabel,
                descriptionLabel,
                priceLabel,
                idLabel,
                quantityBox,
                addToOrderButton
            );
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'article: " + menuItem.getName() + " - " + e.getMessage());
            e.printStackTrace();
            
            // Créer une carte d'article même en cas d'erreur
            Label errorLabel = new Label("Erreur d'affichage");
            errorLabel.getStyleClass().add("error-label");
            
            Label nameLabel = new Label(menuItem.getName());
            nameLabel.getStyleClass().add("menu-item-name");
            
            Label priceLabel = new Label(menuItem.getFormattedPrice());
            priceLabel.getStyleClass().add("menu-item-price");
            
            Button addToOrderButton = new Button("Ajouter au panier");
            addToOrderButton.getStyleClass().add("add-to-order-button");
            addToOrderButton.setMaxWidth(Double.MAX_VALUE);
            
            addToOrderButton.setOnAction(e2 -> {
                mainApp.addToCart(menuItem, 1);
                updateTotalAmount();
            });
            
            card.getChildren().addAll(
                errorLabel,
                nameLabel,
                priceLabel,
                addToOrderButton
            );
        }
        
        return card;
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
        
        // Boutons de connexion/déconnexion
        HBox authButtons = new HBox(10);
        authButtons.setAlignment(Pos.CENTER_RIGHT);
        
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
        
        authButtons.getChildren().addAll(loginButton, logoutButton);
        
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        
        header.getChildren().addAll(logoContainer, leftSpacer, navMenu, rightSpacer, authButtons);
        return header;
    }

    private Node createFooter() {
        VBox footer = new VBox(20);
        footer.getStyleClass().add("footer");
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(50, 0, 50, 0));
        
        Label copyright = new Label("© 2024 CHEZ OLI. Tous droits réservés.");
        copyright.getStyleClass().add("footer-text");
        
        footer.getChildren().add(copyright);
        return footer;
    }

    private ListView<MainApp.OrderItem> createOrderList() {
        ListView<MainApp.OrderItem> orderList = new ListView<>(mainApp.getCartItems());
        orderList.getStyleClass().add("order-list");
        orderList.setPrefHeight(200);
        
        orderList.setCellFactory(param -> new ListCell<MainApp.OrderItem>() {
            private HBox content;
            private ImageView imageView;
            private VBox infoBox;
            private Label nameLabel;
            private Label quantityLabel;
            private Label priceLabel;
            private Button plusButton;
            private Button minusButton;
            private Button deleteButton;
            
            {
                content = new HBox(15);
                content.setAlignment(Pos.CENTER_LEFT);
                content.setPadding(new Insets(10));
                
                imageView = new ImageView();
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                imageView.getStyleClass().add("cart-item-image");
                
                infoBox = new VBox(5);
                infoBox.setAlignment(Pos.CENTER_LEFT);
                
                nameLabel = new Label();
                nameLabel.getStyleClass().add("cart-item-name");
                
                HBox quantityControls = new HBox(10);
                quantityControls.setAlignment(Pos.CENTER_LEFT);
                
                minusButton = new Button("-");
                minusButton.getStyleClass().add("quantity-button");
                
                quantityLabel = new Label();
                quantityLabel.getStyleClass().add("quantity-label");
                
                plusButton = new Button("+");
                plusButton.getStyleClass().add("quantity-button");
                
                priceLabel = new Label();
                priceLabel.getStyleClass().add("cart-item-price");
                
                deleteButton = new Button("×");
                deleteButton.getStyleClass().add("delete-button");
                
                quantityControls.getChildren().addAll(
                    minusButton,
                    quantityLabel,
                    plusButton
                );
                
                infoBox.getChildren().addAll(nameLabel, quantityControls);
                
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                
                content.getChildren().addAll(
                    imageView,
                    infoBox,
                    spacer,
                    priceLabel,
                    deleteButton
                );
            }
            
            @Override
            protected void updateItem(MainApp.OrderItem item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Utiliser getImagePath pour charger l'image avec le même système que dans le reste de l'application
                        String imagePath = item.getMenuItem().getImagePath();
                        System.out.println("Chargement de l'image du panier pour: " + item.getMenuItem().getName() + " - Chemin: " + imagePath);
                        
                        if (imagePath != null) {
                            // Vérifier si c'est une URL externe (pour le cas de l'image de remplacement)
                            if (imagePath.startsWith("http")) {
                                imageView.setImage(new Image(imagePath));
                            } else {
                                // Pour les ressources internes
                                imageView.setImage(new Image(imagePath));
                            }
                        } else {
                            // Fallback si l'image est null
                            String defaultPath = getClass().getResource("/images/logo.png").toExternalForm();
                            imageView.setImage(new Image(defaultPath));
                        }
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image dans le panier: " + e.getMessage());
                        // Tenter d'utiliser le placeholder
                        try {
                            imageView.setImage(new Image("https://dummyimage.com/60x60/cccccc/ffffff.png&text=No+Image"));
                        } catch (Exception ex) {
                            // Ne rien faire si même le placeholder échoue
                        }
                    }
                    
                    nameLabel.setText(item.getMenuItem().getName());
                    quantityLabel.setText(String.valueOf(item.getQuantity()));
                    priceLabel.setText(String.format("%,.0f FCFA", 
                        item.getMenuItem().getPrice() * item.getQuantity()));
                    
                    plusButton.setOnAction(e -> {
                        mainApp.updateCartItemQuantity(item, item.getQuantity() + 1);
                        updateTotalAmount();
                    });
                    minusButton.setOnAction(e -> {
                        mainApp.updateCartItemQuantity(item, item.getQuantity() - 1);
                        updateTotalAmount();
                    });
                    deleteButton.setOnAction(e -> {
                        mainApp.removeFromCart(item);
                        updateTotalAmount();
                    });
                    
                    setGraphic(content);
                }
            }
        });
        
        return orderList;
    }

    private void updateTotalAmount() {
        totalAmount = mainApp.getCartTotal();
        System.out.println("Mise à jour du total du panier: " + totalAmount + " FCFA");
        
        if (totalAmountLabel != null) {
            totalAmountLabel.setText(String.format("%,.0f FCFA", totalAmount));
        } else {
            System.err.println("ERREUR: totalAmountLabel est null");
            
            // Tenter de trouver le label dans la scène
            if (scene != null) {
                Label foundLabel = (Label) scene.lookup(".total-amount");
                if (foundLabel != null) {
                    foundLabel.setText(String.format("%,.0f FCFA", totalAmount));
                    System.out.println("Label trouvé et mis à jour dans la scène");
                } else {
                    System.err.println("Impossible de trouver le label dans la scène");
                }
            }
        }
    }

    private void confirmOrder() {
        // Vérifier que le panier n'est pas vide
        if (mainApp.getCartItems().isEmpty()) {
            showAlert("Erreur", "Votre panier est vide. Veuillez ajouter des articles avant de confirmer.");
            return;
        }
        
        // Vérifier que l'utilisateur est connecté
        if (!mainApp.isLoggedIn()) {
            showAlert("Erreur", "Vous devez être connecté pour passer une commande.");
            mainApp.showLogin();
            return;
        }
        
        // Afficher la fenêtre de sélection du mode de paiement
        Dialog<String> paymentDialog = new Dialog<>();
        paymentDialog.setTitle("Méthode de paiement");
        paymentDialog.setHeaderText("Choisissez votre mode de paiement");
        
        // Configurer les boutons
        ButtonType waveType = new ButtonType("Wave", ButtonBar.ButtonData.LEFT);
        ButtonType orangeType = new ButtonType("Orange Money", ButtonBar.ButtonData.LEFT);
        ButtonType cashType = new ButtonType("Espèces (sur place)", ButtonBar.ButtonData.LEFT);
        ButtonType cancelType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        paymentDialog.getDialogPane().getButtonTypes().addAll(waveType, orangeType, cashType, cancelType);
        
        // Ajouter le logo pour chaque méthode de paiement
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Montant total à payer: " + String.format("%,.0f FCFA", mainApp.getCartTotal()));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Créer une zone pour les options de paiement
        HBox paymentOptions = new HBox(30);
        paymentOptions.setAlignment(Pos.CENTER);
        
        // Wave option
        VBox waveOption = createPaymentOption("Wave", "/images/wave_logo.png", "Payer avec Wave");
        
        // Orange Money option
        VBox orangeOption = createPaymentOption("Orange Money", "/images/orange_money_logo.png", "Payer avec Orange Money");
        
        // Cash option
        VBox cashOption = createPaymentOption("Espèces", "/images/cash_icon.png", "Payer en espèces à la livraison");
        
        paymentOptions.getChildren().addAll(waveOption, orangeOption, cashOption);
        layout.getChildren().addAll(titleLabel, paymentOptions);
        
        paymentDialog.getDialogPane().setContent(layout);
        
        // Convertir le résultat
        paymentDialog.setResultConverter(dialogButton -> {
            if (dialogButton == waveType) {
                return "Wave";
            } else if (dialogButton == orangeType) {
                return "Orange Money";
            } else if (dialogButton == cashType) {
                return "Espèces";
            }
            return null;
        });
        
        Optional<String> result = paymentDialog.showAndWait();
        
        result.ifPresent(paymentMethod -> {
            if ("Wave".equals(paymentMethod) || "Orange Money".equals(paymentMethod)) {
                showQRCodePayment(paymentMethod);
            } else if ("Espèces".equals(paymentMethod)) {
                processOrder("Espèces");
            }
        });
    }

    private VBox createPaymentOption(String title, String imagePath, String description) {
        VBox option = new VBox(10);
        option.setAlignment(Pos.CENTER);
        option.setPadding(new Insets(10));
        option.getStyleClass().add("payment-option");
        
        ImageView logo = new ImageView();
        try {
            Image image = null;
            // Essayer de charger l'image à partir des ressources
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                image = new Image(imageUrl.toExternalForm());
            } else {
                // Fallback vers une image générique
                image = new Image("https://dummyimage.com/80x80/ccc/fff.png&text=" + title);
            }
            logo.setImage(image);
            logo.setFitWidth(80);
            logo.setFitHeight(80);
            logo.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image " + imagePath + ": " + e.getMessage());
        }
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold;");
        
        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(TextAlignment.CENTER);
        
        option.getChildren().addAll(logo, titleLabel, descLabel);
        return option;
    }

    private void showQRCodePayment(String paymentMethod) {
        Dialog<Boolean> qrDialog = new Dialog<>();
        qrDialog.setTitle("Paiement par " + paymentMethod);
        qrDialog.setHeaderText("Scannez le QR code pour payer");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        
        Label amountLabel = new Label("Montant: " + String.format("%,.0f FCFA", mainApp.getCartTotal()));
        amountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // QR Code (simulé)
        ImageView qrCodeView = new ImageView();
        try {
            // Générer un QR code avec le texte contenant les infos de paiement
            String qrData = "Paiement Le Chezoli\n" + 
                             "Montant: " + mainApp.getCartTotal() + " FCFA\n" +
                             "Référence: ORDER-" + System.currentTimeMillis();
            
            // Essayer de charger une image de QR code générique
            Image qrImage = new Image("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + 
                                       URLEncoder.encode(qrData, "UTF-8"));
            qrCodeView.setImage(qrImage);
        } catch (Exception e) {
            // Fallback en cas d'erreur de chargement du QR
            try {
                qrCodeView.setImage(new Image(getClass().getResource("/images/qr_code_sample.png").toExternalForm()));
            } catch (Exception ex) {
                System.err.println("Erreur lors du chargement du QR code: " + ex.getMessage());
            }
        }
        
        Label instructionLabel = new Label("1. Ouvrez votre application " + paymentMethod + "\n" +
                                           "2. Scannez ce code QR\n" +
                                           "3. Confirmez le paiement sur votre téléphone");
        instructionLabel.setWrapText(true);
        
        Button confirmButton = new Button("J'ai payé");
        confirmButton.setOnAction(e -> {
            qrDialog.setResult(Boolean.TRUE);
            qrDialog.close();
        });
        
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(e -> {
            qrDialog.setResult(Boolean.FALSE);
            qrDialog.close();
        });
        
        HBox buttons = new HBox(20, confirmButton, cancelButton);
        buttons.setAlignment(Pos.CENTER);
        
        content.getChildren().addAll(amountLabel, qrCodeView, instructionLabel, buttons);
        qrDialog.getDialogPane().setContent(content);
        
        // Ajouter un bouton par défaut pour que la fenêtre puisse être fermée
        qrDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = qrDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        
        Optional<Boolean> result = qrDialog.showAndWait();
        
        if (result.isPresent() && result.get()) {
            // L'utilisateur a confirmé le paiement
            processOrder(paymentMethod);
        }
    }

    private void processOrder(String paymentMethod) {
        // Créer et enregistrer la commande dans la base de données
        Order order = new Order();
        order.setUserId(mainApp.getCurrentUser().getUsername());
        order.setOrderDate(new Date());
        order.setStatus("En attente");
        order.setPaymentMethod(paymentMethod);
        
        // Utiliser directement les OrderItem de MainApp
        order.setItems(new ArrayList<>(mainApp.getCartItems()));
        
        if (mainApp.getOrderDAO().createOrder(order)) {
            showAlert("Succès", "Votre commande a été enregistrée avec succès!\n" +
                               "Méthode de paiement: " + paymentMethod + "\n" +
                               "Vous pouvez suivre l'état de votre commande dans votre profil.");
            mainApp.clearCart();
            updateTotalAmount();
        } else {
            showAlert("Erreur", "Un problème est survenu lors de l'enregistrement de votre commande. Veuillez réessayer.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 