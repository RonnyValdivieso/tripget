package com.tripget.tripget.Conexion;

/**
 * Created by ivonne on 13/08/17.
 */

public class Constantes {


    public  static final String MENSAJE = "mensaje";

    //Status
    public static final String ESTADO = "estado";
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    //WebService URLs

    public static final String URL = "http://mobulancer.com/tripgetWebService/view";

    //GET DATA
    public static final String GET_TRIPS = URL + "/getTrips.php";
    public static final String GET_TRIP_BY_ID = URL + "/getTripById.php";
    public static final String GET_TRIPS_BY_BUDGET = URL + "/getTripsByBudget.php";
    public static final String  GET_TRIPS_BY_DESTINATION = URL + "/getTripsByDestination.php";
    public static final String  GET_TRIPS_BY_USER_ID = URL + "/getTripsByUserId.php";
    public static final String GET_ID_BY_TOKEN = URL + "/getIdByToken.php";
    public static final String  INSERT_USER = URL + "/setUser.php";
    public static final String INSERT_TRIP = URL + "/setTrip.php";
    public static final String DELETE_TRIP = URL + "/deleteTrip.php";
    public static final String EDIT_TRIP = URL + "/";
}
