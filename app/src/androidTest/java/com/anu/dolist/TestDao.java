package com.anu.dolist;

import android.content.Context;

import androidx.room.Room;
import androidx.test.runner.AndroidJUnit4;

import com.anu.dolist.db.EventDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

//@RunWith(AndroidJUnit4.class)
//public class TestDao {
//    private EventDatabase userDao;
//    private TestDatabase db;
//
//    @Before
//    public void createDb() {
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
//        userDao = db.getUserDao();
//    }
//
//    @After
//    public void closeDb() throws IOException {
//        db.close();
//    }
//
//    @Test
//    public void writeUserAndReadInList() throws Exception {
//        User user = TestUtil.createUser(3);
//        user.setName("george");
//        userDao.insert(user);
//        List<User> byName = userDao.findUsersByName("george");
//        assertThat(byName.get(0), equalTo(user));
//    }
//}
