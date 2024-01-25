package com.base.viewmodel

import androidx.lifecycle.ViewModel

/**
 * Created by vophamtuananh on 1/7/18.
 */
open class FragmentViewModel : ViewModel() {

    var initialized = false

    open fun onInitialized() {}

}
