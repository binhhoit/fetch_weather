package com.fetch.weather.ui.feature.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.base.fragment.BaseFragment
import com.fetch.weather.R
import com.fetch.weather.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragment() {
    private var _binding: FragmentDashboardBinding? = null
    val binding get() = _binding!!
    override val lifecycleObserver = DashboardLifecycleObserver(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setupBottomNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.dashboard_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.dashboardBottomNavigation.setupWithNavController(navController)
    }

    fun resetUIBottomBar() {
        lifecycleObserver.resetBottomNavigationBar()
    }

    fun navigationBottomBar(position: Int) {
        binding.dashboardBottomNavigation.selectedItemId = when (position) {
            1 -> R.id.searchFragment
            else -> R.id.favoriteFragment
        }
    }
}