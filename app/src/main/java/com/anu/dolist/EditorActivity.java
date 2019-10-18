package com.anu.dolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventAttrib;
import com.anu.dolist.db.EventRepository;
import com.anu.dolist.notify.MyNotificationPublisher;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static com.anu.dolist.MainActivity.arrayAdapter;


/**
 * @author Limin Deng(u6849956)
 */
public class EditorActivity extends AppCompatActivity {


    private int eventId = -1;
    private Intent go; // received information


    Context context;
    boolean eventOnCalendar =false;
    boolean falseDatePicker = false;
    private Calendar mCalendar = Calendar.getInstance(); // start from today
    private String newLocation = ""; // container to store the selected place


    /**
     * Perform initialization of all fragments.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);



        context = EditorActivity.this;
        // UI
        final EditText editTitle = findViewById(R.id.edit_event_title);
        // it has been changed to places autocomplete
        //final EditText editLocation = findViewById(R.id.edit_event_location);
        final Button editDate = findViewById(R.id.edit_event_date);
        final Button editTime = findViewById(R.id.edit_event_time);
        final Button editAlert = findViewById(R.id.edit_event_alert);
        final EditText editUrl = findViewById(R.id.edit_event_url);
        final EditText editNote = findViewById(R.id.edit_event_notes);
        TextView cancel = findViewById(R.id.edit_tb_left);
        final TextView add = findViewById(R.id.edit_tb_right);
        final Button calEvent = findViewById(R.id.Calendar_event);


        /**
         * select event location
         * google places autocomplete
         */
        // get map API key
        String apiKey = getString(R.string.google_maps_key);

        // Initialize Places. For simplicity, the API key is hard-coded. In a production
        // environment we recommend using a secure mechanism to manage API keys.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // get the fragment
        final AutocompleteSupportFragment editLocation = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.edit_event_location);

        // add attributes to place object
        editLocation.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        editLocation.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                // fill the eventLocation
                Log.i(Constants.TAG.toString(), "Place: " + place.getName() + ", " + place.getId());
                double myLatitude = place.getLatLng().latitude;
                double myLongititude = place.getLatLng().longitude;
                // name, latitude, longititude
                newLocation = place.getName() + "/" + myLatitude + "/" + myLongititude;

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(Constants.TAG.toString(), "An error occurred: " + status);
            }
        });





        // get data
        go = getIntent();
        eventId = go.getIntExtra(EventAttrib.ID.toString(), -1);


        // old event
        if (eventId != -1) {

            // get event
            EventRepository er = new EventRepository(getApplication());
            Event selectedEvent = er.getEventById(eventId);
            String eventTitle = selectedEvent.title;
            String eventLocation = selectedEvent.location;
            String eventDate = selectedEvent.date;
            String eventTime = selectedEvent.time;
            String eventAlert = selectedEvent.alert;
            String eventUrl = selectedEvent.url;
            String eventNotes = selectedEvent.notes;


            // fill in data
            if (eventTitle != null) {
                editTitle.setText(eventTitle);
            }
            if (eventLocation != null) {

                if (!eventLocation.equals("")) {
                    String placeName = eventLocation.split("/")[0];
                    editLocation.setHint(placeName);
                } else {
                    // if there is not location
                    editLocation.setHint("Search a place");
                }

//                editLocation.setText(eventLocation); // this doesn't work
            }
            if (eventDate != null) {
                editDate.setText(eventDate);
            }
            if (editTime != null) {
                editTime.setText(eventTime);
            }
            if (eventAlert != null) {
                editAlert.setText(eventAlert);
            }
            if (eventUrl != null) {
                editUrl.setText(eventUrl);
            }
            if (eventNotes != null) {
                editNote.setText(eventNotes);
            }



        }



        /**
         * select time
         */
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
              //  Calendar mCalendar = Calendar.getInstance();
                // hour and minute are current time
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditorActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mCalendar.set(Calendar.MINUTE, selectedMinute);
                        mCalendar.set(Calendar.SECOND, 0);
                        editTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        /**
         * select date
         */
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Calendar mCalendar = Calendar.getInstance();
                new DatePickerDialog(
                        EditorActivity. this, date ,
                        mCalendar.get(Calendar. YEAR ) ,
                        mCalendar.get(Calendar. MONTH ) ,
                        mCalendar.get(Calendar. DAY_OF_MONTH )
                ).show() ;

                    }

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet (DatePicker view , int year , int monthOfYear , int dayOfMonth) {
                    mCalendar.set(Calendar. YEAR , year) ;
                    mCalendar.set(Calendar. MONTH , monthOfYear) ;
                    mCalendar.set(Calendar. DAY_OF_MONTH , dayOfMonth) ;
                    updateLabel() ;
                }
            } ;

            private void updateLabel () {
                String myFormat = "dd/MM/yy" ;
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale. getDefault ()) ;
                Date date = mCalendar.getTime();
                editDate .setText(sdf.format(date)) ;

            }


        });


        /**
         * toolbar
         */
        Toolbar tb = findViewById(R.id.edit_toolbar);
        setSupportActionBar(tb);



        /**
         * Callbacks for cancel and add actions
         * @author: Limin Deng(u6849956)
         */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditorActivity.this, MainActivity.class));
                finish();
            }
        });


        /**
         * alert
         */
        editAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditorActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Do you want to set alarm")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editAlert.setText("Alarm set");
                                final EventRepository er = new EventRepository(getApplication());

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editAlert.setText("None");
                            }
                        })
                        .show();

            }
        });

        calEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = checkPermission();
                if (result) {
                      eventOnCalendar = true;
                }
            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // empty not allowed
                if (editTitle.getText().toString().equals("") ||
                        editDate.getText().toString().equals("dd/MM/yy")||
                        editTime.getText().toString().equals("00:00")) {
                    // show alert
                    new AlertDialog.Builder(EditorActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Title cannot be empty")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.out.println("do nothing");
                                }
                            })
                            //.setNegativeButton("No",null)
                            .show();

                } else {


                    final EventRepository er = new EventRepository(getApplication());

                    // insert one record
                    if (eventId == -1) {
                        Event newEvent = new Event(editTitle.getText().toString());
                        // TODO
//                        newEvent.location = editLocation.getText().toString();
                        newEvent.location = newLocation;
                        newEvent.date = editDate.getText().toString();
                        newEvent.time = editTime.getText().toString();
                        newEvent.alert = editAlert.getText().toString();
                        newEvent.url = editUrl.getText().toString();
                        newEvent.notes = editNote.getText().toString();
                        newEvent.completed = false;

                        String dateTime =newEvent.date+" "+newEvent.time;

                        long current = Calendar.getInstance().getTimeInMillis(); // current time
                        long scheduled = getTimeinMilli(dateTime); // schedule time


                        falseDatePicker = checkPastDateTime(current,scheduled);
                        if(!(falseDatePicker)) {
                            try {
                                er.insertOneEvent(newEvent);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // schedule notification
                            if (!editAlert.getText().toString().equals("None")) {
                                scheduleNotification(getNotification(editTitle.getText().toString()), scheduled, eventId);
                            }


                            // show info
                            Context context = getApplicationContext();
                            CharSequence text = "Add completely";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();


                            // after toast, finish the activity
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(EditorActivity.this, MainActivity.class));
                                    EditorActivity.this.finish();

                                }
                            }, 1000);

                        }

                        // update
                    } else {

                        Event updatedEvent = er.getEventById(eventId);
                        updatedEvent.title = editTitle.getText().toString();
                        //TODO
//                        updatedEvent.location = editLocation.getText().toString();
                        updatedEvent.location = newLocation;
                        updatedEvent.date = editDate.getText().toString();
                        updatedEvent.time = editTime.getText().toString();
                        updatedEvent.alert = editAlert.getText().toString();
                        updatedEvent.url = editUrl.getText().toString();
                        updatedEvent.notes = editNote.getText().toString();
                        updatedEvent.completed = false;

                        String dateTime =updatedEvent.date+" "+updatedEvent.time;
                       // er.updateOneEvent(updatedEvent);

                        long current = Calendar.getInstance().getTimeInMillis(); // current time
                        long scheduled = getTimeinMilli(dateTime); //converted scheduled time

                        falseDatePicker = checkPastDateTime(current,scheduled);
                        if(!(falseDatePicker)) {
                            er.updateOneEvent(updatedEvent);

                            if(eventOnCalendar) {
                                writeCalendarEvent(updatedEvent, scheduled);
                            }

                            // schedule notification
                            if (!editAlert.getText().toString().equals("None")) {
                                scheduleNotification(getNotification(editTitle.getText().toString()), scheduled, eventId);
                            }


                            // show info
                            Context context = getApplicationContext();
                            CharSequence text = "Update completely";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();


                            // after toast, finish the activity
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(EditorActivity.this, MainActivity.class));
                                    EditorActivity.this.finish();
                                }
                            }, 1000);
                        }

                    }

                }

            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Edit Event");

    }


    /**
     * @author: Supriya Kamble
     * @param notification
     * @param delay: time to shoot notification
     * @param id: notification id
     */
    private void scheduleNotification (Notification notification , long delay, int id) {
        System.out.println("i am called: "+id);
        Intent notificationIntent = new Intent( EditorActivity.this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , id) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast ( EditorActivity.this, id , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP , delay , pendingIntent) ;
    }


    /**
     * @author: Supriya Kamble
     * @param content
     * @return
     */
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( EditorActivity.this, Constants.default_notification_channel_id ) ;
        builder.setContentTitle( "Reminder" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( Constants.NOTIFICATION_CHANNEL_ID );
        return builder.build() ;
    }

    /**
     * check for WRITE_CALENDAR permission
     * @return true if successfully gain permission
     */

    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_CALENDAR)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.WRITE_CALENDAR}, Constants.MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.WRITE_CALENDAR}, Constants.MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Callback for the result from requesting permissions. If permission gained, we will be able to write events on calendar.
     * @param requestCode The request code passed in requestPermissions
     * @param permissions The requested permissions
     * @param grantResults The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_WRITE_CALENDAR:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    eventOnCalendar = true;
                     // writeCalendarEvent(events, mCalendar);
                } else {
                    eventOnCalendar = false;
                }
                break;
        }
    }

    /**
     *
     * @param events the event will be recorded
     * @param timeInMilli The time when it is scheduled
     */
    private void writeCalendarEvent(Event events, long timeInMilli) {
        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 3);
        event.put(CalendarContract.Events.TITLE, events.title);
        event.put(CalendarContract.Events.DESCRIPTION, events.notes);
        event.put(CalendarContract.Events.EVENT_LOCATION, events.location);
        event.put(CalendarContract.Events.DTSTART, timeInMilli);//startTimeMillis
        event.put(CalendarContract.Events.DTEND, timeInMilli + 60*60);//endTimeMillis
        event.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
        event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true
        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
        System.out.println("event writing: "+event.get(CalendarContract.Events.TITLE));

        Uri baseUri;
        if (Build.VERSION.SDK_INT >= 8) {
            baseUri = Uri.parse("content://com.android.calendar/events");
        } else {
            baseUri = Uri.parse("content://calendar/events");
        }
        getApplicationContext().getContentResolver().insert(baseUri, event);

        Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();
    }

    /**
     * check scheduled time for not setting a past event
     * @param current current time
     * @param scheduled scheduled time, it must be later than current time
     * @return true if schedule time is later than current time.
     */
    private boolean checkPastDateTime(long current, long scheduled) {
        if (scheduled <= current) {
            new AlertDialog.Builder(EditorActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("You cannot set past reminder")
                    .setNegativeButton("Got it", null)
                    .show();
            return true;
        }else{
            return false;
        }

    }

    /**
     *
     * @param dateTimeString transform String type time to long type time, which is represented by the number of microseconds which has been passed
     * @return transformed number which represent passed microseconds.
     */
    private long getTimeinMilli(String dateTimeString) {
        Calendar calendarUpdate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yy hh:mm");
        Date date = null;
        try {
            date = dateFormat.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarUpdate.set(Calendar.DATE ,date.getDate());
        calendarUpdate.set(Calendar.HOUR_OF_DAY,date.getHours());
        calendarUpdate.set(Calendar.MINUTE,date.getMinutes());
        return calendarUpdate.getTimeInMillis();

    }


}
