package com.data.repository

import com.data.BuildConfig
import com.data.common.SharePreferenceManager
import com.data.model.LocationResponse
import com.data.network.remote.ServiceAPI

class LocationRepositoryImpl constructor(private var serviceAPI: ServiceAPI,
                                         private var localData: SharePreferenceManager) :
    LocationRepository {
    override suspend fun getCoordinatesByLocationName(locationName: String) =
        serviceAPI.getCoordinatesByLocationName(
            locationName = locationName,
            limit = limitResult)

    override suspend fun getCoordinatesSaveLocal() =
        localData.listLocationFavorite

    companion object {
        private const val limitResult = 5
    }

}
