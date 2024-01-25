package com.fetch.weather.ui.feature.dashboard.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.base.fragment.BaseFragment
import com.fetch.weather.databinding.FragmentFavoriteLocationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment() {
    private var _binding: FragmentFavoriteLocationBinding? = null
    val binding get() = _binding!!
    override val lifecycleObserver = SearchLifecycleObserver(this)

    val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override val requestStatusBar = LIGHT

    private fun findNavControllerDashboard() =
        parentFragment?.parentFragment?.findNavController()
}