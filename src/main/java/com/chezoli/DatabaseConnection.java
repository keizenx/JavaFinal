package com.chezoli;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Remplacez ces valeurs par celles de votre installation
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_chez_oli?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "franckX"; // Mot de passe mis à jour
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à la base de données réussie !");
                
                // Création de la base de données si elle n'existe pas
                createDatabaseIfNotExists();
            } catch (ClassNotFoundException e) {
                System.out.println("Driver MySQL introuvable : " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
            }
        }
        return connection;
    }

    private static void createDatabaseIfNotExists() {
        try {
            connection.createStatement().execute(
                "CREATE DATABASE IF NOT EXISTS restaurant_chez_oli"
            );
            connection.createStatement().execute(
                "USE restaurant_chez_oli"
            );
            System.out.println("Base de données créée ou sélectionnée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de la base de données : " + e.getMessage());
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
} 