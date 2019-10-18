package com.anu.dolist;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import androidx.room.Database;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventDao;
import com.anu.dolist.db.EventDatabase;
import com.anu.dolist.db.EventRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.equalTo;


/**
 * @author Supriya Kamble(U6734521)
 * Testcases for database add, update and delete.
 */

@RunWith(AndroidJUnit4.class)
public class TestDao {
    private EventRepository er;


    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void createDb() {
        er= new EventRepository(activityRule.getActivity().getApplication());
    }

    /**
     * Insert into database test case
     */

    private int targetId = 0;

    @Test
    public void insertTestDb() throws ExecutionException, InterruptedException {

        Event one = new Event("COMP6442 Lab");
        one.date = "18/11/19";
        one.time ="12:00";
        one.alert ="None";
        one.completed =false;
        one.location = "108 North Rd, Acton ACT 2601/-35.275278/ 149.120607";
        one.notes = "COMP6442 lab is interesting";
        one.url ="http://anu.edu.au";
        int targetId = (int)er.insertOneEvent(one);


        Event e = er.getEventById(targetId);

        assertThat(e.title, equalTo(one.title));
        assertThat(e.date, equalTo(one.date));
        assertThat(e.alert, equalTo(one.alert));
        assertThat(e.completed, equalTo(one.completed));
        assertThat(e.time, equalTo(one.time));
        assertThat(e.url, equalTo(one.url));
        assertThat(e.notes, equalTo(one.notes));
        assertThat(e.location, equalTo(one.location));


    }
    /**
     * Update database test case
     */

    @Test
    public void updateTestDb() throws ExecutionException, InterruptedException {

        Event one = new Event("COMP6442 Lab");
        one.date = "18/11/19";
        one.time ="12:00";
        er.updateOneEvent(one);

        List<Event> events = er.getAllEvents();
        assertThat(events.get(0).title, equalTo(one.title));
        assertThat(events.get(0).date, equalTo(one.date));
    }
    /**
     * Delete database test case
     */
    @Test
    public void deleteTestDb() {

        List<Event> events = er.getAllEvents();
        int count = events.size();
        er.deleteOneEvent(events.get(0));
        int afterDeleteCount = events.size();
        assertThat(count, equalTo(afterDeleteCount));



    }
}
