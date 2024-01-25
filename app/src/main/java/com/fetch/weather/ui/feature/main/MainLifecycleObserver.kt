package com.fetch.weather.ui.feature.main

import androidx.lifecycle.DefaultLifecycleObserver

class MainLifecycleObserver(private val mainActivity: MainActivity) : DefaultLifecycleObserver {
    init {
        mainActivity.apply {
            lifecycle
        }
    }
}
