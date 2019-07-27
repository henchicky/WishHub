package com.example.wishhub.HomePage;

import java.io.Serializable;

public class Post implements Serializable {

    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    private String price;
    private String title;
    private String itemcondition;
    private String uploaddate;

    public Post(String postid, String postimage, String description, String publisher, String price, String title, String itemcondition, String uploaddate) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.price = price;
        this.title = title;
        this.itemcondition = itemcondition;
        this.uploaddate = uploaddate;
    }

    public String getUploaddate() { return uploaddate; }

    public void setUploaddate(String uploaddate) { this.uploaddate = uploaddate; }

    public Post() { }

    public String getItemcondition() { return itemcondition; }

    public void setItemcondition(String itemcondition) { this.itemcondition = itemcondition; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
