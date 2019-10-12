package com.anu.dolist.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * @author: Limin
 */
@Entity(tableName = "event",
        indices = {@Index("title"), @Index("location"), @Index("starts"), @Index("ends"), @Index("alert"), @Index("url"), @Index("notes")}
)
public class Event {


    // After change this, I can modify the title

//       @ColumnInfo(name = "eid")
//    public int id;
    @PrimaryKey()
    @ColumnInfo(name = "title")
    @NotNull public String title;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "date")
    public String date;

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

    // 0 for incompleted, 1 for completed
    @ColumnInfo(name = "category")
    public boolean category;

    @Ignore
    public Event(@NotNull String title, String location, String date, String starts, String ends, String alert, String url, String notes, boolean category) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.starts = starts;
        this.ends = ends;
        this.alert = alert;
        this.url = url;
        this.notes = notes;
        this.category = category;
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
        this.starts = "";
        this.ends = "";
        this.alert = "";
        this.url = "";
        this.notes = "";
        this.category = false;
    }
}


