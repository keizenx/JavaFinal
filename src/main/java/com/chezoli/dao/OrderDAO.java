package com.chezoli.dao;

import com.chezoli.MenuItem;
import com.chezoli.Order;
import com.chezoli.MainApp;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private static final String INSERT_ORDER = "INSERT INTO orders (user_id, order_date, status, payment_method) VALUES (?, ?, ?, ?)";
    private static final String INSERT_ORDER_ITEM = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM orders";
    private static final String SELECT_BY_USER = "SELECT * FROM orders WHERE user_id = ?";
    private static final String UPDATE_STATUS = "UPDATE orders SET status = ? WHERE id = ?";
    private static final String CREATE_ORDER_SQL = 
        "INSERT INTO orders (user_id, order_date, status, payment_method) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_ORDERS_SQL = 
        "SELECT id, user_id, order_date, status, payment_method FROM orders ORDER BY order_date DESC";
    private static final String SELECT_ORDERS_BY_USER_SQL = 
        "SELECT id, user_id, order_date, status, payment_method FROM orders WHERE user_id = ? ORDER BY order_date DESC";

    private MenuItemDAO menuItemDAO;

    public OrderDAO() {
        this.menuItemDAO = new MenuItemDAO();
    }

    public boolean createOrder(Order order) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement orderStmt = conn.prepareStatement(CREATE_ORDER_SQL, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setString(1, order.getUserId());
                orderStmt.setTimestamp(2, new Timestamp(order.getOrderDate().getTime()));
                orderStmt.setString(3, order.getStatus());
                orderStmt.setString(4, order.getPaymentMethod());
                
                int affectedRows = orderStmt.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }
                
                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);
                        order.setId(orderId);
                        
                        // Insert order items
                        for (MainApp.OrderItem item : order.getItems()) {
                            try (PreparedStatement itemStmt = conn.prepareStatement(
                                    "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)")) {
                                itemStmt.setInt(1, orderId);
                                itemStmt.setInt(2, item.getMenuItem().getId());
                                itemStmt.setInt(3, item.getQuantity());
                                itemStmt.executeUpdate();
                            }
                        }
                        
                        conn.commit();
                        return true;
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.id, o.user_id, o.order_date, o.status, o.payment_method, " +
                      "oi.quantity, mi.*, c.name as category_name FROM orders o " +
                      "LEFT JOIN order_items oi ON o.id = oi.order_id " +
                      "LEFT JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                      "LEFT JOIN categories c ON mi.category_id = c.id " +
                      "ORDER BY o.id, o.order_date DESC";
                      
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            int currentOrderId = -1;
            Order currentOrder = null;
            
            while (rs.next()) {
                int orderId = rs.getInt("o.id");
                
                if (orderId != currentOrderId) {
                    if (currentOrder != null) {
                        orders.add(currentOrder);
                        System.out.println("Commande #" + currentOrderId + " ajoutée avec " + 
                                          currentOrder.getItems().size() + " articles");
                    }
                    
                    currentOrder = new Order();
                    currentOrder.setId(orderId);
                    currentOrder.setUserId(rs.getString("o.user_id"));
                    currentOrder.setOrderDate(rs.getTimestamp("o.order_date"));
                    currentOrder.setStatus(rs.getString("o.status"));
                    currentOrder.setPaymentMethod(rs.getString("o.payment_method"));
                    currentOrderId = orderId;
                    
                    System.out.println("Nouvelle commande trouvée : #" + orderId + 
                                      " - Date : " + rs.getTimestamp("o.order_date") + 
                                      " - Client : " + rs.getString("o.user_id") + 
                                      " - Statut : " + rs.getString("o.status"));
                }
                
                // Vérifier si cette ligne contient un article de commande
                if (rs.getObject("mi.id") != null) {
                    MenuItem menuItem = new MenuItem(
                        rs.getString("mi.name"),
                        rs.getString("mi.description"),
                        rs.getDouble("mi.price"),
                        rs.getString("category_name"),
                        rs.getString("mi.image_url")
                    );
                    menuItem.setId(rs.getInt("mi.id"));
                    
                    currentOrder.addItem(new MainApp.OrderItem(menuItem, rs.getInt("oi.quantity")));
                    
                    System.out.println("  - Article ajouté : " + menuItem.getName() + 
                                      " - Quantité : " + rs.getInt("oi.quantity"));
                }
            }
            
            if (currentOrder != null) {
                orders.add(currentOrder);
                System.out.println("Commande #" + currentOrderId + " ajoutée avec " + 
                                  currentOrder.getItems().size() + " articles");
            }
            
            System.out.println("Nombre total de commandes récupérées : " + orders.size());
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes : " + e.getMessage());
            e.printStackTrace();
        }
        
        return orders;
    }

    public List<Order> getOrdersByUser(String userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.id, o.user_id, o.order_date, o.status, o.payment_method, " +
                       "oi.quantity, mi.*, c.name as category_name FROM orders o " +
                       "LEFT JOIN order_items oi ON o.id = oi.order_id " +
                       "LEFT JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                       "LEFT JOIN categories c ON mi.category_id = c.id " +
                       "WHERE o.user_id = ? " +
                       "ORDER BY o.id, o.order_date DESC";
                      
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            int currentOrderId = -1;
            Order currentOrder = null;
            
            while (rs.next()) {
                int orderId = rs.getInt("o.id");
                
                if (orderId != currentOrderId) {
                    if (currentOrder != null) {
                        orders.add(currentOrder);
                    }
                    
                    currentOrder = new Order();
                    currentOrder.setId(orderId);
                    currentOrder.setUserId(rs.getString("o.user_id"));
                    currentOrder.setOrderDate(rs.getTimestamp("o.order_date"));
                    currentOrder.setStatus(rs.getString("o.status"));
                    currentOrder.setPaymentMethod(rs.getString("o.payment_method"));
                    currentOrderId = orderId;
                }
                
                // Vérifier si cette ligne contient un article de commande
                if (rs.getObject("mi.id") != null) {
                    MenuItem menuItem = new MenuItem(
                        rs.getString("mi.name"),
                        rs.getString("mi.description"),
                        rs.getDouble("mi.price"),
                        rs.getString("category_name"),
                        rs.getString("mi.image_url")
                    );
                    menuItem.setId(rs.getInt("mi.id"));
                    
                    currentOrder.addItem(new MainApp.OrderItem(menuItem, rs.getInt("oi.quantity")));
                }
            }
            
            if (currentOrder != null) {
                orders.add(currentOrder);
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Statut de la commande #" + orderId + " mis à jour : " + newStatus);
                return true;
            } else {
                System.out.println("Aucune commande trouvée avec l'ID " + orderId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du statut de la commande : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getString("user_id"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setStatus(rs.getString("status"));
        order.setPaymentMethod(rs.getString("payment_method"));
        
        // Charger les éléments de la commande
        List<MainApp.OrderItem> items = getOrderItems(order.getId());
        order.setItems(items);
        
        return order;
    }

    private List<MainApp.OrderItem> getOrderItems(int orderId) {
        List<MainApp.OrderItem> items = new ArrayList<>();
        String query = "SELECT oi.quantity, mi.*, c.name as category_name " +
                      "FROM order_items oi " +
                      "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                      "JOIN categories c ON mi.category_id = c.id " +
                      "WHERE oi.order_id = ?";
                      
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem menuItem = new MenuItem(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("category_name"),
                        rs.getString("image_url")
                    );
                    menuItem.setId(rs.getInt("id"));
                    
                    items.add(new MainApp.OrderItem(menuItem, rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des articles de la commande #" + orderId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return items;
    }
}