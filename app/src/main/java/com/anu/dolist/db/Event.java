package com.anu.dolist.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "event",
indices = {@Index("title"), @Index("location"), @Index("starts"), @Index("ends"), @Index("alert"), @Index("url"), @Index("notes")})
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "eid")
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "starts")
    public String starts;

    @ColumnInfo(name = "ends")
    public String ends;

    @ColumnInfo(name = "alert")
    public String alert;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "notes")
    public String notes;

    public Event(@NotNull String title, String location, String starts, String ends, String alert, String url, String notes) {
        this.title = title;
        this.location = location;
        this.starts = starts;
        this.ends = ends;
        this.alert = alert;
        this.url = url;
        this.notes = notes;
    }
}


