package com.anu.dolist;

import java.util.Date;
/**
 * author:u6472601
 * complete: 1:uncomplete, 0:complete
 * categorize: 1:one time, 0:repeat
 * date_time: HH:mm:ss of the time
 * date_day: MMM dd yyyy of the time
 **/
public class Item {
    public String content;
    public int complete;
    public int categorize;
    public String date_day;
    public String date_time;
    public String location;

    public Item(String content,int complete,int categorize,String date_day,String locatoion,String date_time){
        this.content = content;
        this.categorize = categorize;
        this.complete = complete;
        this.date_day = date_day;
        this.date_time = date_time;
        this.location = locatoion;
    }

}
