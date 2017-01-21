package com.collage.util.model;


@SuppressWarnings("ALL")
public class User {

    public String fullName;
    public String email;
    public String uid;

    public User() {
        // default constructor for Firebase
    }

    public User(String fullName, String email, String uid) {
        this.fullName = fullName;
        this.email = email;
        this.uid = uid;
    }
}
