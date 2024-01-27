package com.fetch.weather.ui.feature.dashboard.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.domain.user.ProfileUseCase
import com.data.model.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class ProfileViewModelImpl(private val profileUseCase: ProfileUseCase) : ProfileViewModel() {

    private val _profileState = MutableLiveData<DataState<String>>()
    override val profileState get() = _profileState
    override fun getProfile() {
        profileUseCase.getProfileInfo()
            .flowOn(Dispatchers.IO)
            .onStart {
                _profileState.value = DataState.Loading(true)
            }
            .onEach {
                _profileState.value = DataState.Success(it)
            }
            .catch {
                _profileState.value = DataState.Failure(it)
            }
            .onCompletion {
                _profileState.value = DataState.Loading(false)
            }
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)
    }

    override fun logOut() {
        profileUseCase.logOutProfile()
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}