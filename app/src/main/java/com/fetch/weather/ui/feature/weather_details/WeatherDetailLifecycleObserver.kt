package com.fetch.weather.ui.feature.weather_details

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.data.model.DataState
import com.fetch.weather.R
import com.fetch.weather.ui.feature.dashboard.search.LocationAdapter

class WeatherDetailLifecycleObserver(private val fragment: WeatherDetailFragment)
    : DefaultLifecycleObserver {

    private val adapter by lazy { ForecastsWeatherDailyAdapter() }
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        initView()
        observerData()
        fragment.apply {
            argument.location.let {
                viewModel.getDataWeatherByGeocoding(it.lat ?: 0.0, it.lon ?: 0.0)
            }
        }
    }

    private fun initView() {
        fragment.apply {
            binding.apply {
                rcvForecastsWeatherDaily.adapter = adapter
            }
        }
    }

    private fun observerData() {
        fragment.apply {
            viewModel.weatherDataState.observe(this) { state ->
                when (state) {
                    is DataState.Success -> {
                        adapter.submitList(state.data.weatherDailyDetails)
                    }

                    is DataState.Loading -> {
                        if (state.isLoading) showLoading() else hideLoading()
                    }

                    is DataState.Failure -> {
                        showError(Throwable(getString(R.string.fragment_search_can_t_get_geocoding_by_location_name)))
                    }
                }

            }
        }
    }
}
