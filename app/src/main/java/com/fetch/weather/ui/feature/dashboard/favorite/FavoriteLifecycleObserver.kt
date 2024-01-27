package com.fetch.weather.ui.feature.dashboard.favorite

import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.base.fragment.FragmentLifecycleObserver
import com.data.model.DataState
import com.fetch.weather.R
import com.fetch.weather.ui.feature.dashboard.search.LocationAdapter

class FavoriteLifecycleObserver(private val fragment: FavoriteFragment) :
    FragmentLifecycleObserver {

    private val adapter by lazy { LocationAdapter() }
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        initView()
        observerData()
    }

    private fun initView() {
        fragment.apply {
            binding.apply { rcvLocation.adapter = adapter }
            adapter.callback = { navigateToDetailWeatherPage(it) }
            viewModel.getListLocationFavorite()
        }
    }

    private fun observerData() {
        fragment.apply {
            viewModel.geocodingState.observe(this) { state ->
                when (state) {
                    is DataState.Success -> {
                        binding.ctlEmpty.isVisible = state.data.isEmpty()
                        adapter.submitList(state.data)
                    }

                    is DataState.Loading -> {
                        if (state.isLoading) showLoading() else hideLoading()
                    }

                    is DataState.Failure -> {
                        showError(Throwable(getString(R.string.fragment_search_can_t_get_geocoding_by_location_name)))
                    }
                }

            }
        }
    }
}
