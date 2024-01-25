package com.data.domain.location

import com.data.domain.UseCase
import com.data.model.LocationResponse
import com.data.repository.LocationRepository
import com.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeocodingUserCase(private val locationRepository: LocationRepository) :
    UseCase<String, Flow<List<LocationResponse>>>() {
    override fun execute(param: String?) = flow {
        if (param == null)
            throw Exception("Invalid location name")
        emit(locationRepository.getCoordinatesByLocationName(param))
    }
}
