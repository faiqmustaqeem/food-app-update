package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 9/19/2017.
 */

public class MyOrdersModel {
    private String dealID;
    private String title;
    private String duration;
    private String price;
    private String type;
    private String imageUrl;
    private String description;
    private String status;

    public MyOrdersModel(String ID, String title, String duration, String price, String type, String imageUrl, String description, String status){
        this.dealID = ID;
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.type = type;
        this.imageUrl = imageUrl;
        this.description = description;
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getTitle() {
        return title;
    }
    public String getDuration() {
        return duration;
    }
    public String getPrice() {
        return price;
    }
    public String getType() {
        return type;
    }
    public String getDealID() {
        return dealID;
    }
    public String getDescription() {
        return description;
    }
    public String getStatus() {
        return status;
    }
}
