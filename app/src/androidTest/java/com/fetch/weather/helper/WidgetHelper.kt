package com.fetch.weather.helper

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.base.widgets.RippleButton
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

object WidgetHelper {

    fun withErrorInInputLayout(expectedErrorText: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            override fun matchesSafely(view: View): Boolean {
                if (view !is TextInputLayout) {
                    return false
                }

                val error = view.error ?: return false

                val hint = error.toString()

                return expectedErrorText == hint
            }

            override fun describeTo(description: Description) {}
        }
    }

    fun withDrawable(resId: Int): Matcher<View> {
        return DrawableMatcher(resId)
    }

    fun setViewVisibitity(visibility: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Show / Hide View"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(RippleButton::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.apply { this.visibility = visibility }
            }
        }
    }

    fun hasDrawable(id: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(item: View?): Boolean {
                val imageView = item!!.findViewById<ImageView>(id)
                return imageView.drawable != null
            }
        }
    }

    fun hasDrawable(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(item: View?): Boolean {
                val imageView = item as ImageView
                return imageView.drawable != null
            }
        }
    }

    fun withViewAtPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val viewHolder = item!!.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    fun assertItemCount(count: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(item: View?): Boolean {
                val recyclerView = item as RecyclerView
                val adapter = recyclerView.adapter
                return adapter!!.itemCount == count
            }
        }
    }

    fun clickDrawables(): ViewAction {
        return object : ViewAction {
            override fun getConstraints() //must be a textview with drawables to do perform
                : Matcher<View> {
                return allOf(
                    isAssignableFrom(TextView::class.java),
                    object : BoundedMatcher<View, TextView>(TextView::class.java) {
                        override fun matchesSafely(tv: TextView): Boolean {
                            if (tv.requestFocusFromTouch())
                            //get fpocus so drawables become visible
                                for (d in tv.compoundDrawables)
                                //if the textview has drawables then return a match
                                    if (d != null)
                                        return true

                            return false
                        }

                        override fun describeTo(description: Description) {
                            description.appendText("has drawable")
                        }
                    })
            }

            override fun getDescription(): String {
                return "click drawables"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                if (tv.requestFocusFromTouch())
                //get focus so drawables are visible
                {
                    val drawables = tv.compoundDrawables

                    val tvLocation = Rect()
                    tv.getHitRect(tvLocation)

                    val tvBounds = arrayOfNulls<Point>(4) //find textview bound locations
                    tvBounds[0] = Point(tvLocation.left, tvLocation.centerY())
                    tvBounds[1] = Point(tvLocation.centerX(), tvLocation.top)
                    tvBounds[2] = Point(tvLocation.right, tvLocation.centerY())
                    tvBounds[3] = Point(tvLocation.centerX(), tvLocation.bottom)

                    for (location in 0..3)
                        if (drawables[location] != null) {
                            val bounds = drawables[location].bounds
                            tvBounds[location]!!.offset(
                                bounds.width() / 2,
                                bounds.height() / 2
                                                       ) //get drawable click location for left, top, right, bottom
                            if (tv.dispatchTouchEvent(
                                    MotionEvent.obtain(
                                        android.os.SystemClock.uptimeMillis(),
                                        android.os.SystemClock.uptimeMillis(),
                                        MotionEvent.ACTION_DOWN,
                                        tvBounds[location]!!.x.toFloat(),
                                        tvBounds[location]!!.y.toFloat(),
                                        0
                                                      )
                                                     )
                            )
                                tv.dispatchTouchEvent(
                                    MotionEvent.obtain(
                                        android.os.SystemClock.uptimeMillis(),
                                        android.os.SystemClock.uptimeMillis(),
                                        MotionEvent.ACTION_UP,
                                        tvBounds[location]!!.x.toFloat(),
                                        tvBounds[location]!!.y.toFloat(),
                                        0
                                                      )
                                                     )
                        }
                }
            }
        }
    }
}

class DrawableMatcher(private var resId: Int = 0) : TypeSafeMatcher<View>() {
    override fun matchesSafely(item: View): Boolean {
        if (item is RippleButton) {
            val currentDrawable = item.compoundDrawablesRelative[0]
            val expectedDrawable = item.context.resources.getDrawable(resId, null)
            return (getBitmap(currentDrawable).sameAs(getBitmap(expectedDrawable)))
        }
        return false
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(resId)
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
                                        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
