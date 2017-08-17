package com.tripget.tripget.Model;

/**
 * Created by ivonne on 09/08/17.
 */

public class Notification {

    private String usernameNotification,userActionNotification, userGIDNotification, tripIdNotification;
    private int userImgNotification, tripImgNotification;

    public Notification() {
    }

    public Notification(String usernameNotification, String userActionNotification, String userGIDNotification, String tripIdNotification, int userImgNotification, int tripImgNotification) {
        this.usernameNotification = usernameNotification;
        this.userActionNotification = userActionNotification;
        this.userGIDNotification = userGIDNotification;
        this.tripIdNotification = tripIdNotification;
        this.userImgNotification = userImgNotification;
        this.tripImgNotification = tripImgNotification;
    }

    public Notification(String usernameNotification, String userActionNotification, int userImgNotification, int tripImgNotification) {
        this.usernameNotification = usernameNotification;
        this.userActionNotification = userActionNotification;
        this.userImgNotification = userImgNotification;
        this.tripImgNotification = tripImgNotification;
    }

    public String getUsernameNotification() {
        return usernameNotification;
    }

    public void setUsernameNotification(String usernameNotification) {
        this.usernameNotification = usernameNotification;
    }

    public String getUserActionNotification() {
        return userActionNotification;
    }

    public void setUserActionNotification(String userActionNotification) {
        this.userActionNotification = userActionNotification;
    }

    public String getUserGIDNotification() {
        return userGIDNotification;
    }

    public void setUserGIDNotification(String userGIDNotification) {
        this.userGIDNotification = userGIDNotification;
    }

    public String getTripIdNotification() {
        return tripIdNotification;
    }

    public void setTripIdNotification(String tripIdNotification) {
        this.tripIdNotification = tripIdNotification;
    }

    public int getUserImgNotification() {
        return userImgNotification;
    }

    public void setUserImgNotification(int userImgNotification) {
        this.userImgNotification = userImgNotification;
    }

    public int getTripImgNotification() {
        return tripImgNotification;
    }

    public void setTripImgNotification(int tripImgNotification) {
        this.tripImgNotification = tripImgNotification;
    }

}
