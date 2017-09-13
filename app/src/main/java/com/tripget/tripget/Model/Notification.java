package com.tripget.tripget.Model;

/**
 * Created by ivonne on 09/08/17.
 */

public class Notification {

    private String username, id;
    private String photo, trip_image;

    public Notification() {
    }

    public Notification(String username, String photo, String trip_image) {
        this.username = username;
        this.photo = photo;
        this.trip_image = trip_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTrip_image() {
        return trip_image;
    }

    public void setTrip_image(String trip_image) {
        this.trip_image = trip_image;
    }
}
