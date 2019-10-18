package com.anu.dolist;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.MenuPopupWindow;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventRepository;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;


@LargeTest
public class TestMainActivity {

    private EventRepository er;
    private int allEvents = 0;

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void createDb() {
        er= new EventRepository(activityRule.getActivity().getApplication());
    }



    @Test
    public void testMainShowAll() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Show all"))
                .perform(click());


        // check
        int rowCount = getRowCount(onView(allOf(withId(R.id.main_swipe_list))));
        List<Event> events = er.getAllEvents();

        assertThat(rowCount, equalTo(events.size()));

    }

    @Test
    public void testMainShowAllIncomlete() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Show incompleted"))
                .perform(click());


        // check
        int rowCount = getRowCount(onView(allOf(withId(R.id.main_swipe_list))));
        List<Event> events = er.getAllIncompletedEvents();

        assertThat(rowCount, equalTo(events.size()));

    }

    @Test
    public void testMainShowAllComplete() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Show completed"))
                .perform(click());


        // check
        int rowCount = getRowCount(onView(allOf(withId(R.id.main_swipe_list))));
        List<Event> events = er.getAllCompletedEvents();

        assertThat(rowCount, equalTo(events.size()));


    }

    /**
     * Test delete all events
     */
    @Test
    public void testMainDeleteAll() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // Click the item.
        onView(withText("Delete all"))
                .perform(click());


        // check
        List<Event> events = er.getAllEvents();
        assertThat(events.size(), equalTo(0));


    }



    /**
     * Test long click on the item to delete this event
     * @author: Limin Deng
     */
    @Test
    public void testMainDelete() {

        // Long click - No
        int sizeBefore = er.getAllEvents().size();

        if (sizeBefore < 0) return;

        // delete first item
        onData(anything())
                .inAdapterView(allOf(withId(R.id.main_swipe_list), isCompletelyDisplayed()))
                .atPosition(0).perform(longClick());
        onView(withText("No")).perform(click());

        int sizeAfter = er.getAllEvents().size();

        assertThat(sizeAfter, equalTo(sizeBefore));




        // Long click - Yes

        // get target event
        String title = getText(
                onData(anything())
                .inAdapterView(allOf(withId(R.id.main_swipe_list), isCompletelyDisplayed()))
                .atPosition(0)
                .onChildView(withId(R.id.event_title)));

        String time = getText(
                onData(anything())
                        .inAdapterView(allOf(withId(R.id.main_swipe_list), isCompletelyDisplayed()))
                        .atPosition(0)
                        .onChildView(withId(R.id.event_time)));

        String date = getText(
                onData(anything())
                        .inAdapterView(allOf(withId(R.id.main_swipe_list), isCompletelyDisplayed()))
                        .atPosition(0)
                        .onChildView(withId(R.id.event_date)));


        Event deleteEvent = er.getEventByTitleTimeDate(title, time, date);



        // delete first event
        onData(anything())
                .inAdapterView(allOf(withId(R.id.main_swipe_list), isCompletelyDisplayed()))
                .atPosition(0).perform(longClick());
        onView(withText("Yes")).perform(click());
        int sizeAfter2 = er.getAllEvents().size();


        // check
        assertThat(sizeAfter2, equalTo(sizeBefore-1));


        List<Event> events = er.getAllEvents();
        assertThat("has been deleted", !events.contains(deleteEvent));


    }


    /**
     * helper function
     * get title fron listview item
     * then get event information
     * @param dataInteraction onData or onView
     * @return
     */
    public String getText(final DataInteraction dataInteraction) {
        final String[] text = {null};

        dataInteraction.perform(new ViewAction() {

            // check if the matcher if TextView
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            // description for this action
            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            // get the content of the text
            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView)view;
                text[0] = textView.getText().toString();
            }
        });
        return text[0];
    }





    public int getRowCount(final ViewInteraction viewInteraction) {
        final int[] count = {0};

        viewInteraction.perform(new ViewAction() {

            // check if the matcher if TextView
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(SwipeMenuListView.class);
            }

            // description for this action
            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            // get the content of the text
            @Override
            public void perform(UiController uiController, View view) {
                SwipeMenuListView listView = (SwipeMenuListView)view;
                count[0] = listView.getAdapter().getCount();
            }
        });
        return count[0];
    }



}