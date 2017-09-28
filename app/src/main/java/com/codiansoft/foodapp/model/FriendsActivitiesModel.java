package com.codiansoft.foodapp.model;

/**
 * Created by Codiansoft on 9/25/2017.
 */

public class FriendsActivitiesModel {
    private String id, friendName, friendPic, activityTitle, activityDate, activityTime;

    public FriendsActivitiesModel(String id, String friendName, String friendPic, String activityTitle, String activityDate, String activityTime) {
        this.id = id;
        this.friendName = friendName;
        this.friendPic = friendPic;
        this.activityTitle = activityTitle;
        this.activityDate = activityDate;
        this.activityTime = activityTime;
    }

    public String getID() {
        return id;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendPic() {
        return friendPic;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public String getActivityTime() {
        return activityTime;
    }
}