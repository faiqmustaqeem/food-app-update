package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 8/28/2017.
 */

public class SearchCategoriesModel {
    String ID;
    String image;
    String title;

    public SearchCategoriesModel(String ID, String image, String title) {
        this.ID = ID;
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getID() {
        return ID;
    }
}
