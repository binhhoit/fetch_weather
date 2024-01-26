package com.fetch.weather.ui.feature.weather_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.data.model.DataState
import com.data.model.ForecastsWeatherDataResponse
import com.data.model.LocationResponse

abstract class WeatherDetailViewModel : ViewModel() {

    abstract val loadDataDataState: LiveData<DataState<Boolean>>
    abstract val weatherDataTodayState: LiveData<ForecastsWeatherDTO>
    abstract val weathersDetailToday: LiveData<List<ForecastsWeatherDTO>>
    abstract val weatherDataTheNextDayState: LiveData<List<ForecastsWeatherDTO>>
    abstract fun getDataWeatherByGeocoding(lag: Double, lon: Double)
    abstract fun saveLocationListFavorite(locationResponse: LocationResponse)

}