package com.fetch.weather.ui.feature.welcome

import androidx.lifecycle.MutableLiveData
import com.data.common.SharePreferenceManager
import java.util.Locale

class WelcomeViewModelImpl(localData: SharePreferenceManager) :
    WelcomeViewModel() {
    private val _languageLocaleState= MutableLiveData<Locale>()
    override val languageLocaleState get() = _languageLocaleState
    private val _firstOpenAppState= MutableLiveData<Boolean>()
    override val firstOpenAppState get() = _firstOpenAppState

    init {
        _languageLocaleState.value = localData.localeCurrent
        _firstOpenAppState.value = localData.firstOpenApp
    }

}