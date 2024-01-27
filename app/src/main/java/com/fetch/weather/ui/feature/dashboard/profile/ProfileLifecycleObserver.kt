package com.fetch.weather.ui.feature.dashboard.profile

import androidx.lifecycle.LifecycleOwner
import com.base.fragment.FragmentLifecycleObserver
import com.data.model.DataState
import com.fetch.weather.R

class ProfileLifecycleObserver(private val fragment: ProfileFragment) :
    FragmentLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        initView()
        observerData()
    }

    private fun initView() {
        fragment.apply {
            binding.btnLogout.setOnClickListener {
                viewModel.logOut()
                navigateToLoginPage()
            }
            viewModel.getProfile()
        }
    }

    private fun observerData() {
        fragment.apply {
            viewModel.profileState.observe(this) { state ->
                when (state) {
                    is DataState.Success -> {
                        binding.profileName = state.data
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
