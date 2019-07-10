package com.example.wishhub.Authentication;

public class User {
    private String id;
    private String imageURL;
    private String name;
    private String email;
    private String status;

    public User(String id, String imageURL, String name, String email, String status) {
        this.id = id;
        this.imageURL = imageURL;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public User() {
        //empty Constructor
    }

    public String getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public String getEmail() { return email; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
