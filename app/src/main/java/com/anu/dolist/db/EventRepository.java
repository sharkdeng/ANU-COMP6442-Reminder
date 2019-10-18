package com.anu.dolist.db;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;



import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventRepository  {

    private EventDao eventDao;



    public EventRepository(Application app) {
        EventDatabase database = EventDatabase.getDatabase(app);
        eventDao = database.eventDao();
    }

    public long insertOneEvent(Event event) throws ExecutionException, InterruptedException {
        return new insertAsyncTask(eventDao).execute(event).get() ;
    }



    public void updateOneEvent(Event event) {
        new updateAsyncTask(eventDao).execute(event);
    }

    public void deleteOneEvent(Event event) {
        new deleteAsyncTask(eventDao).execute(event); // async thread
//        eventDao.deleteOneEvent(event); // main thread
    }


    public List<Event> getAllEvents() {
        return eventDao.getAllEvents();
    }

    public Cursor getAllEventsCursor() {
        return eventDao.getAllEventsCursor();
    }

    public List<Event> getAllCompletedEvents() {

        return eventDao.getAllCompletedEvents();
    }

    public Cursor getAllCompletedEventsCursor() {
        return eventDao.getAllCompletedEventsCursor();
    }

    public List<Event> getAllIncompletedEvents() {
        return eventDao.getAllIncompletedEvents();
    }

    public Cursor getAllIncompletedEventsCursor() {
        return eventDao.getAllIncompletedEventsCursor();
    }

    public Event getEventById(int id) {
        return eventDao.getEventById(id);
    }


    // for test reason
    public Event getEventByTitleTimeDate(String title, String time, String date) {
        return eventDao.getEventByTitleTimeDate(title, time, date);
    }

    public Cursor getEventByKeywords(String keywords) {
        return eventDao.getEventByKeywords(keywords);
    }

    public void deletAllEvents() {
        new deleteAllAsyncTask(eventDao).execute();
    }

    public void updateCompleted(int id, int completed) {
        eventDao.updateCompleted(id, completed);
    }






    // synchronically because dao doesn't allow executing in main thread
    // 3rd Void: return type
    private static class insertAsyncTask extends AsyncTask<Event, Void, Long> {

        // pass this to database manipulation
        private EventDao eventDao;

        insertAsyncTask(EventDao eventDao) {
            this.eventDao = eventDao;
        }

        @Override
        protected Long doInBackground(Event... events) {
            return this.eventDao.insertOneEvent(events[0]);
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
            this.eventDao.deleteAllEvents();

            return null;
        }
    }


}
