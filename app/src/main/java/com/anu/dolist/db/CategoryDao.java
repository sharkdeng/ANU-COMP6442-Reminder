package com.anu.dolist.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


/**
 * @author: Limin
 */
@Dao
public interface CategoryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOneCategory(Category newCategory);


    @Query("DELETE FROM category")
    void deleteAll();
}
