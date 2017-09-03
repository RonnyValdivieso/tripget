package com.tripget.tripget.Model;


/**
 * Created by ivonne on 08/08/17.
 */

public class Trip {

    private int id;
    private String username;
    private String title;
    private String content;
    private String destination;
    private String trip_date;
    private int food;
    private int accommodation;
    private int trip_transportation;
    private int local_transportation;
    private int entertainment;
    private int shopping;
    private int budget;
    private String photo;
    private String trip_image;
    private int likes;
    private int saved;
    private String number_people;
    private String trip_duration;

    //Para ejemplo
    private String date;

    public Trip() {
    }

    public Trip(int id, String username, String title, String trip_date, int budget, String photo, String trip_image, int likes) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.trip_date = trip_date;
        this.budget = budget;
        this.photo = photo;
        this.trip_image = trip_image;
        this.likes = likes;
    }
    public Trip(int id, String title, String destination,String username, String trip_date, int budget, String photo, String trip_image, int likes) {
        this.id = id;
        this.destination = destination;
        this.username = username;
        this.title = title;
        this.trip_date = trip_date;
        this.budget = budget;
        this.photo = photo;
        this.trip_image = trip_image;
        this.likes = likes;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
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

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
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

    public String getTrip_image() {
        return trip_image;
    }

    public void setTrip_image(String trip_image) {
        this.trip_image = trip_image;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}