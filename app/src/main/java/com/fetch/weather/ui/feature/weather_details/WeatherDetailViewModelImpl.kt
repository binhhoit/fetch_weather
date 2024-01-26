package com.fetch.weather.ui.feature.weather_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.common.SharePreferenceManager
import com.data.domain.weather.WeatherUserCase
import com.data.model.DataState
import com.data.model.ForecastsWeatherDataResponse
import com.fetch.weather.utils.formatDateTime
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.util.Calendar

class WeatherDetailViewModelImpl(private val weatherUserCase: WeatherUserCase,private val local:SharePreferenceManager) : WeatherDetailViewModel() {
    private val _weatherDataState = MutableLiveData<DataState<ForecastsWeatherDataResponse>>()
    override val weatherDataState get() = _weatherDataState
    override fun getDataWeatherByGeocoding(lag: Double, lon: Double) {
        val params = WeatherUserCase.Param(lag,lon)
         val data =  Gson().fromJson(local.dalyWeather, ForecastsWeatherDataResponse::class.java)
        _weatherDataState.value = DataState.Success(data)
        /*weatherUserCase.execute(params)
            .flowOn(Dispatchers.IO)
            .onStart {
                _weatherDataState.value = DataState.Loading(true)
            }
            .onEach {
                 local.dalyWeather = Gson().toJson(it)
                _weatherDataState.value = DataState.Success(it)
            }
            .catch {
                _weatherDataState.value = DataState.Failure(it)
            }
            .onCompletion {
                _weatherDataState.value = DataState.Loading(false)
            }
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)*/
    }

    private fun getListDate(): List<String> {
        val dates = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        dates.add(calendar.time.formatDateTime())
        calendar.add(Calendar.DATE, 1)
        dates.add(calendar.time.formatDateTime())
        calendar.add(Calendar.DATE, 1)
        dates.add(calendar.time.formatDateTime())
        return dates
    }

}