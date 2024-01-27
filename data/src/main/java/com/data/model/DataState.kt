package com.data.model

open class DataState<out T> {

    // Data state for loading to show/hide loading progress
    data class Loading<T>(val isLoading: Boolean = true, val isRefresh: Boolean = false) : DataState<T>()

    // Data state for emitting data to Observer
    data class Success<T>(val data: T) : DataState<T>()

    data class Failure<T>(val message: Throwable) : DataState<T>()

}
