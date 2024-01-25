package com.data.model

open class DataState {

    // Data state for loading to show/hide loading progress
    data class Loading(val isLoading: Boolean = true, val isRefresh: Boolean = false) : DataState()

    // Data state for emitting data to Observer
    data class Success<T>(val data: T) : DataState()

    data class Failure(val message: Throwable) : DataState()

}
