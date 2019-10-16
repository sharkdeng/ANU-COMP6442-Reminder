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
@Entity(tableName = "event",
        indices = {@Index("title"), @Index("location"), @Index("date"), @Index("time"), @Index("alert"), @Index("url"), @Index("notes"),
                @Index("completed")}
)
public class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int _id;

    @ColumnInfo(name = "title")
    @NotNull public String title;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "alert")
    public String alert;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "notes")
    public String notes;

    // 0 for incompleted, 1 for completed
    @ColumnInfo(name = "completed")
    public boolean completed;

    @Ignore
    public Event(@NotNull String title, String location, String date, String time, String alert, String url, String notes, boolean completed) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.alert = alert;
        this.url = url;
        this.notes = notes;
        this.completed = completed;
    }

    /**
     * Error: Room cannot pick a constructor since multiple constructors are suitable.
     * use @Ignore to silence multiple constructor
     * @param title
     */

    public Event(@NotNull String title) {
        this.title = title;
        this.location = "";
        this.date = "";
        this.time = "";
        this.alert = "";
        this.url = "";
        this.notes = "";
        this.completed = false;
    }
}


