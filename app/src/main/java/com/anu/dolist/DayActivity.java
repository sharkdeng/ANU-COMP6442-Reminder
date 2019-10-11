package com.anu.dolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


/**
 * @author: Limin Deng(u6849956)
 */
public class DayActivity extends AppCompatActivity {

    String[] mobileArray = {"12 AM", "1 AM", "2AM", "3 AM", "4 AM", "35AM", "6 AM", "7 AM", "8 AM",
            "9 AM", "10 AM", "11 AM", "Noon",
    "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "8 PM", "9 PM", "10 PM", "11 PM", "12 AM"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);


        /**
         * @author: Limin Deng(u6849956)
         *
         */

        ImageButton btn_back = findViewById(R.id.day_tb_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        TextView month =
//
//        ImageButton btn_select = findViewById(R.id.day_tb_select);
//        btn_select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        ImageButton btn_search = findViewById(R.id.day_tb_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        ImageButton btn_add = findViewById(R.id.day_tb_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DayActivity.this, EditorActivity.class));
            }
        });





        // change textview content
        Intent get = getIntent();

        TextView tv_today = findViewById(R.id.day_today);
        System.out.println(get.getStringExtra("SelectedDate"));
        tv_today.setText(get.getStringExtra("SelectedDate"));



        // fill listview
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.day_time, mobileArray);
        ListView listView = findViewById(R.id.day_time_c);
        listView.setAdapter(adapter);

    }


    /**
     * fill action bar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note,menu);
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

        if(item.getItemId() == R.id.main_add_note){
            Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
