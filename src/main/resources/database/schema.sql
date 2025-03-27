-- Création de la base de données
CREATE DATABASE IF NOT EXISTS restaurant_chez_oli;
USE restaurant_chez_oli;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des catégories de menu
CREATE TABLE IF NOT EXISTS categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Table des articles du menu
CREATE TABLE IF NOT EXISTS menu_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category_id INT,
    image_path VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Table des commandes
CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Table des détails de commande
CREATE TABLE IF NOT EXISTS order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    menu_item_id INT,
    quantity INT NOT NULL,
    price_at_time DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Insertion des données initiales
INSERT INTO categories (name) VALUES 
('Entrées'),
('Plats Principaux'),
('Desserts'),
('Boissons');

-- Insertion d'un administrateur par défaut
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin', 'Administrateur'); 