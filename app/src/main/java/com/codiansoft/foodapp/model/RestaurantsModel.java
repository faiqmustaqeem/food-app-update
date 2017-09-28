package com.codiansoft.foodapp.model;

/**
 * Created by salal-khan on 7/3/2017.
 */

public class RestaurantsModel {
    String id;
    String name;
    String pic;
    String duration;
    String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RestaurantsModel(String id, String name, String pic, String duration, String type) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.duration = duration;
        this.type = type;

    }
}
