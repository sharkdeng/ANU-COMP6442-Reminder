package com.anu.dolist;

import com.anu.dolist.db.Event;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * @author Ran Zhang(u6760490)
 * Calendar insertion test
 */
public class CalendarActivityTest {
    int CaseSize = 10;


    @Test
    public void parseDateTest() {
        int[] result = new int[3];
        for(int i=0;i<CaseSize;i++) {
            String tmp = "";
            double d = Math.random();
           result[2] = (int) (d * 28);
           tmp+=result[2];
            d = Math.random();
            result[1] = (int) (d * 12);
            tmp+=("/"+result[1]);
            d = Math.random();
            result[0] = (int) (d * 100);
            tmp+=("/"+(result[0]));
            result[0] +=2000;
            assertArrayEquals(result,CalendarActivity.parseDate(tmp));
        }


    }

    @Test
    public void registTest(){
        String[] time = {"27/10","25/10","18/9"};
        Map<String, List<String>> map = new HashMap<>();
        for(int i=0;i<10;i++) {
            Event a = new Event("a"+i);
            if(i<4) a.date = time[0];
            else if(i<8) a.date = time[1];
            else a.date = time[2];
            CalendarActivity.registevent(map,a);
        }

        assertArrayEquals(map.get(time[0]).toArray(),new String[]{"a0 ","a1 ","a2 ","a3 "} );
        assertArrayEquals(map.get(time[1]).toArray(),new String[]{"a4 ","a5 ","a6 ","a7 "} );
        assertArrayEquals(map.get(time[2]).toArray(),new String[]{"a8 ","a9 "} );

    }
}