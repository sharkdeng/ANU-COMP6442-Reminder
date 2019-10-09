package com.anu.dolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * @author: Limin Deng(u6849956)
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mv;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        gmap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        gmap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     * toolbar home button callback
     * no use
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                System.out.println("Hello");
                finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);





        mv = findViewById(R.id.map_view);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mv = findViewById(R.id.map_view);
        mv.onCreate(mapViewBundle);
        mv.getMapAsync(this);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            // Show rationale and request permission.
        }



        // Done: how to avoid conflicts
        // because setSelectedItem is not changed
        /**
         * @author: Limin Deng(u6849956)
         */
        // callback when item on BottomNavigationView is selected
        BottomNavigationView bnv = findViewById(R.id.map_nav);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                System.out.println("good");

                switch (menuItem.getItemId()) {
                    case R.id.main_item_1:
                        Intent go1 = new Intent(MapActivity.this, MainActivity.class);
                        startActivity(go1);
                        finish();
                        break;

                    case R.id.main_item_2:

                        Intent go2 = new Intent(MapActivity.this, CalendarActivity.class);
                        startActivity(go2);
                        finish();

                        break;

                    case R.id.main_item_3:
                        System.out.println("Do nothing");
                        break;
                }

                // update selecte state
                return true;
            }
        });
        bnv.setSelectedItemId(R.id.main_item_3);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mv.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mv.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mv.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mv.onStop();
    }
    @Override
    protected void onPause() {
        mv.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mv.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv.onLowMemory();
    }

}
