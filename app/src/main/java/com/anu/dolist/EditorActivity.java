package com.anu.dolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class EditorActivity extends AppCompatActivity {

    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        /*
        @author: u6734521
        get the intent id from MainActivity and put it here
        extra caution of -1 is put, to avoid getting wrong id

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
    }
}
