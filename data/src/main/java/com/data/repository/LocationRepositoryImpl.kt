package com.data.repository

import com.data.BuildConfig
import com.data.model.LocationResponse
import com.data.network.remote.ServiceAPI

class LocationRepositoryImpl constructor(private var serviceAPI: ServiceAPI) :
    LocationRepository {
    override suspend fun getCoordinatesByLocationName(locationName: String): List<LocationResponse> =
        serviceAPI.getCoordinatesByLocationName(
            locationName = locationName,
            limit = limitResult)

    companion object {
        private const val limitResult = 5
    }

}
