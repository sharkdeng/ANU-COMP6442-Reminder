package com.anu.dolist.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface EventDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOneEvent(Event newEvent);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateOneEvent(Event event);


    @Delete
    void deleteOneEvent(Event event);


    @Query("SELECT * FROM event")
    Cursor getAllEventsCursor();


    @Query("SELECT * FROM event WHERE completed")
    Cursor getAllCompletedEventsCursor();


    @Query("SELECT * FROM event WHERE not completed")
    Cursor getAllIncompletedEventsCursor();


    @Query("SELECT * FROM event where _id=:id")
    Event getEventById(int id);

    @Query("SELECT * FROM event where title LIKE :keywords")
    Cursor getEventByKeywords(String keywords);


    @Query("DELETE FROM event")
    void deleteAllEvents();

    @Query("UPDATE event SET completed=:completed WHERE _id=:id")
    void updateCompleted(int id, int completed);




}




