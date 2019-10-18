package com.anu.dolist;

import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventRepository;

import org.junit.Test;

import java.util.List;

public class TestDatabase {

    EventRepository er;
    @Test
    public void testDeleteOneEvent() {
        List<Event> events = er.getAllEvents();
        int originalSize = events.size();
        er.deleteOneEvent(events.get(0));

    }
}
