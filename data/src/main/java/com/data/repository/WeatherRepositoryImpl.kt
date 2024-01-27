package com.data.repository

import com.data.network.remote.ServiceAPI

class WeatherRepositoryImpl constructor(private var serviceAPI: ServiceAPI) :
    WeatherRepository {
    override suspend fun getWeatherDetailByDate(
        lat: Double,
        lon: Double,
    ) = serviceAPI.getWeatherDetailByDate(lat = lat, lon = lon)

}
