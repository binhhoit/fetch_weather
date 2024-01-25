package com.fetch.weather.feature.ui.permission.file

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
import com.fetch.weather.ui.feature.permission.file.PermissionFileAccessFragment
import com.fetch.weather.ui.feature.permission.file.PermissionFileAccessFragmentArgs
import com.fetch.weather.ui.feature.permission.file.PermissionFileAccessFragmentDirections
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.Mockito.verify
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class PermissionFileAccessFragmentTest: BaseUITest<PermissionFileAccessFragment>() {
    private val args = PermissionFileAccessFragmentArgs(isLogin = true)

    override fun createLaunchFragment() = launchFragmentInContainer<PermissionFileAccessFragment>(fragmentArgs = args.toBundle())

    @Before
    fun setupPermissionFileAccess(){
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(),
                mockNavController)
        }
    }

    @Test
    fun a_permission_file_access_press_button_maybe_later() {
        onView(isRoot()).perform(waitFor(1000))
        onView(withText(context.getString(R.string.permission_notification_deny)))
            .check(matches(isDisplayed()))
            .perform(click())

        verify(mockNavController).navigate(
            PermissionFileAccessFragmentDirections.actionPermissionNotificationFragmentToLoginFragment())
    }

    @Test
    fun b_permission_file_access_press_button_allow() {
        onView(isRoot()).perform(waitFor(1000))
        onView(withText(context.getString(R.string.permission_file_access_allow)))
            .check(matches(isDisplayed()))
            .perform(click())

        val uiObject = device.findObject(UiSelector().text("USE THIS FOLDER"))
        assertTrue(uiObject.exists())
        uiObject.clickAndWaitForNewWindow()

        val uiObjectAllow = device.findObject(UiSelector().text("ALLOW"))
        assertTrue(uiObjectAllow.exists())
        uiObjectAllow.clickAndWaitForNewWindow()

        verify(mockNavController).navigate(
            PermissionFileAccessFragmentDirections.actionPermissionNotificationFragmentToLoginFragment())
    }
}