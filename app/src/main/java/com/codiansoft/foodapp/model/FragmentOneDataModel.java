package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 8/17/2017.
 */

public class FragmentOneDataModel {
    private String id, title, description, price, image;
    String sectionTitle;
    boolean isRequired;
    boolean isRow;

    public FragmentOneDataModel() {
    }

    public FragmentOneDataModel(String id, String title, String description, String price, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
        this.isRow = true;
    }
    public FragmentOneDataModel(String sectionTitle, boolean isRequired) {
        this.sectionTitle = sectionTitle;
        this.isRequired = isRequired;
        this.isRow = false;
    }

    public String getID() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
    public String getImageURL() {
        return image;
    }

    public boolean isRow() {
        return isRow;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }
}