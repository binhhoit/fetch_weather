package com.fetch.weather.ui.feature.weather_details

import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.data.model.DataState
import com.fetch.weather.R

class WeatherDetailLifecycleObserver(private val fragment: WeatherDetailFragment)
    : DefaultLifecycleObserver {

    private val adapter by lazy { ForecastsWeatherDailyAdapter() }
    private val adapterWeatherToday by lazy { ForecastsWeatherTodayAdapter() }
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        initView()
        observerData()
        fragment.apply {
            argument.location.let {
                binding.location = it
                viewModel.getDataWeatherByGeocoding(it.lat ?: 0.0, it.lon ?: 0.0)
            }
        }
    }

    private fun initView() {
        fragment.apply {
            binding.apply {
                rcvForecastsWeatherDaily.adapter = adapter
                rcvForecastsWeatherToday.adapter = adapterWeatherToday
                ivBack.setOnClickListener { backScreen() }
                fabSaveLocation.setOnClickListener { saveLocationFavorite() }
            }
        }
    }

    private fun saveLocationFavorite() {
        fragment.apply {
            viewModel.saveLocationListFavorite(argument.location)
            Toast.makeText(requireContext(),
                "Location saved favorite",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun observerData() {
        fragment.apply {
            viewModel.loadDataDataState.observe(this) { state ->
                when (state) {
                    is DataState.Loading -> {
                        if (state.isLoading) showLoading() else hideLoading()
                    }

                    is DataState.Failure -> {
                        showError(Throwable(getString(R.string.fragment_search_can_t_get_geocoding_by_location_name)))
                    }
                }

            }
            viewModel.weatherDataTodayState.observe(this) { data ->
                binding.infoWeatherToday = data
            }
            viewModel.weathersDetailToday.observe(this) { data ->
                adapterWeatherToday.submitList(data)
            }
            viewModel.weatherDataTheNextDayState.observe(this) { data ->
                adapter.submitList(data)
            }
        }
    }
}
