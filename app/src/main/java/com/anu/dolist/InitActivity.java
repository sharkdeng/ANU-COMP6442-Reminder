package com.anu.dolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // the following 2 lines need to be executed before setContentView
        // hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // hide title bar
        getSupportActionBar().hide();


        setContentView(R.layout.activity_init);


        // create sub thread
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);

                    // start MainActivity
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it);

                    // close current activity
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        myThread.start();
    }
}
