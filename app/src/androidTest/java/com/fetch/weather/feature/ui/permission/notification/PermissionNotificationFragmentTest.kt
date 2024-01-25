package com.fetch.weather.feature.ui.permission.notification

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.UiSelector
import com.fetch.weather.R
import com.fetch.weather.feature.ui.BaseUITest
import com.fetch.weather.ui.feature.permission.notification.PermissionNotificationFragment
import com.fetch.weather.ui.feature.permission.notification.PermissionNotificationFragmentArgs
import com.fetch.weather.ui.feature.permission.notification.PermissionNotificationFragmentDirections
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class PermissionNotificationFragmentTest: BaseUITest<PermissionNotificationFragment>() {
    private val args = PermissionNotificationFragmentArgs(isLogin = true)

    override fun createLaunchFragment() = launchFragmentInContainer<PermissionNotificationFragment>(fragmentArgs = args.toBundle())

    @Before
    fun setupPermissionNotification(){
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(),
                mockNavController)
        }
    }

    @Test
    fun permission_notification_press_button_maybe_later() {
        onView(withText(context.getString(R.string.permission_notification_deny)))
            .check(matches(isDisplayed()))
            .perform(click())

        verify(mockNavController).navigate(
            PermissionNotificationFragmentDirections.actionPermissionNotificationFragmentToPermissionFileAccessFragment(isLogin = true))
    }

    @Test
    fun permission_notification_press_button_allow() {
        onView(isRoot()).perform(waitFor(500))
        onView(withText(context.getString(R.string.permission_notification_allow)))
            .check(matches(isDisplayed()))
            .perform(click())

        val uiObject = device.findObject(UiSelector().text("Allow"))
        assertTrue(uiObject.exists())
        uiObject.clickAndWaitForNewWindow()

        verify(mockNavController).navigate(PermissionNotificationFragmentDirections.actionPermissionNotificationFragmentToPermissionFileAccessFragment(isLogin = true))
    }

    @Test
    fun permission_notification_press_button_deny() {
        onView(isRoot()).perform(waitFor(500))
        onView(withText(context.getString(R.string.permission_notification_allow)))
            .check(matches(isDisplayed()))
            .perform(click())

        val uiObject = device.findObject(UiSelector().text("Don't allow"))
        assertTrue(uiObject.exists())
        uiObject.clickAndWaitForNewWindow()
    }

}