package com.fetch.weather.utils

import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import androidx.annotation.DimenRes
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

class CommonUtil {

    companion object {

        val EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+")

        fun isValidEmail(email: String): Boolean {
            return EMAIL_PATTERN.matcher(email).matches()
        }

    }

}

fun Resources.getRawDimensionInDp(@DimenRes dimenResId: Int): Float {
    val value = TypedValue()
    getValue(dimenResId, value, true)
    return TypedValue.complexToFloat(value.data)
}


fun Date.formatDateTime(): String {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-DD", Locale.US)
        formatter.format(this)
    } catch (e: Exception) {
        ""
    }
}