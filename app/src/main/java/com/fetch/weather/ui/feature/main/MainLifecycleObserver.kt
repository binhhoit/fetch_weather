package com.fetch.weather.ui.feature.main


import android.net.ConnectivityManager
import android.net.Network
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.fetch.weather.R
import kotlinx.coroutines.launch

class MainLifecycleObserver(private val mainActivity: MainActivity) :
    DefaultLifecycleObserver {

    private val viewLostNetwork by lazy {
        mainActivity.findViewById<LinearLayout>(R.id.ll_lost_network)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        handleLostNetwork()
    }

    private fun handleLostNetwork(){
        mainActivity.apply {
            getSystemService(this, ConnectivityManager::class.java)
                ?.apply {
                    if(this.isActiveNetworkMetered)
                        viewLostNetwork.isVisible = true
                    registerDefaultNetworkCallback(
                        object : ConnectivityManager.NetworkCallback() {
                            override fun onAvailable(network: Network) {
                                lifecycleScope.launch {
                                    viewLostNetwork.isVisible = false
                                }
                            }

                            override fun onLost(network: Network) {
                                lifecycleScope.launch {
                                    viewLostNetwork.isVisible = true
                                }
                            }
                        })
                }
        }
    }

}
