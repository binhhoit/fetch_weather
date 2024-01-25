package com.data.network.remote

import com.data.network.model.User
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface ServiceAPI {
    @GET("api/v1/login")
    suspend fun login(): ApiResponse<User>
}
