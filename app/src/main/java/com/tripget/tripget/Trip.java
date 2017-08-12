package com.tripget.tripget;


import java.net.URL;
import java.util.Date;

/**
 * Created by ivonne on 08/08/17.
 */

public class Trip {

    private int id;
    private String username;
    private String title;
    private String content;
    private String destination;
    private Date trip_date;
    private int food;
    private int accommodation;
    private int trip_transportation;
    private int local_transportation;
    private int entertainment;
    private int shopping;
    private int total_budget;
    private URL user_image;
    private URL trip_image;
    private int votes;
    private int saved;
    private String number_people;
    private String trip_duration;

    //Para ejemplo
    private int userImg;
    private int userImgUpload;
    private String date;

    public Trip() {
    }

    public Trip(int id, String username, String title, Date trip_date, int total_budget, URL user_image, URL trip_image, int votes, int saved) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.trip_date = trip_date;
        this.total_budget = total_budget;
        this.user_image = user_image;
        this.trip_image = trip_image;
        this.votes = votes;
        this.saved = saved;
    }

    public Trip(String username, String destination, int votes, int userImg, int userImgUpload, String date, int total_budget ) {
        this.username = username;
        this.destination = destination;
        this.votes = votes;
        this.userImg = userImg;
        this.userImgUpload = userImgUpload;
        this.date = date;
        this.total_budget = total_budget;
    }

    public Trip(int id, String username, String title, int total_budget, int votes, int saved, int userImg, int userImgUpload, String date) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.total_budget = total_budget;
        this.votes = votes;
        this.saved = saved;
        this.userImg = userImg;
        this.userImgUpload = userImgUpload;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(Date trip_date) {
        this.trip_date = trip_date;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(int accommodation) {
        this.accommodation = accommodation;
    }

    public int getTrip_transportation() {
        return trip_transportation;
    }

    public void setTrip_transportation(int trip_transportation) {
        this.trip_transportation = trip_transportation;
    }

    public int getLocal_transportation() {
        return local_transportation;
    }

    public void setLocal_transportation(int local_transportation) {
        this.local_transportation = local_transportation;
    }

    public int getEntertainment() {
        return entertainment;
    }

    public void setEntertainment(int entertainment) {
        this.entertainment = entertainment;
    }

    public int getShopping() {
        return shopping;
    }

    public void setShopping(int shopping) {
        this.shopping = shopping;
    }

    public int getTotal_budget() {
        return total_budget;
    }

    public void setTotal_budget(int total_budget) {
        this.total_budget = total_budget;
    }

    public URL getUser_image() {
        return user_image;
    }

    public void setUser_image(URL user_image) {
        this.user_image = user_image;
    }

    public URL getTrip_image() {
        return trip_image;
    }

    public void setTrip_image(URL trip_image) {
        this.trip_image = trip_image;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getSaved() {
        return saved;
    }

    public void setSaved(int saved) {
        this.saved = saved;
    }

    public String getNumber_people() {
        return number_people;
    }

    public void setNumber_people(String number_people) {
        this.number_people = number_people;
    }

    public String getTrip_duration() {
        return trip_duration;
    }

    public void setTrip_duration(String trip_duration) {
        this.trip_duration = trip_duration;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}