package com.fetch.weather.utils

import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import androidx.annotation.DimenRes
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

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

fun String.simpleFormatTime(): String {
    return try {
        val inputFormatter =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val inputDate = inputFormatter.parse(this)
        val outputFormatter =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        outputFormatter.format(inputDate)
    } catch (e: Exception) {
        ""
    }
}


fun String.simpleFormatTimeGetHour(): String {
    return try {
        val inputFormatter =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val inputDate = inputFormatter.parse(this)
        val outputFormatter =
            SimpleDateFormat("HH", Locale.getDefault())
        outputFormatter.format(inputDate) + ":00"
    } catch (e: Exception) {
        ""
    }
}

fun Double?.convertTempKelvinToCelsius() = (this?:0.0) - 273

fun Double?.roundDecimal(): String {
    return DecimalFormat("#.##").format(this)
}
