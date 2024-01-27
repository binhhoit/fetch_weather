package com.fetch.weather.ui.feature.weather_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.common.SharePreferenceManager
import com.data.domain.weather.WeatherUseCase
import com.data.model.DataState
import com.data.model.ForecastsWeatherDataResponse
import com.data.model.LocationResponse
import com.data.model.WeatherDetail
import com.fetch.weather.utils.formatDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.util.Calendar

class WeatherDetailViewModelImpl(
    private val weatherUseCase: WeatherUseCase,
    private val localDate: SharePreferenceManager,
) : WeatherDetailViewModel() {

    private val _loadDataDataState = MutableLiveData<DataState<Boolean>>()
    private val _weatherDataTodayState = MutableLiveData<ForecastsWeatherDTO>()
    private val _weathersDetailToday = MutableLiveData<List<ForecastsWeatherDTO>>()
    private val _weatherDataTheNextDayState = MutableLiveData<List<ForecastsWeatherDTO>>()
    override val loadDataDataState get() = _loadDataDataState
    override val weatherDataTodayState get() =_weatherDataTodayState
    override val weathersDetailToday get() = _weathersDetailToday
    override val weatherDataTheNextDayState get() = _weatherDataTheNextDayState

    override fun getDataWeatherByGeocoding(lag: Double, lon: Double) {
        val params = WeatherUseCase.Param(lag, lon)
        weatherUseCase
            .execute(params)
            .flowOn(Dispatchers.IO)
            .onStart {
                _loadDataDataState.value = DataState.Loading(true)
            }
            .onEach {
                _loadDataDataState.value = DataState.Success(true)
                handleDataFormListForestWeather(it)
            }
            .catch {
                _loadDataDataState.value = DataState.Failure(it)
            }
            .onCompletion {
                _loadDataDataState.value = DataState.Loading(false)
            }
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)
    }

    override fun saveLocationListFavorite(locationResponse: LocationResponse) {
        val locations = localDate.listLocationFavorite
        val isExist = locations.find {
            locationResponse.name == it.name &&
                    locationResponse.lat == it.lat &&
                    locationResponse.lon == it.lon
        }
        if (isExist == null)
            localDate.listLocationFavorite = locations + locationResponse
    }

    private fun handleDataFormListForestWeather(data : ForecastsWeatherDataResponse){
        val forecastWeather = getListDate().map { date ->
            data.weatherDailyDetails.filter { it.timeLine?.contains(date)?:false }
        }

        forecastWeather.let {
            _weathersDetailToday.value = forecastWeather.first().map {
                ForecastsWeatherDTO(
                    it.timeLine.toString(),
                    it.main?.temp ?: 0.0,
                    it.wind?.speed ?: 0.0,
                    (it.main?.humidity ?: 0).toDouble(),
                    it.weather.first().icon)
            }
        }

        forecastWeather.map {
            summaryOfWeatherInformation(it)
        }.let {
            _weatherDataTodayState.value = it.first()
            _weatherDataTheNextDayState.value = it
        }
    }

    private fun summaryOfWeatherInformation(data: List<WeatherDetail>): ForecastsWeatherDTO {
        return ForecastsWeatherDTO(
            data.first().timeLine?:"",
            calculateAverage(data.map { it.main?.temp ?: 0.0 }),
            calculateAverage(data.map { it.wind?.speed ?: 0.0 }),
            calculateAverage(data.map { (it.main?.humidity ?: 0).toDouble() }),
            data[data.size / 2].weather.first().icon
        )
    }

    private fun calculateAverage(numbers: List<Double>): Double {
        if (numbers.isEmpty()) {
            return 0.0
        }
        val total = numbers.sum()
        return total / numbers.size
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
        calendar.add(Calendar.DATE, 1)
        dates.add(calendar.time.formatDateTime())
        return dates
    }

}