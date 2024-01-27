package com.fetch.weather.ui.feature.dashboard.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.base.fragment.BaseFragment
import com.data.model.LocationResponse
import com.fetch.weather.databinding.FragmentSearchBinding
import com.fetch.weather.ui.feature.dashboard.DashboardFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment() {
    private var _binding: FragmentSearchBinding? = null
    val binding get() = _binding!!
    override val lifecycleObserver = SearchLifecycleObserver(this)

    val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override val requestStatusBar = LIGHT

    fun navigateToDetailWeatherPage(locationResponse: LocationResponse) {
        findNavControllerDashboard()?.navigate(
            DashboardFragmentDirections
                .actionDashboardFragmentToWeatherDetailFragment(locationResponse)
        )
    }

    private fun findNavControllerDashboard() =
        parentFragment?.parentFragment?.findNavController()
}