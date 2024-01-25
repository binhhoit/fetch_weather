package com.fetch.weather.feature.ui.onboarding

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fetch.weather.R
import com.fetch.weather.feature.ui.BaseUITest
import com.fetch.weather.ui.feature.onboarding.OnboardingFragment
import com.fetch.weather.ui.feature.onboarding.OnboardingFragmentDirections
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class OnboardingFragmentTest: BaseUITest<OnboardingFragment>() {
    override fun createLaunchFragment() = launchFragmentInContainer<OnboardingFragment>()
    @Test
    fun slide_onboarding_step_register_warranty() {
        onView(isRoot()).perform(waitFor(3000))
        onView(withText(context.getString(R.string.onboarding_login))).check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.onboarding_register_warranty))).check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.onboarding_enjoy_extra))).check(matches(isDisplayed()))
    }

    @Test
    fun slide_onboarding_step_my_tool() {
        onView(isRoot()).perform(waitFor(3000))
        onView(withText(context.getString(R.string.onboarding_login))).check(matches(isDisplayed()))
        onView(withId(R.id.vp2_onboarding)).perform(swipeLeft())
        onView(isRoot()).perform(waitFor(200))
        onView(withText(context.getString(R.string.onboarding_manage_my_tool))).check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.onboarding_manage_warranty))).check(matches(isDisplayed()))
    }


    @Test
    fun slide_onboarding_step_unlock_rewards() {
        onView(isRoot()).perform(waitFor(3000))
        onView(withText(context.getString(R.string.onboarding_login))).check(matches(isDisplayed()))
        onView(withId(R.id.vp2_onboarding)).perform(swipeLeft()).perform(swipeLeft())
        onView(isRoot()).perform(waitFor(200))
        onView(withText(context.getString(R.string.onboarding_unlock_rewards))).check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.onboarding_one_point))).check(matches(isDisplayed()))
    }

    @Test
    fun slide_onboarding_step_easy_and_free() {
        onView(isRoot()).perform(waitFor(3000))
        onView(withText(context.getString(R.string.onboarding_login))).check(matches(isDisplayed()))
        onView(withId(R.id.vp2_onboarding))
            .perform(swipeLeft())
            .perform(swipeLeft())
            .perform(swipeLeft())
        onView(isRoot()).perform(waitFor(200))
        onView(withText(context.getString(R.string.onboarding_easy_and_free))).check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.onboarding_it_takes))).check(matches(isDisplayed()))
    }

    @Test
    fun onboarding_click_button_login() {
        onView(isRoot()).perform(waitFor(3000))

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        onView(withId(R.id.btn_login)).perform(click())

        verify(mockNavController).navigate(OnboardingFragmentDirections.actionOnboardingFragmentToNavigationPermission(true))
    }


    @Test
    fun onboarding_click_button_join_now() {
        onView(isRoot()).perform(waitFor(3000))

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        onView(withId(R.id.btn_join_now)).perform(click())

        verify(mockNavController).navigate(OnboardingFragmentDirections.actionOnboardingFragmentToNavigationPermission(false))
    }


    @Test
    fun onboarding_click_button_skip() {
        onView(isRoot()).perform(waitFor(3000))

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }
        onView(withId(R.id.tv_skip)).perform(click())

        verify(mockNavController).navigate(OnboardingFragmentDirections.actionOnboardingFragmentToNavigationPermission(true))
    }
}