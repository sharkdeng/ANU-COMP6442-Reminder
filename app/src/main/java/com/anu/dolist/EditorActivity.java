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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventDao;
import com.anu.dolist.db.EventDatabase;
import com.anu.dolist.db.EventRepository;

import java.util.HashSet;

public class EditorActivity extends AppCompatActivity {

    private EventDao eventDao;

    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        /**
         * @author: Limin Deng(u6849956)
         */
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


        eventDao = EventDatabase.getDatabase(getApplicationContext()).eventDao();

        /**
         * Callbacks for cancel and add actions
         * @author: Limin Deng(u6849956)
         */
        TextView cancel = findViewById(R.id.edit_tb_left);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView add = findViewById(R.id.edit_tb_right);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // insert one record

                TextView title = findViewById(R.id.edit_event_title);
                TextView location = findViewById(R.id.edit_event_location);


                // empty not allowed
                if (title.getText().toString().equals("")) {
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
                } else {
                    // insert one record
                    Event newEvent = new Event(title.getText().toString());
                    EventRepository er = new EventRepository(getApplication());
                    er.insertOneEvent(newEvent);
                }


            }
        });


        /**
         * @author: Supriya Kamble(u6734521)
         * get the intent id from MainActivity and put it here
         * extra caution of -1 is put, to avoid getting wrong id
         */
        EditText editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId",-1);

        if(noteId != -1){
            editText.setText(MainActivity.list.get(noteId));
        }else{
            MainActivity.list.add("");
            noteId = MainActivity.list.size()-1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        /*
        @author: u6734521
        When the text is changed save the changes.
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
