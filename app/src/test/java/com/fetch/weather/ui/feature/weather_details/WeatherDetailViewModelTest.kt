package com.fetch.weather.ui.feature.weather_details

import androidx.lifecycle.Observer
import com.data.common.SharePreferenceManager
import com.data.domain.weather.WeatherUseCase
import com.data.model.DataState
import com.fetch.weather.ui.helper.forecastsWeatherDataResponse
import com.fetch.weather.viewmodel.BaseViewModelTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDetailViewModelTest: BaseViewModelTest<WeatherDetailViewModel>() {

    @Mock
    private lateinit var weatherUseCase: WeatherUseCase
    @Mock
    private lateinit var localData: SharePreferenceManager

    private val mLoadDataDataState: Observer<DataState<Boolean>> = mock()
    private val mWeatherDataTodayState: Observer<ForecastsWeatherDTO> = mock()
    private val mWeathersDetailToday: Observer<List<ForecastsWeatherDTO>> = mock()
    private val mWeatherDataTheNextDayState: Observer<List<ForecastsWeatherDTO>> = mock()

    override fun createViewModel(): WeatherDetailViewModel {
        return WeatherDetailViewModelImpl(weatherUseCase, localData)
    }

    @Before
    override fun setUp() {
        MockitoAnnotations.openMocks(this)
        super.setUp()
    }

    @Test
    fun `get data weather by geocoding success, test DataState Success`() = testCoroutineRule.runTest {
        // arrange
        whenever(weatherUseCase.execute(any()))
            .thenReturn(flowOf(forecastsWeatherDataResponse))
        mViewModel.loadDataDataState.observeForever(mLoadDataDataState)
        mViewModel.weatherDataTodayState.observeForever(mWeatherDataTodayState)
        mViewModel.weathersDetailToday.observeForever(mWeathersDetailToday)
        mViewModel.weatherDataTheNextDayState.observeForever(mWeatherDataTheNextDayState)

        // action
        mViewModel.getDataWeatherByGeocoding(0.0,0.0)

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mLoadDataDataState, Times(1)).onChanged(DataState.Loading(true))
        verify(mLoadDataDataState, Times(1)).onChanged(any<DataState.Success<Boolean>>())
        verify(mLoadDataDataState, Times(1)).onChanged(DataState.Loading(false))

    }

    @Test
    fun `get data weather by geocoding fail, test DataState Failure`() = testCoroutineRule.runTest{
        // arrange
        whenever(weatherUseCase.execute(any()))
            .thenReturn(flow { throw Throwable("API error") })

        mViewModel.loadDataDataState.observeForever(mLoadDataDataState)
        mViewModel.weatherDataTodayState.observeForever(mWeatherDataTodayState)
        mViewModel.weathersDetailToday.observeForever(mWeathersDetailToday)
        mViewModel.weatherDataTheNextDayState.observeForever(mWeatherDataTheNextDayState)

        // action
        mViewModel.getDataWeatherByGeocoding(0.0,0.0)

        withContext(Dispatchers.IO) {
            Thread.sleep(2000)
        }

        // assert
        verify(mLoadDataDataState, Times(1)).onChanged(DataState.Loading(true))
        verify(mLoadDataDataState, Times(1)).onChanged(any<DataState.Failure<Boolean>>())
        verify(mLoadDataDataState, Times(1)).onChanged(DataState.Loading(false))
    }
}