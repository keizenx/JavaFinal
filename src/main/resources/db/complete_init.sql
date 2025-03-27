-- Création de la base de données
DROP DATABASE IF EXISTS chezoli;
CREATE DATABASE chezoli;
USE chezoli;

-- Table des utilisateurs
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des catégories de menu
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Table des articles du menu
CREATE TABLE menu_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category_id INT,
    image_url VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Table des commandes
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'En attente',
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Table des détails de commande
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    menu_item_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Insertion des catégories
INSERT INTO categories (name) VALUES 
('Entrées'),
('Plats Principaux'),
('Desserts'),
('Boissons');

-- Insertion des articles du menu
INSERT INTO menu_items (name, description, price, category_id, image_url) VALUES
-- Entrées
('Salade César', 'Laitue romaine, croûtons, parmesan, sauce césar maison', 8.99, 1, '/images/Notre_Menu/image_113.png'),
('Soupe à l''oignon', 'Soupe à l''oignon gratinée traditionnelle', 7.99, 1, '/images/Notre_Menu/image_114.png'),
('Carpaccio de boeuf', 'Fines tranches de boeuf, câpres, parmesan', 12.99, 1, '/images/Notre_Menu/image_115.png'),

-- Plats Principaux
('Steak-frites', 'Entrecôte grillée, frites maison, sauce au poivre', 24.99, 2, '/images/Notre_Menu/image_116.png'),
('Poulet rôti', 'Poulet fermier rôti aux herbes, légumes de saison', 19.99, 2, '/images/Notre_Menu/image_117.png'),
('Saumon grillé', 'Saumon frais grillé, riz basmati, sauce citron', 22.99, 2, '/images/Notre_Menu/image_118.png'),

-- Desserts
('Crème brûlée', 'Crème brûlée à la vanille de Madagascar', 7.99, 3, '/images/Notre_Menu/image_119.png'),
('Tarte Tatin', 'Tarte aux pommes caramélisées, crème fraîche', 8.99, 3, '/images/Notre_Menu/image_120.png'),
('Mousse au chocolat', 'Mousse au chocolat noir 70%', 6.99, 3, '/images/Notre_Menu/image_121.png'),

-- Boissons
('Vin rouge maison', 'Verre de vin rouge de la maison', 5.99, 4, '/images/Notre_Menu/image_122.png'),
('Eau minérale', 'Eau minérale plate ou gazeuse 75cl', 3.99, 4, '/images/Notre_Menu/image_123.png'),
('Café expresso', 'Café expresso 100% arabica', 2.99, 4, '/images/Notre_Menu/telechargement_1.png');

-- Insertion de l'administrateur
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'Admin'); 