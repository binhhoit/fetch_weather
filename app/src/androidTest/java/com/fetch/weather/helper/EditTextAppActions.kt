package com.fetch.weather.helper

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.fetch.weather.ui.feature.widgets.AppEditText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import java.util.Locale

class EditTextAppActions {
    class ReplaceTextAction(private val text: String): ViewAction {
        override fun getDescription(): String {
            return String.format(Locale.ROOT, "replace text(%s)", text)
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), ViewMatchers.isAssignableFrom(AppEditText::class.java))
        }

        override fun perform(uiController: UiController?, view: View?) {
            if (view is AppEditText) {
                view.let {
                    view.text = text
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun replaceText(text: String): ViewAction {
            return actionWithAssertions(ReplaceTextAction(text))
        }
    }
}