package com.onlinequiz.model;

public class User {
    private int id;
    private String username;
    private boolean isAdmin;

    public User(int id, String username, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public int getId(){ return id; }
    public String getUsername(){ return username; }
    public boolean isAdmin(){ return isAdmin; }
}
