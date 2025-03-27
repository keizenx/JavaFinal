package com.chezoli;

import java.net.URL;

public class MenuItem {
    private int id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;

    public MenuItem(String name, String description, double price, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Méthode pour obtenir le chemin de l'image adapté à l'environnement
    public String getImagePath() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        
        // Nettoyer le chemin (éliminer les slashes au début si présents)
        String cleanImageName = imageUrl;
        if (cleanImageName.startsWith("/")) {
            cleanImageName = cleanImageName.substring(1);
        }
        if (cleanImageName.startsWith("images/")) {
            cleanImageName = cleanImageName.substring(7);
        }
        if (cleanImageName.startsWith("Notre_Menu/")) {
            cleanImageName = cleanImageName.substring(11);
        }
        
        // Essayer les chemins d'accès les plus probables
        String[] paths = {
            "/images/Notre_Menu/" + cleanImageName,
            "/images/" + cleanImageName,
            "/images/Notre_Menu/" + imageUrl,
            "/images/" + imageUrl,
            imageUrl
        };
        
        for (String path : paths) {
            try {
                URL resource = getClass().getResource(path);
                if (resource != null) {
                    System.out.println("Image trouvée! " + path);
                    return resource.toExternalForm();
                }
            } catch (Exception e) {
                // Ignorer et essayer le prochain chemin
            }
        }
        
        // Fallback: Utiliser une image par défaut
        System.out.println("Impossible de trouver l'image: " + imageUrl + " - Utilisation d'une image de remplacement");
        
        try {
            // Essayons d'utiliser le logo comme fallback
            URL fallbackResource = getClass().getResource("/images/logo.png");
            if (fallbackResource != null) {
                return fallbackResource.toExternalForm();
            }
        } catch (Exception e) {
            // Ignorer si le fallback échoue également
        }
        
        // Si aucune image n'est trouvée, retourner un indicateur visible que l'image est manquante
        return "https://dummyimage.com/150x150/ff0000/ffffff.png&text=Image+manquante";
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Méthode pour formater le prix
    public String getFormattedPrice() {
        return String.format("%,.0f FCFA", price);
    }

    @Override
    public String toString() {
        return name + " - " + getFormattedPrice();
    }
} 