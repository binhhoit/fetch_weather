package com.data.domain.weather

import com.data.domain.UseCase
import com.data.model.ForecastsWeatherDataResponse
import com.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherUseCase(private val weatherRepository: WeatherRepository) :
    UseCase<WeatherUseCase.Param, Flow<ForecastsWeatherDataResponse>>() {
    override fun execute(param: Param?) = flow {
        if (param == null)
            throw Exception("Invalid Param Weather")
        emit(weatherRepository.getWeatherDetailByDate(param.lat, param.lon))
    }

    class Param(val lat: Double, val lon: Double, )
}
