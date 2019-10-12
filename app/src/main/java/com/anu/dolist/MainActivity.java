package com.anu.dolist;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> list = new ArrayList<>();
    private EventRepository er;
    private List<Event> events;
    static ArrayAdapter arrayAdapter;
    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    public static String PACKAGE_NAME;
    private ActionBar ab;
    private SearchView searchBtn;









    /**
     * Add menu items to toolbar
     * @author: u6734521
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note,menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * callback for menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);



        /**
         * callbacks on menu items
         */
        if(item.getItemId() == R.id.main_add_note){

            Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
            startActivity(intent);
            finish();
            return true;

        } else if (item.getItemId() == R.id.main_show_incompleted) {

            Log.d("Tag", "Incompleted");

            // clear the list first
            list.clear();

            events = er.getIncompletedEvents();


            // Avoid this error
            // Attempt to invoke interface method 'java.util.Iterator java.util.List.iterator()' on a null object reference
            // at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2778)
            if (events != null) {
                for (Event event: events) {
                    System.out.println("new record");
                    System.out.println(event.title);
                    list.add(event.title);
                }
            } else {
                list.add("Hello from DAO");
            }


            // fill in listView
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);


            // change title
            ab.setTitle("Incompleted");

            return true;

        } else if (item.getItemId() == R.id.main_show_completed) {

            Log.d("Tag", "Completed");

            // clear the list first
            list.clear();

            events = er.getCompletedEvents();


            // Avoid this error
            // Attempt to invoke interface method 'java.util.Iterator java.util.List.iterator()' on a null object reference
            // at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2778)
            if (events != null) {
                for (Event event: events) {
                    System.out.println("new record");
                    System.out.println(event.title);
                    list.add(event.title);
                }
            } else {
                list.add("Hello from DAO");
            }

            // fill in listView
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);

            //
            ab.setTitle("Completed");

            return true;

        } else if (item.getItemId() == R.id.main_delete_all) {

            er.deleteAll();
            return true;

        }


        return false;
    }





    /**
     * @author: u6734521
     * listView for notes taking with an example note
     * SharedPreferences to store data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get package name
        PACKAGE_NAME = getApplicationContext().getPackageName();
        System.out.println(PACKAGE_NAME);


        ab = getSupportActionBar();
        ab.setTitle("All events");

        // initiate EventRepository
        er = new EventRepository(getApplication());
        listView = findViewById(R.id.main_lv);

        searchBtn = findViewById(R.id.main_search_btn);






//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.anu.dolist", Context.MODE_PRIVATE);
//        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
//        if(set == null){
//            list.add("Example note");
//        }else{
//            list = new ArrayList(set);
//
//        }
//
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
//        listView.setAdapter(arrayAdapter);
        /**
         * @author: u6734521
         * to jump to editor activity when the list item is pressed.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
//
//                // pass data
                EventRepository er = new EventRepository(getApplication());

                String title = ((TextView) view).getText().toString();
                Event seletecEvent = er.getEventByTitle(title);
                intent.putExtra("title", seletecEvent.title);
                intent.putExtra("location", seletecEvent.location);
                intent.putExtra("start", seletecEvent.starts);
                intent.putExtra("end", seletecEvent.ends);
                intent.putExtra("alert", seletecEvent.alert);
                intent.putExtra("url", seletecEvent.url);
                intent.putExtra("notes", seletecEvent.notes);

                // I don't want this
//                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });

        /*
        @author: u6734521
        when the list is long pressed, pop up delete alert confirmation.
        if opted yes, delete else keep data as such.
        sharedPreference to update delete
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                // make it final for later access
                final int itemDelete = position;


                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setTitle("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.remove(itemDelete);
                                arrayAdapter.notifyDataSetChanged();

//                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.anu.dolist", Context.MODE_PRIVATE);
//                                HashSet<String> set = new HashSet(MainActivity.list);
//                                sharedPreferences.edit().putStringSet("notes",set).apply();


                                // delete by using Dao
                                TextView child = (TextView) listView.getChildAt(itemDelete);
                                EventRepository er = new EventRepository(getApplication());
                                Event targetEvent = er.getEventByTitle(child.getText().toString());
                                er.deleteOneEvent(targetEvent);

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });




        /**
         * BottomNavigationView
         * @author: Limin Deng(u6849956)
         */
        // callback when item on BottomNavigationView is selected
        BottomNavigationView bnv = findViewById(R.id.main_nav);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                System.out.println("good");

                switch (menuItem.getItemId()) {
                    case R.id.main_item_1:
                        System.out.println("Do nothing");
                        break;

//                    case R.id.main_item_2:
//
//                    //    startActivity(new Intent(MainActivity.this, CalendarActivity.class));
//                        finish();
//                        break;

                    case R.id.main_item_3:
                        Intent go3 = new Intent(MainActivity.this, MapsActivity.class);
                        go3.putExtra("placeNumber",1); //Supriya
                        startActivity(go3);
                        finish();
                        break;
                }

                // update selecte state
                return true;
            }
        });
        bnv.setSelectedItemId(R.id.main_item_1);


        /**
         * Database
         * @author: Limin Deng(u6849956)
         */


        // clear the list first
        list.clear();

        // show data list
        events = er.getAllEvents();

        // Avoid this error
        // Attempt to invoke interface method 'java.util.Iterator java.util.List.iterator()' on a null object reference
        // at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2778)
        if (events != null) {
            for (Event event: events) {
                System.out.println("new record");
                System.out.println(event.title);
                list.add(event.title);
            }
        } else {
            list.add("Hello from DAO");
        }



        // fill in listView
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);


    }



    public void filterEvents(View view) {
        System.out.println("Hello! Search");


    }

}
