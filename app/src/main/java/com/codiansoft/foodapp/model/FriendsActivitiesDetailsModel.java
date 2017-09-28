package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 9/26/2017.
 */

public class FriendsActivitiesDetailsModel {
    String ID;
    String image;
    String text;

    public FriendsActivitiesDetailsModel(String ID, String image, String text) {
        this.ID = ID;
        this.image = image;
        this.text = text;
    }

    public String getID() {
        return ID;
    }

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

}
