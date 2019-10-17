package com.anu.dolist;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventAttrib;
import com.anu.dolist.db.EventRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // listview
//    private ListView listView;
    private Cursor myCursor;
    // swipe menu
    private SwipeMenuListView listView;


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
        if(item.getItemId() == R.id.main_show_all) {

            myCursor = er.getAllEventsCursor();
            MyCursor ca = new MyCursor(getApplicationContext(), myCursor);
            listView.setAdapter(ca);

            // change title
            ab.setTitle("All Events");

            return true;

        } else if (item.getItemId() == R.id.main_show_incompleted) {

            myCursor = er.getAllIncompletedEventsCursor();
            MyCursor ca = new MyCursor(getApplicationContext(), myCursor);
            listView.setAdapter(ca);

            ab.setTitle("Incompleted Events");

            return true;

        } else if (item.getItemId() == R.id.main_show_completed) {

            myCursor = er.getAllCompletedEventsCursor();
            MyCursor ca = new MyCursor(getApplicationContext(), myCursor);
            listView.setAdapter(ca);

            ab.setTitle("Completed Events");

            return true;

        } else if (item.getItemId() == R.id.main_delete_all) {

            er.deletAllEvents();
            recreate();

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


        // change ListView to SwipeListView to support swipe menus
//        listView = findViewById(R.id.main_lv);
        listView = findViewById(R.id.main_swipe_list);


        ab = getSupportActionBar();
        ab.setTitle("All Events");

        // initiate EventRepository
        er = new EventRepository(getApplication());


        searchBtn = findViewById(R.id.main_search_btn);


        FloatingActionButton fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                finish();
                Snackbar.make(view, "Add a new Event", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();

            }
        });


        /**
         * Swipe Menu to for following functions
         * 1) mark the event as completed or incompleted
         */
        // create menus
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem markCompleted = new SwipeMenuItem(getApplicationContext());
                markCompleted.setBackground(new ColorDrawable(Color.YELLOW));
                markCompleted.setIcon(R.drawable.greentickmark);
            //    markCompleted.getIcon().setTint(-16711936);
                markCompleted.setWidth(200);
                menu.addMenuItem(markCompleted);
                markCompleted.setTitle("Completed");


                SwipeMenuItem markIncompleted = new SwipeMenuItem(getApplicationContext());
                markIncompleted.setBackground(new ColorDrawable(Color.BLUE));
                markIncompleted.setIcon(android.R.drawable.ic_delete);
                markIncompleted.setWidth(200);
                menu.addMenuItem(markIncompleted);
                markCompleted.setTitle("Incompleted");
            }
        };

        // bind menus
        listView.setMenuCreator(creator);

        // menu callback
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                Cursor c = (Cursor)listView.getAdapter().getItem(position);
                int eventId = c.getInt(c.getColumnIndexOrThrow(EventAttrib.ID.toString()));

                switch (index) {
                    case 0:
                        // completed
                        er.updateCompleted(eventId, 1);

                        recreate();

                        break;
                    case 1:
                        // incompleted
                        er.updateCompleted(eventId, 0);

                        recreate();

                        break;
                }

                return false;
            }
        });




        /**
         * if the event is completed, cannot modify it
         * if the event is incomplete, mark as complete or modify it
         * @author: u6734521
         * to jump to editor activity when the list item is pressed.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor c = (Cursor)adapterView.getAdapter().getItem(position);
                int e_completed = c.getInt(c.getColumnIndexOrThrow(EventAttrib.COMPLETED.toString()));

                // not completed, can modify it
                if (e_completed == 0){

                    Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                    int e_id = c.getInt(c.getColumnIndexOrThrow(EventAttrib.ID.toString()));
                    intent.putExtra(EventAttrib.ID.toString(), e_id);

                    startActivity(intent);
                    finish();


                } else {
                    // completed
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("You cannot modify a completed event")
                            .setNegativeButton("Sure",null)
                            .show();
                }

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
                final AdapterView av = adapterView;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setTitle("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                EventRepository er = new EventRepository(getApplication());
                                Cursor c = (Cursor)av.getAdapter().getItem(position);
                                Event selectedEvent = er.getEventById(c.getInt(c.getColumnIndexOrThrow(EventAttrib.ID.toString())));
                                er.deleteOneEvent(selectedEvent);

                                // refresh the activity without animation
                                recreate();

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
         * check completed and update database
         * load data
         * @author: Limin Deng(u6849956)
         */
        myCursor = er.getAllEventsCursor();
        MyCursor ca = new MyCursor(getApplicationContext(), myCursor);
        listView.setAdapter(ca);


    }


    public void filterEvents(View view) {
        System.out.println("Hello! Search");
    }

}
