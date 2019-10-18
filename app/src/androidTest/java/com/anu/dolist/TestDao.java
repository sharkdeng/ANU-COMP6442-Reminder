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

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.equalTo;

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


    @Test
    public void insertTestDb() {

        Event one = new Event("COMP6442 Lab");
        one.date = "18/11/19";
        one.time ="12:00";
        one.alert ="None";
        one.completed =false;
        one.location = "108 North Rd, Acton ACT 2601/-35.275278/ 149.120607";
        one.notes = "COMP6442 lab is interesting";
        one.url ="http://anu.edu.au";
        er.insertOneEvent(one);
        List<Event> events = er.getAllEvents();
        System.out.println("total insertion: "+events);
        assertThat(events.get(0).title, equalTo(one.title));
        assertThat(events.get(0).date, equalTo(one.date));
        assertThat(events.get(0).alert, equalTo(one.alert));
        assertThat(events.get(0).completed, equalTo(one.completed));
        assertThat(events.get(0).time, equalTo(one.time));
        assertThat(events.get(0).url, equalTo(one.url));
        assertThat(events.get(0).notes, equalTo(one.notes));
        assertThat(events.get(0).location, equalTo(one.location));

    }

    @Test
    public void updateTestDb() {

        Event one = new Event("COMP6442 Lab");
        one.date = "18/11/19";
        one.time ="12:00";
        er.updateOneEvent(one);
        List<Event> events = er.getAllEvents();
        assertThat(events.get(0).title, equalTo(one.title));
        assertThat(events.get(0).date, equalTo(one.date));
    }

    @Test
    public void deleteTestDb() {

        List<Event> events = er.getAllEvents();
        int count = events.size();
        er.deleteOneEvent(events.get(0));
        int afterDeleteCount = events.size();
        assertThat(count, equalTo(afterDeleteCount));



    }
}
