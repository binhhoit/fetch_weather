package com.base.fragment

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface FragmentLifecycleObserver: DefaultLifecycleObserver{

    fun onViewCreated(owner: LifecycleOwner) {}

    fun onInitialized(owner: LifecycleOwner) {}

}