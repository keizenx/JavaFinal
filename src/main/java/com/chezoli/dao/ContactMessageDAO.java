package com.chezoli.dao;

import com.chezoli.ContactMessage;
import com.chezoli.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ContactMessageDAO {
    private final DatabaseConnection dbConnection;

    public ContactMessageDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void createContactMessage(String name, String email, String subject, String message) {
        String sql = "INSERT INTO contact_messages (name, email, subject, message, date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, subject);
            pstmt.setString(4, message);
            pstmt.setString(5, currentDate);
            
            pstmt.executeUpdate();
            System.out.println("Message de contact enregistré avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du message de contact: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<ContactMessage> getAllMessages() {
        List<ContactMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM contact_messages ORDER BY date DESC";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ContactMessage message = new ContactMessage(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("subject"),
                    rs.getString("message"),
                    rs.getString("date")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des messages: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }

    public void deleteMessage(int id) {
        String sql = "DELETE FROM contact_messages WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Message supprimé avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du message: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 