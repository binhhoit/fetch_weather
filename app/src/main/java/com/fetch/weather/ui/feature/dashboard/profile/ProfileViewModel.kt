package com.fetch.weather.ui.feature.dashboard.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.data.model.DataState
import com.data.model.LocationResponse

abstract class ProfileViewModel: ViewModel() {
    abstract val profileState: LiveData<DataState<String>>
    abstract fun getProfile()
    abstract fun logOut()

}