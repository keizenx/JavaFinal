package com.chezoli;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.chezoli.MainApp.OrderItem;

public class Order {
    private int id;
    private String username;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private String userId;
    private List<MainApp.OrderItem> items;
    private String paymentMethod;

    public Order() {
        this.items = new ArrayList<>();
        this.status = "En attente";
    }

    public Order(int id, Date orderDate, double totalAmount, String status, String userId) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public Order(String username, List<MainApp.OrderItem> items, String status) {
        this.username = username;
        this.items = items;
        this.status = status;
        this.orderDate = new Date();
        this.userId = username;
        calculateTotal();
    }

    public Order(int id, String userId, Date orderDate, List<MainApp.OrderItem> items, String status, String paymentMethod) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.items = items;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    private void calculateTotal() {
        this.totalAmount = items.stream()
            .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
            .sum();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public List<MainApp.OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return getTotalAmount();
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItems(List<MainApp.OrderItem> items) {
        this.items = new ArrayList<>(items);
        calculateTotal();
    }
    
    // Méthode pour convertir les OrderItem en MainApp.OrderItem
    public void setOrderItems(List<OrderItem> items) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items.clear();
        }
        
        for (OrderItem item : items) {
            this.items.add(new MainApp.OrderItem(item.getMenuItem(), item.getQuantity()));
        }
        calculateTotal();
    }

    public void addItem(MainApp.OrderItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        calculateTotal();
    }

    public String getFormattedTotal() {
        return String.format("%,.0f FCFA", totalAmount);
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return String.format("Commande de %s - %s - %s", username, getFormattedTotal(), status);
    }
} 