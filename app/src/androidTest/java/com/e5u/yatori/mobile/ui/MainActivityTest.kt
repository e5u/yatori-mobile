package com.e5u.yatori.mobile.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.e5u.yatori.mobile.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for MainActivity
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testActivityLaunches() {
        // Check that the main activity launches successfully
        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testBinaryStatusCardIsDisplayed() {
        // Check that binary status card is visible
        onView(withId(R.id.binaryStatusText))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testExecutionStatusCardIsDisplayed() {
        // Check that execution status card is visible
        onView(withId(R.id.executionStatusText))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testFabIsDisplayed() {
        // Check that FAB is visible
        onView(withId(R.id.fab))
            .check(matches(isDisplayed()))
    }
}
