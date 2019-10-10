package com.anu.dolist.db;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


/**
 * version is for database update
 * java.lang.IllegalStateException: Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.
 * get database
 * @author: Limin
 */
@Database(entities = {Event.class, Category.class},
        version = 1 //
       )
public abstract class EventDatabase extends RoomDatabase {

    // singleton
    private static EventDatabase INSTANCE;

    // pass value to nested class
    public abstract EventDao eventDao();
    public abstract CategoryDao categoryDao();

    private static final String DB_NAME = "events.db";


    // synchorinzed means only one thread can access this method when there are multiple threads
    public static synchronized EventDatabase getDatabase(final Context context) {

        // when we don't have database
        if (INSTANCE == null) {
            Log.d("Database", "creating...");
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    EventDatabase.class,
                    DB_NAME)
                    .fallbackToDestructiveMigration() // if we don't have "exportSchema = false", 迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                    .allowMainThreadQueries() // SHOULD NOT BE USED IN PRODUCTION !!! TODO how to solve this?
                    .addCallback(roomCallback)
                    .build();

        }

        return INSTANCE;
    }

    public void clearDb() {
        if (INSTANCE != null) {
            new PopulateDatabaseAsyncTask(INSTANCE).execute();
        }
    }


    /**
     * 1) For the first time, app get just installed
     * the callback will be executed
     * and data cannot be shown
     *
     * 2) But secondly, the database already exits,
     * the callback will not be executed
     */
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            System.out.println("EventDatabase is created");
            Log.d("Database", "EventDatabase is created");
            new PopulateDatabaseAsyncTask(INSTANCE).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            System.out.println("EventDatabase is opened");

        }
    };


    // populate the database
    private static class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        private final EventDao eventDao;
        private final CategoryDao categoryDao;

        // pass value
        public PopulateDatabaseAsyncTask(EventDatabase instance){
            eventDao = instance.eventDao();
            categoryDao = instance.categoryDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            // clear db
            eventDao.deleteAll();
            categoryDao.deleteAll();

            Log.d("Database", "Sureeee");

            // fill in data
            Event one = new Event("6442 Lab");
            Event two = new Event("Python Lab");
            Event three = new Event("Database Lab");

            eventDao.insertOneEvent(one);
            eventDao.insertOneEvent(two);
            eventDao.insertOneEvent(three);


            Category cat1 = new Category("Incomplete");
            Category cat2 = new Category("Completed");
            categoryDao.insertOneCategory(cat1);
            categoryDao.insertOneCategory(cat2);



            return null;

        }
    }

}
