package com.chezoli.dao;

import com.chezoli.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String INSERT_USER = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    private static final String SELECT_USER = "SELECT * FROM users WHERE username = ?";
    private static final String UPDATE_USER = "UPDATE users SET password = ?, role = ? WHERE username = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE username = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";

    public boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retourne true si un utilisateur correspondant est trouvé
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public User getUser(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    public boolean createUser(User user) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS)) {
            
            while (rs.next()) {
                users.add(new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return users;
    }

    public boolean updateUser(User user) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getUsername());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            
            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            return false;
        }
    }
} 