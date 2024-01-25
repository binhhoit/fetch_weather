package com.fetch.weather.feature.ui.welcome

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fetch.weather.R
import com.fetch.weather.feature.ui.BaseUITest
import com.fetch.weather.ui.feature.welcome.WelcomeFragment
import com.fetch.weather.ui.feature.welcome.WelcomeFragmentDirections
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class WelcomeFragmentTest: BaseUITest<WelcomeFragment>() {

    override fun createLaunchFragment() = launchFragmentInContainer<WelcomeFragment>()
    @Test
    fun show_icon_logo_app_and_loading() {
        device.waitForIdle(500)
        onView(withId(R.id.iv_logo)).check(matches(isDisplayed()))
        device.waitForIdle(500)
        onView(withId(R.id.pb_loading))
            .check(matches(isDisplayed()))

    }

    @Test
    fun navigate_other_screen_after_4_second() {
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        onView(isRoot()).perform(waitFor(3000))

        verify(mockNavController).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToOnboardingFragment())
    }
}