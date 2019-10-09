package com.anu.dolist.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;


/**
 * wrapper for EventDao.insert, update, and delete
 * Example:
 * 1) eventdatabase.allowMainThread + eventDao.insert // main thread
 * 2) eventrepository.insert  // background thread
 */
public class EventRepository {
    private EventDao eventDao;
    private List<Event> events;

    public EventRepository(Application app) {
        EventDatabase  database = EventDatabase.getDatabase(app);
        eventDao = database.eventDao();
        events = eventDao.getAllEvents();
    }

    public void insertOneEvent(Event event) {
        new insertAsyncTask(eventDao).execute(event);
    }

    public void updateOneEvent(Event event) {
        new updateAsyncTask(eventDao).execute(event);
    }

    public void deleteOneEvent(Event event) {
        new deleteAsyncTask(eventDao).execute(event); // async thread
//        eventDao.deleteOneEvent(event); // main thread
    }

    public List<Event> getAllEvents() {
        return events;
    }


    // synchronically because dao doesn't allow executing in main thread
    private static class insertAsyncTask extends android.os.AsyncTask<Event, Void, Void> {

        // pass this to database manipulation
        private EventDao eventDao;

        insertAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            this.eventDao.insertOneEvent(events[0]);

            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Event, Void, Void> {

        // pass this to database manipulation
        private EventDao eventDao;

        updateAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            this.eventDao.updateOneEvent(events[0]);

            return null;
        }
    }



    private static class deleteAsyncTask extends AsyncTask<Event, Void, Void> {

        // pass this to database manipulation
        private EventDao eventDao;

        deleteAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            this.eventDao.deleteOneEvent(events[0]);

            return null;
        }
    }

}
