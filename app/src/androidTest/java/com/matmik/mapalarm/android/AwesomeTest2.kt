package com.matmik.mapalarm.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

import androidx.test.espresso.matcher.ViewMatchers.withId
import com.matmik.mapalarm.android.ui.login.LoginActivity

@RunWith(AndroidJUnit4::class)
class AwesomeTest2 {

    @get:Rule  val activityRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun sameActivity() {
        onView(withId(R.id.fieldEmail))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fieldPassword))
            .check(matches(isDisplayed()))
        onView(withId(R.id.emailSignInButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.emailCreateAccountButton))
            .check(matches(isDisplayed()))
    }
}