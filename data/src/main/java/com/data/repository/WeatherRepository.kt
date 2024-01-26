package com.data.repository

import com.data.model.ForecastsWeatherDataResponse

interface WeatherRepository {
    suspend fun getWeatherDetailByDate(
        lat: Double,
        lon: Double
    ): ForecastsWeatherDataResponse

}
