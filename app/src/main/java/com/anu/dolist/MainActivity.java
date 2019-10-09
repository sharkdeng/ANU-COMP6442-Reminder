package com.anu.dolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.anu.dolist.db.Event;


import com.anu.dolist.db.EventDao;
import com.anu.dolist.db.EventDatabase;
import com.anu.dolist.db.EventRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> list = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    public static String PACKAGE_NAME;

    /**
     * attributes for database
     * @author: Limin Deng u6849956
     */
    private List<Event> events;



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

        if(item.getItemId() == R.id.add_note){
            Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
            startActivity(intent);
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

        ListView listView = findViewById(R.id.main_lv);

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                intent.putExtra("noteId",i);
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
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemDelete =i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setTitle("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.remove(itemDelete);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.anu.dolist", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet(MainActivity.list);
                                sharedPreferences.edit().putStringSet("notes",set).apply();
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

                    case R.id.main_item_2:

                        startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                        finish();
                        break;

                    case R.id.main_item_3:
                        Intent go3 = new Intent(MainActivity.this, MapActivity.class);
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
        // get the database to do insert, update, and delete
        EventRepository er = new EventRepository(getApplication());

        // access instance method, now the onCreate and onOpen methods in Callback can be called
        Event e2 = new Event("COMP2220");
        Event e3 = new Event("COMP3330");

        er.insertOneEvent(e2);
        er.insertOneEvent(e3);


        // access data
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
}
