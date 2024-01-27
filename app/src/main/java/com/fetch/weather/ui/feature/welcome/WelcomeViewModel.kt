package com.fetch.weather.ui.feature.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

abstract class WelcomeViewModel: ViewModel() {

    abstract val isLogin: LiveData<Boolean>

}