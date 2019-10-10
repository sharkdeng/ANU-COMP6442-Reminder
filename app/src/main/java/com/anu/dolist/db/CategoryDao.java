package com.anu.dolist.db;


import androidx.room.Dao;
import androidx.room.Query;


/**
 * @author: Limin
 */
@Dao
public interface CategoryDao {

    @Query("DELETE FROM category")
    void deleteAll();
}
