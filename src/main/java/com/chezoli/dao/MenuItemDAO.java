package com.chezoli.dao;

import com.chezoli.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    private static final String SELECT_ALL = "SELECT m.*, c.name as category_name FROM menu_items m JOIN categories c ON m.category_id = c.id";
    private static final String INSERT = "INSERT INTO menu_items (name, description, price, category_id, image_url) VALUES (?, ?, ?, (SELECT id FROM categories WHERE name = ?), ?)";
    private static final String UPDATE = "UPDATE menu_items SET name = ?, description = ?, price = ?, category_id = ?, image_url = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM menu_items WHERE id = ?";
    private static final String SELECT_BY_CATEGORY = "SELECT m.*, c.name as category_name FROM menu_items m JOIN categories c ON m.category_id = c.id WHERE c.name = ?";

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            
            while (rs.next()) {
                items.add(createMenuItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des plats : " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    public List<MenuItem> getMenuItemsByCategory(String category) {
        List<MenuItem> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_CATEGORY)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                items.add(createMenuItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des plats par catégorie : " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    public boolean addMenuItem(MenuItem item) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setString(4, item.getCategory());
            
            // Si l'URL de l'image contient un chemin complet, extraire juste le nom du fichier
            String imageUrl = item.getImageUrl();
            if (imageUrl != null && imageUrl.contains("/")) {
                String[] parts = imageUrl.split("/");
                imageUrl = parts[parts.length - 1];
            }
            
            pstmt.setString(5, imageUrl);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du plat : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMenuItem(MenuItem item) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setDouble(3, item.getPrice());
            
            // Obtenir l'ID de catégorie
            int categoryId = getCategoryId(item.getCategory(), conn);
            stmt.setInt(4, categoryId);
            
            stmt.setString(5, item.getImageUrl());
            stmt.setInt(6, item.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du plat: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to get category ID
    private int getCategoryId(String categoryName, Connection conn) throws SQLException {
        if (categoryName == null) {
            throw new SQLException("La catégorie ne peut pas être nulle");
        }
        
        String sql = "SELECT id FROM categories WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Catégorie non trouvée: " + categoryName);
                }
            }
        }
    }

    public boolean deleteMenuItem(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du plat : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private MenuItem createMenuItemFromResultSet(ResultSet rs) throws SQLException {
        String imageUrl = rs.getString("image_url");
        
        // Vérifier si l'URL de l'image contient déjà le chemin complet
        if (imageUrl != null && !imageUrl.startsWith("/")) {
            // Si c'est juste le nom du fichier, on présume qu'il est dans Notre_Menu
            imageUrl = imageUrl;
        }
        
        MenuItem item = new MenuItem(
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getString("category_name"),
            imageUrl
        );
        item.setId(rs.getInt("id"));
        
        // Afficher les informations du MenuItem pour le debug
        System.out.println("MenuItem créé à partir de la base de données - ID: " + item.getId() + 
                         " | Nom: " + item.getName() + 
                         " | Image: " + item.getImageUrl());
        return item;
    }
} 