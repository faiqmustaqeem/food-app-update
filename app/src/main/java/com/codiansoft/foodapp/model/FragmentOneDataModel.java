package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 8/17/2017.
 */

public class FragmentOneDataModel {
    private String id, title, description, price, image;
    String sectionTitle;
    boolean isRequired;
    boolean isRow;

    private String restaurant_id;
    private String branch_id;
    private String variation;

    public FragmentOneDataModel() {
    }

    public FragmentOneDataModel(String id, String title, String description, String price, String image, String restaurant_id,String branch_id,String variation) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
        this.isRow = true;
        this.setRestaurant_id(restaurant_id);
        this.setBranch_id(branch_id);
        this.setVariation(variation);
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

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }
}