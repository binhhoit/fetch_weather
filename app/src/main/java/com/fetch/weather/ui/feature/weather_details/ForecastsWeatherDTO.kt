package com.fetch.weather.ui.feature.weather_details

data class ForecastsWeatherDTO(
    val timeLine: String,
    val temp: Double = 0.0,
    val speedWind: Double = 0.0,
    val humidity: Double = 0.0,
    val weatherIcon: String?,
)
