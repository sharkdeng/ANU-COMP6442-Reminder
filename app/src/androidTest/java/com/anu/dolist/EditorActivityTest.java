package com.anu.dolist;

import android.icu.text.CaseMap;
import android.widget.DatePicker;

import com.google.android.material.snackbar.Snackbar;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class EditorActivityTest {

    @Rule
    public ActivityTestRule<EditorActivity> mActivityTestRule = new ActivityTestRule<>(EditorActivity.class);

    private String EmptyString = "";
    private String TitleString = "TestEvent";
    private String EmptyMessage = "Title cannot be empty!";

    private String TimeMessage = "You should set time and date for this Event!";

    @Test
    public void EmptyTitle() {
        Espresso.onView(withId(R.id.edit_event_title)).perform(typeText(EmptyString));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.edit_fab)).perform(click());
        Espresso.onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(EmptyMessage)));

    }

    @Test
    public void EmptyTime() throws InterruptedException {
        Espresso.onView(withId(R.id.edit_event_title)).perform(typeText(TitleString));
        Thread.sleep(250);
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.edit_fab)).perform(click());
        Espresso.onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(TimeMessage)));

    }
}