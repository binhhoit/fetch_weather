package com.fetch.weather.ui.feature.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.base.activity.BaseActivity
import com.fetch.weather.R
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    override val lifecycleObserver = MainLifecycleObserver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun transparentStatusBar() = true

    override fun showFullScreen() = false

}
