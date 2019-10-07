package com.anu.dolist.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity (tableName = "category",
        indices = {@Index("name")})
public class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cid")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

}
