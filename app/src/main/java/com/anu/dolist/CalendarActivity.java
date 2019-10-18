package com.anu.dolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import sun.bob.mcalendarview.*;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Limin Deng(u6849956) Ran Zhang(u6760490)
 */
public class CalendarActivity extends AppCompatActivity {

    private EventRepository er = new EventRepository(getApplication());
    Map<String,List<String>> es = new HashMap<>();
    TextView tv;

    /**
     *  parse date into array for setting marks on calendar
     * @param date date which need to be parsed
     * @return parsed date in array type
     */
    public static int[] parseDate(String date){
        int[] time = new int[3];
        String[] tmp = date.split("/");
        time[0] = Integer.valueOf(tmp[2])+2000;
        time[1] = Integer.valueOf(tmp[1]);
        time[2] = Integer.valueOf(tmp[0]);
        return time;
    }

    /**
     * record events which are in the same day for showing
     * @param map place to store events
     * @param a event need to be stored
     */
    public static  void registevent(Map<String,List<String>> map, Event a){
        if(!map.containsKey(a.date)){
            List<String > tmp = new ArrayList<>();

            tmp.add(a.title+" " +a.time);
            map.put(a.date,tmp);
        }
        else{
            map.get(a.date).add(a.title+" "+a.time);
        }
    }

    /**
     * Perform initialization of all fragments.
     * @author Limin
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);




        // getActionBar() return null
        // this works
        // original actionbat
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.abs_layout);
        actionBar.setTitle("Calendar");





        // get the reference of CalendarView
        MCalendarView cv =  findViewById(R.id.cal);
        List<Event> events = er.getAllEvents();
        for(Event a:events){

            int [] tmp =parseDate(a.date);
            registevent(es,a);
            if(!a.completed)
            cv.markDate(
                    new DateData(tmp[0], tmp[1], tmp[2]).setMarkStyle(new MarkStyle(  MarkStyle.BACKGROUND, Color.RED)
                    ));
            else
                cv.markDate(
                        new DateData(tmp[0], tmp[1], tmp[2]).setMarkStyle(new MarkStyle(  MarkStyle.BACKGROUND, Color.GREEN)
                        ));
        }

        cv.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                String d = ""+date.getDay()+"/"+date.getMonth()+"/"+(date.getYear()-2000);
                tv = findViewById(R.id.textView);
                if(es.containsKey(d)) {
                    List<String> show = es.get(d);
                    String result = "";
                    for (String a : show) result += (a + "\n");

                    tv.setText(result);
                }
                else tv.setText("");
            }
        });

        // Done: how to avoid conflicts
        // because setSelectedItem is not changed

        /**
         * @author Limin Deng(u6849956)
         */
        // callback when item on BottomNavigationView is selected
        BottomNavigationView bnv = findViewById(R.id.cal_nav);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                System.out.println("good");

                switch (menuItem.getItemId()) {
                    case R.id.main_item_1:
                        Intent go1 = new Intent(CalendarActivity.this, MainActivity.class);
                        startActivity(go1);
                        finish();
                        break;

                    case R.id.main_item_2:

                        System.out.println("Do nothing");
                        break;

                    case R.id.main_item_3:
                        Intent go3 = new Intent(CalendarActivity.this, MapsActivity.class);
                        startActivity(go3);
                        finish();
                        break;
                }

                // update selecte state
                return true;
            }
        });
        bnv.setSelectedItemId(R.id.main_item_2);


    }


}
