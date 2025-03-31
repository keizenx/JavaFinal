package com.chezoli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASS = "franckX";
    private static final String DB_NAME = "chezoli";
    
    private static DatabaseConnection instance;
    private Connection connection;
    private boolean initialized = false;

    private DatabaseConnection() {
        // Constructeur privé pour empêcher l'instanciation
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.out.println("Création d'une nouvelle connexion...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            
            // Vérifier si la base de données est sélectionnée
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("USE " + DB_NAME);
            }
            
            System.out.println("Connexion établie avec succès!");
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connexion fermée");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }

    public void checkAndInitializeDatabase() {
        if (initialized) {
            return;
        }

        System.out.println("Vérification de la base de données...");
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connexion au serveur MySQL établie");
            
            // Vérifier si la base de données existe
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SHOW DATABASES LIKE '" + DB_NAME + "'");
                if (!stmt.getResultSet().next()) {
                    System.out.println("Base de données '" + DB_NAME + "' non trouvée, création en cours...");
                    stmt.execute("CREATE DATABASE " + DB_NAME);
                    System.out.println("Base de données '" + DB_NAME + "' créée avec succès!");
                } else {
                    System.out.println("Base de données '" + DB_NAME + "' existe déjà");
                }
            }

            // Se connecter à la base de données
            try (Connection dbConn = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS)) {
                System.out.println("Connexion à la base de données '" + DB_NAME + "' établie");
                
                // Vérifier si les tables existent
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
        
        initialized = true;
    }

    private boolean tablesExist(Connection conn) throws SQLException {
        String[] requiredTables = {"users", "categories", "menu_items", "orders", "order_items", "contact_messages"};
        
        for (String table : requiredTables) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SHOW TABLES LIKE '" + table + "'");
                if (!stmt.getResultSet().next()) {
                    System.out.println("Table '" + table + "' non trouvée");
                    return false;
                }
                System.out.println("Table '" + table + "' trouvée");
            }
        }
        return true;
    }

    private void executeSqlScript(Connection conn) {
        System.out.println("Chargement du script SQL d'initialisation...");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/db/init.sql")))) {
            
            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
            
            String[] instructions = script.toString().split(";");
            for (String instruction : instructions) {
                instruction = instruction.trim();
                if (!instruction.isEmpty()) {
                    System.out.println("Exécution de l'instruction SQL: " + instruction);
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(instruction);
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de l'exécution de l'instruction: " + e.getMessage());
                        // Continue avec les instructions suivantes même si une échoue
                    }
                }
            }
            System.out.println("Script SQL d'initialisation exécuté avec succès!");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution du script SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 