package com.base.viewmodel

import androidx.lifecycle.ViewModel
import com.base.utils.SingleLiveEvent

/**
 * Created by vophamtuananh on 1/7/18.
 */
abstract class ActivityViewModel : ViewModel() {
    abstract val showBottomNavigation: SingleLiveEvent<Boolean>
}
