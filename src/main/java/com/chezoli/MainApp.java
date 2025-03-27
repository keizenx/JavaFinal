package com.chezoli;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import com.chezoli.dao.OrderDAO;
import com.chezoli.dao.UserDAO;

public class MainApp extends Application {
    private Stage primaryStage;
    private MenuApp menuApp;
    private AboutApp aboutApp;
    private ContactApp contactApp;
    private LoginApp loginApp;
    private SignUpApp signUpApp;
    private HomeApp homeApp;
    private OrderApp orderApp;
    private AdminApp adminApp;
    private boolean isLoggedIn = false;
    private ObservableList<OrderItem> cartItems = FXCollections.observableArrayList();
    private double cartTotal = 0.0;
    private List<User> users = new ArrayList<>();
    private User currentUser;
    private List<Order> orders = new ArrayList<>();
    private OrderDAO orderDAO;
    private UserDAO userDAO;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Restaurant Chez Oli");
        this.primaryStage.setWidth(1200);
        this.primaryStage.setHeight(800);
        this.primaryStage.setResizable(true);
        this.primaryStage.setMaximized(true);
        
        // Initialisation des données
        users = new ArrayList<>();
        orders = new ArrayList<>();
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
        
        // Initialiser les DAO
        com.chezoli.dao.MenuItemDAO menuItemDAO = new com.chezoli.dao.MenuItemDAO();
        
        // Charger les commandes depuis la base de données
        orders = orderDAO.getAllOrders();
        
        // Charger les articles du menu depuis la base de données
        List<MenuItem> menuItems = menuItemDAO.getAllMenuItems();
        System.out.println("Articles du menu chargés depuis la base de données: " + menuItems.size());
        
        // Afficher les détails des articles pour diagnostic
        System.out.println("===== DIAGNOSTIC: ARTICLES DU MENU =====");
        for (MenuItem item : menuItems) {
            System.out.println("ID: " + item.getId() + 
                             " | Nom: " + item.getName() + 
                             " | Prix: " + item.getPrice() + 
                             " | Catégorie: " + item.getCategory() + 
                             " | Image: " + item.getImageUrl());
        }
        System.out.println("=======================================");
        
        // Ajout d'un administrateur par défaut en mémoire
        users.add(new User("admin", "admin123", "ADMIN"));
        
        // Vérifier l'existence et la création de l'utilisateur admin dans la base de données
        User adminUser = userDAO.getUser("admin");
        System.out.println("Vérification de l'admin dans la base de données: " + 
                           (adminUser != null ? 
                            "User=" + adminUser.getUsername() + ", Role=" + adminUser.getRole() : 
                            "Admin introuvable"));
        
        if (adminUser == null) {
            // Créer l'utilisateur admin s'il n'existe pas
            User newAdmin = new User("admin", "admin123", "ADMIN");
            boolean created = userDAO.createUser(newAdmin);
            System.out.println("Création de l'admin dans la base de données: " + 
                               (created ? "Réussie" : "Échec"));
            
            // Vérifier à nouveau
            adminUser = userDAO.getUser("admin");
            System.out.println("Admin après création: " + 
                               (adminUser != null ? 
                                "User=" + adminUser.getUsername() + ", Role=" + adminUser.getRole() : 
                                "Toujours introuvable"));
        }
        
        // Afficher tous les utilisateurs de la base de données
        List<User> dbUsers = userDAO.getAllUsers();
        System.out.println("===== UTILISATEURS EN BASE DE DONNÉES =====");
        for (User user : dbUsers) {
            System.out.println("User: " + user.getUsername() + ", Role: " + user.getRole());
        }
        System.out.println("==========================================");
        
        // Initialisation des applications
        homeApp = new HomeApp(this);
        menuApp = new MenuApp(this);
        aboutApp = new AboutApp(this);
        contactApp = new ContactApp(this);
        loginApp = new LoginApp(this);
        signUpApp = new SignUpApp(this);
        
        // Initialiser OrderApp avec les articles du menu chargés depuis la base de données
        orderApp = new OrderApp(this, "Tout", menuItems);
        adminApp = new AdminApp(this);
        
        showLogin(); // On commence par la page de connexion
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
        // Mettre à jour l'interface utilisateur pour refléter l'état de connexion
        if (isLoggedIn) {
            showHome(); // Rediriger vers la page d'accueil après connexion
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void showMenu() {
        Scene scene = menuApp.createScene();
        primaryStage.setScene(scene);
    }

    public void showAbout() {
        Scene scene = aboutApp.createScene();
        primaryStage.setScene(scene);
    }

    public void showContact() {
        Scene scene = contactApp.createScene();
        primaryStage.setScene(scene);
    }

    public void showLogin() {
        Scene scene = loginApp.createScene();
        primaryStage.setScene(scene);
    }

    public void showSignUp() {
        Scene scene = signUpApp.createScene();
        primaryStage.setScene(scene);
    }

    public void showHome() {
        Scene scene = homeApp.createScene();
        primaryStage.setScene(scene);
    }

    public void showOrder(String category, List<MenuItem> menuItems) {
        OrderApp orderApp = new OrderApp(this, category, menuItems);
        primaryStage.setScene(orderApp.createScene());
    }

    public ObservableList<OrderItem> getCartItems() {
        return cartItems;
    }

    public void addToCart(MenuItem menuItem, int quantity) {
        // Vérifier si l'article existe déjà dans le panier
        for (OrderItem item : cartItems) {
            if (item.getMenuItem().getId() == menuItem.getId()) {
                // Article trouvé, mettre à jour la quantité
                item.setQuantity(item.getQuantity() + quantity);
                recalculateCartTotal();
                return;
            }
        }
        
        // Nouvel article à ajouter
        OrderItem newItem = new OrderItem(menuItem, quantity);
        cartItems.add(newItem);
        recalculateCartTotal();
        System.out.println("Article ajouté au panier: " + menuItem.getName() + " x" + quantity + " - Prix: " + menuItem.getPrice() * quantity + " FCFA");
    }

    public void updateCartItemQuantity(OrderItem item, int newQuantity) {
        if (newQuantity <= 0) {
            cartItems.remove(item);
        } else {
            item.setQuantity(newQuantity);
        }
        recalculateCartTotal();
        System.out.println("Quantité mise à jour pour: " + item.getMenuItem().getName() + " - Nouvelle quantité: " + newQuantity);
    }

    public void removeFromCart(OrderItem item) {
        cartItems.remove(item);
        recalculateCartTotal();
        System.out.println("Article retiré du panier: " + item.getMenuItem().getName());
    }

    public void clearCart() {
        cartItems.clear();
        cartTotal = 0;
        System.out.println("Panier vidé");
    }

    private void recalculateCartTotal() {
        cartTotal = getCartTotal();
    }

    public double getCartTotal() {
        double total = 0.0;
        for (OrderItem item : cartItems) {
            if (item.getMenuItem() != null) {
                total += item.getMenuItem().getPrice() * item.getQuantity();
            }
        }
        System.out.println("Calcul du total du panier: " + total + " FCFA (pour " + cartItems.size() + " articles)");
        return total;
    }

    public static class OrderItem {
        private final MenuItem menuItem;
        private int quantity;

        public OrderItem(MenuItem menuItem, int quantity) {
            this.menuItem = menuItem;
            this.quantity = quantity;
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return String.format("%s x%d - %,.0f FCFA", 
                menuItem.getName(), 
                quantity, 
                menuItem.getPrice() * quantity);
        }
    }

    public void showAdminScene() {
        primaryStage.setScene(adminApp.getScene());
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public List<Order> getOrders() {
        // Toujours retourner les commandes depuis la base de données
        return orderDAO.getAllOrders();
    }

    public void addOrder(Order order) {
        orders.add(order);
        // Recharger toutes les commandes depuis la base de données
        orders = orderDAO.getAllOrders();
    }

    public boolean isAdmin() {
        // Vérifier si l'utilisateur est connecté et a le rôle ADMIN (attention à la casse)
        boolean result = currentUser != null && ("ADMIN".equalsIgnoreCase(currentUser.getRole()) || 
                                      "Administrateur".equalsIgnoreCase(currentUser.getRole()));
        
        System.out.println("Vérification admin: " + 
                          (currentUser != null ? 
                           "User=" + currentUser.getUsername() + ", Role=" + currentUser.getRole() : 
                           "User=null") + 
                          " -> " + result);
        
        return result;
    }

    public boolean authenticate(String username, String password) {
        System.out.println("Tentative d'authentification pour: " + username);
        
        // D'abord, essayer d'authentifier via la base de données
        if (userDAO.authenticate(username, password)) {
            User dbUser = userDAO.getUser(username);
            if (dbUser != null) {
                System.out.println("Authentification réussie via base de données: " + dbUser.getUsername() + ", Rôle: " + dbUser.getRole());
                setCurrentUser(dbUser);
                return true;
            }
        }
        
        // Ensuite, essayer l'authentification en mémoire
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .map(user -> {
                    System.out.println("Authentification réussie via mémoire: " + user.getUsername() + ", Rôle: " + user.getRole());
                    setCurrentUser(user);
                    return true;
                })
                .orElse(false);
    }

    public void showAdmin() {
        primaryStage.setScene(adminApp.createScene());
        primaryStage.show();
    }

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public static void main(String[] args) {
        launch(args);
    }
} 