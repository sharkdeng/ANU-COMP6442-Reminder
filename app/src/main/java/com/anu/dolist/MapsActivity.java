package com.anu.dolist;//package com.anu.dolist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.ActionBar;

import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.anu.dolist.MainActivity.locations;


/**
 * @author Limin Deng(u6849956), Supriya Kamble(u6734521)
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {


    private GoogleMap mMap;

    LocationManager locationManager;

    LocationListener locationListener;

    EventRepository er;

    /**
     *
     * @param requestCode : it is a request code for location permission.
     * @param permissions :permission of location
     * @param grantResults: if requested granted will be greater than 0
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation, "Your location");

            }


        }

    }

    /**
     *
     * @param location : location of user
     * @param title : title for marker
     */
    public void centerMapOnLocation(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.clear();

        if (title != "Your location") {

            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // init er
        er = new EventRepository(getApplication());



        /**
         * show action bar
         */
        ActionBar actionBar =  getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.abs_layout);
        actionBar.setTitle("Map");



        /**
         * BottomNavigationView
         * @author: Limin Deng(u6849956)
         */
        // callback when item on BottomNavigationView is selected
        BottomNavigationView bnv = findViewById(R.id.map_nav);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.main_item_1:

                        startActivity(new Intent(MapsActivity.this, MainActivity.class));
                        finish();
                        break;

                    case R.id.main_item_2:

                        startActivity(new Intent(MapsActivity.this, CalendarActivity.class));
                        finish();
                        break;

                    case R.id.main_item_3:
                        System.out.println("do nothing");

                        break;
                }

                // update selecte state
                return true;
            }
        });
        bnv.setSelectedItemId(R.id.main_item_3);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        addAllEventMarkers();




//        mMap.setOnMapLongClickListener(this);
//
//        Intent intent = getIntent();
//
//        if (intent.getIntExtra("placeNumber", 0) == 0) {
//
//            // zoom in on user's location
//
//            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//            locationListener = new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//
//                    centerMapOnLocation(location, "Your location");
//
//                }
//
//                @Override
//                public void onStatusChanged(String s, int i, Bundle bundle) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String s) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String s) {
//
//                }
//            };
//
////            if (Build.VERSION.SDK_INT < 23) {
////
////                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
////
////            } else {
//
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//                    centerMapOnLocation(lastKnownLocation, "Your location");
//
//                } else {
//
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//                }
//
//
//
//
//
//        } else {
//
//            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
//            placeLocation.setLatitude(locations.get(intent.getIntExtra("placeNumber", 0)).latitude);
//            placeLocation.setLongitude(locations.get(intent.getIntExtra("placeNumber", 0)).longitude);
//
//            centerMapOnLocation(placeLocation, MainActivity.places.get(intent.getIntExtra("placeNumber", 0)));
//
//        }

    }



    /**
     * independet function for clear map and add markers back again
     * @author: Limin Deng
     */
    public void addAllEventMarkers() {

        if (mMap == null) return;
        /**
         * mark current location
         * move camera here
         */
        // Add a marker in current location and move the camera
        LatLng currentPlace = new LatLng(Constants.CURRENT_LAT, Constants.CURRENT_LON);
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(currentPlace)
                .title("You are here"));

        // I don't want zoom
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace,15)); // move camera to current location and zoom
//        mMap.animateCamera(CameraUpdateFactory.zoomTo( 15.0f ) );


        /**
         * mark all event locations
         */
        // different markers to distinguish completedEvents and incompletedEvents
        List<Event> completedEvents = er.getAllCompletedEvents();
        List<Event> incompletedEvents = er.getAllIncompletedEvents();

        // get completed locations
        for (Event e: completedEvents) {
            // make sure it has location
            if (!e.location.equals("")) {
                String lat = e.location.split("/")[1];
                String lon = e.location.split("/")[2];
                double lat_d = Double.valueOf(lat);
                double lon_d = Double.valueOf(lon);
                mMap.addMarker(new MarkerOptions()
                        // make it different from current loca
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(new LatLng(lat_d, lon_d))
                        .title("Good"));
            }
        }

        // get completed locations
        for (Event e: incompletedEvents) {
            // make sure it has location
            if (!e.location.equals("")) {
                String lat = e.location.split("/")[1];
                String lon = e.location.split("/")[2];
                double lat_d = Double.valueOf(lat);
                double lon_d = Double.valueOf(lon);
                mMap.addMarker(new MarkerOptions()
                        // make it different from current loca
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .position(new LatLng(lat_d, lon_d))
                        .title("Good"));
            }
        }
    }





    /**
     * independent function to draw geofence circles
     * @author: Limin Deng
     */
    public void addAllGeofenceCircles() {
        List<Event> events = er.getAllEvents();
        for (Event e: events) {
            if (!e.location.equals("")){

                double lat = Double.valueOf(e.location.split("/")[1]);
                double lon = Double.valueOf(e.location.split("/")[2]);

                CircleOptions circleOptions = new CircleOptions()
                        .center( new LatLng(lat, lon))
                        .radius(Constants.GEOFENCE_RADIUS_IN_METERS)
                        .fillColor(0x40ff0000)
                        .strokeColor(Color.TRANSPARENT)
                        .strokeWidth(10);
                // Get back the mutable Circle
                Circle circle = mMap.addCircle(circleOptions);
            }
        }

    }




    /**
     *
     * @param latLng : latitude and longitude of location
     */

    @Override
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String address = "";

        try {

            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (listAddresses != null && listAddresses.size() > 0) {

                if (listAddresses.get(0).getThoroughfare() != null) {

                    if (listAddresses.get(0).getSubThoroughfare() != null) {

                        address += listAddresses.get(0).getSubThoroughfare() + " ";

                    }

                    address += listAddresses.get(0).getThoroughfare();

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (address == "") {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");

            address = sdf.format(new Date());

        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        MainActivity.places.add(address);
        locations.add(latLng);

        MainActivity.arrayAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();

    }



    /**
     * Add menu items to toolbar
     * @author: u6734521
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }




    /**
     * toggle geoface virsualizing
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        /**
         * callbacks on menu items
         */
        if(item.getItemId() == R.id.map_menu_geofence_show) {

            mMap.clear();
            addAllEventMarkers();
            addAllGeofenceCircles();

            return true;
        } else if (item.getItemId() == R.id.map_menu_geofence_hide) {
            mMap.clear();
            addAllEventMarkers();
            return true;

        }

        return false;
    }

}
