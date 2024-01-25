package com.base.fragment

import java.lang.ref.WeakReference

/**
 * Created by vophamtuananh on 1/7/18.
 */
class WaitThread(fragment: ViewModelFragment) : Thread() {

    private var fragmentWeak: WeakReference<ViewModelFragment>? = null
    private var isStopped: Boolean = false
    private val mObject = Object()

    init {
        fragmentWeak = WeakReference(fragment)
    }

    override fun run() {
        val fragment = fragmentWeak?.get()
        if (fragment != null) {
            while (!fragment.isViewCreated() && !isStopped) {
                try {
                    synchronized(mObject) {
                        mObject.wait()
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

            if (isStopped)
                return

            val finalFragment = fragmentWeak?.get()
            finalFragment?.requireActivity()?.runOnUiThread {
                if (!finalFragment.isInitialized()) {
                    finalFragment.onInitialized()
                }
            }
        }
    }

    fun continueProcessing() {
        synchronized(mObject) {
            mObject.notifyAll()
        }
    }

    fun stopProcessing() {
        isStopped = true
        synchronized(mObject) {
            mObject.notifyAll()
        }
    }
}

