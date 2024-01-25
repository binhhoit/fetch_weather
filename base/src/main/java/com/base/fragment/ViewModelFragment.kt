package com.base.fragment

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import com.base.viewmodel.FragmentViewModel

abstract class ViewModelFragment : BaseFragment() {

    abstract val viewModel: FragmentViewModel
    protected var mViewCreated = false
    private var mViewDestroyed = false
    private var mWaitThread: WaitThread? = null

    open fun onInitialized() {
        viewModel.initialized = true
        if(lifecycleObserver is FragmentLifecycleObserver) {
            (lifecycleObserver as FragmentLifecycleObserver).onInitialized(viewLifecycleOwner)
        }
        viewModel.onInitialized()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val animation: Animation? = if(nextAnim != 0) {
            AnimationUtils.loadAnimation(context, nextAnim)
        } else {
            super.onCreateAnimation(transit, enter, nextAnim)
        }
        if(enter) {
            animation?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    if (mViewDestroyed)
                        return
                    mWaitThread = WaitThread(this@ViewModelFragment)
                    mWaitThread?.start()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        return animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewCreated = true
        mViewDestroyed = false
        if(mWaitThread != null) {
            mWaitThread?.continueProcessing()
        } else {
            if(!isInitialized()) {
                onInitialized()
            }
        }
    }

    override fun onDestroyView() {
        mWaitThread?.stopProcessing()
        mViewDestroyed = true
        mViewCreated = false
        super.onDestroyView()
    }

    fun isViewCreated(): Boolean {
        return mViewCreated
    }

    fun isInitialized(): Boolean {
        return viewModel.initialized
    }
}