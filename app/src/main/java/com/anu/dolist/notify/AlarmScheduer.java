package com.anu.dolist.notify;

import android.app.AlarmManager;
import android.app.PendingIntent;

import java.util.Calendar;
import java.util.Locale;

public class AlarmScheduer {


    /**
     * Schedules a single alarm
     */

    private void scheduleAlarm() {
        Calendar datetiemToAlarm = Calendar.getInstance(Locale.getDefault());
        datetiemToAlarm.setTimeInMillis(System.currentTimeMillis());

        Calendar today = Calendar.getInstance(Locale.getDefault());


    }


}
