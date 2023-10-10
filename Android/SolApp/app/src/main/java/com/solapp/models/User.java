package com.solapp.models;

public class User {

    private String username;
    private String name;
    private String roll;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructs a user with specified username and name.
     * @param username user's username
     * @param name user name
     */
    public User(String username, String name) {
        this.username = username;
        this.name = name;
        this.roll = roll;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
