package com.base.viewmodel

import androidx.lifecycle.LifecycleOwner

/**
 * Created by vophamtuananh on 1/7/18.
 */
interface CommonView {

    fun showLoading(whiteBackground: Boolean = false, onDismiss: (() -> Unit)? = null, cancelable: Boolean = false)

    fun showError(throwable: Throwable, tryAgainAction: (() -> Unit)?)

    fun showError(throwable: Throwable)

    fun hideLoading()

}