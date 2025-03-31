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
    private static Connection connection = null;

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("Tentative de connexion à la base de données...");
        if (!initialized) {
            System.out.println("Base de données non initialisée, initialisation en cours...");
            checkAndInitializeDatabase();
            initialized = true;
        }

        if (connection == null || connection.isClosed()) {
            try {
                System.out.println("Création d'une nouvelle connexion...");
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                // S'assurer que la base de données est sélectionnée
                connection.createStatement().execute("USE chezoli");
                System.out.println("Connexion établie avec succès!");
            } catch (ClassNotFoundException e) {
                System.err.println("Erreur: Driver MySQL non trouvé!");
                throw new SQLException("MySQL JDBC Driver not found.", e);
            }
        } else {
            // Vérifier si la base de données est toujours sélectionnée
            try {
                connection.createStatement().execute("USE chezoli");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la sélection de la base de données: " + e.getMessage());
            }
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void checkAndInitializeDatabase() {
        System.out.println("Vérification de la base de données...");
        try (Connection conn = DriverManager.getConnection(URL_WITHOUT_DB, USER, PASSWORD)) {
            System.out.println("Connexion au serveur MySQL établie");
            
            if (!databaseExists(conn, "chezoli")) {
                System.out.println("Base de données 'chezoli' non trouvée, création en cours...");
                createDatabase(conn);
            } else {
                System.out.println("Base de données 'chezoli' existe déjà");
            }
            
            try (Connection dbConn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("Connexion à la base de données 'chezoli' établie");
                if (!tablesExist(dbConn)) {
                    System.out.println("Tables non trouvées, exécution du script d'initialisation...");
                    executeSqlScript(dbConn);
                } else {
                    System.out.println("Toutes les tables existent déjà");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
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
            System.out.println("Création de la base de données 'chezoli'...");
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS chezoli");
            stmt.executeUpdate("USE chezoli");
            stmt.close();
            System.out.println("Base de données 'chezoli' créée avec succès!");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static boolean tablesExist(Connection conn) {
        try {
            // Vérifier si toutes les tables nécessaires existent
            String[] requiredTables = {"users", "categories", "menu_items", "orders", "order_items", "contact_messages"};
            for (String table : requiredTables) {
                ResultSet rs = conn.getMetaData().getTables(null, null, table, null);
                if (!rs.next()) {
                    System.out.println("Table '" + table + "' non trouvée");
                    rs.close();
                    return false;
                }
                System.out.println("Table '" + table + "' trouvée");
                rs.close();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence des tables: " + e.getMessage());
            return false;
        }
    }

    private static void executeSqlScript(Connection conn) {
        try {
            System.out.println("Chargement du script SQL d'initialisation...");
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
            
            // S'assurer que la base de données est sélectionnée
            stmt.execute("USE chezoli");
            
            for (String sqlInstruction : sqlInstructions) {
                if (!sqlInstruction.trim().isEmpty()) {
                    try {
                        System.out.println("Exécution de l'instruction SQL: " + sqlInstruction.trim());
                        stmt.execute(sqlInstruction);
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de l'exécution de l'instruction SQL: " + sqlInstruction);
                        System.err.println("Message d'erreur: " + e.getMessage());
                        // Continue with next instruction instead of aborting
                    }
                }
            }
            
            stmt.close();
            System.out.println("Script SQL d'initialisation exécuté avec succès!");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution du script SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}