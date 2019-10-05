package com.anu.dolist;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * author:u6472601
 * complete: 1:uncomplete, 0:complete
 * categorize: 1:one time, 0:repeat
 * date: in MM-dd-yyyy HH:mm:ss type of the time
 * when: 0 means it is the work in the past, 1 means it is the work today, 2 means it is the work tomorrow, 3 means it is the work in the future
 **/
public class Item {
    public static final long one_day = 1000*60*60*24;
    public String content;
    public int complete;
    public int categorize;
    public String date;
    public String location;
    public int when;
    public int id;

    public Item(int id,String content,int complete,int categorize,String date/*,String locatoion*/){
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
}

