package com.data.network.intercepter

import android.content.Context
import com.base.exception.NoConnectionException
import com.base.utils.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val mContext: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtil.isConnected(mContext)) {
            throw NoConnectionException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}