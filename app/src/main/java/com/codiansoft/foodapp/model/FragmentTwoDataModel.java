package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 8/21/2017.
 */

public class FragmentTwoDataModel {
    private String id, title, description, price, image;
    String sectionTitle;
    boolean isRequired;
    boolean isRow;

    public FragmentTwoDataModel() {
    }

    public FragmentTwoDataModel(String id, String title, String description, String price, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
        this.isRow = true;
    }
    public FragmentTwoDataModel(String sectionTitle, boolean isRequired) {
        this.sectionTitle = sectionTitle;
        this.isRequired = isRequired;
        this.isRow = false;
    }

    public String getTitle() {
        return title;
    }
    public String getID() {
        return id;
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