package com.tripget.tripget;

/**
 * Created by ivonne on 08/08/17.
 */

public class Trip {

    private String username;
    private String totalBudget;
    private int userImg;
    private int userImgUpload;
    private String votes;
    private String locationDate;
    public Trip() {
    }

    public Trip(String username, String totalBudget, int userImg, int userImgUpload, String votes, String locationDate) {
        this.username = username;
        this.totalBudget = totalBudget;
        this.userImg = userImg;
        this.userImgUpload = userImgUpload;
        this.votes = votes;
        this.locationDate = locationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(String totalBudget) {
        this.totalBudget = totalBudget;
    }

    public int getUserImg() {
        return userImg;
    }

    public void setUserImg(int userImg) {
        this.userImg = userImg;
    }

    public int getUserImgUpload() {
        return userImgUpload;
    }

    public void setUserImgUpload(int userImgUpload) {
        this.userImgUpload = userImgUpload;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getLocationDate() {
        return locationDate;
    }

    public void setLocationDate(String locationDate) {
        this.locationDate = locationDate;
    }
}
