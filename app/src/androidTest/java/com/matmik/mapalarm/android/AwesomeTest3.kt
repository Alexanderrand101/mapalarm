package com.matmik.mapalarm.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

@RunWith(AndroidJUnit4::class)
class AwesomeTest3 {

    @get:Rule  val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
    }

    @Test
    fun testClick() {
        onView(withId(R.id.add_alarm_btn)).perform(click())
        onView(withId(R.id.name))
            .check(matches(isDisplayed()))
        onView(withId(R.id.time))
            .check(matches(isDisplayed()))
        onView(withId(R.id.description))
            .check(matches(isFocusable()))
    }
}