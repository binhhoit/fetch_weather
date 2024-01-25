package com.fetch.weather.ui.feature.dashboard

import androidx.core.view.forEachIndexed
import androidx.lifecycle.LifecycleOwner
import com.base.fragment.FragmentLifecycleObserver
import com.fetch.weather.R

class DashboardLifecycleObserver(private val fragment: DashboardFragment) : FragmentLifecycleObserver {

    private val titleBottomNavigationBar by lazy {
        var list: List<String>
        fragment.apply {
            list = listOf(
                getString(R.string.bottom_navigation_search),
                getString(R.string.bottom_navigation_favorite),
            )
        }
        list
    }

    override fun onViewCreated(owner: LifecycleOwner) {
        fragment.setupBottomNavigation()
    }

    fun resetBottomNavigationBar(){
        fragment.apply {
            binding.dashboardBottomNavigation.menu.forEachIndexed { index, item ->
                item.title = titleBottomNavigationBar[index]
            }
        }
    }

}
