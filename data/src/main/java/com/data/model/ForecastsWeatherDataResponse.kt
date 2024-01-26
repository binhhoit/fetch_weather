package com.data.model

import com.google.gson.annotations.SerializedName

data class ForecastsWeatherDataResponse (
    @SerializedName("cod"     ) var cod     : String?         = null,
    @SerializedName("message" ) var message : Int?            = null,
    @SerializedName("cnt"     ) var cnt     : Int?            = null,
    @SerializedName("list"    ) var weatherDailyDetails    : List<WeatherDetail> = listOf(),
)