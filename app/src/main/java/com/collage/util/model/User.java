package com.collage.util.model;


public class User {

    public String fullName;
    public String email;
    public String uid;

    @SuppressWarnings("unused")
    public User() {
        // default constructor for Firebase
    }

    public User(String fullName, String email, String uid) {
        this.fullName = fullName;
        this.email = email;
        this.uid = uid;
    }
}
