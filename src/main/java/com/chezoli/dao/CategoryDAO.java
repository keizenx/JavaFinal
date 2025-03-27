package com.chezoli.dao;

import com.chezoli.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT name FROM categories ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des catégories : " + e.getMessage());
        }
        return categories;
    }

    public boolean addCategory(String name) {
        String query = "INSERT INTO categories (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
            return false;
        }
    }

    public boolean updateCategory(String oldName, String newName) {
        String query = "UPDATE categories SET name = ? WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newName);
            stmt.setString(2, oldName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la catégorie : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategory(String name) {
        String query = "DELETE FROM categories WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la catégorie : " + e.getMessage());
            return false;
        }
    }

    public boolean categoryExists(String name) {
        String query = "SELECT COUNT(*) FROM categories WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de la catégorie : " + e.getMessage());
        }
        return false;
    }
} 