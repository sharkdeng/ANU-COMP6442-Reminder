package com.anu.dolist.db;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface EventDao {
    @Query("SELECT * FROM event WHERE title=:title LIMIT 1")
    Event findEventByTitle(String title);

    boolean addOneEvent();
    boolean deleteOneEvent();
    boolean updateOneEvent();

    LiveData<List<Event>> getAllEvents();
}




