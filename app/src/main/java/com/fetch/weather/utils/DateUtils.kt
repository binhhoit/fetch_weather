package com.fetch.weather.utils

class DateUtils {
    companion object {
        fun isLeapYear(year: Int): Boolean {
            return when {
                year % 4 != 0 -> {
                    false
                }
                year % 400 == 0 -> {
                    true
                }
                year % 100 == 0 -> {
                    false
                }
                else -> {
                    true
                }
            }
        }
    }
}