package com.fetch.weather.ui.feature.dashboard.search

import androidx.lifecycle.LifecycleOwner
import com.base.fragment.FragmentLifecycleObserver

class SearchLifecycleObserver(private val fragment: SearchFragment) :
    FragmentLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        fragment.apply {

        }
    }
}
