package com.base.fragment

import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import com.base.activity.BaseActivity
import com.base.viewmodel.CommonView

abstract class BaseFragment : Fragment(), CommonView {

    companion object {
        @IntDef(NONE, DARK, LIGHT)
        @Retention(AnnotationRetention.SOURCE)
        annotation class RequestStatusBar

        const val NONE = 0
        const val DARK = 1
        const val LIGHT = 2
    }

    abstract val lifecycleObserver: DefaultLifecycleObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(lifecycleObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(lifecycleObserver is FragmentLifecycleObserver) {
            (lifecycleObserver as FragmentLifecycleObserver).onViewCreated(viewLifecycleOwner)
        }
    }

    override fun onResume() {
        if (requireActivity() is BaseActivity)
            when (requestStatusBar) {
                DARK -> {
                    (requireActivity() as BaseActivity).updateStatusBar(false)
                }

                LIGHT -> {
                    (requireActivity() as BaseActivity).updateStatusBar(true)
                }
            }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun showLoading(whiteBackground: Boolean, onDismiss: (() -> Unit)?, cancelable: Boolean) {
        requireActivity().apply {
            if (this is BaseActivity) {
                showLoading(whiteBackground, onDismiss, cancelable)
            }
        }
    }

    override fun hideLoading() {
        requireActivity().apply {
            if (this is BaseActivity) {
                hideLoading()
            }
        }
    }

    override fun showError(throwable: Throwable) {
        requireActivity().apply {
            if (this is BaseActivity) {
                showError(throwable)
            }
        }
    }

    override fun showError(throwable: Throwable, tryAgainAction: (() -> Unit)?) {
        requireActivity().apply {
            if (this is BaseActivity) {
                showError(throwable, tryAgainAction)
            }
        }
    }

    @RequestStatusBar
    open val requestStatusBar : Int = NONE
}
