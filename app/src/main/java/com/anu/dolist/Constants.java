package com.anu.dolist;

/**
 * global variables
 */
public class Constants {

    public static String TAG = "Shark";

    public static double CURRENT_LAT;
    public static double CURRENT_LON;

    public final static String NOTIFICATION_CHANNEL_ID = "10001" ;
    public final static String default_notification_channel_id = "default" ;
    public final static int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;


    // request codes
    public static final int LOCATION_REQUEST_CODE = 1000; // get current location
    public static final int BACK_LOC_REQUEST_CODE = 1001; // for geofence

    // geofence
    public static long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 60 * 60 * 1000;
    public static float GEOFENCE_RADIUS_IN_METERS = 500.0f;

    public static final int GEOFENCE_REQ_CODE = 0;



}
