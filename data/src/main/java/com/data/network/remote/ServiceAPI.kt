package com.data.network.remote

import com.data.model.ForecastsWeatherDataResponse
import com.data.model.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceAPI {
    @GET("geo/1.0/direct")
    suspend fun getCoordinatesByLocationName(
        @Query("q") locationName: String,
        @Query("limit") limit: Int = 5
    ): List<LocationResponse>

    @GET("data/2.5/forecast")
    suspend fun getWeatherDetailByDate(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): ForecastsWeatherDataResponse
}
