package com.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.io.Serializable
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.roundToInt


/**
 * Created by vophamtuananh on 12/24/17.
 */

const val DELAY_TRANSITION_TIME: Long = 700
inline fun <reified T> Activity.start(clearBackStack: Boolean = false, bundle: Bundle? = null, activityOptions: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (clearBackStack) {
        if (activityOptions == null) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        } else {
            //need to delay for better UI performance in case no transition between activities
            Handler().postDelayed({ finish() }, DELAY_TRANSITION_TIME)
        }
    }
    bundle?.let {
        intent.putExtras(bundle)
    }
    ActivityCompat.startActivity(this, intent, activityOptions)
}

inline fun <reified T> Activity.startForResult(requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundle?.let {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode)
}

inline fun <reified T> Activity.startForResult(requestCode: Int, bundle: Bundle? = null, activityOptions: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundle?.let {
        intent.putExtras(bundle)
    }
    ActivityCompat.startActivityForResult(this, intent, requestCode, activityOptions)
}

inline fun <reified T> Fragment.startForResult(activity:Activity, requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(activity, T::class.java)
    bundle?.let {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Fragment> createNewFragment(context: Context, bundle: Bundle? = null): T {
    return T::class.java.cast(Fragment.instantiate(context, T::class.java.name, bundle))!!
}

inline fun <reified T : Fragment> Context.newFragment(bundle: Bundle? = null): T {
    return T::class.java.cast(Fragment.instantiate(this, T::class.java.name, bundle))!!
}

inline fun <reified T : Fragment> FragmentManager.newFragment(context: Context, bundle: Bundle? = null): T {
    return T::class.java.cast(fragmentFactory.instantiate(context.classLoader, T::class.java.name))!!.apply {
        arguments = bundle
    }
}

inline fun <reified T : Activity> Fragment.getOwnActivity(): T? {
    activity ?: return null
    return T::class.java.cast(activity)
}

/*fun <T> BehaviorSubject<T>.onNextIfNew(newValue: T) {
    if (this.value != newValue) {
        onNext(newValue)
    }
}*/

fun ReentrantLock.skipIfRunning(block: () -> Unit){
    if (this.tryLock())
    {
        // Got the lock
        try
        {
            // Process record
            block.invoke()
        }
        finally
        {
            // Make sure to unlock so that we don't cause a deadlock
            this.unlock()
        }
    }
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

fun getColorWithAlpha(color: Int, ratio: Float): Int {
    val newColor: Int
    val alpha = (Color.alpha(color) * ratio).roundToInt()
    val r = Color.red(color)
    val g = Color.green(color)
    val b = Color.blue(color)
    newColor = Color.argb(alpha, r, g, b)
    return newColor
}

fun Boolean.negative() = !this

fun Boolean?.safe(default: Boolean = false) = this ?: default

fun String?.safe(default: String = ""): String = if (this.isNullOrEmpty()) default else this

fun Int?.safe(default: Int = 0): Int = this ?: default

fun Float?.safe(default: Float = 0f): Float = this ?: default

fun Double?.safe(default: Double = 0.0): Double = this ?: default