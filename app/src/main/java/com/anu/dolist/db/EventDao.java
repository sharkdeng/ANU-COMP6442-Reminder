package com.anu.dolist.db;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface EventDao {
    @Query("SELECT * FROM event WHERE title=:title LIMIT 1")
    Event findEventByTitle(String title);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOneEvent(Event newEvent);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvents(Event... events);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateOneEvent(Event event);

    @Delete
    void deleteOneEvent(Event event);


    @Query("DELETE FROM event")
    void deleteAll();

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

//    @Query("SELECT * FROM event WHERE category=incompleted")
//    LiveData<List<Event>> getAllIncompletedEvents();





//    @Insert
//    public void insertBothUsers(User user1, User user2);
//
//    @Insert
//    public void insertUsersAndFriends(User user, List<User> friends);s
//    boolean addOneEvent();
//    boolean deleteOneEvent();


}




