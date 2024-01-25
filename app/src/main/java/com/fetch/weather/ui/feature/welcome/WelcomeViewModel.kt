package com.fetch.weather.ui.feature.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

abstract class WelcomeViewModel: ViewModel() {

    abstract val languageLocaleState: LiveData<Locale>

    abstract val firstOpenAppState: LiveData<Boolean>

}