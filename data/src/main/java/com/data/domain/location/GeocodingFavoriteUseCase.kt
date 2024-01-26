package com.data.domain.location

import com.data.domain.UseCase
import com.data.model.LocationResponse
import com.data.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeocodingFavoriteUseCase(private val locationRepository: LocationRepository) :
    UseCase<Unit, Flow<List<LocationResponse>>>() {
    override fun execute(param: Unit?) = flow {
        emit(locationRepository.getCoordinatesSaveLocal())
    }
}
