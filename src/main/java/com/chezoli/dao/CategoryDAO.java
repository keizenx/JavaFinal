package com.chezoli.dao;

import com.chezoli.Category;
import com.chezoli.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final DatabaseConnection dbConnection;

    public CategoryDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Category category = new Category(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des catégories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return categories;
    }

    public void createCategory(String name) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Catégorie créée avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la catégorie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateCategory(int id, String name) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Catégorie mise à jour avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la catégorie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Catégorie supprimée avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la catégorie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la catégorie: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
} 