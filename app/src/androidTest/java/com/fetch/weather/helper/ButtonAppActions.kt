package com.fetch.weather.helper

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.fetch.weather.ui.feature.widgets.AppButton
import com.fetch.weather.ui.feature.widgets.AppButtonOutline
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import java.util.Locale

class ButtonAppActions {
    class ClickAction : ViewAction {
        override fun getDescription(): String {
            return String.format(Locale.ROOT, "click action button app")
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), ViewMatchers.isAssignableFrom(AppButton::class.java))
        }

        override fun perform(uiController: UiController?, view: View?) {
            if (view is AppButton) {
                view.let {
                    view.callOnClick()
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun click(): ViewAction {
            return actionWithAssertions(ClickAction())
        }
    }
}

class ButtonOutlineAppActions {
    class ClickAction : ViewAction {
        override fun getDescription(): String {
            return String.format(Locale.ROOT, "click action button app")
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), ViewMatchers.isAssignableFrom(
                AppButtonOutline::class.java))
        }

        override fun perform(uiController: UiController?, view: View?) {
            if (view is AppButtonOutline) {
                view.let {
                    view.callOnClick()
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun click(): ViewAction {
            return actionWithAssertions(ClickAction())
        }
    }
}