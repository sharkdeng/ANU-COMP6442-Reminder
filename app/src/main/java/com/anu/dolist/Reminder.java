package com.anu.dolist;

import android.Manifest;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventRepository;
import com.anu.dolist.notify.GeofenceBroadcastReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Reminder extends Application {


    // send current location
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    // GeoFence
    private GeofencingClient geofencingClient;
    private Geofence geofence;
    List geofenceList = new ArrayList();

    private EventRepository er;



    @Override
    public void onCreate() {
        super.onCreate();

        getCurrentLocation();
        enableGeofence();
    }




    /**
     * get current location
     * @author: Limin Deng
     */
    public void getCurrentLocation() {

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            System.out.println("location permitted");


            // method 2
            // Construct a FusedLocationProviderClient.
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



            // reqeust
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(20 * 1000);

            // callback
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        System.out.println("nothing");
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {

                            Constants.CURRENT_LAT  = location.getLatitude();
                            Constants.CURRENT_LON= location.getLongitude();



//                            txtLocation.setText(String.format(Locale.US, "%s -- %s", myLat, myLon));
                        }
                    }
                }
            };

            // set update
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,  Looper.myLooper());

            // remove request or it will take resources
            if (mFusedLocationClient != null) {
                mFusedLocationClient.removeLocationUpdates(locationCallback);
            }


            // get current location
            Task<Location> task = mFusedLocationClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null) {
                        //Write your implemenation here
                        Log.d("AndroidClarified",location.getLatitude()+" "+location.getLongitude());

                        // enter map activity
                        Intent intent = new Intent(this MapsActivity.class);
                        // fill the two global variables
                        Constants.CURRENT_LAT = location.getLatitude();
                        Constants.CURRENT_LON = location.getLongitude();
                    }
                }
            });

        } else {
            getLocationPermission();
        }
    }



    /**
     * code snipt to get permission for retrieving current location
     * @author: Limin Deng
     */
    private void getLocationPermission() {

        // A local method to request required permissions;
        // See https://developer.android.com/training/permissions/requesting
//            getLocationPermission();
        System.out.println("need location permission");

        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.LOCATION_REQUEST_CODE);


            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    /**
     * one more permission for geofence
     * @author: Limin Deng
     */
    private void getBackLocPermission() {

        // A local method to request required permissions;
        // See https://developer.android.com/training/permissions/requesting
//            getLocationPermission();

        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    Constants.BACK_LOC_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }



    /**
     * geofence - 3 get pending intent
     * @author: Limin Deng
     */
    private PendingIntent geofencePendingIntent;
    private PendingIntent getGeofencePendingIntent(Event event) {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().

        // pass eventId and event title to
        intent.putExtra(Constants.GEO_CHANNEL_ID, event._id);
        intent.putExtra(Constants.GEO_CHANNEL_NAME, event.title);

        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }


    /**
     * @author: Limin Deng
     */
    public void enableGeofence() {
        /**
         * geofence
         */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED  &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            // geoFence client
            geofencingClient = LocationServices.getGeofencingClient(this);

            // 1 - geofence for each event
            List<Event> events = er.getAllEvents();
            for (Event e: events) {
                // it has location
                if (!e.location.equals("")) {

                    // get three
                    String requestId = String.valueOf(e._id);
                    double lat = Double.valueOf(e.location.split("/")[1]);
                    double lon = Double.valueOf(e.location.split("/")[2]);

                    // create one geofence
                    geofence = new Geofence.Builder()
                            // Set the request ID of the geofence. This is a string to identify this
                            // geofence.
                            .setRequestId(requestId) // entry.getKey() can be eventId
                            .setCircularRegion(
                                    lat,
                                    lon,
                                    Constants.GEOFENCE_RADIUS_IN_METERS
                            )
                            .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                    Geofence.GEOFENCE_TRANSITION_EXIT)
                            .build();

                    // add to list
                    geofenceList.add(geofence);
                }



                // 2 - request for each event
                GeofencingRequest request = new GeofencingRequest.Builder()
                        // Notification to trigger when the Geofence is created
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                        .addGeofences(geofenceList) // add a Geofence
                        .build();





                // 3 - intent for each event
                geofencingClient.addGeofences(request, getGeofencePendingIntent(e))
                        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Geofences added
                                // ...

                                System.out.println("Gooood!!!!Gooood!!!!Gooood!!!!");
                                System.out.println("Gooood!!!!");
                            }
                        })
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to add geofences
                                // ...
                            }
                        });
            }



        } else {
            getBackLocPermission();
        }
    }
}

