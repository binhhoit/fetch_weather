package com.fetch.weather.ui.feature.dashboard.search

import androidx.lifecycle.LifecycleOwner
import com.base.fragment.FragmentLifecycleObserver
import com.data.model.DataState
import com.fetch.weather.R

class SearchLifecycleObserver(private val fragment: SearchFragment) :
    FragmentLifecycleObserver {

    private val adapter by lazy { LocationAdapter() }
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        initViewSearch()
        observerData()
    }

    private fun initViewSearch() {
        fragment.apply {
            binding.apply {
                rcvLocation.adapter = adapter
                edtLocation.setOnClickIconEnd {
                    if (!edtLocation.text.isNullOrEmpty())
                        viewModel.searchLocationName(edtLocation.text ?: "")
                }
            }

            adapter.callback = {

            }

        }
    }


    private fun observerData() {
        fragment.apply {
            viewModel.geocodingState.observe(this) { state ->
                when (state) {
                    is DataState.Success -> {
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
