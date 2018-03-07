package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 9/21/2017.
 */

public class ReservationTableModel {
    private String ID;
    private String number;


    public ReservationTableModel(String ID, String number){
        this.setID(ID);
        this.setNumber(number);
    }

    public String getID() {
        return ID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public void setID(String ID) {
        this.ID = ID;
    }
    public String getTitle()
    {
        return "Table - "+this.number;
    }
}
