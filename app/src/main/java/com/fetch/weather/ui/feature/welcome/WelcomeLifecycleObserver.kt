package com.fetch.weather.ui.feature.welcome

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class WelcomeLifecycleObserver(private val fragment: WelcomeFragment)
    : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Handler(Looper.myLooper()!!)
            .postDelayed({
                if (fragment.viewModel.firstOpenAppState.value == true) {

                } else {
                    fragment.navigateToLoginScreen()
                }
            }, 4000)

    }
}
