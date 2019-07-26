package com.example.wishhub.Authentication;

public class User {
    private String id;
    private String imageURL;
    private String name;
    private String email;
    private String status;
    private String joindate;
    private String bio;

    public User(String id, String imageURL, String name, String email, String status, String joindate, String bio) {
        this.id = id;
        this.imageURL = imageURL;
        this.name = name;
        this.email = email;
        this.status = status;
        this.joindate = joindate;
        this.bio = bio;
    }

    public User() {
        //empty Constructor
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getJoindate() { return joindate; }

    public void setJoindate(String joindate) { this.joindate = joindate; }

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
