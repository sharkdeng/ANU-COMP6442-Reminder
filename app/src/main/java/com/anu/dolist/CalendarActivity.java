package com.anu.dolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;


/**
 * @author: Limin Deng(u6849956)
 */
public class CalendarActivity extends AppCompatActivity {

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
        CalendarView cv = findViewById(R.id.cal);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int date) {

                // to next activity
                Intent go = new Intent(CalendarActivity.this, DayActivity.class);

                // get selected date
                // working
                String selectedDate = date + "/" + month + "/" + year;

                // pop up window
                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();


                Calendar c = Calendar.getInstance();
                c.set(year, month, date);
                long eventOccursOn = c.getTimeInMillis(); //this is what you want to use later


                // get selected date
                // not working
                // return null
//                long selectedDate = calendarView.getDate();
//                Date sd = new Date(selectedDate);

                String monthString = "";
                switch (month) {
                    case 1:
                        monthString = "January";
                        break;
                    case 2:
                        monthString = "February";
                        break;
                    case 3:
                        monthString = "March";
                        break;
                    case 4:
                        monthString = "April";
                        break;
                    case 5:
                        monthString = "May";
                        break;
                    case 6:
                        monthString = "June";
                        break;
                    case 7:
                        monthString = "July";
                        break;
                    case 8:
                        monthString = "August";
                        break;
                    case 9:
                        monthString = "September";
                        break;
                    case 10:
                        monthString = "October";
                        break;
                    case 11:
                        monthString = "November";
                        break;
                    case 12:
                        monthString = "December";
                        break;
                }
                // pass data to next activity first
                go.putExtra("SelectedDate", selectedDate);
                go.putExtra("Month", monthString);

                // then transit
                startActivity(go);

                // close current activity
//                finish();
            }
        });



        // set selected date 22 May 2016 in milliseconds
        cv.setDate(1463918226920L);

        // set Monday as the first day of the week
        cv.setFirstDayOfWeek(2);

        final int firstDayOfWeek= cv.getFirstDayOfWeek(); // get first day of the week





        // FIXME: how to avoid conflicts
        /**
         * @author: Limin Deng(u6849956)
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
                        Intent go3 = new Intent(CalendarActivity.this, MapActivity.class);
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
