package com.anu.dolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventRepository;
import java.util.HashSet;


/**
 * @author: Limin Deng(u6849956)
 */
public class EditorActivity extends AppCompatActivity {


    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        // get data
        Intent go = getIntent();
        String eventTitle = go.getStringExtra("title");
        String eventLocation = go.getStringExtra("location");
        String eventStart = go.getStringExtra("start");
        String eventEnd = go.getStringExtra("end");
        String eventAlert = go.getStringExtra("alert");
        String eventUrl = go.getStringExtra("url");
        String eventNotes = go.getStringExtra("notes");

        // get all UIs
        TextView cancel = findViewById(R.id.edit_tb_left);
        final TextView add = findViewById(R.id.edit_tb_right);

        // UI
        final EditText editTitle = findViewById(R.id.edit_event_title);
        final EditText editLocation = findViewById(R.id.edit_event_location);
        final EditText editUrl = findViewById(R.id.edit_event_url);
        final EditText editNote = findViewById(R.id.edit_event_notes);
        final Button editStart = findViewById(R.id.edit_event_start);
        final Button editEnd = findViewById(R.id.edit_event_end);
        final Button editAlert = findViewById(R.id.edit_event_alert);

        // change right button on the toolbar
        if (eventTitle != null) {
            add.setText("Update");
        }else {
            add.setText("Add");
        }

        // fill in data
        if (eventTitle != null) {
            editTitle.setText(eventTitle);
        }
        if (eventLocation != null) {
            editLocation.setText(eventLocation);
        }
        if (eventStart != null) {
            editStart.setText(eventStart);
        }
        if (eventEnd != null) {
            editEnd.setText(eventEnd);
        }
        if (eventAlert != null) {
            editAlert.setText(eventAlert);
        }
        if (eventUrl != null) {
            editUrl.setText(eventUrl);
        }
        if (eventNotes != null) {
            editNote.setText(eventNotes);
        }





        // toolbar
        Toolbar tb = findViewById(R.id.edit_toolbar);
        setSupportActionBar(tb);
        // use customized Cancel instead
        // click home button
//        tb.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // because previous view is not finished
//                finish();
//            }
//        });
        // make sure toolbar is not null
        if (getSupportActionBar() != null){
            // show back arrow
            // we use customized Cancel text instead
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // add menu
        // use customized Add text instead
//        tb.inflateMenu(R.menu.add_note);



        /**
         * Callbacks for cancel and add actions
         * @author: Limin Deng(u6849956)
         */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // insert one record


                // empty not allowed
                if (editTitle.getText().toString().equals("")) {
                    // show alert
                    new AlertDialog.Builder(EditorActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Title cannot be empty")
                            .setTitle("Title cannot be empty")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.out.println("do nothing");
                                }
                            })
                            .setNegativeButton("No",null)
                            .show();

                    // add
                } else {

                    EventRepository er = new EventRepository(getApplication());


                    Event newEvent = new Event(editTitle.getText().toString());
                    newEvent.location = editLocation.getText().toString();
                    newEvent.starts = editStart.getText().toString();
                    newEvent.ends = editEnd.getText().toString();
                    newEvent.alert = editAlert.getText().toString();
                    newEvent.url = editUrl.getText().toString();
                    newEvent.notes = editNote.getText().toString();




                    // insert one record
                    if (add.getText().toString().equals("Add")) {

                        er.insertOneEvent(newEvent);


                        // show info
                        Context context = getApplicationContext();
                        CharSequence text = "Add completely";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text,  duration);
                        toast.show();


                        // after toast, finish the activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EditorActivity.this.finish();
                            }
                        }, 2000);



                        // update
                    } else {
                        er.updateOneEvent(newEvent);


                        // show info
                        Context context = getApplicationContext();
                        CharSequence text = "Update completely";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text,  duration);
                        toast.show();


                        // after toast, finish the activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EditorActivity.this.finish();
                            }
                        }, 2000);


                    }


                }

            }
        });



        editTitle.setText(eventTitle);

        /**
         * @author: Supriya Kamble(u6734521)
         * get the intent id from MainActivity and put it here
         * extra caution of -1 is put, to avoid getting wrong id
         */
        EditText editText = findViewById(R.id.edit_event_notes);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId",-1);

        if(noteId != -1){
            editText.setText(MainActivity.list.get(noteId));
        }else{
            MainActivity.list.add("");
            noteId = MainActivity.list.size()-1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }


        /**
         * update text
         */
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.list.set(noteId,String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.anu.dolist", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet(MainActivity.list);
                sharedPreferences.edit().putStringSet("notes",set).apply();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ActionBar ab = getSupportActionBar();
        ab.setTitle("Edit Event");


    }
}
