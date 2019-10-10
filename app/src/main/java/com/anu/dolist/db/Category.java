package com.anu.dolist.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Limin
 */
@Entity (tableName = "category",
        indices = {@Index("name")})
public class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cid")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @Ignore
    private int subcategroy;

    public Category (@NotNull String name) {
        this.name = name;
    }

}
