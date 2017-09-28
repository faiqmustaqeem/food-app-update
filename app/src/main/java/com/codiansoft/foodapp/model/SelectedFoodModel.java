package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 9/6/2017.
 */

public class SelectedFoodModel {
    private String title, price;
    String sectionTitle;
    String quantity;
    String choices;

    public SelectedFoodModel() {
    }

    public SelectedFoodModel(String ID, String title, String price, String quantity, String choices) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.choices = choices;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }
}