package com.codiansoft.foodapp.model;

/**
 * Created by salal-khan on 7/4/2017.
 */

public class DealsModel {
    private String dealID;
    private String title;
    private String duration;
    private String price;
    private String type;
    private String imageUrl;
    private String description;

    public DealsModel(String ID, String title, String duration, String price, String type, String imageUrl, String description){
        this.dealID = ID;
        this.title = title;
        this.duration = duration;
        this.price = price;
        this.type = type;
        this.imageUrl = imageUrl;
        this.description = description;
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
}
