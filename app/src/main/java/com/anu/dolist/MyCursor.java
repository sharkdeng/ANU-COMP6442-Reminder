package com.anu.dolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.anu.dolist.db.EventAttrib;

/**
 * @author Limin Deng
 * @author Supriya
 */

public class MyCursor extends CursorAdapter {


    public MyCursor (Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    /** The newView method is used to inflate a new view and return it, you don't bind any data to the view at this point.
     * @param context context Interface to application's global information
     * @param cursor cursor The cursor from which to get the data.
     * @param parent parent The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.cursor_row, parent, false);
    }

    /**
     * The bindView method is used to bind all data to a given view, such as setting the text on a TextView.
     * @param view Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor The cursor from which to get the data.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template

        // ui
        TextView title = (TextView) view.findViewById(R.id.event_title);
        TextView date = (TextView) view.findViewById(R.id.event_date);
        TextView time = (TextView) view.findViewById(R.id.event_time);


        // from database
        String e_title = cursor.getString(cursor.getColumnIndexOrThrow(EventAttrib.TITLE.toString()));
        String e_location = cursor.getString(cursor.getColumnIndexOrThrow(EventAttrib.LOCATION.toString()));
        String e_date = cursor.getString(cursor.getColumnIndexOrThrow(EventAttrib.DATE.toString()));
        String e_time = cursor.getString(cursor.getColumnIndexOrThrow(EventAttrib.TIME.toString()));
        String e_alert = cursor.getString(cursor.getColumnIndexOrThrow(EventAttrib.ALERT.toString()));
        String e_url = cursor.getString(cursor.getColumnIndexOrThrow(EventAttrib.URL.toString()));
        String e_notes = cursor.getString(cursor.getColumnIndexOrThrow(EventAttrib.NOTES.toString()));
        int e_completed = cursor.getInt(cursor.getColumnIndexOrThrow(EventAttrib.COMPLETED.toString()));


        // set content
        title.setText(e_title);
        date.setText(e_date);
        time.setText(e_time);


        if (e_completed == 0) {
            title.setBackgroundColor(Color.RED);
        } else {
            title.setBackgroundColor(Color.GREEN);
        }
    }
}

