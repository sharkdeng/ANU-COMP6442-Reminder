package com.anu.dolist;//package com.anu.dolist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.anu.dolist.MainActivity.locations;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;

    LocationListener locationListener;

    public static double currentLat;
    public static double currentLon;



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


        // get data
        Intent intent = getIntent();



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

        /**
         * mark current location
         */
        // Add a marker in current location and move the camera
        LatLng currentPlace = new LatLng(currentLat, currentLon);
        mMap.addMarker(new MarkerOptions()
                .position(currentPlace)
                .title("Marker in Sydney"));

        // I don't want zoom
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace,15)); // move camera to current location and zoom
//        mMap.animateCamera(CameraUpdateFactory.zoomTo( 15.0f ) );




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
    public void onSelect(View v) {
        Intent intent = new Intent();
        intent.putExtra("location", locations );
        setResult(RESULT_OK, intent);
        finish();
    }

}
