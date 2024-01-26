package com.fetch.weather.ui.feature.dashboard.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.domain.location.GeocodingFavoriteUseCase
import com.data.model.DataState
import com.data.model.LocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class FavoriteViewModelImpl(private val geocodingFavoriteUseCase: GeocodingFavoriteUseCase)
    : FavoriteViewModel() {

    private val _geocodingState =
        MutableLiveData<DataState<List<LocationResponse>>>()
    override val geocodingState get() = _geocodingState
    override fun getListLocationFavorite() {
        geocodingFavoriteUseCase.execute()
            .flowOn(Dispatchers.IO)
            .onStart {
                _geocodingState.value = DataState.Loading(true)
            }
            .onEach {
                _geocodingState.value = DataState.Success(it)
            }
            .catch {
                _geocodingState.value = DataState.Failure(it)
            }
            .onCompletion {
                _geocodingState.value = DataState.Loading(false)
            }
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)
    }

}