package com.fetch.weather.feature.ui.login

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.fetch.weather.R
import com.fetch.weather.feature.ui.BaseUITest
import com.fetch.weather.helper.ButtonAppActions
import com.fetch.weather.helper.EditTextAppActions
import com.fetch.weather.ui.feature.login.LoginFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginFragmentTest: BaseUITest<LoginFragment>() {
    override fun createLaunchFragment() = launchFragmentInContainer<LoginFragment>(themeResId = R.style.AppTheme)

    @Before
    fun setupNav() {
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }
    }
    @Test
    fun login_view_empty_fill() {
        onView(ViewMatchers.isRoot()).perform(waitFor(3000))

        onView(withId(R.id.edt_display_name))
            .perform(
                EditTextAppActions.replaceText(""),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.btn_login))
            .check(matches(ViewMatchers.isNotEnabled()))

        onView(withId(R.id.tv_error))
            .check(matches(isDisplayed()))
            .check(matches(withText(context.getString(R.string.fragment_login_valid_name_empty))))
    }

    @Test
    fun login_view_short_name() {
        onView(ViewMatchers.isRoot()).perform(waitFor(3000))

        onView(withId(R.id.edt_display_name))
            .perform(
                EditTextAppActions.replaceText("abc"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.btn_login))
            .check(matches(ViewMatchers.isNotEnabled()))

        onView(withId(R.id.tv_error))
            .check(matches(isDisplayed()))
            .check(matches(withText(context.getString(R.string.fragment_login_valid_name_short))))

    }

    @Test
    fun login_view_valid_name() {
        onView(ViewMatchers.isRoot()).perform(waitFor(3000))

        onView(withId(R.id.edt_display_name))
            .perform(
                EditTextAppActions.replaceText("abcdefa"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.btn_login))
            .check(matches(ViewMatchers.isEnabled()))
            .perform(ButtonAppActions.click())

    }


}