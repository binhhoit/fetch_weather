package com.fetch.weather.ui.feature.weather_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.data.model.DataState
import com.data.model.ForecastsWeatherDataResponse

abstract class WeatherDetailViewModel : ViewModel() {

    abstract val weatherDataState: LiveData<DataState<ForecastsWeatherDataResponse>>
    abstract fun getDataWeatherByGeocoding(lag: Double, lon: Double)

}