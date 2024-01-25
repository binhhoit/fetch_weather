package com.fetch.weather.ui.feature.welcome

import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeLifecycleObserver(private val fragment: WelcomeFragment)
    : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        fragment.apply {
            lifecycleScope.launch {
                delay(3000)
                if (fragment.viewModel.isLogin.value == true) {
                    navigateToDashboardScreen()
                } else {
                    navigateToLoginScreen()
                }
            }
            pbLoading.postDelayed({
                requireActivity().runOnUiThread {
                    val drawable = ContextCompat.getDrawable(requireContext(), com.base.R.drawable.ic_loading_animated)
                    val bounds = pbLoading.indeterminateDrawable.bounds
                    pbLoading.indeterminateDrawable = drawable
                    pbLoading.indeterminateDrawable.bounds = bounds
                }
            },1500)
        }
    }
}
