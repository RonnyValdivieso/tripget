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
    public static final String GET_TRIPS = URL + "/getTrips";
    public static final String GET_TRIPS_BY_BUDGET = URL + "/getTripsByBudget";
    public static final String  GET_TRIPS_BY_DESTINATION = URL + "/getTripsByDestination.php";
}
