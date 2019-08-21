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
    private String name, location, meetup, delivery, wish, sold;

    public Post(String postid, String postimage, String description, String publisher, String price, String title, String itemcondition, String uploaddate, String name, String location, String meetup, String delivery, String wish, String sold) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.price = price;
        this.title = title;
        this.itemcondition = itemcondition;
        this.uploaddate = uploaddate;
        this.name = name;
        this.location = location;
        this.meetup = meetup;
        this.delivery = delivery;
        this.wish = wish;
        this.sold = sold;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getMeetup() {
        return meetup;
    }

    public void setMeetup(String meetup) {
        this.meetup = meetup;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
