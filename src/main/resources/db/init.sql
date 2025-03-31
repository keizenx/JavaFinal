-- Création de la base de données
CREATE DATABASE IF NOT EXISTS chezoli;
USE chezoli;

-- Création des tables uniquement si elles n'existent pas
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Insertion de l'utilisateur admin par défaut seulement s'il n'existe pas déjà
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');

-- Création de la table des catégories
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Création de la table des plats
CREATE TABLE IF NOT EXISTS menu_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category_id INT NOT NULL,
    image_url VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Création de la table des commandes
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'En attente',
    payment_method VARCHAR(30),
    FOREIGN KEY (user_id) REFERENCES users(username)
);

-- Création de la table des articles de commande
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    menu_item_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- Création de la table des messages de contact
CREATE TABLE IF NOT EXISTS contact_messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertion des catégories par défaut seulement si elles n'existent pas
INSERT IGNORE INTO categories (name) VALUES 
('Entrées'),
('Plats Principaux'),
('Desserts'),
('Boissons');

-- Insertion des plats par défaut seulement s'ils n'existent pas déjà
INSERT IGNORE INTO menu_items (name, description, price, category_id, image_url) VALUES
-- Entrées
('ATTIEKE POULET', 'Délicieux attiéké accompagné de poulet grillé', 1000, (SELECT id FROM categories WHERE name = 'Entrées'), 'image_113.png'),
('ATTIEKE POISSON', 'Attiéké servi avec du poisson frit', 1000, (SELECT id FROM categories WHERE name = 'Entrées'), 'image_114.png'),
('GARBA', 'Le fameux garba traditionnel', 1000, (SELECT id FROM categories WHERE name = 'Entrées'), 'telechargement_1.png'),

-- Plats Principaux
('FRITE POULET', 'Frites maison avec poulet grillé', 1000, (SELECT id FROM categories WHERE name = 'Plats Principaux'), 'image_115.png'),
('CODYS', 'Notre spécialité maison', 5000, (SELECT id FROM categories WHERE name = 'Plats Principaux'), 'image_116.png'),
('Test', 'Article de test', 2000, (SELECT id FROM categories WHERE name = 'Plats Principaux'), 'gaucheOli.PNG'),

-- Desserts
('Galette avec sucre', 'Galette traditionnelle sucrée', 300, (SELECT id FROM categories WHERE name = 'Desserts'), 'image_120.png'),
('CREPE SUCRE', 'Crêpe fine au sucre', 500, (SELECT id FROM categories WHERE name = 'Desserts'), 'image_121.png'),
('Gateau au four', 'Gâteau maison cuit au four', 500, (SELECT id FROM categories WHERE name = 'Desserts'), 'image_122.png'),
('Yaourt', 'Yaourt frais fait maison', 500, (SELECT id FROM categories WHERE name = 'Desserts'), 'image_123.png'),

-- Boissons
('ORANGINA', 'Orangina bien frais', 500, (SELECT id FROM categories WHERE name = 'Boissons'), 'image_117.png'),
('EAU', 'Eau minérale', 100, (SELECT id FROM categories WHERE name = 'Boissons'), 'image_118.png'),
('COCA-COLA', 'Coca-Cola bien frais', 500, (SELECT id FROM categories WHERE name = 'Boissons'), 'image_119.png');

-- Afficher l'état des tables après l'initialisation
SELECT 'Nombre de catégories :' AS Message, COUNT(*) AS Total FROM categories;
SELECT 'Nombre d\'articles au menu :' AS Message, COUNT(*) AS Total FROM menu_items;
SELECT 'Nombre d\'utilisateurs :' AS Message, COUNT(*) AS Total FROM users; 