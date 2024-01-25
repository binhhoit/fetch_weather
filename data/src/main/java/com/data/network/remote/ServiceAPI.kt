package com.data.network.remote

import com.data.model.LocationResponse
import com.data.network.model.User
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceAPI {
    @GET("geo/1.0/direct")
    suspend fun getCoordinatesByLocationName(
        @Query("q") locationName: String,
        @Query("limit") limit: Int = 5
    ): List<LocationResponse>
}
