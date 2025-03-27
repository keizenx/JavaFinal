package com.chezoli.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.stream.Collectors;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/chezoli?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String URL_WITHOUT_DB = "jdbc:mysql://localhost:3306?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "franckX";
    private static boolean initialized = false;

    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            checkAndInitializeDatabase();
            initialized = true;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }

    private static void checkAndInitializeDatabase() {
        try {
            // Se connecter sans spécifier la base de données
            Connection conn = DriverManager.getConnection(URL_WITHOUT_DB, USER, PASSWORD);
            
            // Vérifier si la base de données existe déjà
            if (!databaseExists(conn, "chezoli")) {
                // Créer la base de données si elle n'existe pas
                createDatabase(conn);
                
                // Exécuter le script SQL d'initialisation
                executeSqlScript(conn);
                
                System.out.println("Base de données créée et initialisée avec succès!");
            } else {
                // Vérifier si les tables existent
                Connection dbConn = DriverManager.getConnection(URL, USER, PASSWORD);
                if (!tablesExist(dbConn)) {
                    // Créer les tables si elles n'existent pas
                    executeSqlScript(conn);
                    System.out.println("Tables créées avec succès!");
                } else {
                    System.out.println("Base de données déjà initialisée, connexion établie.");
                }
                dbConn.close();
            }
            
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification/initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static boolean databaseExists(Connection conn, String dbName) {
        try {
            ResultSet rs = conn.getMetaData().getCatalogs();
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(dbName)) {
                    rs.close();
                    return true;
                }
            }
            rs.close();
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence de la base de données: " + e.getMessage());
            return false;
        }
    }
    
    private static void createDatabase(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS chezoli");
            stmt.executeUpdate("USE chezoli");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static boolean tablesExist(Connection conn) {
        try {
            ResultSet rs = conn.getMetaData().getTables(null, null, "users", null);
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence des tables: " + e.getMessage());
            return false;
        }
    }

    private static void executeSqlScript(Connection conn) {
        try {
            // Charger le script SQL depuis les ressources
            InputStream is = DatabaseConnection.class.getResourceAsStream("/db/init.sql");
            if (is == null) {
                System.err.println("Fichier SQL d'initialisation non trouvé!");
                return;
            }
            
            String sqlScript = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
            
            // Diviser le script en instructions SQL individuelles
            String[] sqlInstructions = sqlScript.split(";");
            
            Statement stmt = conn.createStatement();
            
            for (String sqlInstruction : sqlInstructions) {
                if (!sqlInstruction.trim().isEmpty()) {
                    try {
                        stmt.execute(sqlInstruction);
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de l'exécution de l'instruction SQL: " + sqlInstruction);
                        System.err.println("Message d'erreur: " + e.getMessage());
                        // Continue with next instruction instead of aborting
                    }
                }
            }
            
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution du script SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}