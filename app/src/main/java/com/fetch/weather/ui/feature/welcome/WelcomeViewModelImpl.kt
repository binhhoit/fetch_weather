package com.fetch.weather.ui.feature.welcome

import androidx.lifecycle.MutableLiveData
import com.data.common.SharePreferenceManager
import java.util.Locale

class WelcomeViewModelImpl(localData: SharePreferenceManager) : WelcomeViewModel() {
    private val _isLogin= MutableLiveData<Boolean>()
    override val isLogin get() = _isLogin

    init {
        _isLogin.value = localData.userToken.isNotEmpty() && localData.userToken.isNotEmpty()
    }

}