package com.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


/**
 * Created by vophamtuananh on 2/27/18.
 */
@Suppress("DEPRECATION")
class NetworkUtil {

    companion object {

        const val TYPE_WIFI = 1
        const val TYPE_MOBILE = 2
        const val TYPE_ETHERNET = 3
        const val TYPE_NOT_CONNECTED = 0

        @SuppressLint("MissingPermission")
        fun isConnected(context: Context): Boolean {
            val cm: ConnectivityManager? =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                TYPE_NOT_CONNECTED != getConnectivityStatus(context)
            } else {
                val activeNetwork = cm?.activeNetworkInfo
                null != activeNetwork && activeNetwork.isConnected
            }
        }

        @SuppressLint("MissingPermission")
        fun getConnectivityStatus(context: Context): Int {
            val cm: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val capabilities: NetworkCapabilities? =
                    cm.getNetworkCapabilities(cm.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return TYPE_MOBILE
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return TYPE_WIFI
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return TYPE_ETHERNET
                        }
                    }
                }
            } else {
                val activeNetwork = cm.activeNetworkInfo
                if (null != activeNetwork) {
                    when (activeNetwork.type) {
                        ConnectivityManager.TYPE_WIFI -> return TYPE_WIFI
                        ConnectivityManager.TYPE_MOBILE -> return TYPE_MOBILE
                        ConnectivityManager.TYPE_ETHERNET -> return TYPE_ETHERNET
                    }
                }
            }
            return TYPE_NOT_CONNECTED
        }

    }
}