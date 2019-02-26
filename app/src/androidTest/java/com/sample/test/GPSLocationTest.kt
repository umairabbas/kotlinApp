package com.sample.test


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GPSLocationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION")

    @Test
    fun gPSLocationTest() {
        val appCompatImageView = onView(
                allOf(withId(R.id.buttonLocation),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frameLayoutInput),
                                        0),
                                1),
                        isDisplayed()))
        appCompatImageView.perform(click())

        Thread.sleep(2700)

        val textInputEditText = onView(
                allOf(withId(R.id.textInputAddress), withText("Melatener Straße 96A, 52074 Aachen"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.textInputLayoutAddress),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText.perform(click())

        val textInputEditText2 = onView(
                allOf(withId(R.id.textInputAddress), withText("Melatener Straße 96A, 52074 Aachen"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.textInputLayoutAddress),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText2.perform(replaceText("Melatener Straße 96"))

        Thread.sleep(2700)

        val textInputEditText3 = onView(
                allOf(withId(R.id.textInputAddress), withText("Melatener Straße 96"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.textInputLayoutAddress),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText3.perform(closeSoftKeyboard())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
