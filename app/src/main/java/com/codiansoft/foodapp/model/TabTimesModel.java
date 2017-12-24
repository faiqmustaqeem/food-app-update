package com.codiansoft.foodapp.model;

/**
 * Created by ali on 12/24/17.
 */

public class TabTimesModel {
    String toTime, fromTime;

    public TabTimesModel(String toTime, String fromTime) {
        this.toTime = toTime;
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public String getFromTime() {
        return fromTime;
    }
}
