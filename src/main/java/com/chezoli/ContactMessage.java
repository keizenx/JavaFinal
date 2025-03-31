package com.chezoli;

public class ContactMessage {
    private int id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String date;

    public ContactMessage(int id, String name, String email, String subject, String message, String date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.date = date;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
    public String getDate() { return date; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setMessage(String message) { this.message = message; }
    public void setDate(String date) { this.date = date; }
} 