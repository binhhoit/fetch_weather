package com.data.repository

import com.data.model.LocationResponse

interface LocationRepository {
    suspend fun getCoordinatesByLocationName(locationName: String): List<LocationResponse>

    suspend fun getCoordinatesSaveLocal(): List<LocationResponse>


}
