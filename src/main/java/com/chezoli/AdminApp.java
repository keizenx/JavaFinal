package com.chezoli;

import com.chezoli.dao.MenuItemDAO;
import com.chezoli.dao.UserDAO;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;
import com.chezoli.dao.OrderDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public class AdminApp {
    private MainApp mainApp;
    private Scene scene;
    private TabPane tabPane;
    private Tab menuManagementTab;
    private Tab userManagementTab;
    private Tab orderManagementTab;
    private MenuItemDAO menuItemDAO;
    private UserDAO userDAO;
    private TableView<MenuItem> menuTable;
    private TableView<User> userTable;
    private TableView<MenuItem> menuItemsTable;

    // Variables for menu item form
    private TextField nameField;
    private TextField descField;
    private TextField priceField;
    private ComboBox<String> categoryCombo;
    private Label imageLabel;
    private AtomicReference<String> selectedImagePath;
    private Button addButton;
    private Button cancelButton;

    public AdminApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.menuItemDAO = new MenuItemDAO();
        this.userDAO = new UserDAO();
        createScene();
    }

    public Scene getScene() {
        return scene;
    }

    private void refreshMenuTable() {
        if (menuTable != null) {
            List<MenuItem> items = menuItemDAO.getAllMenuItems();
            menuTable.setItems(FXCollections.observableArrayList(items));
        }
    }

    public Scene createScene() {
        VBox root = new VBox();
        root.getStyleClass().add("admin-root");

        // En-tête
        HBox header = new HBox(10);
        header.getStyleClass().add("admin-header");
        header.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("← Retour au site");
        backButton.getStyleClass().add("admin-back-button");
        backButton.setOnAction(e -> mainApp.showHome());

        Label titleLabel = new Label("Administration");
        titleLabel.getStyleClass().add("admin-title");

        header.getChildren().addAll(backButton, titleLabel);

        // Statistiques
        HBox statsBox = new HBox(20);
        statsBox.getStyleClass().add("admin-stats");
        statsBox.setAlignment(Pos.CENTER);

        statsBox.getChildren().addAll(
            createStatCard("Plats au menu", "25"),
            createStatCard("Utilisateurs", String.valueOf(mainApp.getUsers().size())),
            createStatCard("Commandes", String.valueOf(mainApp.getOrders().size())),
            createStatCard("Chiffre d'affaires", formatPrice(calculateTotalRevenue()))
        );

        // Onglets
        tabPane = new TabPane();
        tabPane.getStyleClass().add("admin-tab-pane");
        
        // Initialiser les onglets
        menuManagementTab = new Tab("Gestion des plats");
        menuManagementTab.setClosable(false);
        setupMenuManagementTab();
        
        userManagementTab = new Tab("Gestion des Utilisateurs");
        userManagementTab.setClosable(false);
        userManagementTab.setContent(createUserManagementTab());
        
        orderManagementTab = new Tab("Gestion des Commandes");
        orderManagementTab.setClosable(false);
        setupOrderManagementTab();
        
        tabPane.getTabs().addAll(menuManagementTab, userManagementTab, orderManagementTab);
        
        root.getChildren().addAll(header, statsBox, tabPane);
        
        scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        return scene;
    }

    private VBox createStatCard(String label, String value) {
        VBox card = new VBox(5);
        card.getStyleClass().add("stat-card");
        card.setAlignment(Pos.CENTER);
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");
        
        Label descLabel = new Label(label);
        descLabel.getStyleClass().add("stat-label");
        
        card.getChildren().addAll(valueLabel, descLabel);
        return card;
    }

    private double calculateTotalRevenue() {
        return mainApp.getOrders().stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    private String formatPrice(double price) {
        return String.format("%,.0f FCFA", price);
    }

    private void setupMenuManagementTab() {
        // Liste des plats
        menuItemsTable = new TableView<>();
        menuItemsTable.setEditable(true);

        TableColumn<MenuItem, String> nameCol = new TableColumn<>("Nom");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setName(event.getNewValue());
            menuItemDAO.updateMenuItem(item);
            refreshMenuItems();
        });

        TableColumn<MenuItem, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setDescription(event.getNewValue());
            menuItemDAO.updateMenuItem(item);
            refreshMenuItems();
        });

        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Prix");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceCol.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setPrice(event.getNewValue());
            menuItemDAO.updateMenuItem(item);
            refreshMenuItems();
        });

        TableColumn<MenuItem, String> categoryCol = new TableColumn<>("Catégorie");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryCol.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setCategory(event.getNewValue());
            menuItemDAO.updateMenuItem(item);
            refreshMenuItems();
        });

        TableColumn<MenuItem, String> imageCol = new TableColumn<>("Image URL");
        imageCol.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        imageCol.setCellFactory(TextFieldTableCell.forTableColumn());
        imageCol.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setImageUrl(event.getNewValue());
            menuItemDAO.updateMenuItem(item);
            refreshMenuItems();
        });

        TableColumn<MenuItem, Void> deleteCol = new TableColumn<>("Actions");
        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button editButton = new Button("Modifier");
            private final HBox buttonsBox = new HBox(5);

            {
                deleteButton.setOnAction(event -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    if (showConfirmDialog("Supprimer le plat", "Êtes-vous sûr de vouloir supprimer ce plat ?")) {
                        menuItemDAO.deleteMenuItem(item.getId());
                        refreshMenuItems();
                    }
                });
                
                editButton.setOnAction(event -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    // Remplir le formulaire avec les valeurs de l'item
                    nameField.setText(item.getName());
                    descField.setText(item.getDescription());
                    priceField.setText(String.valueOf(item.getPrice()));
                    categoryCombo.setValue(item.getCategory());
                    selectedImagePath.set(item.getImageUrl());
                    imageLabel.setText(item.getImageUrl().substring(item.getImageUrl().lastIndexOf("/") + 1));
                    
                    // Changer le bouton "Ajouter" en "Mettre à jour"
                    addButton.setText("Mettre à jour");
                    
                    // Stocker l'ID de l'élément à modifier
                    addButton.setUserData(item.getId());
                    
                    // Rendre visible le bouton d'annulation
                    cancelButton.setVisible(true);
                });
                
                buttonsBox.getChildren().addAll(editButton, deleteButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }
        });

        menuItemsTable.getColumns().addAll(nameCol, descCol, priceCol, categoryCol, imageCol, deleteCol);

        // Formulaire d'ajout
        nameField = new TextField();
        nameField.setPromptText("Nom du plat");
        descField = new TextField();
        descField.setPromptText("Description");
        priceField = new TextField();
        priceField.setPromptText("Prix");
        categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Entrées", "Plats Principaux", "Desserts", "Boissons");
        categoryCombo.setPromptText("Catégorie");

        Button imageButton = new Button("Choisir une image");
        imageLabel = new Label("Aucune image sélectionnée");
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        
        selectedImagePath = new AtomicReference<>();
        
        imageButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
            if (file != null) {
                try {
                    // Créer le dossier s'il n'existe pas
                    File resourcesDir = new File("src/main/resources/images/Notre_Menu");
                    if (!resourcesDir.exists()) {
                        resourcesDir.mkdirs();
                    }
                    
                    // Copier l'image dans le dossier des ressources
                    String fileName = file.getName();
                    Path destination = resourcesDir.toPath().resolve(fileName);
                    Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                    
                    // Stocker simplement le nom du fichier, sans le chemin complet
                    selectedImagePath.set(fileName);
                    imageLabel.setText(fileName);
                    
                    System.out.println("Image copiée vers: " + destination.toAbsolutePath());
                    System.out.println("Nom d'image enregistré: " + fileName);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Erreur lors de la copie de l'image : " + ex.getMessage());
                    alert.showAndWait();
                }
            }
        });

        addButton = new Button("Ajouter");
        addButton.setOnAction(e -> {
            try {
                // Validate category is selected
                if (categoryCombo.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner une catégorie");
                    alert.showAndWait();
                    return;
                }
                
                // Validate image is selected
                if (selectedImagePath.get() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez sélectionner une image");
                    alert.showAndWait();
                    return;
                }
                
                double price = Double.parseDouble(priceField.getText());
                
                // Vérifier si nous sommes en train de mettre à jour un plat existant
                if (addButton.getUserData() != null) {
                    // Mode mise à jour
                    int itemId = (int) addButton.getUserData();
                    MenuItem updatedItem = new MenuItem(
                        nameField.getText(),
                        descField.getText(),
                        price,
                        categoryCombo.getValue(),
                        selectedImagePath.get()
                    );
                    updatedItem.setId(itemId);
                    
                    if (menuItemDAO.updateMenuItem(updatedItem)) {
                        // Réinitialiser le formulaire
                        nameField.clear();
                        descField.clear();
                        priceField.clear();
                        categoryCombo.setValue(null);
                        selectedImagePath.set(null);
                        imageLabel.setText("Aucune image sélectionnée");
                        addButton.setText("Ajouter");
                        addButton.setUserData(null);
                        refreshMenuItems();
                        
                        showAlert("Succès", "Le plat a été mis à jour avec succès");
                    }
                } else {
                    // Mode ajout
                    MenuItem newItem = new MenuItem(
                        nameField.getText(),
                        descField.getText(),
                        price,
                        categoryCombo.getValue(),
                        selectedImagePath.get()
                    );
                    
                    if (menuItemDAO.addMenuItem(newItem)) {
                        nameField.clear();
                        descField.clear();
                        priceField.clear();
                        categoryCombo.setValue(null);
                        selectedImagePath.set(null);
                        imageLabel.setText("Aucune image sélectionnée");
                        refreshMenuItems();
                    }
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le prix doit être un nombre valide");
                alert.showAndWait();
            }
        });
        
        // Bouton d'annulation pour l'édition
        cancelButton = new Button("Annuler");
        cancelButton.setVisible(false); // Caché par défaut
        cancelButton.setOnAction(e -> {
            // Réinitialiser le formulaire
            nameField.clear();
            descField.clear();
            priceField.clear();
            categoryCombo.setValue(null);
            selectedImagePath.set(null);
            imageLabel.setText("Aucune image sélectionnée");
            addButton.setText("Ajouter");
            addButton.setUserData(null);
            cancelButton.setVisible(false);
        });
        
        // Mettre le bouton d'annulation à côté du bouton d'ajout
        HBox buttonsBox = new HBox(10, addButton, cancelButton);

        VBox addForm = new VBox(10);
        addForm.setPadding(new Insets(10));
        addForm.getChildren().addAll(
            new Label("Ajouter un nouveau plat"),
            nameField,
            descField,
            priceField,
            categoryCombo,
            new HBox(10, imageButton, imageLabel),
            buttonsBox
        );

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(menuItemsTable, addForm);

        menuManagementTab.setContent(layout);
        
        refreshMenuItems();
    }

    private void refreshMenuItems() {
        menuItemsTable.getItems().setAll(menuItemDAO.getAllMenuItems());
    }

    private VBox createUserManagementTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label sectionTitle = new Label("Gestion des Utilisateurs");
        sectionTitle.getStyleClass().add("admin-section-title");
        
        // Formulaire d'ajout d'utilisateur
        VBox addUserForm = new VBox(15);
        addUserForm.getStyleClass().add("admin-form");
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("USER", "ADMIN");
        roleCombo.setPromptText("Sélectionnez un rôle");
        
        Button addButton = new Button("Ajouter l'utilisateur");
        addButton.getStyleClass().add("admin-success-button");
        
        formGrid.addRow(0, new Label("Nom d'utilisateur:"), usernameField);
        formGrid.addRow(1, new Label("Mot de passe:"), passwordField);
        formGrid.addRow(2, new Label("Rôle:"), roleCombo);
        
        addButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String role = roleCombo.getValue();
                
                if (username.isEmpty() || password.isEmpty() || role == null) {
                    showAlert("Erreur", "Tous les champs sont obligatoires");
                    return;
                }
                
                User newUser = new User(username, password, role);
                if (userDAO.createUser(newUser)) {
                    showAlert("Succès", "L'utilisateur a été ajouté avec succès");
                    refreshUserTable();
                    clearUserForm(usernameField, passwordField, roleCombo);
                } else {
                    showAlert("Erreur", "Impossible d'ajouter l'utilisateur");
                }
            } catch (Exception ex) {
                showAlert("Erreur", "Une erreur est survenue: " + ex.getMessage());
            }
        });
        
        addUserForm.getChildren().addAll(new Label("Ajouter un nouvel utilisateur"), formGrid, addButton);
        
        // Table des utilisateurs
        userTable = new TableView<>();
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<User, String> usernameCol = new TableColumn<>("Nom d'utilisateur");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<User, String> roleCol = new TableColumn<>("Rôle");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        TableColumn<User, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Supprimer");
            {
                deleteBtn.getStyleClass().add("admin-delete-button");
                deleteBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (showConfirmDialog("Supprimer l'utilisateur", 
                        "Êtes-vous sûr de vouloir supprimer cet utilisateur ?")) {
                        if (userDAO.deleteUser(user.getUsername())) {
                            refreshUserTable();
                        }
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
        
        userTable.getColumns().addAll(usernameCol, roleCol, actionsCol);
        refreshUserTable();
        
        content.getChildren().addAll(sectionTitle, addUserForm, new Label("Liste des utilisateurs"), userTable);
        return content;
    }

    private void refreshUserTable() {
        if (userTable != null) {
            List<User> users = userDAO.getAllUsers();
            userTable.setItems(FXCollections.observableArrayList(users));
        }
    }

    private void clearUserForm(TextField usernameField, PasswordField passwordField, ComboBox<String> roleCombo) {
        usernameField.clear();
        passwordField.clear();
        roleCombo.setValue(null);
    }

    private void setupOrderManagementTab() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // En-tête
        Label header = new Label("Gestion des commandes");
        header.getStyleClass().add("admin-section-title");
        
        // Tableau des commandes
        TableView<Order> orderTable = new TableView<>();
        orderTable.setEditable(true);
        
        TableColumn<Order, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getId()));
        
        TableColumn<Order, String> userCol = new TableColumn<>("Utilisateur");
        userCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUserId()));
        
        TableColumn<Order, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOrderDate().toString()));
        
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(param -> {
            double total = param.getValue().getItems().stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
            return new SimpleObjectProperty<>(total);
        });
        totalCol.setCellFactory(column -> {
            return new TableCell<Order, Double>() {
                @Override
                protected void updateItem(Double total, boolean empty) {
                    super.updateItem(total, empty);
                    if (empty || total == null) {
                        setText(null);
                    } else {
                        setText(String.format("%,.0f FCFA", total));
                    }
                }
            };
        });
        
        TableColumn<Order, String> statusCol = new TableColumn<>("Statut");
        statusCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus()));
        statusCol.setCellFactory(tc -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch(status.toLowerCase()) {
                        case "en attente":
                            setStyle("-fx-text-fill: orange;");
                            break;
                        case "confirmée":
                            setStyle("-fx-text-fill: blue;");
                            break;
                        case "en préparation":
                            setStyle("-fx-text-fill: purple;");
                            break;
                        case "prête":
                            setStyle("-fx-text-fill: green;");
                            break;
                        case "livrée":
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "annulée":
                            setStyle("-fx-text-fill: red;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        
        TableColumn<Order, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(param -> {
            return new TableCell<Order, Void>() {
                private final HBox buttons = new HBox(5);
                private final Button detailsButton = new Button("Détails");
                private final Button confirmButton = new Button("Confirmer");
                private final Button prepareButton = new Button("En préparation");
                private final Button readyButton = new Button("Prête");
                private final Button cancelButton = new Button("Annuler");
                
                {
                    detailsButton.setOnAction(event -> {
                        Order order = getTableView().getItems().get(getIndex());
                        showOrderDetails(order);
                    });
                    
                    confirmButton.setOnAction(event -> {
                        Order order = getTableView().getItems().get(getIndex());
                        if ("En attente".equalsIgnoreCase(order.getStatus())) {
                            updateOrderStatus(order, "Confirmée");
                        }
                    });
                    
                    prepareButton.setOnAction(event -> {
                        Order order = getTableView().getItems().get(getIndex());
                        if ("Confirmée".equalsIgnoreCase(order.getStatus())) {
                            updateOrderStatus(order, "En préparation");
                        }
                    });
                    
                    readyButton.setOnAction(event -> {
                        Order order = getTableView().getItems().get(getIndex());
                        if ("En préparation".equalsIgnoreCase(order.getStatus())) {
                            updateOrderStatus(order, "Prête");
                        }
                    });
                    
                    cancelButton.setOnAction(event -> {
                        Order order = getTableView().getItems().get(getIndex());
                        if (!("Livrée".equalsIgnoreCase(order.getStatus()) || "Annulée".equalsIgnoreCase(order.getStatus()))) {
                            updateOrderStatus(order, "Annulée");
                        }
                    });
                    
                    buttons.getChildren().addAll(detailsButton);
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Order order = getTableView().getItems().get(getIndex());
                        buttons.getChildren().clear();
                        
                        // Toujours afficher le bouton détails
                        buttons.getChildren().add(detailsButton);
                        
                        // Ajouter les boutons en fonction du statut actuel
                        switch(order.getStatus().toLowerCase()) {
                            case "en attente":
                                buttons.getChildren().addAll(confirmButton, cancelButton);
                                break;
                            case "confirmée":
                                buttons.getChildren().addAll(prepareButton, cancelButton);
                                break;
                            case "en préparation":
                                buttons.getChildren().addAll(readyButton, cancelButton);
                                break;
                            case "prête":
                                Button deliverButton = new Button("Livrer");
                                deliverButton.setOnAction(e -> updateOrderStatus(order, "Livrée"));
                                buttons.getChildren().addAll(deliverButton, cancelButton);
                                break;
                            case "livrée":
                            case "annulée":
                                // Aucune action supplémentaire pour ces statuts
                                break;
                        }
                        
                        setGraphic(buttons);
                    }
                }
            };
        });
        
        orderTable.getColumns().addAll(idCol, userCol, dateCol, totalCol, statusCol, actionsCol);
        
        // Rafraîchir les données
        refreshOrderTable(orderTable);
        
        // Bouton de rafraîchissement
        Button refreshButton = new Button("Rafraîchir");
        refreshButton.setOnAction(e -> refreshOrderTable(orderTable));
        
        content.getChildren().addAll(header, orderTable, refreshButton);
        orderManagementTab.setContent(content);
    }

    private void refreshOrderTable(TableView<Order> orderTable) {
        orderTable.getItems().clear();
        List<Order> orders = mainApp.getOrders();
        if (orders.isEmpty()) {
            System.out.println("Aucune commande trouvée dans la base de données.");
        } else {
            orderTable.getItems().addAll(orders);
        }
    }

    private void updateOrderStatus(Order order, String newStatus) {
        // Mettre à jour le statut de la commande
        order.setStatus(newStatus);
        
        // Mettre à jour dans la base de données
        if (mainApp.getOrderDAO().updateOrderStatus(order.getId(), newStatus)) {
            showAlert("Succès", "Le statut de la commande #" + order.getId() + " a été modifié à: " + newStatus);
            
            // Récupérer le contenu de l'onglet et rafraîchir le tableau
            VBox content = (VBox) orderManagementTab.getContent();
            
            // Chercher le TableView dans le contenu
            for (javafx.scene.Node node : content.getChildren()) {
                if (node instanceof TableView) {
                    @SuppressWarnings("unchecked")
                    TableView<Order> tableView = (TableView<Order>) node;
                    refreshOrderTable(tableView);
                    break;
                }
            }
        } else {
            showAlert("Erreur", "Impossible de mettre à jour le statut de la commande.");
        }
    }

    private void showOrderDetails(Order order) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Détails de la commande #" + order.getId());
        dialog.setHeaderText("Client: " + order.getUserId() + " - Date: " + order.getOrderDate());
        
        DialogPane dialogPane = dialog.getDialogPane();
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        // Tableau des articles commandés
        TableView<MainApp.OrderItem> itemsTable = new TableView<>();
        
        TableColumn<MainApp.OrderItem, String> nameCol = new TableColumn<>("Article");
        nameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMenuItem().getName()));
        
        TableColumn<MainApp.OrderItem, Integer> quantityCol = new TableColumn<>("Quantité");
        quantityCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getQuantity()));
        
        TableColumn<MainApp.OrderItem, Double> priceCol = new TableColumn<>("Prix unitaire");
        priceCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getMenuItem().getPrice()));
        priceCol.setCellFactory(column -> {
            return new TableCell<MainApp.OrderItem, Double>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty || price == null) {
                        setText(null);
                    } else {
                        setText(String.format("%,.0f FCFA", price));
                    }
                }
            };
        });
        
        TableColumn<MainApp.OrderItem, Double> subtotalCol = new TableColumn<>("Sous-total");
        subtotalCol.setCellValueFactory(param -> {
            double subtotal = param.getValue().getMenuItem().getPrice() * param.getValue().getQuantity();
            return new SimpleObjectProperty<>(subtotal);
        });
        subtotalCol.setCellFactory(column -> {
            return new TableCell<MainApp.OrderItem, Double>() {
                @Override
                protected void updateItem(Double subtotal, boolean empty) {
                    super.updateItem(subtotal, empty);
                    if (empty || subtotal == null) {
                        setText(null);
                    } else {
                        setText(String.format("%,.0f FCFA", subtotal));
                    }
                }
            };
        });
        
        itemsTable.getColumns().addAll(nameCol, quantityCol, priceCol, subtotalCol);
        
        // Convertir les items de la commande en OrderItem de MainApp
        List<MainApp.OrderItem> orderItems = order.getItems().stream()
            .map(item -> new MainApp.OrderItem(item.getMenuItem(), item.getQuantity()))
            .collect(Collectors.toList());
        
        itemsTable.getItems().addAll(orderItems);
        
        // Calculer le total
        double total = orderItems.stream()
            .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
            .sum();
        
        Label totalLabel = new Label("Total: " + String.format("%,.0f FCFA", total));
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label statusLabel = new Label("Statut: " + order.getStatus());
        statusLabel.setStyle("-fx-font-weight: bold;");
        
        content.getChildren().addAll(itemsTable, totalLabel, statusLabel);
        
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        
        dialog.showAndWait();
    }

    private boolean showConfirmDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #333; -fx-padding: 10px;");

        Label title = new Label("Restaurant Le Chezoli");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        HBox navMenu = new HBox(20);
        navMenu.setAlignment(Pos.CENTER);

        String[] menuItems = {"Accueil", "Menu", "Commander", "À Propos", "Contact"};
        
        // Ajouter l'item Admin si l'utilisateur est admin
        Label adminLabel = new Label("Admin");
        adminLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-cursor: hand;");
        adminLabel.setOnMouseClicked(e -> {
            // Ne rien faire ici car nous sommes déjà sur la page Admin
        });
        
        for (String item : menuItems) {
            Label menuItem = new Label(item);
            menuItem.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-cursor: hand;");
            menuItem.setOnMouseClicked(e -> {
                switch (item) {
                    case "Accueil":
                        mainApp.showHome();
                        break;
                    case "Menu":
                        mainApp.showMenu();
                        break;
                    case "Commander":
                        mainApp.showOrder(null, null); // Passer null pour utiliser les valeurs par défaut
                        break;
                    case "À Propos":
                        mainApp.showAbout();
                        break;
                    case "Contact":
                        mainApp.showContact();
                        break;
                }
            });
            navMenu.getChildren().add(menuItem);
        }
        
        // Ajouter Admin dans le menu de navigation
        navMenu.getChildren().add(adminLabel);
        
        // Ajouter le bouton de déconnexion
        Label logoutLabel = new Label("Déconnexion");
        logoutLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-cursor: hand;");
        logoutLabel.setOnMouseClicked(e -> {
            mainApp.setLoggedIn(false);
            mainApp.setCurrentUser(null);
            mainApp.showLogin();
        });
        navMenu.getChildren().add(logoutLabel);

        header.getChildren().addAll(title, navMenu);
        return header;
    }
} 