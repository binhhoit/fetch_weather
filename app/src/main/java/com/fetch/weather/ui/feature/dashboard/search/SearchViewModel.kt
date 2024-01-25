package com.fetch.weather.ui.feature.dashboard.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.data.model.DataState
import com.data.model.LocationResponse

abstract class SearchViewModel : ViewModel() {
    abstract val geocodingState: LiveData<DataState<List<LocationResponse>>>
    abstract fun searchLocationName(locationName: String)
}