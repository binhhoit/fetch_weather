package com.fetch.weather.feature.ui.permission.location

import android.os.Build
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiSelector
import com.fetch.weather.R
import com.fetch.weather.feature.ui.BaseUITest
import com.fetch.weather.ui.feature.permission.location.PermissionLocationFragment
import com.fetch.weather.ui.feature.permission.location.PermissionLocationFragmentArgs
import com.fetch.weather.ui.feature.permission.location.PermissionLocationFragmentDirections
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class PermissionLocationFragmentTest: BaseUITest<PermissionLocationFragment>() {
    private val args = PermissionLocationFragmentArgs(isLogin = true)

    override fun createLaunchFragment() = launchFragmentInContainer<PermissionLocationFragment>(fragmentArgs = args.toBundle())

    @Before
    fun setupPermissionLocation(){
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(),
                mockNavController)
        }
    }

    fun tearDown(){
       revokePermissions("android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION")
    }

    private fun revokePermissions(vararg permissions: String) {
        permissions.forEach {
            InstrumentationRegistry.getInstrumentation().uiAutomation.
            executeShellCommand("pm revoke ${context.packageName} $it")
        }
    }

    @Test
    fun permission_location_press_button_maybe_later() {

        onView(withText(context.getString(R.string.permission_location_deny)))
            .check(matches(isDisplayed()))
            .perform(click())

        verify(mockNavController).navigate(PermissionLocationFragmentDirections.actionPermissionLocationFragmentToPermissionNotificationFragment(isLogin = true))
    }

    @Test
    fun permission_location_press_button_allow_only_one() {
        onView(isRoot()).perform(waitFor(500))
        onView(withText(context.getString(R.string.permission_location_allow)))
            .check(matches(isDisplayed()))
            .perform(click())

        val uiObject = device.findObject(UiSelector().text("Only this time"))
        assertTrue(uiObject.exists())
        uiObject.clickAndWaitForNewWindow()

        verify(mockNavController).navigate(PermissionLocationFragmentDirections.actionPermissionLocationFragmentToPermissionNotificationFragment(isLogin = true))
        tearDown()
    }

    @Test
    fun permission_location_press_button_allow_using_app() {
        onView(withText(context.getString(R.string.permission_location_allow)))
            .check(matches(isDisplayed()))
            .perform(click())

        val uiObject = device.findObject(UiSelector().text(when {
            Build.VERSION.SDK_INT == 23 -> "Allow"
            Build.VERSION.SDK_INT <= 28 -> "ALLOW"
            Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
            else -> "While using the app"
        }))
        assertTrue(uiObject.exists())
        uiObject.clickAndWaitForNewWindow()

        verify(mockNavController).navigate(PermissionLocationFragmentDirections.actionPermissionLocationFragmentToPermissionNotificationFragment(isLogin = true))
        tearDown()
    }

    @Test
    fun permission_location_press_button_allow_and_deny() {
        onView(isRoot()).perform(waitFor(500))
        onView(withText(context.getString(R.string.permission_location_allow)))
            .check(matches(isDisplayed()))
            .perform(click())
        val uiObject = device.findObject(UiSelector().text("Don't allow"))
        assertTrue(uiObject.exists())
        uiObject.click()
    }

}