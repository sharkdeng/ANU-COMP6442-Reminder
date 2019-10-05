package com.anu.dolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);




        // FIXME: how to avoid duplicate
        /**
         * @author: Limin Deng(u6849956)
         */
        // callback when item on BottomNavigationView is selected
//        BottomNavigationView bnv = findViewById(R.id.map_nav);
//        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                System.out.println("good");
//
//                switch (menuItem.getItemId()) {
//                    case R.id.main_item_1:
//                        Intent go1 = new Intent(MapActivity.this, MainActivity.class);
//                        startActivity(go1);
//                        finish();
//                        break;
//
//                    case R.id.main_item_2:
//
//                        Intent go2 = new Intent(MapActivity.this, CalendarActivity.class);
//                        startActivity(go2);
//                        finish();
//
//                        break;
//
//                    case R.id.main_item_3:
//                        System.out.println("Do nothing");
//                        break;
//                }
//
//                // update selecte state
//                return true;
//            }
//        });
//        bnv.setSelectedItemId(R.id.main_item_1);
    }
}
