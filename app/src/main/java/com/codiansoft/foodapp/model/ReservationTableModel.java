package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 9/21/2017.
 */

public class ReservationTableModel {
    private String ID;
    private String title;
    private String capacity;
    private String price;
    private String type;
    private String imageUrl;
    private String description;
    private String status;

    public ReservationTableModel(String ID, String title, String status, String capacity){
        this.ID = ID;
        this.title = title;
        this.capacity = capacity;
        this.price = price;
        this.type = type;
        this.imageUrl = imageUrl;
        this.description = description;
        this.status = status;
    }

    public String getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public String getCapacity() {
        return capacity;
    }
    public String getStatus() {
        return status;
    }
}
