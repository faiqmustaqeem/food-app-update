package com.codiansoft.foodapp.model;

/**
 * Created by CodianSoft on 02/01/2018.
 */

public class CountryModel {
    private String id ;
    private String name ;

   public  CountryModel(String id ,String name)
    {
        this.setName(name);
        this.setId(id);
    }

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
}
