package com.fetch.weather.ui.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.data.model.DataState

abstract class LoginViewModel: ViewModel() {

    abstract val loginLiveData: LiveData<DataState<Boolean>>
    abstract fun doLogin(userName: String)

}