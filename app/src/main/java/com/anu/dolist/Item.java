package com.anu.dolist;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * author:u6472601
 * complete: 1:uncomplete, 0:complete
 * categorize: 1:one time, 0:repeat
 * date: in MM-dd-yyyy HH:mm:ss type of the time
 * when: 0 means it is the work in the past, 1 means it is the work today, 2 means it is the work tomorrow, 3 means it is the work in the future
 **/
public class Item extends SQLiteOpenHelper {
    public static final String Database_path = "/data/data/com.kvikesh800gmail.relativlayoutjava/databases/";
    public static final String Database_name = "item.db";
    public static final int version = 1;
    public SQLiteDatabase sqlite;
    public Context context;
    private static final String Table_name = "item";//name of table
    public static final long one_day = 1000*60*60*24;
    public String content;//name of column
    public int complete;//name of column
    public int categorize;//name of column
    public String date;//name of column
    public String location;//name of column
    public int when;
    public int id;//name of column


    public Item(Context context,int id,String content,int complete,int categorize,String date/*,String locatoion*/){
        super(context,Database_name, null, version);
        this.context = context;
        this.content = content;
        this.categorize = categorize;
        this.complete = complete;
        this.date = date;
        this.id = id;
    //    this.location = locatoion;
        this.when = whenday(date);
    }

    public Date repeat_time(int categorize, String date_time){
        DateFormat fmt =new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(date_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(categorize ==0){
            Long time = date.getTime()+one_day;
            date.setTime(time);
            return date;
        }
        else{
            return date;
        }
    }

    public int whenday(String date_time){
        DateFormat fmt =new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(date_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long time = date.getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date beginOfDate = calendar1.getTime();
        Long now_past = beginOfDate.getTime();
        Long compare = time - now_past;
        if(compare<=0){
            return 0;
        }
        else if(compare>0&&compare<=one_day){
            return 1;
        }
        else if(compare>one_day&&compare<=2*one_day){
            return 2;
        }
        else{
            return 3;
        }
    }
/**
 * part of the code based on https://github.com/vikesh8860/QuizBook/blob/master/app/src/main/java/com/kvikesh800gmail/relativlayoutjava/books.java
 **/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void createDatabase() {
        createDB();
    }

    private void createDB() {

        boolean dbexist = DBexists();//calling the function to check db exists or not
        if (!dbexist)//if database doesnot exist
        {

            this.getReadableDatabase();//Create an empty file
            copyDBfromResource();//copy the database file information of assets folder to newly create file
        }
    }

    private void copyDBfromResource() {

        InputStream is;
        OutputStream os;
        String filePath = Database_path + Database_name;
        try {
            is = context.getAssets().open(Database_name);//reading purpose
            os = new FileOutputStream(filePath);//writing purpose
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);//writing to file
            }
            os.flush();//flush the outputstream
            is.close();//close the inputstream
            os.close();//close the outputstream

        } catch (IOException e) {
            throw new Error("Problem copying database file:");
        }
    }

    public void openDatabase() throws SQLException//called by onCreate method of Questions Activity
    {

        String myPath = Database_path + Database_name;
        sqlite = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private boolean DBexists()//Check whether the db file exists or not
    {
        SQLiteDatabase db = null;
        try {
            String databasePath = Database_path + Database_name;
            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);
            db.setLockingEnabled(true);
        } catch (SQLException e) {
            Log.e("Sqlite", "Database not found");
        }
        if (db != null)
            db.close();///close the opened file
        return db != null ? true : false;

    }
    public String ReadContent(int i)//Used to read the content from the item.db file where id is given and we choose id randomly
    {
        String cont = "";
        Cursor c = sqlite.rawQuery("SELECT content FROM " + Table_name + " WHERE " + id + " = " + i + "", null);//cursor to that query
        if (c.moveToFirst())
           cont = c.getString(0);
        else
            cont = "";
        return cont;
    }
    public int ReadComplete(int i)//Used to read whether the task is complete from the item.db file where id is given and we choose id randomly
    {
        int compl = -1;
        Cursor c = sqlite.rawQuery("SELECT complete FROM " + Table_name + " WHERE " + id + " = " + i + "", null);//cursor to that query
        if (c.moveToFirst())
            compl = c.getInt(0);
        else
            compl = -1;
        return compl;
    }

    public int ReadCategorize(int i)//Used to read the categorize from the item.db file where id is given and we choose id randomly
    {
        int categ = -1;
        Cursor c = sqlite.rawQuery("SELECT categorize FROM " + Table_name + " WHERE " + id + " = " + i + "", null);//cursor to that query
        if (c.moveToFirst())
            categ = c.getInt(0);
        else
            categ = -1;
        return categ;
    }
    public String ReadDate(int i)//Used to read the date from the item.db file where id is given and we choose id randomly
    {
        String da = "";
        Cursor c = sqlite.rawQuery("SELECT content FROM " + Table_name + " WHERE " + id + " = " + i + "", null);//cursor to that query
        if (c.moveToFirst())
            da = c.getString(0);
        else
            da = "";
        return da;
    }
    public String ReadLocation(int i)//Used to read the location from the item.db file where id is given and we choose id randomly
    {
        String lac = "";
        Cursor c = sqlite.rawQuery("SELECT location FROM " + Table_name + " WHERE " + id + " = " + i + "", null);//cursor to that query
        if (c.moveToFirst())
            lac = c.getString(0);
        else
            lac = "";
        return lac;
    }

}

