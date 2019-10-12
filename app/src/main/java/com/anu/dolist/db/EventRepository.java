package com.anu.dolist.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;


/**
 * Wrapper for EventDao.insert, update, and delete
 * Example:
 * 1) eventdatabase.allowMainThread + eventDao.insert // main thread
 * 2) eventrepository.insert  // background thread
 * @author: Limin
 */
public class EventRepository {
    private EventDao eventDao;
    private List<Event> allEvents;
    private List<Event> completedEvents;
    private List<Event> incompletedEvents;


    public EventRepository(Application app) {
        EventDatabase  database = EventDatabase.getDatabase(app);
        eventDao = database.eventDao();
        allEvents = eventDao.getAllEvents();
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



    public Event getEventByTitle(String title) {
        return eventDao.getEventByTitle(title);
    }

    public int getId(String title) {
        return eventDao.getId(title);
    }

    public List<Event> getCompletedEvents() {
        // it works for List<Event> and allowMainThread
        // but not works for LiveData
//        return eventDao.getCompletedEvents();

        return eventDao.getCompletedEvents();

//        // FIXME: get is not recommend
//        try {
//            return new getCompletedEventsAsyncTask(eventDao).execute().get();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }


    }

    public List<Event> getIncompletedEvents() {

        return eventDao.getIncompletedEvents();

//        // FIXME: get is not recommend
//        try {
//            return new getIncompletedEventsAsyncTask(eventDao).execute().get();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    public List<Event> getAllEvents() {

        return eventDao.getAllEvents();

//        // FIXME: get is not recommend
//        try {
//            return new getAllEventsAsyncTask(eventDao).execute().get();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }

    }

    public void deleteAll() {
        new deleteAllAsyncTask(eventDao).execute();
    }



    // synchronically because dao doesn't allow executing in main thread
    // 3rd Void: return type
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


    private static class deleteAllAsyncTask extends AsyncTask<Event, Void, Void> {

        // pass this to database manipulation
        private EventDao eventDao;

        deleteAllAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected Void doInBackground(Event... events) {
            this.eventDao.deleteAll();

            return null;
        }
    }

    private static class getAllEventsAsyncTask extends AsyncTask<Event, Void,  List<Event>> {

        // pass this to database manipulation
        private EventDao eventDao;

        getAllEventsAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            return this.eventDao.getAllEvents();
        }
    }

    private static class getIncompletedEventsAsyncTask extends AsyncTask<Event, Void,  List<Event>> {

        // pass this to database manipulation
        private EventDao eventDao;

        getIncompletedEventsAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            return this.eventDao.getIncompletedEvents();
        }
    }

    private static class getCompletedEventsAsyncTask extends AsyncTask<Event, Void, List<Event>> {

        // pass this to database manipulation
        private EventDao eventDao;

        getCompletedEventsAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected List<Event> doInBackground(Event... events) {
            return this.eventDao.getCompletedEvents();
        }

        // get result
        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);

        }
    }
}
