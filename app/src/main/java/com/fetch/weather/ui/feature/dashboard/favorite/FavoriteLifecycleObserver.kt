package com.fetch.weather.ui.feature.dashboard.favorite

import androidx.lifecycle.LifecycleOwner
import com.base.fragment.FragmentLifecycleObserver

class FavoriteLifecycleObserver(private val fragment: FavoriteFragment) :
    FragmentLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        fragment.apply {

        }
    }
}
