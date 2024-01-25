package com.fetch.weather.feature.ui

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.NavController
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.fetch.weather.ui.feature.main.MainActivity
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

abstract class BaseUITest<F : Fragment> {

    lateinit var device: UiDevice

    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var scenario: FragmentScenario<F>

    @Mock
    lateinit var mockNavController: NavController

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        scenario = createLaunchFragment()
        MockitoAnnotations.openMocks(this)
    }

    abstract fun createLaunchFragment(): FragmentScenario<F>

    fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}