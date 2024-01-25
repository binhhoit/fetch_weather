package com.fetch.weather.ui.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.data.domain.user.LoginUserCase
import com.data.model.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class LoginViewModelImpl(private val loginUserCase: LoginUserCase): LoginViewModel() {

    private val _loginLiveData = MutableLiveData<DataState>()
    override val loginLiveData: LiveData<DataState> get() = _loginLiveData

    override fun doLogin(userName: String, password: String) {
        loginUserCase.execute(LoginUserCase.Param(userName, password))
            .flowOn(Dispatchers.IO)
            .onStart {
                _loginLiveData.value = DataState.Loading(true)
            }
            .onEach {
                _loginLiveData.value = DataState.Success("Login Success")
            }
            .catch {
                _loginLiveData.value = DataState.Failure(it)
            }
            .onCompletion {
                _loginLiveData.value = DataState.Loading(false)
            }
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)
    }

}