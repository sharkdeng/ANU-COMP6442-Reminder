package com.anu.dolist.db;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;




@Database(entities = {Event.class},
        version = 1,
        exportSchema = false
)
public abstract class EventDatabase extends RoomDatabase {
    // singleton
    private static EventDatabase INSTANCE;

    // pass value to nested class
    public abstract EventDao eventDao();

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

        // pass value
        public PopulateDatabaseAsyncTask(EventDatabase instance){
            eventDao = instance.eventDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            // clear db
            eventDao.deleteAllEvents();


            // fill in data
//            Event one = new Event("COMP6442 Lab");
//            one.location = "108 North Rd, Acton ACT 2601/-35.275278/ 149.120607";
//
//            Event two = new Event("COMP6730 Lab");
//            two.location = "108 North Rd, Acton ACT 2601/-35.275278/ 149.120607";
//
//            Event three = new Event("COMP6240 Lab");
//            three.location ="108 North Rd, Acton ACT 2601/-35.275278/ 149.120607";
//
//
//
//            eventDao.insertOneEvent(one);
//            eventDao.insertOneEvent(two);
//            eventDao.insertOneEvent(three);





            return null;

        }
    }


}



