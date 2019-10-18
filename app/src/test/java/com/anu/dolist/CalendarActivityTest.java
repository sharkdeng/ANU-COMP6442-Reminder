package com.anu.dolist;

import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

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
}