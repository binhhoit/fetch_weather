package com.fetch.weather.ui.feature.dashboard.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.data.model.DataState
import com.data.model.LocationResponse

abstract class FavoriteViewModel: ViewModel() {
    abstract val geocodingState: LiveData<DataState<List<LocationResponse>>>
    abstract fun getListLocationFavorite()
}