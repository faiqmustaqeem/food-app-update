package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 8/24/2017.
 */

public class FoodModel {
    private String title, description, price, image;
    String sectionTitle;
    boolean isRequired;
    boolean isRow;
    String category;
    boolean singleChoiceSelection;
    String foodItemID;
    private boolean singleChoiceVariation;

    public FoodModel() {
    }

    public FoodModel(String foodItemID, String title, String price, String category) {
        this.foodItemID = foodItemID;
        this.title = title;
        this.price = price;
        this.isRow = true;
        this.category = category;
        this.singleChoiceSelection = false;
    }

    public FoodModel(String sectionTitle, String sectionCategory, boolean isRequired) {
        this.sectionTitle = sectionTitle;
        this.isRequired = isRequired;
        this.isRow = false;
        this.category = sectionCategory;
    }

    public String getFoodItemID() {
        return foodItemID;
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
    public String getSectionCategory() {
        return category;
    }
    public boolean isSectionRequired() {
        return isRequired;
    }

    public void setSingleChoiceSelection(boolean b) {
        this.singleChoiceSelection = b;
    }

    public boolean getSingleChoiceSelection() {
        return singleChoiceSelection;
    }

    public boolean getSingleChoiceVariation() {
        return singleChoiceVariation;
    }

    public void setSingleChoiceVariation(boolean singleChoiceVariation) {
        this.singleChoiceVariation = singleChoiceVariation;
    }
}
