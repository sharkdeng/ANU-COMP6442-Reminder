    package com.anu.dolist;

    import android.content.Context;
    import android.graphics.Rect;
    import android.widget.DatePicker;
    import android.widget.ListView;
    import android.widget.TimePicker;

    import androidx.room.Database;
    import androidx.room.Room;
    import androidx.test.espresso.contrib.PickerActions;
    import androidx.test.filters.LargeTest;
    import androidx.test.rule.ActivityTestRule;
    import androidx.test.core.app.ApplicationProvider;


    import com.anu.dolist.db.Event;
    import com.anu.dolist.db.EventDao;
    import com.anu.dolist.db.EventDatabase;
    import com.anu.dolist.db.EventRepository;

    import org.hamcrest.Matchers;
    import org.junit.Assert;
    import org.junit.Before;
    import org.junit.Rule;
    import org.junit.Test;

    import java.util.Calendar;
    import java.util.List;

    import static androidx.test.espresso.Espresso.onView;
    import static androidx.test.espresso.action.ViewActions.click;
    import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
    import static androidx.test.espresso.action.ViewActions.typeText;
    import static androidx.test.espresso.assertion.ViewAssertions.matches;
    import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
    import static androidx.test.espresso.matcher.ViewMatchers.withId;
    import static androidx.test.espresso.matcher.ViewMatchers.withText;
    import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.equalTo;

    /**
     * author u6734521(Supriya Kamble)
     */

    @LargeTest
    public class TextChangeTest {

        private String title, time, mDate, url, notes;
        private EventRepository er;

        @Rule
        public ActivityTestRule<EditorActivity> activityRule
                = new ActivityTestRule<>(EditorActivity.class);


        @Before
        public void initValidString() {
            // Specify a valid string.
            title = "ANU";
            time = "4:15";
            mDate= "31/10/19";
            url = "https://www.anu.edu.au";
            notes ="Welcome to Australian National University";


        }

        @Before
        public void createDb() {
            er= new EventRepository(activityRule.getActivity().getApplication());
        }
        @Test
        public void changeTitleText() {

            onView(withId(R.id.edit_event_title))
                    .perform(typeText(title), closeSoftKeyboard());

            onView(withId(R.id.edit_event_title))
                    .check(matches(withText("ANU")));


    //
        }

        @Test
        public void changeDateText() {

                onView(withId(R.id.edit_event_date)).perform(click());
                onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 10, 31));
                onView(withId(android.R.id.button1)).perform(click());
                onView(withId(R.id.edit_event_date))
                        .check(matches(withText(mDate)));
            }

            @Test
            public void changeTimeText() {

                onView(withId(R.id.edit_event_time)).perform(click());
                onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(4, 15));

                onView(withId(android.R.id.button1)).perform(click());
                onView(withId(R.id.edit_event_time))
                        .check(matches(withText(time)));


        }

        @Test
        public void changeUrlText() {

            onView(withId(R.id.edit_event_url))
                    .perform(typeText(url), closeSoftKeyboard());

            onView(withId(R.id.edit_event_url))
                    .check(matches(withText("https://www.anu.edu.au")));


        }


        @Test
        public void changeNotesText() {

            onView(withId(R.id.edit_event_notes))
                    .perform(typeText(notes), closeSoftKeyboard());

            onView(withId(R.id.edit_event_notes))
                    .check(matches(withText("Welcome to Australian National University")));
        }

        @Test
        public void saveButton() {

            onView(withId(R.id.edit_event_title))
                    .perform(typeText(title), closeSoftKeyboard());

            onView(withId(R.id.edit_event_date)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 10, 31));
            onView(withId(android.R.id.button1)).perform(click());


            onView(withId(R.id.edit_event_time)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(4, 15));
            onView(withId(android.R.id.button1)).perform(click());

            onView(withId(R.id.edit_event_url))
                    .perform(typeText(url), closeSoftKeyboard());

            onView(withId(R.id.edit_event_notes))
                    .perform(typeText(notes), closeSoftKeyboard());

            onView(withId(R.id.edit_tb_right)).perform(click());

            List<Event> events = er.getAllEvents();
            String etitle = events.get(0).title;
            String edate = events.get(0).date;
            String etime = events.get(0).time;
            String eurl = events.get(0).url;
            String enotes = events.get(0).notes;

            assertThat(etitle, equalTo(title));
            assertThat(edate, equalTo(mDate));
            assertThat(etime, equalTo(time));
            assertThat(eurl, equalTo(url));
            assertThat(enotes, equalTo(notes));


        }


    }
